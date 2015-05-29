package genetic;

import java.util.Objects;

/**
* @author Guy Ezer
* Silly and short implementation of a Pair container (like c++).
* This class is a container of two values. Can be treated as a Key-Value container.
* Can be sorted by Value if it is a primitive number type (integer, double, float, long).
*/
	
public final class Pair<T1, T2> implements Comparable <Pair<T1,T2>>{
	
	public T1 first; //key
	public T2 second;//value
	
	/**
	 * Constructor of the Pair class.
	 * @param a , the Key of the object, from type T1.
	 * @param b , the Value of the object, from type T2. 
	 */
	public Pair (T1 a , T2 b){
		//c'tor
		this.first = a;
		this.second = b;
	}
	
	/**
	 * Copy constructor of the Pair class. Performs a deep copy from another object.
	 * @param a , another Pair object, to be copied.
	 */
	public Pair (Pair<T1,T2> a){
		//copy c'tor
		this.first = a.first;
		this.second = a.second;
	}
	
	/**
	 * Default constructor of the Pair class. Set both the Key and the Value to null.
	 */
	public Pair(){
		//default c'tor
		this.first = null;
		this.second = null;
	}
	
	
	/**
	 * Checks if the Pair object has null fields.
	 * @return true if the Pair object has any null fields.
	 */
	public boolean isEmpty(){
		//true if both values are null
		return (this.first == null && this.second == null);
	}
	
	
	/**
	 * Return the first element of the pair - the key.
	 * @return the key of the Pair object.
	 */
	public T1 getKey(){
		//for formalization. similar to javafx's Pair.
		return this.first;
	}
	
	
	/**
	 * Returns the second element of the pair - the value.
	 * @return the value of the Pair object.
	 */
	public T2 getValue(){
		//for formalization. similar to javafx's Pair.
		return this.second;
	}
	
	/**
	 * Generates a string of the Pair object.
	 * @return String representing the Pair object.
	 */
	public String toString(){
		//generates a string
		return "("+this.first.toString()+" , "+this.second.toString()+")";
	}
	
	/**
	 * Checks if two Pair Objects are equal.
	 * @param a, another Pair Object
	 * @return true if equal, false if not.
	 */
	public boolean equals(Pair<T1,T2> a){
		//returns if two object are equal, does not care about nulls.
		return (this.first == a.first && this.second == a.second);
	}
	
	
	/**
	 * Returns a hashcode of the pair.
	 * @return hashcode of the pair.
	 */
	public int hashCode(){
		//for equals, or hash tables
		return Objects.hash(this.first,this.second);
	}
	
	
	/**
	 * Compares to another Pair object.
	 * @param Another Pair object, arg0
	 * @return 1 if arg0 is smaller than this object, 0 if equals, and -1 if larger.
	 */
	@Override
	public int compareTo(Pair<T1, T2> arg0) {
		//for making this class comparable, so it can be sorted.
		switch(this.second.getClass().getName()){
		
		case("java.lang.Integer"):
			return Integer.compare((Integer)this.second, (Integer)arg0.second);
		
		case("java.lang.Float"):
			return Float.compare((Float)this.second, (Float)arg0.second);
		
		case("java.lang.Double"):
			return Double.compare((Double)this.second, (Double)arg0.second);
		
		case("java.lang.Long"):
			return Long.compare((Long)this.second, (Long)arg0.second);
		
		default:
			return 0;
		}
		
	}
	
	
	
}
