/**
 * Created by tomereiges on 4/3/15.
 */

import java.util.*;

public class Queens extends Genetic<Queens>{

    int N;
    List<Integer> queens;

    public static class Comperator implements Comparator<Queens> {
        @Override
        public int compare(Queens S1, Queens S2) {
            return (S1.fitness - S2.fitness);
        }
    }

    public static Class<Queens> getMyClass() {
        return Queens.class;
    }

    public float getDistance(Queens a, Queens b) {
        if (a.N != b.N)
            return -1;
        int distance = 0;
        for (int i = 0;i < a.N;i++) {
            distance += Math.abs(a.queens.get(i) - b.queens.get(i));
        }
        return distance;
    }

    static Random rnd = new Random(System.currentTimeMillis());
    private String target, value;

    private int fitness;

    public String getValue() {
        return this.value;
    }

    public int getFitness() {
        return this.fitness;
    }

    public Queens() {
        this.N = 16;
        this.generateValue();
        this.calculateFitness();
    }

    public Queens(List<Integer> queens, int N) {
        this.N = N;
        this.queens = queens;
        this.calculateFitness();
    }

    public void generateValue() {
        this.queens = new ArrayList<Integer>();
        for (int i = 0;i < this.N;i++) {
            this.queens.add(i);
        }
        Collections.shuffle(this.queens);
    }


    public void mutate() {
        int pos1 = rnd.nextInt(this.N);
        int pos2 = rnd.nextInt(this.N);
        int temp = this.queens.get(pos1);
        this.queens.set(pos1, this.queens.get(pos2));
        this.queens.set(pos2, temp);
        this.calculateFitness();
    }

/*
(i, j) = (random.randint(0, self.N-1), random.randint(0, self.N-1))
		temp = self.queens[i]
		self.queens[i] = self.queens[j]
		self.queens[j] = temp
		self.calculate_fitness()
		#if i == j:
		#	self.mutate()
 */

    @Override
    public Queens mate(Queens S2) {
        if (this.N != S2.N) {
            System.out.println("N's don't match!");
            return null;
        }
        int N = this.N;
        List<Integer> queens = new ArrayList<Integer>();
        List<Integer> queens1 = new ArrayList<Integer>(this.queens);
        List<Integer> queens2 = new ArrayList<Integer>(S2.queens);
        for (int i = 0; i < N; i++) {
            queens.add(-1);
        }
        Collections.shuffle(queens1);
        for (int i = 0;i < N/2;i++) {
            queens.set(i, queens1.get(i));
            queens2.remove((Integer)queens1.get(i));
        }
        for (Integer v : queens2) {
            queens.set(queens.indexOf(-1), v);
        }
        return new Queens(queens, N);
    }

    public void calculateFitness() {
        int fitness = 0;
        for (int i = 0;i < this.N;i++) {
            for (int j = i+1;j < this.N;j++) {
                fitness += (Math.abs((i - j)) == Math.abs(this.queens.get(i) - this.queens.get(j)))?1:0;
            }
        }
        this.fitness = fitness;
    }


    public String toString() {
        String ret = "[";
        for (int i : this.queens) {
            ret += i + ",";
        }
        ret = ret.substring(0, ret.length()-1) + "]";
        ret += "\n";
        ret += "Fitness: "+this.fitness+"\n";
        return ret;
    }

}

