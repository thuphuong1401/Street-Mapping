import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MapGUI extends JPanel{
	//private static MapGUI temp;
	public static HashMap<String, Edge> edges;    //ArrayList<Road> roads;
	public static HashMap<String, Node> mapIntersection;
	public static boolean thickLines = false;
	
	public static double minLat, minLong, maxLat, maxLong;
	public static double xScale, yScale;
	
	public MapGUI(HashMap<String, Edge> edges, HashMap<String, Node> mapIntersection, double minLat, double maxLat, double minLong, double maxLong) {
		MapGUI.edges = edges;
		MapGUI.mapIntersection = mapIntersection;
		MapGUI.minLat = minLat;
		MapGUI.maxLat = maxLat;
		MapGUI.minLong = minLong;
		MapGUI.maxLong = maxLong;
		
		setPreferredSize(new Dimension(850, 850));	
	}
	
	//paint component
	public void paintComponent(Graphics g) {
		
		//use 2D graphics to display lines with double values for coordinates
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		
		g2.setColor(Color.BLACK);
		
		//increase the thickness of the lines if the map is of the University of Rochester
		if(thickLines) {
			g2.setStroke(new BasicStroke(3));
		}
		
		//set the scales 
		xScale = this.getWidth() / (maxLong - minLong);
		yScale = this.getHeight() / (maxLat - minLat);
		
		Node node1, node2;
		
		double x1, y1, x2, y2;
		
		//Draw the roads
		for(Edge e : edges.values()) {
			scale();
			
			node1 = mapIntersection.get(e.intersection1ID);
			node2 = mapIntersection.get(e.intersection2ID);
			
			x1 = node1.longitude;
			y1 = node1.latitude;
			x2 = node2.longitude;
			y2 = node2.latitude;
		
			g2.draw(new Line2D.Double((x1-minLong) * xScale, getHeight() - ((y1 - minLat) * yScale), 
					(x2-minLong) * xScale, getHeight() - ((y2 - minLat) * yScale)));
			
		}
		
		//draw the highlighted road using Dijkstra's algorithm
		if(Graph.dijkstraPath != null) {
			
			g2.setColor(Color.RED);
			
			for(int i = 0; i < Graph.dijkstraPath.size() - 1; i++) {
				
				x1 = Graph.dijkstraPath.get(i).longitude;
				y1 = Graph.dijkstraPath.get(i).latitude;
				x2 = Graph.dijkstraPath.get(i+1).longitude;
				y2 = Graph.dijkstraPath.get(i+1).latitude;
				
				g2.draw(new Line2D.Double((x1-minLong) * xScale, getHeight() - ((y1 - minLat) * yScale), 
						(x2-minLong) * xScale, getHeight() - ((y2 - minLat) * yScale)));
			}
		}	
	}
	
	//Resecale the panel
	public void scale() {
		xScale = this.getWidth() / (maxLong - minLong);
		yScale = this.getHeight() / (maxLat - minLat);	
	}
}