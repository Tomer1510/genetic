package genetic;

public class K_Node {
	public int fitness; // fitness is basically 'value'
	public String choice;
	public int level; // depth in tree
	
	public double potential; // estimate
	
	
	K_Node(int a, String b){
		this.fitness = a;
		this.choice = b;
	}
	
	K_Node(int a,String b, int c){
		this.fitness = a;
		this.choice = b;
		this.level = c;
	}
	
	K_Node(int a, String b, int c, double d){
		this.fitness = a;
		this.choice = b;
		this.level = c;
		this.potential = d;
	}
}
