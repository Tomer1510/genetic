package genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tomereiges on 4/11/15.
 */
public class Island<T extends Genetic<T>>{
    private int POP_SIZE;
    private double ELITE_RATE, MIGRATION_RATE, MUTATION_RATE;
    private List<Runner<T>> islands;
    private Class<T> clss;
    private Random rnd = new Random();

    public Island(Class<T> clss, int islands, double MIGRATION_RATE, int POP_SIZE, double MUTATION_RATE, double ELITE_RATE) {
        this.islands = new ArrayList<Runner<T>>();
        for (int i = 0;i < islands; i++) {
            this.islands.add(new Runner<T>(clss, Runner.Choose.TOUR, POP_SIZE, MUTATION_RATE, ELITE_RATE));
        }
        this.POP_SIZE = POP_SIZE;
        this.ELITE_RATE = ELITE_RATE;
        this.MUTATION_RATE = MUTATION_RATE;
        this.MIGRATION_RATE = MIGRATION_RATE;
        this.clss = clss;
    }

    public void setMigrationRate(double MIGRATION_RATE) {
        this.MIGRATION_RATE = (MIGRATION_RATE>=0)?MIGRATION_RATE:this.MIGRATION_RATE;
    }

    // Default method for adding an island - use the default parameters defined in c'tor
    public void addIsland() {
        this.islands.add(new Runner<T>(this.clss, Runner.Choose.TOUR, this.POP_SIZE, this.MUTATION_RATE, this.ELITE_RATE));
    }

    // Default method for adding multiple island with the default parameters defined in c'tor
    public void addIsland(int n) {
        for (int i = 0;i < n;i++) {
            this.addIsland();
        }
    }

    // Add an island with custom genetic parameters
    public void addIsland(int POP_SIZE, double MUTATION_RATE, double ELITE_RATE) {
        this.islands.add(new Runner<T>(this.clss, Runner.Choose.TOUR, POP_SIZE, MUTATION_RATE, ELITE_RATE));
    }

    // Add multiple islands with custom genetic parameters
    public void addIsland(int POP_SIZE, double MUTATION_RATE, double ELITE_RATE, int n) {
        for (int i = 0;i < n;i++) {
            this.addIsland(POP_SIZE, MUTATION_RATE, ELITE_RATE);
        }
    }

    // Add an island with a custom selection method
    public void addIsland(int POP_SIZE, double MUTATION_RATE, double ELITE_RATE, Runner.Choose CHOOSE) {
        this.islands.add(new Runner<T>(this.clss, CHOOSE, POP_SIZE, MUTATION_RATE, ELITE_RATE));
    }

    // Add multiple islands with custom selection method
    public void addIsland(int POP_SIZE, double MUTATION_RATE, double ELITE_RATE, Runner.Choose CHOOSE, int n) {
        for (int i = 0;i < n;i++) {
            this.addIsland(POP_SIZE, MUTATION_RATE, ELITE_RATE, CHOOSE);
        }
    }

    private void migrate() {
        for (int i = 0; i < this.islands.size();i++) {
            for (int j = 0; j < this.MIGRATION_RATE * this.islands.get(i).POP_SIZE; j++) {
                int index = rnd.nextInt(this.islands.get(i).POP_SIZE);
                T p = this.islands.get(i).population.remove(index);
                this.islands.get(i).POP_SIZE--;

                this.islands.get(i < this.islands.size()-1 ? i+1 : 0).population.add(p);
                this.islands.get(i < this.islands.size()-1 ? i+1 : 0).POP_SIZE++;
            }
        }
    }

    public void doEvolutionAll() {
        for (int i = 0;i < this.islands.size();i++) {
            System.out.println("Island " + (i+1) + ":");
            this.islands.get(i).doEvolution();
            System.out.println();
        }
        migrate();
        System.out.println("---------------------------------------------- \n\n\n\n");
    }

    public void run(int ITERATIONS) {
        for (int i = 0;i < ITERATIONS;i++) {
            System.out.println("-- Day " + (i+1) + " --\n\n");
            this.doEvolutionAll();
        }
    }




}
