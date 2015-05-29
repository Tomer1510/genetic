package genetic;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by tomereiges on 4/3/15.
 */


public class Runner<T extends Genetic<T>> {

    List<T> population;
    double ELITE_RATE, MUTATION_RATE;
    int NUMBER_OF_GENS, POP_SIZE, TOUR_SIZE;
    static Random rnd = new Random(System.currentTimeMillis());
    Class<T> clss;
    Choose CHOOSING_METHOD;
    boolean stopAtZero = true;
    double std, avg;
    private double LOCAL_OPTIMUM_REL_STD = 0.0;
    private double LOCAL_OPTIMUM_FIX = 0.0;
    private int ELITE_TOUR_SIZE;
    private boolean L_FLAG;
    public enum Choose {
        BASIC, RWS, TOUR, ELITE_TOUR
    }



    public Runner(Class<T> clss, Choose method, int POP_SIZE, double MUTATION_RATE, double ELITE_RATE) {

        this.clss = clss;
        this.CHOOSING_METHOD = method;
        this.POP_SIZE = POP_SIZE;
        this.ELITE_RATE = ELITE_RATE;
        this.MUTATION_RATE = MUTATION_RATE;
        this.NUMBER_OF_GENS = 5000;
        this.TOUR_SIZE = (int)(Math.log(POP_SIZE)/Math.log(2));
        this.ELITE_TOUR_SIZE = (int)(Math.log((int)(POP_SIZE*ELITE_RATE))/Math.log(2));
        this.L_FLAG = false;
        this.population = new ArrayList<T>();
        try {
            for (int i = 0; i < POP_SIZE; i++) {
                this.population.add((T) clss.newInstance());
                this.population.get(i).set_lifetime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Runner(Class<T> clss, Choose method, int POP_SIZE, double MUTATION_RATE, double ELITE_RATE,boolean L) {

        this.clss = clss;
        this.CHOOSING_METHOD = method;
        this.POP_SIZE = POP_SIZE;
        this.ELITE_RATE = ELITE_RATE;
        this.MUTATION_RATE = MUTATION_RATE;
        this.NUMBER_OF_GENS = 5000;
        this.TOUR_SIZE = (int)(Math.log(POP_SIZE)/Math.log(2));
        this.ELITE_TOUR_SIZE = (int)(Math.log((int)(POP_SIZE*ELITE_RATE))/Math.log(2));
        this.L_FLAG = L;
        this.population = new ArrayList<T>();
        try {
            for (int i = 0; i < POP_SIZE; i++) {
                this.population.add((T) clss.newInstance());
                if (L_FLAG) this.population.get(i).set_lifetime();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printStats() {
        this.avg = 0;
        this.std = 0;
        for (T p : this.population) {
            this.avg += p.getFitness();
        }
        this.avg /= this.POP_SIZE;

        for (T p : this.population) {
            this.std += Math.pow(this.avg - p.getFitness(), 2);
        }
        this.std = Math.sqrt(this.std/this.POP_SIZE);

        System.out.println("Average fitness: " + this.avg);
        System.out.println("Std. of fitness: " + this.std);
        System.out.println("Relative std. of fitness: " + (100*this.std/this.avg) + "%");
        System.out.println("Total distance: " + this.total_distance());
    }
 
 
    /*
    def print_statistics(self):
                avg = sum([p.fitness for p in self.population])/float(self.POP_SIZE)
                std = float(sum([(avg - self.population[i].fitness)**2  for i in xrange(0, self.POP_SIZE)]))
                std = math.sqrt(std/self.POP_SIZE)
                print "Average of fitness: ", avg
                print "Std. of fitness: ", std
     */


    private int chooseRWS() {
        int max_fitness = (int)this.population.get(this.population.size()-1).getFitness();
        int total_fitness = 0;
        for (T p : this.population) {
            total_fitness += Math.abs(max_fitness - p.getFitness())+1;
        }
        int pos = rnd.nextInt(total_fitness-1) + 1;
        int i = 0;
        for (T p : this.population) {
            if (Math.abs(max_fitness - p.getFitness()) + 1 + i >= pos)
                return this.population.indexOf(p);
            i += Math.abs(max_fitness - p.getFitness()) + 1;
        }
        return -1;
    }

    private int chooseBasic() {
        return rnd.nextInt((int)(this.POP_SIZE * this.ELITE_RATE));
    }

    private int chooseTour() {
        int min_i = -1;
        for (int i = 0;i < this.TOUR_SIZE;i++) {
            int pos = rnd.nextInt(this.POP_SIZE);
            if (min_i == -1 || this.population.get(pos).getFitness() < this.population.get(min_i).getFitness())
                min_i = pos;
        }
        return (min_i == -1)?rnd.nextInt(this.TOUR_SIZE):min_i;
    }

    private int chooseEliteTour(){
        int min_i = -1;
        for (int i = 0;i < this.ELITE_TOUR_SIZE;i++) {
            int pos = rnd.nextInt((int)(this.POP_SIZE * this.ELITE_RATE));
            if (min_i == -1 || this.population.get(pos).getFitness()
                    < this.population.get(min_i).getFitness())
                min_i = pos;
        }
        return (min_i == -1)?rnd.nextInt(this.ELITE_TOUR_SIZE):min_i;
    }

    private void matePopulation() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        for (int i = (int)(this.POP_SIZE * this.ELITE_RATE);i < this.POP_SIZE;i ++) {
            int pos1 = 0, pos2 = 0;
            if (this.CHOOSING_METHOD == Choose.BASIC) {
                pos1 = this.chooseBasic();
                pos2 = this.chooseBasic();
            }
            else if (this.CHOOSING_METHOD == Choose.RWS) {
                pos1 = this.chooseRWS();
                pos2 = this.chooseRWS();
            }
            else if (this.CHOOSING_METHOD == Choose.TOUR) {
                pos1 = this.chooseTour();
                pos2 = this.chooseTour();
            }
            else if (this.CHOOSING_METHOD == Choose.ELITE_TOUR){
                pos1 = this.chooseEliteTour();
                pos2 = this.chooseEliteTour();
            }
            //            this.population.set(i, this.population(pos1).(T)this.clss.getMethod("mate").invoke(this.population.get(pos1)));

            this.population.set(i, this.population.get(pos1).mate(this.population.get(pos2)));
            if (L_FLAG) this.population.get(i).set_lifetime();
            if (rnd.nextInt(100) <= this.MUTATION_RATE*100)
                this.population.get(i).mutate();
        }
    }

    private void life_time(){
        for (int i = this.POP_SIZE;i < this.POP_SIZE;i ++) {
            if (this.population.get(i).minus_lifetime()==0){

                int pos1 = 0, pos2 = 0;
                boolean flag = true;
                if (this.CHOOSING_METHOD == Choose.BASIC) {
                    while (flag){
                        pos1 = this.chooseBasic();
                        pos2 = this.chooseBasic();
                        flag = !(this.population.get(pos1).get_lifetime()> 0 && this.population.get(pos2).get_lifetime()> 0);
                    }
                }
                else if (this.CHOOSING_METHOD == Choose.RWS) {
                    while (flag){
                        pos1 = this.chooseRWS();
                        pos2 = this.chooseRWS();
                        flag = !(this.population.get(pos1).get_lifetime()> 0 && this.population.get(pos2).get_lifetime()> 0);
                    }                   }
                else if (this.CHOOSING_METHOD == Choose.TOUR) {
                    while (flag){
                        pos1 = this.chooseTour();
                        pos2 = this.chooseTour();
                        flag = !(this.population.get(pos1).get_lifetime()> 0 && this.population.get(pos2).get_lifetime()> 0);
                    }
                }
                else if (this.CHOOSING_METHOD == Choose.ELITE_TOUR){
                    while (flag){
                        pos1 = this.chooseEliteTour();
                        pos2 = this.chooseEliteTour();
                        flag = !(this.population.get(pos1).get_lifetime()> 0 && this.population.get(pos2).get_lifetime()> 0);
                    }
                }

                this.population.set(i, this.population.get(pos1).mate(this.population.get(pos2)));
                this.population.get(i).set_lifetime();
                if (rnd.nextInt(100) <= this.MUTATION_RATE*100)
                    this.population.get(i).mutate();

            }
        }
    }


    private void sortPopulation() throws IllegalAccessException, InstantiationException{
        Collections.sort(this.population, (java.util.Comparator) this.clss.getClasses()[0].newInstance());
        //Collections.sort(this.population, (java.util.Comparator) T.Comperator.class.newInstance());
        //Collections.sort(this.population, (java.util.Comparator) Genetic.Comperator.class.newInstance());
    }

    public void printFittest() {
        System.out.println(this.population.get(0));
        //System.out.println(this.population.get(1));
    }

    public void doEvolution () {

        try {

            if (L_FLAG) this.life_time();
            this.matePopulation();
            this.sortPopulation();


            this.printStats();
            this.printFittest();

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    private double total_distance(){
        // sums all of the distance of portion of population
        double sum = 0;
        for (int i = 0; i< this.POP_SIZE-1; i++){
            sum += this.population.get(i).getDistance(this.population.get(i),
                    this.population.get(i+1))*(1 - (float)i/this.POP_SIZE);
        }

        return sum;
    }

    /*
        LOCAL_OPTIMUM_REL_STD (%) - when the relative std is lower than this,
            we assume we're approaching local optimum

        LOCAL_OPTIMUM_FIX - when we try to avoid the local optimum we use this factor
            to increase the mutation rate and lower the elite rate
    */
    private void handleLocalOptimum() {
        double relativeStd = this.std/this.avg;
        if (relativeStd < this.LOCAL_OPTIMUM_REL_STD) {
            if (this.MUTATION_RATE <= 0.90)
                this.MUTATION_RATE*=(1 + this.LOCAL_OPTIMUM_FIX);

            if (this.ELITE_RATE >= 0.10)
                this.ELITE_RATE*=(1 - this.LOCAL_OPTIMUM_FIX);
        }
    }

    public void run(){
        double timeElapsed = System.nanoTime();
        //int fitness_array    
        int times = 1;
        for (int i = 0; i < this.NUMBER_OF_GENS; i++, ++times) {
            System.out.println("Generation " + (i+1) + ":");
            System.out.println("Time elapsed: " +
                   (System.nanoTime() - timeElapsed)/1000000000 + " (s)");
            System.out.println("Cache hit: "+(100F*(float)Text.cacheHit/Text.cacheCounter)+"%");
            Text.cacheHit=0;
            Text.cacheCounter=1;
            this.doEvolution();
            this.handleLocalOptimum();

            if (this.stopAtZero && this.population.get(0).getFitness() == 0) {
                break;
            }
        }
        double tt = (System.nanoTime() - timeElapsed);
        System.out.println("--------------");
        this.printFittest();
        System.out.println("~~~~------~~~~");
        System.out.println("Avg. running time per generation: "+ (tt/times));
        System.out.println(times);
        System.out.println(tt/times);
    }
}