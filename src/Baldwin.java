import java.util.*;

/**
 * Created by tomereiges on 4/19/15.
 */
public class Baldwin extends Genetic<Baldwin>{
    float fitness;
    int N;
    double RANDOM_PERCENT, TRUE_PERCENT, FALSE_PERCENT;
    String value, target;
    int GUESSING_TRIES;
    static Random rnd = new Random(System.currentTimeMillis());

    public static class Comperator extends Genetic.Comperator<Baldwin> {
        @Override
        public int compare(Baldwin G1, Baldwin G2) {
            return (G1.fitness != 0 || G2.fitness != 0) ? super.compare(G1, G2) : (G1.getAmountOfRandom() - G2.getAmountOfRandom());
        }
    }


    @Override
    public Baldwin mate(Baldwin B2) {
        if (this.target != B2.target) {
            System.out.println("Targets don't match!");
            return null;
        }
        String v = "";
        for (int i = 0;i < this.value.length();i++) {
            v += rnd.nextBoolean()?this.value.charAt(i):B2.value.charAt(i);
        }
        return new Baldwin(v, this.target);
    }


    public int getAmountOfRandom() {
        return this.value.length() - this.value.replace("?", "").length();
    }

    public int getAmountOfFalse() {
        int ret = 0;
        for (int i = 0;i < this.value.length();i++)
            ret += (this.value.charAt(i) != '?' && this.value.charAt(i) != this.target.charAt(i))?1:0;
        return ret;
    }

    public int getAmountOfTrue() {
        int ret = 0;
        for (int i = 0;i < this.value.length();i++)
            ret += (this.value.charAt(i) == this.target.charAt(i))?1:0;
        return ret;
    }

    // Here instead of mutation we will apply the "learning" algorithm
    public void mutate() {
        int pos = rnd.nextInt(this.value.length());
        char val = '?';
        switch (rnd.nextInt(3)) {
            case 0: val='0'; break;
            case 1: val='1'; break;
            case 2: val='?'; break;
        }
        this.calculateFitness();
    }

    private String guess() {
        String ret = "";

        for (int i = 0;i < this.value.length();i++) {
            if (this.value.charAt(i) == '?')
                ret += (rnd.nextBoolean())?'1':'0';
            else
                ret += this.value.charAt(i);
        }

        return ret;
    }

    public void calculateFitness() {
        this.fitness = 0;
        for (int i = 0;i < this.GUESSING_TRIES;i++) {
            if (this.guess().compareTo(this.target) == 0)
                break;
            this.fitness++;
        }
    }

    public void generateValue() {
        this.value = "";
        List<Integer> indexes= new ArrayList<Integer>();
        for (int i = 0;i < N;i++)
            indexes.add(i);
        Collections.shuffle(indexes);

        for (int i = 0;i < N;i++) {
            int val = indexes.remove(0);

            // TRUE
            if (val >= (N*(this.RANDOM_PERCENT + this.TRUE_PERCENT))) {
                this.value += this.target.charAt(i);
            }

            // FALSE
            else if (val >= N*(this.RANDOM_PERCENT)) {
                this.value += this.target.charAt(i)=='1'?'0':'1';
            }

            //RANDOM
            else {
                this.value += '?';
            }
        }
    }

    public float getFitness() {
        return this.fitness;
    }


    public String toString() {
        return ("Value: "+this.value + "\n" + "Target: " + this.target)
                + "\nFitness: "+this.fitness + "\n" + "Amount of ?: " + (100*this.getAmountOfRandom()/((float)this.value.length())) + "%\n\n";
    }


    public Baldwin() {
        this.N = 20;
        this.RANDOM_PERCENT = 0.5;
        this.TRUE_PERCENT = 0.25;
        this.FALSE_PERCENT = 0.25;
        this.GUESSING_TRIES = 1000;
        this.target = "10110101100001101010";
        this.generateValue();
        this.calculateFitness();
    }



    public Baldwin(String value, String target) {
        this.N = 20;
        this.RANDOM_PERCENT = 0.5;
        this.TRUE_PERCENT = 0.25;
        this.FALSE_PERCENT = 0.25;
        this.GUESSING_TRIES = 1000;
        this.value = value;
        this.target = target;
        this.calculateFitness();
    }


    public Baldwin(Baldwin b) {
        this.N = b.N;
        this.RANDOM_PERCENT = b.RANDOM_PERCENT;
        this.TRUE_PERCENT = b.TRUE_PERCENT;
        this.FALSE_PERCENT = b.FALSE_PERCENT;
        this.GUESSING_TRIES = b.GUESSING_TRIES;
        this.target = b.target;
        this.value = b.value;
        this.calculateFitness();
    }

    // Unused at the moment
    public Baldwin(int GUESSING_TRIES, float RANDOM, float TRUE, float FALSE) {
        if (this.RANDOM_PERCENT <= 1) {
            this.RANDOM_PERCENT = RANDOM;
        }
        else {
            this.RANDOM_PERCENT = 1;
        }

        if (this.RANDOM_PERCENT + TRUE + FALSE <= 1) {
            this.TRUE_PERCENT = TRUE;
            this.FALSE_PERCENT = FALSE;
        }
        else {
            this.TRUE_PERCENT = (this.FALSE_PERCENT = (1-this.RANDOM_PERCENT)/2);
        }
        this.GUESSING_TRIES = GUESSING_TRIES;
        this.generateValue();
        this.calculateFitness();
    }

    public Baldwin(int GUESSING_TRIES, float RANDOM, float TRUE, float FALSE, String target) {
        if (this.RANDOM_PERCENT <= 1) {
            this.RANDOM_PERCENT = RANDOM;
        }
        else {
            this.RANDOM_PERCENT = 1;
        }

        if (this.RANDOM_PERCENT + TRUE + FALSE <= 1) {
            this.TRUE_PERCENT = TRUE;
            this.FALSE_PERCENT = FALSE;
        }
        else {
            this.TRUE_PERCENT = (this.FALSE_PERCENT = (1-this.RANDOM_PERCENT)/2);
        }
        this.target = target;
        this.GUESSING_TRIES = GUESSING_TRIES;
        this.generateValue();
        this.calculateFitness();
    }



}
