
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by tomereiges on 4/3/15.
 */


public class Main {
    static Scanner reader = new Scanner(System.in);
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


        // Ask user for input
        boolean interactive = true;


        if (interactive) {
            String in = "";
            System.out.println("Please enter the parameters: ");

            System.out.println("POP_SIZE(" + POP_SIZE + "): ");
            in = reader.nextLine();
            if (in.length() > 0)
                POP_SIZE = Integer.parseInt(in);

            System.out.println("MUTATION_RATE(" + MUTATION_RATE + "): ");
            in = reader.nextLine();
            if (in.length() > 0)
                MUTATION_RATE = Double.parseDouble(in);


            System.out.println("ELITE_RATE(" + ELITE_RATE + "): ");
            in = reader.nextLine();
            if (in.length() > 0)
                ELITE_RATE = Double.parseDouble(in);


            System.out.println("Selection method(TOUR) TOUR/BASIC/ELITE_TOUR/RWS: ");
            in = reader.nextLine();
            if (in.length() > 0) {
                if (in.equals("TOUR"))
                    SELECTION_METHOD = Runner.Choose.TOUR;
                if (in.equals("BASIC"))
                    SELECTION_METHOD = Runner.Choose.BASIC;
                if (in.equals("ELITE_TOUR"))
                    SELECTION_METHOD = Runner.Choose.ELITE_TOUR;
                if (in.equals("RWS"))
                    SELECTION_METHOD = Runner.Choose.RWS;
            }

            System.out.println("Selection problem(BALDWIN) STR/QUEENS/FUNC/BALDWIN: ");
            in = reader.nextLine();
            if (in.length() > 0) {
                if (in.equals("STR"))
                    problem = Problem.STR;
                if (in.equals("QUEENS"))
                    problem = Problem.QUEENS;
                if (in.equals("FUNC"))
                    problem = Problem.FUNC;
                if (in.equals("BALDWIN"))
                    problem = Problem.BALDWIN;
            }

            System.out.println("Stop engine when best fitness is 0(y)? y/n ");
            in = reader.nextLine();
            if (in.length() > 0)
                stopAtZero = in.charAt(0)=='y';

            System.out.println("Use life time optimization as a survival method(n)? y/n ");
            in = reader.nextLine();
            if (in.length() > 0)
                life_time = in.charAt(0)=='y';

            System.out.println("Run the engine with islands(n)? y/n ");
            in = reader.nextLine();
            if (in.length() > 0) {
                islands = in.charAt(0) == 'y';
                if (islands) {
                    System.out.println("MIGRATION_RATE(" + MIGRATION_RATE + "): ");
                    in = reader.nextLine();
                    if (in.length() > 0)
                        MIGRATION_RATE = Double.parseDouble(in);

                    System.out.println("Amount of islands(" + NUM_ISLANDS + "): ");
                    in = reader.nextLine();
                    if (in.length() > 0)
                        NUM_ISLANDS = Integer.parseInt(in);

                    System.out.println("How many iterations?(" + NUM_ITERATIONS + ") ");
                    in = reader.nextLine();
                    if (in.length() > 0)
                        NUM_ITERATIONS = Integer.parseInt(in);
                }
            }


        }


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
