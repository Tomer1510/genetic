
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tomereiges on 4/3/15.
 */


public class Main {

    private enum Problem {
        STR, QUEENS, FUNC, BALDWIN
    }

    public static void main(String[] args) {
        // Population size
        int POP_SIZE = 1000;

        // Mutation rate
        double MUTATION_RATE = 0.55;

        // Elite rate
        double ELITE_RATE = 0.30;

        // Selection method
        Runner.Choose SELECTION_METHOD = Runner.Choose.TOUR;

        // The problem to run on
        Problem problem = Problem.BALDWIN;

        // Stop when the fittest of a generation has fitness of 0
        boolean stopAtZero = true;

        // True if life-time survival method is used
        boolean life_time = false;

        // Activate islands
        boolean islands = false;

        // Migration rate for islands
        double MIGRATION_RATE = 0.35;

        // Amount of islands
        int NUM_ISLANDS = 5;

        // How many iterations for islands
        int NUM_ITERATIONS = 500;



        if (problem == Problem.BALDWIN) {
            if (islands) {
                Island<Baldwin> island = new Island<Baldwin>(Baldwin.class, NUM_ISLANDS, MIGRATION_RATE, POP_SIZE, MUTATION_RATE, ELITE_RATE);
                island.run(NUM_ITERATIONS);
            }
            else {
                Runner<Baldwin> runner = new Runner<Baldwin>(Baldwin.class, SELECTION_METHOD, POP_SIZE, MUTATION_RATE, ELITE_RATE, life_time);
                runner.stopAtZero = stopAtZero;
                runner.run();
            }
        }

        if (problem == Problem.STR) {
            if (islands) {
                Island<STR> island = new Island<STR>(STR.class, NUM_ISLANDS, MIGRATION_RATE, POP_SIZE, MUTATION_RATE, ELITE_RATE);
                island.run(NUM_ITERATIONS);
            }
            else {
                Runner<STR> runner = new Runner<STR>(STR.class, SELECTION_METHOD, POP_SIZE, MUTATION_RATE, ELITE_RATE, life_time);
                runner.stopAtZero = stopAtZero;
                runner.run();
            }
        }

        if (problem == Problem.QUEENS) {
            if (islands) {
                Island<Queens> island = new Island<Queens>(Queens.class, NUM_ISLANDS, MIGRATION_RATE, POP_SIZE, MUTATION_RATE, ELITE_RATE);
                island.run(NUM_ITERATIONS);
            }
            else {
                Runner<Queens> runner = new Runner<Queens>(Queens.class, SELECTION_METHOD, POP_SIZE, MUTATION_RATE, ELITE_RATE, life_time);
                runner.stopAtZero = stopAtZero;
                runner.run();
            }
        }
        
        if (problem == Problem.FUNC) {
            if (islands) {
                Island<Function> island = new Island<Function>(Function.class, NUM_ISLANDS, MIGRATION_RATE, POP_SIZE, MUTATION_RATE, ELITE_RATE);
                island.run(NUM_ITERATIONS);
            }
            else {
                Runner<Function> runner = new Runner<Function>(Function.class, SELECTION_METHOD, POP_SIZE, MUTATION_RATE, ELITE_RATE, life_time);
                runner.stopAtZero = stopAtZero;
                runner.run();
            }
        }



    }

}
