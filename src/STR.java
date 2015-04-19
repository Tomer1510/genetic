/**
 * Created by tomereiges on 4/3/15.
 */

import java.util.Random;
import java.util.Comparator;

public class STR extends Genetic<STR>{

    public float getDistance(STR a, STR b) {
        if (!a.target.equals(b.target))
            return -1;
        return new STR(a.getValue(), b.getValue()).getFitness();
    }

    static Random rnd = new Random(System.currentTimeMillis());
    private String target, value;

    private float fitness;

    public String getValue() {
        return this.value;
    }

    public float getFitness() {
        return this.fitness;
    }

    public STR() {
        this.target = "hello world";
        this.generateValue();
        this.calculateFitness();
    }

    public STR(String s) {
        this.target = "hello world";
        this.value = s;
        this.calculateFitness();
    }

    public STR(String s, String target) {
        this.target = target;
        this.value = s;
        this.calculateFitness();
    }

    public void generateValue() {
        this.value = "";
        for (int i = 0;i < this.target.length();i++) {
            char c = (char)(rnd.nextInt(90) + 32);
            value += c;
        }
    }

    public void mutate() {
        int pos = rnd.nextInt(this.value.length());
        char val = (char)(((int)this.value.charAt(pos) + rnd.nextInt(90)) % 122);
        this.value = this.value.substring(0, pos) + val + this.value.substring(pos+1);
        this.calculateFitness();
    }

    @Override
    public STR mate(STR S2) {
        if (this.target != S2.target) {
            System.out.println("Targets don't match!");
            return null;
        }
        String target = this.target;
        String s = "";
        for (int i = 0;i < target.length();i++) {
            if (rnd.nextBoolean())
                s += this.value.charAt(i);
            else
                s += S2.value.charAt(i);
        }
        return new STR(s, target);
    }

    public void calculateFitness() {
        int fitness = 0;
        for (int i = 0;i < this.target.length();i++) {
            fitness += (this.target.charAt(i) == this.value.charAt(i))?0:100;
        }
        this.fitness = fitness;
    }

    public String toString() {
        return ("Value: "+this.value + "" + "\t Target: " + this.target)
            + "\nFitness: "+this.fitness + "\n";
    }

}

