
public class Edge {
	String id;
	String intersection1ID;
	String intersection2ID;
	double distance;
	
	public Edge(String id, String intersection1ID, String intersection2ID, double distance) {
		this.id = id;
		this.intersection1ID = intersection1ID;
		this.intersection2ID = intersection2ID;
		this.distance = distance;
	}
}
