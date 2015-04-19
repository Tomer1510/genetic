import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tomereiges on 4/3/15.
 */


public class Main {


    public static void main(String[] args) {
        /*
        Runner<Queens> runner = new Runner<Queens>(Queens.class, Runner.Choose.TOUR, 2000, 0.65, 0.45);
        runner.run();

        Runner<STR> runner2 = new Runner<STR>(STR.class, Runner.Choose.TOUR, 2000, 0.55, 0.35);
        runner2.run();
        */

        Island<Queens> island = new Island<Queens>(Queens.class, 5, 2000, 0.45, 0.55);
        island.doEvolutionAll();
        island.doEvolutionAll();
        island.doEvolutionAll();
        island.doEvolutionAll();
        island.doEvolutionAll();
        island.doEvolutionAll();
    }

}
