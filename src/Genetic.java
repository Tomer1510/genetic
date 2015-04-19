import java.util.Comparator;

/**
 * Created by tomereiges on 4/3/15.
 */
abstract public class Genetic<T> {
    //String value;
    //int fitness;
    public abstract T mate(T G2);
    public abstract static class Comperator { };
    public static Class getMyClass() { return Genetic.class; };
    public abstract void calculateFitness();
    public abstract void mutate();
    public abstract void generateValue();
    public abstract float getFitness();
    //public abstract float getDistance(T a, T b);
}
