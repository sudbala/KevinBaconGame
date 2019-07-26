import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Driver class and the final class of the game that actually runs game
 * @author Sudharsan Balasubramani and Dhruv Uppal
 *
 */
public class BaconDriver {

	public static void main(String[] args) throws Exception {
		BaconReader reader = new BaconReader();
		Map<String, String> actorMap = reader.read("actorsTest.txt");
		Map<String, String> movieMap = reader.read("moviesTest.txt");
		Map<String, ArrayList<String>> movieActorMap = reader.idRead("movie-actorsTest.txt");
		Graph<String, Set<String>> graph = reader.buildGraph(actorMap, movieMap, movieActorMap);
		
		BaconUI game = new BaconUI(graph, "Kevin Bacon");
		game.runUI();

	}

}
