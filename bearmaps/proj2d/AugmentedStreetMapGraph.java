package bearmaps.proj2d;

import bearmaps.proj2ab.KDTree;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.PointSet;
import bearmaps.proj2c.streetmap.StreetMapGraph;
import bearmaps.proj2c.streetmap.Node;
import bearmaps.proj2d.utils.HashTrieMap;
import bearmaps.proj2d.utils.TrieMap;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, Rui Gao
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private final Map<Point, Long> pointIDMap;
    private final PointSet pointSet;
    private final TrieMap<List<Map<String, Object>>> nameLocationsMap;
    private final Map<String, String> cleanFullMap;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        List<Node> nodes = this.getNodes();
        pointIDMap = new HashMap<>();
        nameLocationsMap = new HashTrieMap<>();
        cleanFullMap = new HashMap<>();
        for (Node n : nodes) {
            double lon = n.lon();
            double lat = n.lat();
            String name = n.name();
            long id = n.id();
            if (name == null) {
                pointIDMap.put(new Point(lon, lat), id);
            } else {
                Map<String, Object> location = new HashMap<>();
                location.put("lat", lat);
                location.put("lon", lon);
                location.put("name", name);
                location.put("id", id);
                String cleaned = cleanString(name);
                List<Map<String, Object>> locations = nameLocationsMap.get(cleaned);
                if (locations == null) {
                    locations = new LinkedList<>();
                    nameLocationsMap.put(cleaned, locations);
                    cleanFullMap.put(cleaned, name);
                }
                locations.add(location);
            }
        }
        pointSet = new KDTree(pointIDMap.keySet());
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point closestPoint = pointSet.nearest(lon, lat);
        return pointIDMap.get(closestPoint);
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM nameLocationsMap that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of nameLocationsMap whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> cleanedNames = nameLocationsMap.keysWithPrefix(prefix, 20);
        List<String> fullNames = new ArrayList<>();
        for (String cleaned : cleanedNames) {
            fullNames.add(cleanFullMap.get(cleaned));
        }
        return fullNames;
    }

    /**
     * For Project Part III (gold points)
     * Collect all nameLocationsMap that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of nameLocationsMap whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        return nameLocationsMap.get(cleanString(locationName));
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
