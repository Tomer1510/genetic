package genetic;

import java.util.*;

public class Knapsack extends Genetic<Knapsack>{

    public double getDistance(Knapsack a, Knapsack b) {
        return Math.abs(a.fitness - b.fitness);
    }

    static Random rnd = new Random(System.currentTimeMillis());
    
    private int sack_threshold;
    private ArrayList<Item> ITEMS;
    private String choise;

    private double fitness;


    public double getFitness() {
        return this.fitness;
    }
    
    static final int THRESHOLD = 15;
    
    static final ArrayList<Item> ITEM_VEC = initVec();
    
    private static ArrayList<Item> initVec(){
    	ArrayList<Item> m = new ArrayList<Item>();
    	
    	m.add(new Item(5,5));
    	m.add(new Item(3,6));
    	m.add(new Item(12,12));
    	m.add(new Item(1,10));
    	m.add(new Item(2,9));
    	
    	return m;
    }
    

    public Knapsack() {
    	sack_threshold = THRESHOLD;
    	this.ITEMS = ITEM_VEC;
        this.generateValue();
        this.calculateFitness();
    }
    
    public Knapsack(int thresh){
    	this.sack_threshold = thresh;
    	this.ITEMS = ITEM_VEC;
    	this.generateValue();
    	this.calculateFitness();
    }
    
    public Knapsack(String n){
    	this.sack_threshold = THRESHOLD;
    	this.ITEMS = ITEM_VEC;
    	this.choise = n;
    	this.calculateFitness();
    }
    
    public Knapsack(int thresh, ArrayList<Item> I){
    	for(int i=0;i<I.size();i++)
    	this.sack_threshold = thresh;
    	this.ITEMS = I;
    	this.generateValue();
    	this.calculateFitness();
    }

    public void generateValue() {
    	this.choise = "";
       for (int i=0; i<this.ITEMS.size(); i++)
    	   this.choise+= rnd.nextInt(2);
    }

    public void mutate() {
        // change a random bit in choise
    	int pos = rnd.nextInt(this.choise.length()-1);
    	this.choise = this.choise.substring(0,pos) + (49-this.choise.charAt(pos)) + this.choise.substring(pos+1);
    	this.calculateFitness();
    }

    @Override
    public Knapsack mate(Knapsack S2) {
        // taking an item from s1 or s2 w/ a prob. of 50%
    	if (this.choise.length() != S2.choise.length())
    		return this;
    	String I = "";
    	for (int i = 0; i<this.choise.length(); i++)
    		I += (rnd.nextInt(2)==0) ? this.choise.charAt(i) : S2.choise.charAt(i);
    	return (new Knapsack(I));
    }

    public void calculateFitness() {
    	this.fitness = 0;
    	int w = 0;
    	for (int i=0 ; i<this.choise.length() ; i++){
    		this.fitness += (this.choise.charAt(i)-48)*this.ITEMS.get(i).getValue();
    		w += (this.choise.charAt(i)-48)*this.ITEMS.get(i).getWeight();
    		if (w > this.sack_threshold){
    			this.fitness = 0; // make it non zero and highest
    			return;
    		}
    	}
    	this.fitness -= 2*this.fitness; // make it negative for out engine
    }
    
    public void change_choise(String c){
    	this.choise = c;
    }
    // for MultiKnapsack
    public int size(){
    	return this.ITEMS.size();
    }
    
    public void setChoice(String s){
    	this.choise = s;
    }
    // ------------------
    public String toString() {
        String ts = "Items (w,v) :";
        for (int i = 0; i<this.choise.length(); i++){
        	if (this.choise.charAt(i)=='1')
        		ts += " , " + this.ITEMS.get(i).toString();
        }
        return ts + "\n" + "fitness: " + this.fitness;
    }

}

