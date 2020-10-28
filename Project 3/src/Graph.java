import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Graph {
	static HashMap<String, Node> mapIntersection = new HashMap<String, Node>();
	static HashMap<String, Edge> mapRoad = new HashMap<String, Edge>(); 
	static ArrayList<Node> dijkstraPath = new ArrayList<Node>();

	//calculate distance between 2 intersections using Haversine formula 
	public static double calculateDistance(Node one, Node two) {
		double lat1 = Math.toRadians(one.latitude);
		double long1 = Math.toRadians(one.longitude);
		double lat2 = Math.toRadians(two.latitude);
		double long2 = Math.toRadians(two.longitude);
		double r = 6378.1; //radius of the earth

		double distance = 2*r*Math.asin(Math.sqrt(Math.pow(Math.sin(((lat2 - lat1)/2)), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin((long2 - long1)/2), 2)));
		return distance;
	}


	//use in MapGUI
	//find maximum latitude
	public double findMaxLat(HashMap<String, Node> mapIntersection) {
		double n = Double.MIN_VALUE;
		for(Node x : mapIntersection.values()) {
			if(x.latitude > n) {
				n = x.latitude;
			}
		}
		return n;
	}

	//find maximum longitude
	public double findMaxLong(HashMap<String, Node> mapIntersection) {
		double n = -1000;
		for(Node x : mapIntersection.values()) {
			if(x.longitude > n) {
				n = x.longitude;
			}
		}
		return n;
	}


	//find minimum latitude
	public double findMinLat(HashMap<String, Node> mapIntersection) {
		double n = Double.MAX_VALUE;
		for(Node x : mapIntersection.values()) {
			if(x.latitude < n) {
				n = x.latitude;
			}
		}
		return n;
	}

	//find minimum longitude
	public double findMinLong(HashMap<String, Node> mapIntersection) {
		double n = Double.MAX_VALUE;
		for(Node x : mapIntersection.values()) {
			if(x.longitude < n) {
				n = x.longitude;
			}
		}
		return n;
	}


	public static void main(String[] args) {
		//start the timer
		long startTime = System.currentTimeMillis();
		Graph g = new Graph();
		Scanner scan = null;

		//read files
		try {
			scan = new Scanner(new File(args[0]));
		} catch(Exception e) {
			System.out.println("File not found!");
		}

		if(args[0].contains("/ur.txt")) {
			MapGUI.thickLines = true;
		}

		while(scan.hasNextLine()) {
			String line = scan.nextLine();	
			String arr[] = line.split("	");
			if(arr[0].equals("i")) { //same thing for roads
				Node intersection = new Node(arr[1], Double.parseDouble(arr[2]), Double.parseDouble(arr[3]));
				g.mapIntersection.put(arr[1], intersection);
			} else {
				//id, i1, i2, distance
				double distance = calculateDistance(g.mapIntersection.get(arr[2]), g.mapIntersection.get(arr[3])); 
				Edge road = new Edge(arr[1], arr[2], arr[3], distance);
				g.mapIntersection.get(arr[2]).addToAdjList(g.mapIntersection.get(arr[3])); //add dest node to src node's adjacency list
				g.mapIntersection.get(arr[3]).addToAdjList(g.mapIntersection.get(arr[2]));
				//g.mapIntersection.get(arr[2]).printAdjList();
				g.mapRoad.put(arr[1], road);
			}
		}
		//		printPath(g, g.mapIntersection.get(args[1]), g.mapIntersection.get(args[2]));
		//		MapGUI gui = new MapGUI(mapRoad, mapIntersection, g.findMinLat(mapIntersection), g.findMaxLat(mapIntersection), g.findMinLong(mapIntersection), g.findMaxLong(mapIntersection));
		//		JFrame frame = new JFrame(args[0]);
		//		frame.add(gui);
		//		frame.setVisible(true);
		//		frame.setSize(800, 800);
		//		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);


		//for command line arguments
		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("--show")) {
				MapGUI gui = new MapGUI(mapRoad, mapIntersection, g.findMinLat(mapIntersection), g.findMaxLat(mapIntersection), g.findMinLong(mapIntersection), g.findMaxLong(mapIntersection));
				JFrame frame = new JFrame(args[0]);
				frame.add(gui);
				frame.setVisible(true);
				frame.setSize(800, 800);
				frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
			}
			if(args[i].equals("--directions")) {
				printPath(g, g.mapIntersection.get(args[i+1]), g.mapIntersection.get(args[i+2]));
			}	
		}
	}

	//currently not finding the shortest distance
	//dijkstra to find shortest path on the map
	public static void dijkstra(Node src, Node dest) {
		src.nodeDistance = 0;
		PriorityQueue<Node> pq = new PriorityQueue<Node>();
		pq.add(src);

		while(!pq.isEmpty()) {
			Node n = pq.poll();//first loop => poll the src node
			n.visited = true; //mark the node as visited

			for(int i = 0; i < n.adjacencyList.size(); i++) {
				Node a = n.adjacencyList.get(i);
				if(a.visited == true) { //if visited
					if(a.nodeDistance + calculateDistance(n, a) < n.nodeDistance) { //replace
						n.nodeDistance = a.nodeDistance + calculateDistance(a, n);
						n.prev = a;
						pq.add(a);
					} 
				}
				else { //if not visited
					a.nodeDistance = n.nodeDistance + calculateDistance(a, n);
					a.prev = n;
					pq.add(a);	
				}
			}
		}
		Node curr = dest;
		while(curr != null) {
			dijkstraPath.add(curr);
			curr = curr.prev;
		}
		System.out.println("Total distance to " + dest.id + " is " + dest.nodeDistance + " miles");
	}

	//print shortest path
	public static void printPath(Graph g, Node src, Node dest) {
		dijkstra(src, dest);
		if(dest.equals(src)) {
			System.out.println(src.id);
		} else {
			System.out.println("Path = ");
			for(int i = dijkstraPath.size() - 1; i >= 0; i--) {
				System.out.println(dijkstraPath.get(i).id);
			}
		}
	}
}
