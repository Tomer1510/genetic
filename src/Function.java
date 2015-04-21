/**
 * Created by tomereiges on 4/3/15.
 */

import java.util.Random;
import java.util.Comparator;

public class Function extends Genetic<Function>{

    public float getDistance(Function a, Function b) {
        return Math.abs(a.fitness - b.fitness);
    }

    static Random rnd = new Random(System.currentTimeMillis());
    private int x1 , x2; // function parameters

    private float fitness;

    public float getFitness() {
        return this.fitness;
    }

    public Function() {
        this.generateValue();
        this.calculateFitness();
    }
    
    public Function(int m, int n){
    	this.x1 = m;
    	this.x2 = n;
    	this.calculateFitness();
    }

    public void generateValue() {
       this.x1 = rnd.nextInt(Integer.MAX_VALUE) * (2*rnd.nextInt(2)-1);
       this.x2 = rnd.nextInt(Integer.MAX_VALUE) * (2*rnd.nextInt(2)-1);
    }

    public void mutate() {
        // change a random bit in x1, x2 or both
    	int mode = rnd.nextInt(3);
    	// mode 0 - change x1
    	// mode 1 - change x2
    	// mode 2 - change both
    	if (mode > 0){
    		int pos1 = rnd.nextInt(Integer.toBinaryString(x1).length());
    		this.x1 = x1^(1<<pos1);
    	}
    	if (mode != 1){
    		int pos2 = rnd.nextInt(Integer.toBinaryString(x1).length());
    		this.x2 = x2^(1<<pos2);
    	}
        this.calculateFitness();
    }

    @Override
    public Function mate(Function S2) {
        // taking a bit from S1 or S2 with a 50% chance
    	return new Function(combine_num(this.x1,S2.x1),combine_num(this.x2,S2.x2));
    }
    
    private int combine_num (int a, int b){
    	//taking a bit from a or b with a 50% chance
		String _a = Integer.toBinaryString(Math.min(Math.abs(a), Math.abs(b)));
		String _b = Integer.toBinaryString(Math.max(Math.abs(a), Math.abs(b)));
		String padd = "";
		for (int i=0; i<_b.length()-_a.length(); i++) padd += "0";
		_a = padd+_a;
		int c = 0;
		for (int i=0; i<_b.length(); i++){
			c = c<<1;
			if (rnd.nextInt(2)==0)
				c += _a.charAt(i)-48;
			else
				c += _b.charAt(i)-48;
		}
		// declaring the sign
		if (rnd.nextInt(2)==0)
			if (a>0)	return c;
			else return -1*c;
		else
			if (b>0) return c;
			else return -1*c;
    }

    public void calculateFitness() {
        this.fitness = (float) (20+x1*x1+x2*x2 - 10*(Math.cos(2*Math.PI*x1) + Math.cos(2*Math.PI*x2))); // declared function
    }

    public String toString() {
        return ("x1: "+this.x1 + " , x2: " + this.x2 +" ")
            + "\nFitness: "+this.fitness + "\n";
    }

}

