package genetic;

public final class Item {
	private int weight,value;
	
	public Item(int w, int v){
		this.weight = w;
		this.value = v;
	}
	
	public int getWeight(){
		return this.weight;
	}
	
	public int getValue(){
		return this.value;
	}
		
	public String toString(){
		return "("+this.weight+";"+this.value+")";
	}
}
