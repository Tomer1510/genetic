package genetic;

public class FrContainer implements Comparable<FrContainer>{
	public double ratio;
	public int index;
	
	public FrContainer(double a, int b){
		ratio = a;
		index = b;
	}

	@Override
	public int compareTo(FrContainer a) {
		return (Double.compare(this.ratio, a.ratio)); 
	}
}
