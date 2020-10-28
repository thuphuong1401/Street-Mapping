import java.util.*;

public class Node implements Comparable<Node>{
	String id;
	double latitude;
	double longitude;
	ArrayList<Node> adjacencyList = new ArrayList<Node>();
	boolean visited = false;
	double nodeDistance = Integer.MAX_VALUE;
    Node prev = null;
	

	//constructor
	public Node(String id, double latitude, double longitude) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
	}	
	
	//add a node directly connected to the source node
	public void addToAdjList(Node dest) {
		this.adjacencyList.add(dest);
	}
	
	public void printAdjList() {
		System.out.print(id + ":");
		for(Node a: adjacencyList) {
			System.out.print(a.id + " ");
		}
		System.out.println();
	}
	@Override
	public int compareTo(final Node o) {
		if(nodeDistance > o.nodeDistance) {
			return 1;
		}
		else if(nodeDistance < o.nodeDistance) {
			return -1;
		} else {
			return 0;
		}
	}
}
