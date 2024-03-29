package genetic;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tomereiges on 4/3/15.
 */


public class MainGuy {
    static Scanner reader = new Scanner(System.in);
    private enum Problem {
        STR, QUEENS, FUNC, BALDWIN, KNAPSACK, MULTISACK, GRAPH_KNAPSACK, MonoCipher, PolyCipher, KColoring
    }

    public static int count(int size, boolean withSpace, String text) {
        int ret = 0;
        int flag = 0;
        for(int i = 0;i < text.length();i++) {
            if((text.charAt(i) >= 'a' && text.charAt(i) <= 'z') || (withSpace && text.charAt(i) == ' ')) {
                flag++;
                if (flag >= size) {
                    ret++;
                }
            }
            else {
                flag=0;
            }

        }

        return ret;
    }


    public static void main(String[] args) throws IOException {


        if (false) {
            try {
                Graph g = new Graph("DSJC125.1.col");
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return;
        }
        //System.out.println(MonoCryptor.encrypt("zebrascdfghijklmnopqtuvwxy", "ALICE was beginning to get very tired of sitting by her sister on the bank and of having nothing to do: once or twice she had peeped into the book her sister was readin"));

        //if (true) return;;

        // Population size
        int POP_SIZE = 1024;

        // Mutation rate
        double MUTATION_RATE = 0.80;

        // Elite rate
        double ELITE_RATE = 0.8;

        // Selection method
        Runner.Choose SELECTION_METHOD = Runner.Choose.BASIC;

        // The problem to run on
        Problem problem = Problem.KColoring;

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
        int NUM_ITERATIONS = 3;


        // Ask user for input
        boolean interactive = false;


        // File path
        String path = "C:\\Users\\Guy Ezer\\Desktop\\Genetic\\WEING1.DAT";
        MK_Container a = new MK_Container(path);

        // Search Method
        String search_method = "BFS"; // choosing from DFS/BFS

        // Hiuristic Method
        String hiuristic_method = "FR"; // Choosing from Unlimited / Fraction (UL/FR)

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

        if (problem == Problem.KNAPSACK) {
            if (islands) {
                Island<Knapsack> island = new Island<Knapsack>(Knapsack.class, NUM_ISLANDS, MIGRATION_RATE, POP_SIZE, MUTATION_RATE, ELITE_RATE);
                island.run(NUM_ITERATIONS);
            }
            else {
                Runner<Knapsack> runner = new Runner<Knapsack>(Knapsack.class, SELECTION_METHOD, POP_SIZE, MUTATION_RATE, ELITE_RATE, life_time);
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

        if (problem == Problem.MULTISACK) {
            if (islands) {
                Island<ExtendedKnapsack> island = new Island<ExtendedKnapsack>(ExtendedKnapsack.class, NUM_ISLANDS, MIGRATION_RATE, POP_SIZE, MUTATION_RATE, ELITE_RATE);
                island.run(NUM_ITERATIONS);
            }
            else {
                Runner<ExtendedKnapsack> runner = new Runner<ExtendedKnapsack>(ExtendedKnapsack.class, SELECTION_METHOD, POP_SIZE, MUTATION_RATE, ELITE_RATE, life_time);
                runner.stopAtZero = stopAtZero;
                runner.run();
            }
        }

        if (problem == Problem.GRAPH_KNAPSACK){
            Branch_and_Bound s = new Branch_and_Bound();
            // choosing from best-first or dfs ("DFS"/"BFS")
            s.Search(search_method,hiuristic_method);
        }

        if (problem == Problem.MonoCipher) {
            if (islands) {
                Island<MonoCipher> island = new Island<MonoCipher>(MonoCipher.class, NUM_ISLANDS, MIGRATION_RATE, POP_SIZE, MUTATION_RATE, ELITE_RATE);
                island.run(NUM_ITERATIONS);
            }
            else {
                Runner<MonoCipher> runner = new Runner<MonoCipher>(MonoCipher.class, SELECTION_METHOD, POP_SIZE, MUTATION_RATE, ELITE_RATE, life_time);
                runner.stopAtZero = stopAtZero;
                runner.run();
            }
        }

        if (problem == Problem.PolyCipher) {
            if (islands) {
                Island<PolyCipher> island = new Island<PolyCipher>(PolyCipher.class, NUM_ISLANDS, MIGRATION_RATE, POP_SIZE, MUTATION_RATE, ELITE_RATE);
                island.run(NUM_ITERATIONS);
            }
            else {
                Runner<PolyCipher> runner = new Runner<PolyCipher>(PolyCipher.class, SELECTION_METHOD, POP_SIZE, MUTATION_RATE, ELITE_RATE, life_time);
                runner.stopAtZero = stopAtZero;
                runner.run();
            }
        }


        if (problem == Problem.KColoring) {
            KColoring.initGraph();
            System.out.println(KColoring.g);
            if (islands) {
                Island<KColoring> island = new Island<KColoring>(KColoring.class, NUM_ISLANDS, MIGRATION_RATE, POP_SIZE, MUTATION_RATE, ELITE_RATE);
                island.run(NUM_ITERATIONS);
            }
            else {
                Runner<KColoring> runner = new Runner<KColoring>(KColoring.class, SELECTION_METHOD, POP_SIZE, MUTATION_RATE, ELITE_RATE, life_time);
                runner.stopAtZero = stopAtZero;
                runner.run();
            }
        }

    }
       
        /*String key = "lemon";
        String text = "hello my name is guy did u mention my name is guy this is cool this is almost as cool as guy you know that my name is guy i think i said that already this is nice typing like that who am i joking no it is not tomer is my partner did i say that already  this is cool this is nice my name is guy his name is guy attack at dawn maple story im telling a  story";
        String cipher = PolyCryptor.encrypt(key, text);
        System.out.println(text);
        System.out.println(cipher);
        System.out.println(key);
        System.out.println(calculate_key_length(cipher));*/

}