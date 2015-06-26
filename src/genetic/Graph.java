package genetic;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import genetic.Pair;
import org.omg.CORBA.MARSHAL;


public class Graph {
    int n;
    boolean[][] adjacency_matrix;
    Set<Pair<Integer, Integer>> edges;
    boolean directed = false;

    public Graph(int size) {
        this.n = size;
        this.adjacency_matrix = new boolean[n][n];
        this.edges = new HashSet<>();
        this.directed = false;
    }

    public Graph(int size, boolean directed) {
        this.n = size;
        this.adjacency_matrix = new boolean[n][n];
        this.edges = new HashSet<>();
        this.directed = directed;
    }

    public Graph(String filename){
        try {
            int size;
            File file = new File(filename);
            Scanner reader = new Scanner(file);
            while (reader.hasNext()) {
                String line = reader.nextLine();
                if (line.contains("edge")) {
                    Pattern p = Pattern.compile("p edge (\\d+)");
                    Matcher m = p.matcher(line);
                    m.find();
                    size = Integer.parseInt(m.group(1));
                    this.n = size;
                    this.adjacency_matrix = new boolean[n][n];
                    this.edges = new HashSet<>();
                    continue;
                }
                if (line.contains("e") && !line.contains("c")) {
                    Pattern p = Pattern.compile("^e (\\d+) (\\d+)$");
                    Matcher m = p.matcher(line);
                    if (m.find()) {
                        int u = Integer.parseInt(m.group(1)) - 1;
                        int v = Integer.parseInt(m.group(2)) - 1;
                        this.addEdge(u, v);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean addEdge(int v, int u) {

        if (this.adjacency_matrix[v][u]) {
            return false;
        }
        else {
            this.edges.add(new Pair<>(v, u));

            this.adjacency_matrix[v][u] = true;
            if (!directed) {
                this.adjacency_matrix[u][v] = true;
            }

            return true;
        }
    }

    public boolean removeEdge(int v, int u) {
        if (!this.adjacency_matrix[v][u]) {
            return false;
        }
        else {
            this.edges.remove(new Pair<>(v, u));

            this.adjacency_matrix[v][u] = false;
            if (!directed) {
                this.adjacency_matrix[u][v] = false;
            }

            return true;
        }
    }

    public boolean isAdjacent(int u, int v) {
        return this.edges.contains(new Pair<>(u, v));
    }

    public final Set<Pair<Integer, Integer>> getEdges() {
        return this.edges;
    }

    public String toString() {
        String ret = "";
        for (int i = 0;i < this.n;i++) {
            ret += i+"  ";
        }
        ret += "\n";
        for (Pair<Integer, Integer> edge : this.edges) {
            // Avoid duplications in output
            ret += edge.getKey() + " <---> " + edge.getValue() + "\n";
        }
        return ret;
    }

}
