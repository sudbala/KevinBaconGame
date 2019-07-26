import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * This class is a Library of functions that can be used on graphs, similar
 * to what was made on the short assignment. This Graph Library class augments
 * that short assignment by implementing BFS, getPath, missing Vertices, 
 * averageSeparation, and any additional helper methods.
 * 
 * @author Sudharsan Balasubramani and Dhruv Uppal
 * 
 *
 */
public class BaconGraphLib {

	/**
	 * For the current center of the universe (source), finds shortest path
	 * to all reachable vertices. Returns a shortest path tree
	 * 
	 * @param g			graph that is being analyzed
	 * @param source 	vertex that is center of the universe
	 * @return 			shortest path tree
	 */
	public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source){
		Graph<V,E> paths = new AdjacencyMapGraph<V,E>();
		Set<V> visited = new HashSet<V>();
		Queue<V> queue = new LinkedList<>();
		
		//enqueue source vertex, add to visited, loop until no more verices.
		queue.add(source); 
		visited.add(source);
		paths.insertVertex(source);
		while (!queue.isEmpty()) { 
			V u = queue.remove(); 
			for (V v : g.outNeighbors(u)) { 
				if (!visited.contains(v)) { 
					//if unvisited, add to queue, insert into path tree, directed to parent
					visited.add(v); 
					queue.add(v); 
					paths.insertVertex(v);
					paths.insertDirected(v, u, g.getLabel(v, u));
					
				}
			}
		}
		return paths;
	}
	
	/**
	 * Given the shortest path tree, this method returns the path between
	 * a vertex and the source vertex.
	 * 
	 * @param tree		shortest path tree from bfs
	 * @param v			vertex to find path to
	 * @return			list that has the path
	 */
	public static <V,E> List<V> getPath(Graph<V,E> tree, V v){
		List<V> path = new ArrayList<V>();
		V temp = v;
		path.add(temp);
		if(!tree.hasVertex(v)) {
			path.add(v);
			return path;
		}
		
		while(tree.outDegree(temp) > 0) {
			for(V neighbor: tree.outNeighbors(temp)) {
				path.add(neighbor);
				temp = neighbor;
			}
		}
		return path;		
	}
	
	/**
	 * Determines which vertices in graph but not in subgraph
	 * 
	 * @param graph		original graph created
	 * @param subgraph	path subtree from universe vertex
	 * @return			returns a set of all vertices not present in subgraph
	 */
	public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph){
		Set<V> missing = new HashSet<V>();
		Set<V> compare = new HashSet<V>();
		
		for(V vertex: subgraph.vertices()) {
			compare.add(vertex);
		}
		
		for(V vertex: graph.vertices()) {
			if(!compare.contains(vertex)) {
				missing.add(vertex);
			}
		}
		return missing;
	}
	
	/**
	 * Finds the average distance to root universe in a path tree
	 * 
	 * @param tree		shortest path tree
	 * @param root		universe vertex
	 * @return			returns the average distance
	 */
	public static <V,E> double averageSeparation(Graph<V,E> tree, V root) {
		return helper(tree, root, 1)/tree.numVertices();
	}
	
	/**
	 * Helper that computes the total of the distances.
	 * 
	 * @param tree		shortest path tree
	 * @param root		source universe vertex
	 * @param path		double path that is the accumulator element, increasing path length recursively
	 * @return			returns the total of the distances.
	 */
	private static <V,E> double helper(Graph<V,E> tree, V root, double path) {
		double total = 0;
		for(V child: tree.inNeighbors(root)) {
			total += path;
			total += helper(tree, child, path + 1);
		}
		return total;
	}
	
	
	/**
	 * Takes a random walk from a vertex, up to a given number of steps
	 * So a 0-step path only includes start, while a 1-step path includes start and one of its out-neighbors,
	 * and a 2-step path includes start, an out-neighbor, and one of the out-neighbor's out-neighbors
	 * Stops earlier if no step can be taken (i.e., reach a vertex with no out-edge)
	 * @param g		graph to walk on
	 * @param start	initial vertex (assumed to be in graph)
	 * @param steps	max number of steps
	 * @return		a list of vertices starting with start, each with an edge to the sequentially next in the list;
	 * 			    null if start isn't in graph
	 */
	public static <V,E> List<V> randomWalk(Graph<V,E> g, V start, int steps) {
		List<V> walk = new ArrayList<V>();
		
		if(steps == 0) {
			walk.add(start);
			return walk;
		}
		if(!g.hasVertex(start)) {
			return null;
		}
		
		V vertex = start;
		for(int i = 0; i <= steps; i++) {
			walk.add(vertex);
			
			if(g.outDegree(vertex) == 0) { //checks whether we can still keep walking
				return walk;
			}
			
			List<V> neighbors = new ArrayList<V>();
			for(V neighbor: g.outNeighbors(vertex)) {
				neighbors.add(neighbor);
			}
			vertex = neighbors.get((int)(Math.random()*neighbors.size()));
		}
		return walk;		
	}
	
	/**
	 * Orders vertices in decreasing order by their in-degree
	 * @param g		graph
	 * @return		list of vertices sorted by in-degree, decreasing (i.e., largest at index 0)
	 */
	public static <V,E> List<V> verticesByInDegree(Graph<V,E> g) {
		List<V> sorted = new ArrayList<V>();
		
		for(V vertex: g.vertices()) {
			sorted.add(vertex);
		}
		
		Collections.sort(sorted, new Comparator<V>() {

			@Override
			public int compare(V v1, V v2) {
				return -1*(g.inDegree(v1) - g.inDegree(v2));
			}
			
		});
		
		return sorted;
	}
	
	
	/**
	 * 
	 * **
	 * Orders vertices in decreasing order by their out-degree
	 * @param g		graph
	 * @return		list of vertices sorted by out-degree, decreasing (i.e., largest at index 0)
	 */
	public static <V,E> List<V> verticesByOutDegree(Graph<V,E> g) {
		List<V> sorted = new ArrayList<V>();
		
		for(V vertex: g.vertices()) {
			sorted.add(vertex);
		}
		
		Collections.sort(sorted, new Comparator<V>() {

			@Override
			public int compare(V v1, V v2) {
				return -1*(g.outDegree(v1) - g.outDegree(v2));
			}
			
		});
		
		return sorted;
	}
	
	public static void main(String[] args) {
		Graph<String, String> g = new AdjacencyMapGraph<String, String>();
		// GraphLib graph = new GraphLib();
		g.insertVertex("Kevin Bacon");
		g.insertVertex("Bob");
		g.insertVertex("Alice");
		g.insertVertex("Charlie");
		g.insertVertex("Dartmouth");
		g.insertVertex("Nobody");
		g.insertVertex("Nobody's Friend");

		g.insertDirected("Kevin Bacon", "Alice", "K/A");
		g.insertDirected("Kevin Bacon", "Bob", "K/B");
		g.insertDirected("Alice", "Kevin Bacon", "A/K");
		g.insertDirected("Alice", "Bob", "A/B");
		g.insertDirected("Alice", "Charlie", "A/C");
		g.insertDirected("Bob", "Kevin Bacon", "B/K");
		g.insertDirected("Bob", "Alice", "B/A");
		g.insertDirected("Bob", "Charlie", "B/C");
		g.insertDirected("Charlie", "Alice", "C/A");
		g.insertDirected("Charlie", "Bob", "C/B");
		g.insertDirected("Charlie", "Dartmouth", "C/D");
		g.insertDirected("Dartmouth", "Charlie", "D/C");

		g.insertDirected("Nobody", "Nobody's Friend", "N/NF");
		g.insertDirected("Nobody's Friend", "Nobody", "NF/N");

		System.out.println(BaconGraphLib.bfs(g, "Kevin Bacon"));
		System.out.println(BaconGraphLib.getPath(BaconGraphLib.bfs(g, "Kevin Bacon"), "Dartmouth"));
		System.out.println(BaconGraphLib.missingVertices(g, BaconGraphLib.bfs(g, "Kevin Bacon")));
		System.out.println(BaconGraphLib.averageSeparation(BaconGraphLib.bfs(g, "Kevin Bacon"), "Kevin Bacon"));
	}
	
}
