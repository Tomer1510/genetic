package genetic;

import javax.swing.text.Style;
import java.util.*;

/**
 * Created by tomereiges on 6/23/15.
 */
public class KColoring extends Genetic<KColoring> {
    static Random rand = new Random();
    public static int k = 5;
    private static final int BAN_PENALTY = (int)1e6;
    private Map<Integer, Integer> coloring = new HashMap<>();
    public static Graph g;
    private int fitness;
    static List<KColoring> allColorings = new LinkedList<>();

    public static String GRAPH_FILE = "miles250.col";

    private int generation = 0;

    public double getDistance(KColoring a, KColoring b) {
        return 1;
    }

    public double getFitness() {
        return this.fitness;
    }

    public List<Set<Integer>> getColorSets() {
        List<Set<Integer>> colorSets = new LinkedList<>();
        Map<Integer, Set<Integer>> byColor = new HashMap<>();
        for (int i = 0;i < this.g.n;i++) {
            int color = this.coloring.get(i);
            if (!byColor.containsKey(color)) {
                byColor.put(color, new HashSet<Integer>());
            }
            byColor.get(color).add(i);
        }

        for (int i = 0;i < KColoring.k;i++) {
            colorSets.add(byColor.get(i));
        }

        return colorSets;
    }

    @Override
    public void kill() {
        KColoring.allColorings.remove(this);
        KColoring.allColorings.removeAll(Collections.singleton(null));
    }


    private void removeNulls(List<Pair<Integer, Set<Integer>>> colorSets) {
        //colorSets.removeAll(Collections.singleton(null));
        Iterator<Pair<Integer, Set<Integer>>> it = colorSets.iterator();
        while(it.hasNext()) {
            Pair<Integer, Set<Integer>> colorSet = it.next();
            if (colorSet == null || colorSet.getKey() == null || colorSet.getValue() == null || colorSet.getValue().size() == 0) {
                it.remove();
            }
        }
    }


    public KColoring mate(KColoring c2) {
        Map<Integer, Integer> newColoring = new HashMap<>();
        int nextGeneration = this.generation+1;
        List<Pair<Integer, Set<Integer>>> colorSets = new LinkedList<Pair<Integer, Set<Integer>>>();
        int m = (int)Math.log(rand.nextInt(KColoring.allColorings.size()));
        //int m = 50;
        int q = m/2;
        List<Integer> colorings = new LinkedList<>();

        for (int i = 0;i < KColoring.allColorings.size();i++)
            colorings.add(i);


        Collections.shuffle(colorings);


        /*Collections.sort(colorings, new java.util.Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                double f_a = KColoring.allColorings.get(a).getFitness();
                double f_b = KColoring.allColorings.get(b).getFitness();
                return f_a < f_b ? -1 : f_a == f_b ? 0 : 1;
            }
        });*/


        int counter = 0;
        for (int i = 0;counter < m && i < KColoring.allColorings.size();i++) {
            if (KColoring.allColorings.get(i).generation == Genetic.getGeneration())
                continue;
            for (Set<Integer> s : KColoring.allColorings.get(i).getColorSets()) {
                colorSets.add(new Pair(counter, s));
            }
            counter++;
        }

        Set<Integer> nodes = new HashSet<>();
        for (int i = 0;i < this.g.n;i++) {
            nodes.add(i);
        }



        final Map<Integer, Integer> banned = new HashMap<>();
        for (int i = 0; i < m;i++) {
            banned.put(i, 0);
        }

        for (int c = 0;c < KColoring.k && colorSets.size() > 0;c++) {

            this.removeNulls(colorSets);

            // Sort color sets by size
            Collections.sort(colorSets, new java.util.Comparator<Pair<Integer, Set<Integer>>>() {
                @Override
                public int compare(Pair<Integer, Set<Integer>> a, Pair<Integer, Set<Integer>> b) {
                    /*if (a == null && b != null) {
                        return 1;
                    }
                    if (a != null && b == null) {
                        return -1;
                    }
                    if (a == null && b == null) {
                        return 0;
                    }*/
                    int size_a = (a==null||a.getValue()==null)?0:a.getValue().size();
                    int size_b = (b==null||b.getValue()==null)?0:b.getValue().size();

                    size_a += BAN_PENALTY*banned.get(a.getKey());
                    size_b += BAN_PENALTY*banned.get(b.getKey());

                    return size_a < size_b ? 1 : size_a == size_b ? 0 : -1;
                    //return a==null ? 1 : b==null ? -1 : a.size() < b.size() ? 1 : a.size() == b.size() ? 0 : -1;
                }
            });

            Pair<Integer, Set<Integer>> largest = colorSets.get(0);
            Set<Integer> largestColorSet = largest.getValue();
            largestColorSet.removeAll(Collections.singleton(null));
            for (int node : largestColorSet) {
                newColoring.put(node, c);
                nodes.remove(node);

                // Remove node from all the color sets
                Iterator<Pair<Integer, Set<Integer>>> it = colorSets.iterator();
                while(it.hasNext()) {
                    Set<Integer> colorSet = it.next().getValue();
                    if (colorSet == null) {
                        continue;
                    }
                    if (!colorSet.equals(largestColorSet) && colorSet.contains(node)) {
                        if (colorSet.size() == 1 && colorSet != largestColorSet) {
                            it.remove();
                        }
                        else {
                            colorSet.remove(node);
                        }
                    }
                }

            }

            for (int i = 0; i < m;i++) {
                banned.put(i, Math.max(0, banned.get(i)-1));
            }

            banned.put(largest.getKey(), q);

            colorSets.remove(largest);

        }

        // Assign color to all non-colored vertices left

        for (int node : nodes) {
            newColoring.put(node, rand.nextInt(KColoring.k));
            //nodes.remove(node);
        }


        return new KColoring(newColoring, nextGeneration);
    }

    public void mutate() {
        int v = rand.nextInt(g.n);
        int color = rand.nextInt(KColoring.k);
        this.coloring.put(v, color);
    }

    public void calculateFitness() {
        int conflictingEdges = 0;
        for (Pair<Integer, Integer> e : g.getEdges()) {
            if (this.coloring.get(e.getKey()) == this.coloring.get(e.getValue())) {
                conflictingEdges++;
            }
        }
        this.fitness = conflictingEdges;
    }

    // Generate a random coloring with k colors
    public void generateValue() {
        List<Integer> colors = new LinkedList<>();
        for (int i = 0;i < KColoring.k;i++)
            colors.add(i);
        Collections.shuffle(colors);

        int nodesLeft = g.n;
        int prev = 0;
        for (int i = 0;i < KColoring.k;i++) {
            int howMany = rand.nextInt(nodesLeft - (KColoring.k-i +1));
            if (howMany == -1 && nodesLeft > 0) {
                howMany = 1;
            }
            if (i == k-1) {
                howMany = nodesLeft;
            }
            nodesLeft -= howMany;
            for (int j = prev;j < prev+howMany;j++) {
                coloring.put(j, i);
            }
            prev = prev+howMany;
        }
    }

    public KColoring() {
        if (KColoring.g == null) {
            KColoring.initGraph();
        }
        this.generateValue();
        this.calculateFitness();
        KColoring.allColorings.add(this);
    }

    public KColoring(Map<Integer, Integer> coloring) {
        if (KColoring.g == null) {
            KColoring.initGraph();
        }
        this.coloring = coloring;
            this.calculateFitness();
        KColoring.allColorings.add(this);
    }

    public KColoring(Map<Integer, Integer> coloring, int generation) {
        if (KColoring.g == null) {
            KColoring.initGraph();
        }
        this.coloring = coloring;
        this.calculateFitness();
        KColoring.allColorings.add(this);
        this.generation = generation;
    }

    public static void resetColorings() {
        KColoring.allColorings = new LinkedList<>();
    }

    public static void resetAll() {
        KColoring.resetColorings();
        KColoring.g = null;
    }

    public static void initGraph() {
        if (KColoring.GRAPH_FILE != null) {
            KColoring.g = new Graph(KColoring.GRAPH_FILE);
        }
        else {
            int n = 15;
            int edges = rand.nextInt(n * (n - 1) / 2 + 1) / 2;
            KColoring.g = new Graph(n);

            while (edges > 0) {
                int u = rand.nextInt(n);
                int v = rand.nextInt(n);
                /*if (g.isAdjacent(u, v))
                    continue;*/
                g.addEdge(u, v);
                edges--;
            }
        }
    }

    public String toString() {
        return "Number of vertices: " + this.g.n + "\tNumber of colors: " + KColoring.k + "\n"
                +"Fitness: " + this.getFitness() + "\n"
                +this.coloring.toString() + "\n";
    }


}

