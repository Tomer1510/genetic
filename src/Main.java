import com.sun.org.apache.bcel.internal.generic.POP;

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
        double ELITE_RATE = 0.25;

        // Selection method
        Runner.Choose SELECTION_METHOD = Runner.Choose.TOUR;

        // The problem to run on
        Problem problem = Problem.BALDWIN;

        // Stop when the fittest of a generation has fitness of 0
        boolean stopAtZero = true;




        if (problem == Problem.BALDWIN) {
            Runner<Baldwin> runner = new Runner<Baldwin>(Baldwin.class, SELECTION_METHOD, POP_SIZE, MUTATION_RATE, ELITE_RATE);
            runner.stopAtZero = stopAtZero;
            runner.run();
        }

        if (problem == Problem.STR) {
            Runner<STR> runner = new Runner<STR>(STR.class, SELECTION_METHOD, POP_SIZE, MUTATION_RATE, ELITE_RATE);
            runner.stopAtZero = stopAtZero;
            runner.run();
        }

        if (problem == Problem.QUEENS) {
            Runner<Queens> runner = new Runner<Queens>(Queens.class, SELECTION_METHOD, POP_SIZE, MUTATION_RATE, ELITE_RATE);
            runner.stopAtZero = stopAtZero;
            runner.run();
        }


        //Island<STR> island = new Island<STR>(STR.class, 0, 0.35, 2048, 0.64, 0.45);
        //island.addIsland(2048, 0.64, 0.35, Runner.Choose.TOUR, 2);
        //island.run(5);
    }

}
