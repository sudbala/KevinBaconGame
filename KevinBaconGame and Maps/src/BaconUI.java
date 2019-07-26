import java.io.*;
import java.util.*;
/**
 *This class takes care of the actual interface of the Kevin Bacon Game
 * 
 * @author Sudharsan Balasubramani and Dhruv Uppal
 *
 */
public class BaconUI {
	private Graph<String, Set<String>> graph;
	private String universe;
	
	/**
	 * Constructor for the BaconUI
	 * @param graph	graph 
	 */
	public BaconUI(Graph<String, Set<String>> graph, String universe) {
		this.graph = graph;
		this.universe = universe;
	}
	
	public void runUI() {
		Scanner reader = new Scanner(System.in);
		String command = "";
		System.out.println("Commands:\n" + 
						  	"c <#>: list top (positive number +) or bottom (negative number -) centers of the universe, sorted by average separation\n" + 
							"d <low> <high>: list actors sorted by degree with degree between low and high\n" + 
						  	"i: list actors with infinite separation from the current center\n" +
							"p <name>: find the shortest path from <name> to current center of the universe\n"+
						  	"a: find the average path length over all actors who are connected by some path to the current center\n" + 
							"s <low> <high>: list actors sorted by non-infinte separation from the current center with separation between low and high\n" + 
						  	"n: find the number of actors who have a path to the current center\n" + 
							"u <name>: make <name> the center of the universe\n" +
						  	"q: quit game");
		System.out.println();
		System.out.println(universe + " is now the center of the acting universe, connected to "+ (BaconGraphLib.bfs(graph, universe).numVertices()-1) + "/" + graph.numVertices() + " actors with average separation " + BaconGraphLib.averageSeparation(BaconGraphLib.bfs(graph, universe), universe));
		System.out.println();
		
		while(!command.equals("q")) {
			
			System.out.print(universe + " game >");
			
			command = reader.next();
			
			if(command.equals("c")) {
				int num = reader.nextInt();
				centers(num, reader);
			}
			else if(command.equals("d")) {
				int low =  reader.nextInt();
				int high = reader.nextInt();
				degreeSort(low, high);
			}
			else if(command.equals("i")) {
				infinite();
			}
			else if(command.equals("p")) {
				String name = reader.nextLine();
				if(name.length() > 1)
					name = name.substring(1);
				path(name, reader);
			}
			else if(command.equals("a")) {
				average();
			}
			else if(command.equals("s")) {
				int low =  reader.nextInt();
				int high = reader.nextInt();
				separationSort(low, high);
			}
			else if(command.equals("n")) {
				numberPath();
			}
			else if(command.equals("u")) {
				String name = reader.nextLine();
				if(name.length() > 1)
					name = name.substring(1);
				universe(name, reader);
			}
			
		}
		reader.close();		
	}
	

	/**
	 * list top (positive number +) or bottom (negative number -) centers of the universe, sorted by average separation
	 * @param num	the number of centers to print
	 */
	private void centers(int num, Scanner reader) {
		Graph<String, Set<String>> paths = BaconGraphLib.bfs(graph, universe);
		while(Math.abs(num) > paths.numVertices()) {
			System.out.println("Too high! Enter lower number: ");
			num = reader.nextInt();
		}
		
		Map<String, Double> separation = new HashMap<String, Double>();
		for(String v: paths.vertices()) {
			separation.put(v, BaconGraphLib.averageSeparation(BaconGraphLib.bfs(graph, v), v));
		}
		
		List<String> vertices = new ArrayList<String>();
		vertices.addAll(separation.keySet());
		
		Comparator<String> comp = (String v1, String v2) -> (int)((separation.get(v1) - separation.get(v2))*100); //idea from online (Stack Overflow), because we are dealing with doubles!
		Collections.sort(vertices, comp);
		
		if(num > 0) {
			System.out.println("The top " + num + " centers of the universe are");
			System.out.print("[");
			for(int i = 0; i < num; i++) {
				if(i != num-1)
					System.out.print(vertices.get(i)+ ": " + separation.get(vertices.get(i)) + ", ");
				else
					System.out.print(vertices.get(i)+ ": " + separation.get(vertices.get(i)) + "]");
			}
		}
		else if(num < 0) {
			Collections.reverse(vertices);
			System.out.println("[");
			for(int i = 0; i < Math.abs(num); i++) {
				if(i != num-1)
					System.out.print(vertices.get(i)+ ": " + separation.get(vertices.get(i)) + ", ");
				else
					System.out.print(vertices.get(i)+ ": " + separation.get(vertices.get(i)) + "]");
			}
		}
		System.out.println();
	}
	
	/**
	 * list actors sorted by degree with degree between low and high
	 * 
	 * @param low	low degree constraint
	 * @param high	high degree constraint
	 */
	private void degreeSort(int low, int high) {
		if(low > high)
			System.out.println("Low Degree cannot be higher than High Degree!");
		else{
			List<String> vs = new ArrayList<String>();
			Graph<String, Set<String>> paths = BaconGraphLib.bfs(graph, universe);
			
			for(String v: paths.vertices()) {
				vs.add(v);
			}
			
			Comparator<String> comp = (String v1, String v2) -> paths.inDegree(v2) - paths.inDegree(v2);
			Collections.sort(vs, comp);
			
			System.out.println("Actors sorted by degree, between "+ low+ " and " + high);
			System.out.print("[");
			for(int i = 0; i < vs.size(); i++) {
				if((graph.inDegree(vs.get(i)) >= low) && (graph.inDegree(vs.get(i)) <= high)) {
					if(i != vs.size()-1)
						System.out.print(vs.get(i) + ":" + graph.inDegree(vs.get(i)) + ", " );
					else
						System.out.print(vs.get(i) + ": "+ graph.inDegree(vs.get((i))) + "]");
				}
			}
		}
		System.out.println();
	}
	
	/**
	 * lists actors with infinite separation from the current center
	 */
	private void infinite() {
		Set<String> infinites = BaconGraphLib.missingVertices(graph, BaconGraphLib.bfs(graph, universe));
		System.out.println(infinites);
	}
	
	/**
	 * find the shortest path from <name> to current center of the universe
	 * 
	 * @param name	vertex to find shortest path to from universe
	 */
	private void path(String name, Scanner reader) {
		while(!graph.hasVertex(name)) {
			System.out.print("Actor not in graph. Enter another name: ");
			name = reader.nextLine();
		}
		
		Graph<String, Set<String>> paths = BaconGraphLib.bfs(graph, universe);
		if(paths.hasVertex(name)) {
			List<String> path = BaconGraphLib.getPath(paths, name);
			System.out.println(name +"'s number is " + (path.size()-1));
			for(int i = 0; i < path.size()-1; i++) {
				System.out.println(path.get(i) + " appeared in " + paths.getLabel(path.get(i), path.get(i+1)) + " with " + path.get(i+1));
			}
		}
		else {
			System.out.println(name + " is not connected to " + universe);
		}
		System.out.println();
	}
	
	/**
	 * finds the average path length over all actors who are connected by some path to the current center
	 */
	private void average() {
		double separation = BaconGraphLib.averageSeparation(BaconGraphLib.bfs(graph, universe), universe);
		System.out.println(universe + " has an average separation of " + separation + " from all actors connected.");
	}
	
	/**
	 * list actors sorted by non-infinte separation from the current center with separation between low and high
	 * 
	 * @param low	low bound for separation
	 * @param high	high bound for separation
	 */
	private void separationSort(int low, int high) {
		
		if (high < low)
			System.out.println("Low bound cannot be higher than high bound!");
		else {
			// Store all vertices in a map w/ vertex key and degree value
			Graph<String, Set<String>> paths = BaconGraphLib.bfs(graph, universe);
			Map<String, Integer> vD = new HashMap<String, Integer>();
			for (String v : paths.vertices()) {
				vD.put(v, BaconGraphLib.getPath(paths, v).size() - 1);
			}

			Comparator<String> comp = (String v1, String v2) -> vD.get(v2) - vD.get(v1);

			List<String> vertices = new ArrayList<>();
			vertices.addAll(vD.keySet());
			vertices.sort(comp);

			boolean has = false;

			for (int i = low; i < high; i++) {
				if (vD.containsValue(i))
					has = true;
			}

			if (!has) {
				System.out.println("No actors between degrees entered!");
			} 
			else {
				System.out.println("Sorted by separation between "+ low + " and " + high + ":");
				System.out.print("[");
				for(int i  = 0; i < vertices.size(); i++) {
					if((vD.get(vertices.get(i)) >= low) && (vD.get(vertices.get(i)) <= high)) {
						if(i != vD.size()-1)
							System.out.print(vertices.get(i) + ":" + vD.get(vertices.get(i)) + ", " );
						else
							System.out.print(vertices.get(i) + ": "+ vD.get(vertices.get(i)) + "]");
					}
				}
			}
		}
		System.out.println();		
	}
	
	/**
	 * find the number of actors who have a path to the current center
	 */
	private void numberPath() {
		int number = BaconGraphLib.bfs(graph, universe).numVertices()-1;
		System.out.println("There are "+ number +" actors who have a path to "+ universe + ".");
	}
	
	/**
	 * Changes the universe or center
	 * 
	 * @param name	name to change it to
	 */
	private void universe(String name, Scanner reader) {
		while(!graph.hasVertex(name)) {
			System.out.print("Actor not in graph. Enter another name: ");
			name = reader.nextLine();
		}
		universe = name;
		System.out.println(universe + " is now the center of the acting universe, connected to "+ (BaconGraphLib.bfs(graph, universe).numVertices()-1) + "/" + graph.numVertices() + " actors with average separation " + BaconGraphLib.averageSeparation(BaconGraphLib.bfs(graph, universe), universe));
	}
	
	
	
	
	
	
}
