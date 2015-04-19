import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tomereiges on 4/11/15.
 */
public class Island<T extends Genetic<T>>{
    int POP_SIZE;
    double ELITE_RATE, MIGRATION_RATE;
    List<Runner<T>> islands;
    boolean migrationFlag = false;
    Class<T> clss;
    Random rnd = new Random();

    public Island(Class<T> clss, int islands, int POP_SIZE, double MIGRATION_RATE, double ELITE_RATE) {
        this.islands = new ArrayList<Runner<T>>();
        for (int i = 0;i < islands; i++) {
            this.islands.add(new Runner<T>(clss, Runner.Choose.TOUR, 2000, 0.65, ELITE_RATE));
        }
        this.POP_SIZE = POP_SIZE;
        this.ELITE_RATE = ELITE_RATE;
        this.MIGRATION_RATE = MIGRATION_RATE;
        this.clss = clss;
    }

    public void migrate() {
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
        System.out.println("---------------------------------------------- \n\n\n\n");
    }




}
