import com.sun.org.apache.bcel.internal.generic.POP;
import com.sun.tools.javac.jvm.Gen;

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

    public enum Choose {
        BASIC, RWS, TOUR
    }


    public Runner(Class<T> clss, Choose method, int POP_SIZE, double MUTATION_RATE, double ELITE_RATE) {

        this.clss = clss;
        this.CHOOSING_METHOD = method;
        this.POP_SIZE = POP_SIZE;
        this.ELITE_RATE = ELITE_RATE;
        this.MUTATION_RATE = MUTATION_RATE;
        this.NUMBER_OF_GENS = 500;
        this.TOUR_SIZE = (int)(Math.log(POP_SIZE)/Math.log(2));
        this.population = new ArrayList<T>();
        try {
            for (int i = 0; i < POP_SIZE; i++) {
                this.population.add((T) clss.newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void printStats() {
        double avg = 0, std = 0;
        for (T p : this.population) {
            avg += p.getFitness();
        }
        avg /= this.POP_SIZE;

        for (T p : this.population) {
            std += Math.pow(avg - p.getFitness(), 2);
        }
        std = Math.sqrt(std/this.POP_SIZE);

        System.out.println("Average fitness: " + avg);
        System.out.println("Std. of fitness: " + std);
    }


    /*
    def print_statistics(self):
		avg = sum([p.fitness for p in self.population])/float(self.POP_SIZE)
		std = float(sum([(avg - self.population[i].fitness)**2 	for i in xrange(0, self.POP_SIZE)]))
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
            //            this.population.set(i, this.population(pos1).(T)this.clss.getMethod("mate").invoke(this.population.get(pos1)));

            this.population.set(i, this.population.get(pos1).mate(this.population.get(pos2)));
            if (rnd.nextInt(100) <= this.MUTATION_RATE*100)
                this.population.get(i).mutate();
        }
    }

    private void sortPopulation() throws IllegalAccessException, InstantiationException{
        Collections.sort(this.population, (java.util.Comparator)this.clss.getClasses()[0].newInstance());

    }

    public void printFittest() {
        System.out.println(this.population.get(0));
    }

    public void doEvolution () {

        try {

            this.matePopulation();
            this.sortPopulation();


            this.printStats();
            this.printFittest();

        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void run(){
        double timeElapsed = System.nanoTime();
        for (int i = 0; i < this.NUMBER_OF_GENS; i++) {
            System.out.println("Generation " + (i+1) + ":");
            System.out.println("Time elapsed: " + (System.nanoTime() - timeElapsed)/1000000000 + " (s)");
            this.doEvolution();
            if (this.stopAtZero && this.population.get(0).getFitness() == 0) {
                break;
            }
        }
        System.out.println("--------------");
        this.printFittest();
    }
}
