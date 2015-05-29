package genetic;

import java.util.Random;
import java.util.ArrayList;
import java.io.*; // for reading files

public class MultiKnapsack extends Genetic<MultiKnapsack>{

    public double getDistance(MultiKnapsack a, MultiKnapsack b) {
        return Math.abs(a.fitness - b.fitness);
    }

    static Random rnd = new Random(System.currentTimeMillis());
    
    private ArrayList<Knapsack> sacks; // contains items, thresholds, choices
    private int optimal_value; // solution value, as defined in the file
    private String combin; // the choice
    
    

    private double fitness;


    public double getFitness() {
        return this.fitness;
    }
    
    public MultiKnapsack()throws IOException{
    	this.sacks = new ArrayList<Knapsack>();
    	// reading to the MultiKnap sack from a file
    	// in the format specified in the assignment.
    	String s = "C:\\Users\\Guy Ezer\\Desktop\\WEING1.txt";
    	File file = new File(s);
    	FileInputStream f = new FileInputStream(file);
    	
    	BufferedReader br = new BufferedReader(new InputStreamReader(f));
    	
    	String line = null;
    	// getting the number of sacks, number of items
    	int sack_num = 0 , item_num=0;
    	if ((line = br.readLine()) != null){
    		String [] sizes = line.split(" ");
    		sack_num = Integer.parseInt(sizes[0]);
    		item_num = Integer.parseInt(sizes[1]);
    	}
    	// getting the values
    	int[] values = new int [item_num];
    	for (int i=0; i<item_num;){
    		if ((line = br.readLine()) != null){
    			String [] w = line.split(" ");
    			for (int j=0; j<w.length; j++)
    				if (w[j].length()>0)
    					values[i++] = Integer.parseInt(w[j]);
    		}
    	}
    	
    	// getting the capabilities
    	int[] thresh = new int [sack_num];
    	for (int i=0; i<sack_num;){
    		if ((line = br.readLine()) != null){
    			String [] w = line.split(" ");
    			for (int j=0; j<w.length; j++)
    				if (w[j].length()>0)
    					thresh[i++] = Integer.parseInt(w[j]);
    		}
    	}
   
    	// getting the weights for each sack
    	for (int i=0; i<sack_num; i++){
    		ArrayList<Item> item = new ArrayList<Item>();
    		for (int j=0; j<item_num;){
        		if ((line = br.readLine()) != null){
        			String [] w = line.split(" ");
        			for (int k=0; k<w.length; k++)
        				if (w[k].length()>0 && j<item_num){
        					Item temp = new Item(Integer.parseInt(w[k]),values[j++]);
        					item.add(temp);
        				}
        			
        		}
    		}
    		Knapsack temp = new Knapsack(thresh[i],item);
    		this.sacks.add(temp); // adding the new knapsack problem (finally!)
    	}
    	// getting the optimal solution
    	if ((line = br.readLine()) != null){
    		this.optimal_value = Integer.parseInt(line);
    	}
    	
    	br.close();
    	f.close();
    	
    	// generating a random choice
    	this.generateValue();
    	this.calculateFitness();
    	    	
    }
    
    private void initiateValue(String s){
    	for (int i=0; i<this.sacks.size();i++)
    		this.sacks.get(i).setChoice(s);
    }

    public void generateValue() { // pretty much the same as Knapsack::generateValue
    	this.combin = "";
       for (int i=0; i<this.sacks.get(0).size(); i++)
    	   this.combin+= rnd.nextInt(2);
       this.initiateValue(combin);
    }

    public void mutate() { // pretty much the same as Knapsack::mutate
        // change a random bit in combin
    	int pos = rnd.nextInt(this.combin.length()-1);
    	this.combin = this.combin.substring(0,pos) + (49-this.combin.charAt(pos)) + this.combin.substring(pos+1);
    	this.initiateValue(combin);
    	this.calculateFitness();
    }

    public MultiKnapsack (MultiKnapsack A, String i){
    	this.combin = i;
    	this.optimal_value = A.optimal_value;
    	this.sacks = A.sacks;
    }
    
    @Override
    public MultiKnapsack mate(MultiKnapsack S2) {
        // taking an item from s1 or s2 w/ a prob. of 50%
    	if (this.combin.length() != S2.combin.length())
    		return this;
    	String I = "";
    	for (int i = 0; i<this.combin.length(); i++)
    		I += (rnd.nextInt(2)==0) ? this.combin.charAt(i) : S2.combin.charAt(i);
    	return (new MultiKnapsack(this,I));
    }

    public void calculateFitness() {
    	// if fitness is 0 - we reached the optimal solution. fitness is forced to be non-negative.
    	this.fitness = this.optimal_value;
    	for (int i=0;i<this.sacks.size();i++){
    		this.sacks.get(i).calculateFitness();
    		this.fitness += this.sacks.get(i).getFitness();
    	}
    //	this.fitness = 5;
    }
   

    public String toString() {
    	return "choice: "+this.combin + "\nfitness: "+ this.fitness;
            }

}

