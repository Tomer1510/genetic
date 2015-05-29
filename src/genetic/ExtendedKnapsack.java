package genetic;

import java.util.Random;
import java.util.ArrayList;
import java.io.*; // for reading files

public class ExtendedKnapsack extends Genetic<ExtendedKnapsack>{

    public double getDistance(ExtendedKnapsack a, ExtendedKnapsack b) {
        return Math.abs(a.fitness - b.fitness);
    }

    static Random rnd = new Random(System.currentTimeMillis());
    
    private int optimal_value =0; // optimal value
    private ArrayList<Integer> values = new ArrayList<Integer>(); // items value
    private ArrayList<Integer> thresholds = new ArrayList<Integer>(); // sacks thresholds
    private ArrayList<ArrayList<Integer> > weights = new ArrayList<ArrayList<Integer> >(); // sacks values
    
    private String combin; // combination of items taken
    
    private double fitness;


    public double getFitness() {
        return this.fitness;
    }
    
    public ExtendedKnapsack()throws IOException{
    	MK_Container m = new MK_Container();
    	this.optimal_value = m.optimal_value;
    	this.values = m.Values;
    	this.weights = m.weights;
    	this.thresholds = m.threhold;
    	
    	// generating a random choice
    	this.generateValue();
    	this.calculateFitness(); 
    }

    public void generateValue() {
    	// pretty much the same as Knapsack::generateValue
    	this.combin = "";
       for (int i=0; i<this.values.size(); i++)
    	   this.combin+= (rnd.nextInt(4) == 0) ? "1":"0";
       
    }

    public void mutate() {
    	// pretty much the same as Knapsack::mutate
        // change a random bit in combin
    	int pos = rnd.nextInt(this.combin.length()-1);
    	this.combin = this.combin.substring(0,pos) + (49-this.combin.charAt(pos)) + this.combin.substring(pos+1);
    	this.calculateFitness();
    }

    public ExtendedKnapsack (ExtendedKnapsack A, String i){
    	this.combin = i;
    	
    	this.optimal_value = A.optimal_value;
   
    	this.values = A.values;
    	this.thresholds = A.thresholds;
    	this.weights = A.weights;

    	this.calculateFitness();
    }
    
    @Override
    public ExtendedKnapsack mate(ExtendedKnapsack S2) {
        // taking an item from s1 or s2 w/ a prob. of 50%
    	if (this.combin.length() != S2.combin.length())
    		return this;
    	String I = "";
    	for (int i = 0; i<this.combin.length(); i++)
    		I += (rnd.nextInt(2)==0) ? 
    				this.combin.charAt(i) : S2.combin.charAt(i);
    	return (new ExtendedKnapsack(this,I));
    }

    public void calculateFitness() {
    	// if fitness is 0 - we reached the optimal solution.
    	//fitness is forced to be non-negative.
    	this.fitness = this.optimal_value;
    	//checking that the combination is legal
    	int w;
    	for (int i=0;i<this.thresholds.size();i++){
    		w=0;
    		for (int j=0;j<this.values.size();j++){
    			w += (this.combin.charAt(j)-48) * this.weights.get(i).get(j);
    			if (w > this.thresholds.get(i)){
    				return;
    			}
    		}
    	}
    	
      	for (int i=0;i<this.values.size();i++)
    			this.fitness -= (this.combin.charAt(i)-48)*this.values.get(i);
    	
    }
   

    public String toString() {
    	return "choice: "+this.combin + "\nfitness: "+ this.fitness;
            }

}

