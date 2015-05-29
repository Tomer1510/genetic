package genetic;

import java.io.IOException;
import java.util.*;

public class Knapsack_Search {
	// this is another representation to the Knapsack problem.
	// The problem is redefined here due to space and convinience
	
	public String choice;
	private int optimal_value =0; // optimal value
	private ArrayList<Integer> values = new ArrayList<Integer>(); // items value
	private ArrayList<Integer> thresholds = new ArrayList<Integer>(); // sacks thresholds
	private ArrayList<ArrayList<Integer> > weights = new ArrayList<ArrayList<Integer> >(); // sacks values
	
	private boolean relax = true;
	
	private String cut = "Fr"; // choosing from Fr / Ul
		
	private K_Node get_child (K_Node a,int level){
		String temp = a.choice.substring(0, level) + "1" + a.choice.substring(level+1);
		return new K_Node(calc_fitness(temp),temp,level+1);
	}
		
	private int calc_fitness(String c){ // we don't have need to save fitness
		// if fitness is 0 - we reached the optimal solution. fitness is forced to be non-negative.
    	int fitness = 0;
    	//checking that the combination is legal
    	int w;
    	for (int i=0;i<this.thresholds.size();i++){
    		w=0;
    		for (int j=0;j<this.values.size();j++){
    			w += (c.charAt(j)-48) * this.weights.get(i).get(j);
    			if (w > this.thresholds.get(i)){
    				return -1;
    			}
    		}
    	}
    	
      	for (int i=0;i<this.values.size();i++)
    			fitness += (c.charAt(i)-48)*this.values.get(i);
    	
    	return fitness;
	}
	
	private int ul_hiuristic (String c){
		int fitness = 0;
		for (int i=0;i<this.values.size();i++)
			fitness += (c.charAt(i)-48)*this.values.get(i);
    	return fitness;
	}
	
	private int fr_hiuristic (String c){
		//TODO
		return 0;
	}
	
	public Knapsack_Search(String filename) throws IOException{
		MK_Container m = new MK_Container(filename);
		
		this.values = m.Values;
		this.weights = m.weights;
		this.thresholds = m.threhold;
		this.optimal_value = m.optimal_value;
		
		String c = "";
		for (int i=0;i<this.values.size();i++)
            c+= "0";
        this.choice = c;
	}
	
	private K_Node max(K_Node a, K_Node b){
		return (a.fitness>b.fitness) ? a:b;
	}
	
	// functions w/o cuts
	// TODO add cuts
	
	private int times;
	
	private K_Node DFS (K_Node current, int level){
		//if ((times++)% 10000 == 0) System.out.println("Went through "+(times-1)+" nodes");
		times++;
		if (level == this.choice.length())
			return current;
		return max(DFS(current, level+1),DFS(get_child(current,level),level+1));
	}
	
	private void Ordered_insert (K_Node a, ArrayList<K_Node> b){
		for (int i=0;i<b.size();i++){
			if(a.fitness >= b.get(i).fitness){
				b.add(i, a);
				return;
			}
		}
		b.add(a);
	}
	
	private K_Node Best_First (ArrayList<K_Node> OPEN){
		
		K_Node best = new K_Node(0,this.choice,0);
		
		while (!OPEN.isEmpty()){
			// extracting best
			times++;
			K_Node current = OPEN.remove(0);
			if (current.level == this.choice.length()){
				best = max(best,current);
			if (best.fitness == this.optimal_value)
				return best;
			// pushing children
			}
			else{
				if (this.relax){
				K_Node k = new K_Node(current.fitness,current.choice,current.level+1);
				if (k.fitness >= 0)
					Ordered_insert(k,OPEN);
				if (current.fitness >= 0)
				Ordered_insert(get_child(current,current.level),OPEN);
				}
				else{
					Ordered_insert(get_child(current,current.level),OPEN);
					Ordered_insert(new K_Node(current.fitness,current.choice,current.level+1),OPEN);
				}
			}
		}
		return best;
	}
	
	private String itemList(String c){
		String ret = "Items selected (v) :";
		for (int i=0;i<c.length();i++)
			ret +=  (c.charAt(i)=='1')? " "+this.values.get(i) : "";
		return ret;
	}
	
	public void Search(String method){
		
		K_Node root = new K_Node(0,this.choice);
		if (method.equals("DFS")){
			K_Node s = DFS(root,0);
			System.out.println("Solution found!\nTotal fitness is: "+s.fitness);
			System.out.println(itemList(s.choice));
			System.out.println("Choice: "+s.choice);
			System.out.println("Found in "+times+" nodes");
		}
		else{
			if (method.equals("BFS")){
				ArrayList<K_Node> t = new ArrayList<K_Node>();
				t.add(new K_Node(0,choice,0));
				K_Node s = Best_First(t);
				System.out.println("Solution found!\nTotal fitness is: "+s.fitness);
				System.out.println(itemList(s.choice));
				System.out.println("Choice: "+s.choice);
				System.out.println("Found in "+times+" nodes");
			}	
		}
	}
}
