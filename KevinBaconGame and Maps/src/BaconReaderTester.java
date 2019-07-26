import java.util.*;

/**
 * Tester class for creating maps and the graph
 * 
 * @author Sudharsan Balasubramani  and Dhruv Uppal
 *
 */
public class BaconReaderTester {

	public static void main(String[] args) throws Exception {
		BaconReader reader = new BaconReader();
		
		Map<String, String> actorMap = reader.read("actors.txt");
		Map<String, String> movieMap = reader.read("movies.txt");
		Map<String, ArrayList<String>> movieActorMap = reader.idRead("movie-actors.txt");
		Graph<String, Set<String>> graph = reader.buildGraph(actorMap, movieMap, movieActorMap);
		
		System.out.println(actorMap);
		System.out.println(movieMap);
		System.out.println(movieActorMap);
		System.out.println(graph);

	}

}
