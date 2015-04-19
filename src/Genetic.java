import java.util.Comparator;

/**
 * Created by tomereiges on 4/3/15.
 */
abstract public class Genetic<T> {
    //String value;
    //float fitness;
    public abstract T mate(T G2);
    public static class Comperator<T extends Genetic<T>> implements Comparator<T> {
        @Override
        public int compare(T S1, T S2) {
            return (int)(S1.getFitness() - S2.getFitness());
        }
    }

    public static Class getMyClass() { return Genetic.class; };
    public abstract void calculateFitness();
    public abstract void mutate();
    public abstract void generateValue();
    public abstract float getFitness();
    //public abstract float getDistance(T a, T b);
}
