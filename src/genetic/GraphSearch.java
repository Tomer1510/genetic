package genetic;

public class GraphSearch {
	
	private Knapsack root;
	public boolean cutting_heuristic; // choosing from fractions, or unlimited size.
	
	private Knapsack min(Knapsack A, Knapsack B){
		return (A.getFitness() > B.getFitness()) ? A : B;
	}
	
	public Knapsack DFS(boolean cut,int level){
		// cut will be like alpha - beta cut
		return null;
		/*
			pseudo code (just a basic DFS)
			int DFS (int node, int level){
			
				if (level == MAX)
					return fitness(node);
					
				return Math.min(DFS(Node when level = 0, level+1) , DFS(Node when level = 1, level+1));
			
			*/
	}
	
	public Knapsack Best_First(boolean cut){
		return null;
		
		/*
		 * pseudo code (just a basic best first) (not sure, didn't think much it's 4am)
		 * 	int BestFirst(Vector<node> V , Node current){
		 * 
		 * 		V.remove(current);
		 * 		for (children in current)
		 * 			add to V;
		 * 
		 * 		if(current.level == MAX)
		 * 			return current.fitness;
		 * 
		 * 		return Math.min(BestFirst(V, V[0]));
		 */
	}
	
}
