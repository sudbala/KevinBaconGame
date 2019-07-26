import java.io.*;
import java.util.*;
/**
 * Class created to read the types of files that are given for the Kevin Bacon Game
 * This is a specialized class for this project, not a library class for all graphs.
 * Think of it as a library for just this game.
 * 
 * @author Sudharsan Balasubramani and Dhruv Uppal
 *
 */
public class BaconReader {
		
	/**
	 * Reads the actor and movie files and creates a map mapping ID to the actor/movie name
	 * 
	 * @param file			file to read
	 * @return				returns map of ID and name
	 * @throws Exception
	 */
	public Map<String, String> read(String file) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		BufferedReader in;
		String line =  "";
		try {
			in = new BufferedReader(new FileReader(file));
		}catch (Exception e) {
			throw new Exception("File Not Found");
		}
		
		while((line = in.readLine()) != null) {
			String[] idname = line.split("\\|");
			map.put(idname[0], idname[1]);
		}
		in.close();
		return map;
	}
	
	/**
	 * Quite similar to read method, but is slightly different to account for the multiple actors that
	 * may act in a single movie. Returns movie mapped to actors that have acted in movie
	 * 
	 * @param file			file to read	
	 * @return				returns movie mapped to actors that have acted in movie
	 * @throws Exception
	 */
	public Map<String, ArrayList<String>> idRead(String file) throws Exception{
		Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		BufferedReader in;
		String line =  "";
		try {
			in = new BufferedReader(new FileReader(file));
		}catch (Exception e) {
			throw new Exception("File Not Found");
		}
		
		while((line = in.readLine()) != null) {
			String[] idname = line.split("\\|");
			if(!map.containsKey(idname[0])) {
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(idname[1]);
				map.put(idname[0], temp);
			}
			else {
				map.get(idname[0]).add(idname[1]);
			}
		}
		in.close();
		return map;
	}
	
	/**
	 * Uses the actors, movies, and movieActor maps to create a graph of all relationships
	 * 
	 * @param actors		actor ID to actor name map
	 * @param movies		movie ID to movie name map	
	 * @param movieActor	movie ID to actor ID map
	 * @return
	 */
	public Graph<String, Set<String>> buildGraph(Map<String, String> actors, Map<String, String> movies, Map<String, ArrayList<String>> movieActor){
		Graph<String, Set<String>> g = new AdjacencyMapGraph<String, Set<String>>();

		for (String aID : actors.keySet()) {
			g.insertVertex(actors.get(aID));
		}

		for (String aID : actors.keySet()) {
			for (String mID : movieActor.keySet()) {
				ArrayList<String> mAIDs = movieActor.get(mID);
				if (mAIDs.contains(aID) && mAIDs.size() > 0) {
					for (String mAID : mAIDs) {
						if (!actors.get(mAID).equals(actors.get(aID))) {
							if (!g.hasEdge(actors.get(aID), actors.get(mAID))) {
								Set<String> label = new HashSet<String>();
								label.add(movies.get(mID));
								g.insertUndirected(actors.get(aID), actors.get(mAID), label);
							} else {
								g.getLabel(actors.get(aID), actors.get(mAID)).add(movies.get(mID));
							}
						}
					}
				}
			}
		}
		return g;
	}
}

