package genetic;

import java.io.IOException;
import java.util.*;

public class Branch_and_Bound {
	// this is another representation to the Knapsack problem.
	// The problem is redefined here due to space and convinience
	
	public String choice;
	private int optimal_value =0; // optimal value
	private ArrayList<Integer> values = new ArrayList<Integer>(); // items value
	private ArrayList<Integer> thresholds = new ArrayList<Integer>(); // sacks thresholds
	private ArrayList<ArrayList<Integer> > weights = new ArrayList<ArrayList<Integer> >(); // sacks values
	
	
	private K_Node get_child (K_Node a,String p){
		String temp = a.choice.substring(0, a.level) + "1" + a.choice.substring(a.level+1);
		double potential = this.calc_potential(temp, a.level+1, p);
		int fitness = this.get_fitness(temp);
		return new K_Node(fitness,temp,a.level+1,potential);
	}
		
	private int get_fitness(String c){ // we don't have need to save fitness
		// if fitness is 0 - we reached the optimal solution.
		// fitness is forced to be non-negative.
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
    	
    	return fitness*this.thresholds.size();
	}		

	private double unlimited(String c, int level){
		double sum = 0;
		for (int i=0;i<this.values.size();i++){
			sum += (c.charAt(i)=='1' || i>=level) ?
					this.values.get(i) : 0; 
		}
		return sum*this.thresholds.size();
	}
	
	private double single_sack_fraqtion (int level, int t, int s){
		double sum = 0;
		ArrayList<FrContainer> r  = new ArrayList<FrContainer>();
		for (int i=level;i<this.values.size();i++)
			r.add(new FrContainer((double)this.weights.get(s).get(i)/this.values.get(i),i));
		Collections.sort(r);
		int w = 0,index;
		for (int i=level;i<this.values.size();i++){
			index = r.get(i-level).index;
			if (w+this.weights.get(s).get(index)>t){
				sum += ((double)(this.values.get(index))/this.weights.get(s).get(index))*(t-w);
				return sum;
			}
			w+= this.weights.get(s).get(index);
			sum += this.values.get(index);
		}
		return sum;
	}
	
	private double fraction(String c, int level){
		double sum = 0; int w = 0;
		ArrayList<Integer> remaining = new ArrayList<Integer>();
		// filling the sack with the path until this node
		for (int i=0;i<this.thresholds.size();i++){
			w = 0;
			for (int j=0;j<level;j++){
				sum += (c.charAt(j)-48)*this.values.get(j);
				w += (c.charAt(j)-48)*this.weights.get(i).get(j);
			}
			remaining.add(w);
		}
		// filling the remainer of the sack with the best
		// items by value-to-weight ratio
		for (int i=0;i<this.thresholds.size();i++)
			sum += single_sack_fraqtion(level, this.thresholds.get(i)-w, i);
		return sum;
	}
	
	private double calc_potential(String c, int level, String method){
		if (level == this.values.size())
			return get_fitness(c);
		else{
			if ("UL".equals(method))
				return unlimited(c,level);
			else 
				return fraction(c,level);
		}
	}
	
	public Branch_and_Bound() throws IOException{
		
		MK_Container m = new MK_Container();
		
		this.values = m.Values;
		this.weights = m.weights;
		this.thresholds = m.threhold;
		this.optimal_value = m.optimal_value*this.thresholds.size();
					
		String c = "";
		for (int i=0;i<this.values.size();i++)
            c+= "0";
        this.choice = c;
	}
			
	private void Ordered_insert (K_Node a, ArrayList<K_Node> b){
		for (int i=0;i<b.size();i++){
			if(a.potential >= b.get(i).potential){
				b.add(i, a);
				return;
			}
		}
		b.add(a);
	}
	
	public void Search(String method, String p){
		boolean BFS = "BFS".equals(method);
		ArrayList<K_Node> OPEN = new ArrayList<K_Node>();
		OPEN.add(new K_Node(0,this.choice,0,this.calc_potential(this.choice,0,p))); // adding the root
		
		K_Node best = OPEN.get(0);
		K_Node current = null;
		K_Node left = null;
		K_Node right = null;
		
		int times = 0; // number of nodes expanded
		double timeElapsed = System.nanoTime();
		while (!OPEN.isEmpty() && best.fitness < this.optimal_value){
			// while we still have nodes to explore
			// or we didn't reach our optimal value.
			times ++;
			if (times %10000 == 0) System.out.println(times);
			current = OPEN.remove(0);
			if (current.level == this.values.size())
				best = (best.fitness > current.fitness) ? best:current;
			// updating the best
			else{
					if (current.fitness >= 0 && current.potential >= best.fitness){ 
					// we should expand this node
					// creating two children
					left = new K_Node (current.fitness,current.choice,current.level+1,this.calc_potential(current.choice, current.level+1, p));
					right = this.get_child(current, p);
					
					if (BFS){
						// in case of BFS , we make an ordered insert of nodes
						this.Ordered_insert(left, OPEN);
						this.Ordered_insert(right, OPEN);
					}
					else{
						// in case of DFS , we just put the new nodes first
						OPEN.add(0,left);
						OPEN.add(0,right);
					}
				}
			}
		}
		
		// final prints
		System.out.println("Solution found!\nTotal fitness is: "+best.fitness/this.thresholds.size());
		System.out.println("Time elapsed: " +
                (System.nanoTime() - timeElapsed)/1000000000 + " (s)");
		System.out.println(itemList(best.choice));
		System.out.println("Choice: "+best.choice);
		System.out.println("Found in "+times+" nodes");
		
	
	}
	
	private String itemList(String c){
		String ret = "Items selected (v) :";
		for (int i=0;i<c.length();i++)
			ret +=  (c.charAt(i)=='1')? " "+this.values.get(i) : "";
		return ret;
	}	

}
