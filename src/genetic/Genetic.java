package genetic;

import java.util.Comparator;

/**
 * Created by tomereiges on 4/3/15.
 */
abstract public class Genetic<T> {
    //String value;
    //float fitness;

    final static int LIFE_TIME = 7;
    private static int GENERATION = 0;

    public abstract T mate(T G2);
    public static class Comperator<T extends Genetic<T>> implements Comparator<T> {
        @Override
        public int compare(T S1, T S2) {
            return (int)(S1.getFitness() - S2.getFitness());
        }
    }

    public abstract void calculateFitness();
    public abstract void mutate();
    public abstract void generateValue();
    public abstract double getFitness();
    public abstract double getDistance(T a, T b);
    public static void incrementGeneration() {
        Genetic.GENERATION++;
    }

    public static int getGeneration() {
        return Genetic.GENERATION;
    }

    public void kill() { }

    protected int lifetime;
    public void set_lifetime(){
        this.lifetime = LIFE_TIME;
    }

    public int minus_lifetime(){
        return --this.lifetime;
    }

    public int get_lifetime(){
        return this.lifetime;
    }
}