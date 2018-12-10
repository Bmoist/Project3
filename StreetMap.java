import java.io.*;
import java.util.*;
import javax.swing.*;

public class StreetMap {

	private static HashMap<String, double[]> intersection = new HashMap<String, double[]>();
	private static HashMap<String, String[]> road = new HashMap<String, String[]>();
	private static Graph graph = new Graph();
	private static StreetMapping canvas;

	public static void main(String[] args) {

		readInput(args[0]);

		// JFrame frame = new JFrame(); canvas = new StreetMapping(road, intersection);
		JFrame frame = new JFrame();
		canvas = new StreetMapping(road, intersection);
		frame.setSize(500, 500);
		frame.setResizable(true);
		frame.add(canvas);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		if (args[1].equals("--show")) {
			if (args[2].equals("--directions")) { // as long as it says directions here, then read two other vairables
				Its source = graph.getVertice(args[3]);
				Its target = graph.getVertice(args[4]);
				Dijkstra(graph, source);
				displayPath(args[4]);
				drawRd(target);
			} else if (args[2].equals(null)) {
				canvas.drawMap();
			}
		} else if (args[1].equals("--directions")) {
			if (args[2].equals("--show")) {
				Its source = graph.getVertice(args[3]);
				Its target = graph.getVertice(args[4]);
				Dijkstra(graph, source);
				drawRd(target);
			} else {
				Its source = graph.getVertice(args[2]);
				Its target = graph.getVertice(args[3]);
				Dijkstra(graph, source);
			}
		}


	}

	public static void Dijkstra(Graph g, Its sc) {

		PriorityQueue<Its> q = new PriorityQueue<Its>();
		for (Its it : g.getVertice().values()) {
			it.setD(Integer.MAX_VALUE);
			it.setP(null);
		}
		sc.setD(0);
		q.add(sc);

		while (!q.isEmpty()) {
			Its min = q.remove();

			// For each neighbor
			for (int j = 0; j < min.getNeighbor().size(); j++) {
				Its neighbor = min.neighbor(j);
				double tempDis = min.d() + min.getWeight(neighbor);
				if (tempDis < neighbor.d()) {
					neighbor.setD(tempDis);
					neighbor.setP(min);
					if (q.contains(neighbor)) {
						q.remove(neighbor);
					}
					q.add(neighbor);
				}
			}
		}
	}

	public static void displayPath(String target) {
		Its tg = graph.getVertice(target);
		System.out.print(tg.printP(tg));
		System.out.println(" " + tg.s());
		System.out.println("Total distance traveled: " + tg.d());
	}

	public static void readInput(String fileName) {
		File file;
		Scanner in;
		try {
			file = new File(fileName);
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			return;
		} catch (Exception e) {
			System.out.println("Error initializing scanner.");
			return;
		}

		String line = "";
		String[] info = { "" };

		try {
			while (in.hasNextLine()) {
				line = in.nextLine();
				info = line.split("\\t+");
				checkType(info);
				if (info[1].equals("i75115")) {
					System.out.println("Found!");
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error: array index out of bound");
		} catch (Exception e) {
			System.out.println("Error reading input");
		} finally {
			in.close();
		}
	}

	public static void checkType(String[] its) {
		if (its[0].equals("i")) {
			addIntersection(its);
		} else if (its[0].equals("r")) {
			addRoad(its);
		}
	}

	public static void drawRd(Its it) { // it as target
		if (it.p() == null) { // base case: reached the source node
			return;
		} else {
			canvas.drawRoad(it.p().s(), it.s());
			drawRd(it.p());
		}
	} 

	public static void addIntersection(String[] its) {
		double x1 = Double.parseDouble(its[2]);
		double x2 = Double.parseDouble(its[3]);
		double[] coordinate = { x1, x2 }; //// Problem
		String s = its[1];
		intersection.put(s, coordinate);
		graph.addVertice(s, new Its(s));
	}

	public static void addRoad(String[] its) {
		String s1 = its[2];
		String s2 = its[3];
		String[] ints = { s1, s2 };
		road.put(its[1], ints);
		HashMap<String, Its> vertice = graph.getVertice();

		double wt = distance(intersection.get(s1)[0], intersection.get(s1)[1], intersection.get(s2)[0],
				intersection.get(s2)[1]);

		graph.setEdge(vertice.get(s1), vertice.get(s2), wt);

		/*
		 * if (vertice.containsKey(s1)) { if (vertice.containsKey(s2)) {
		 * graph.setEdge(vertice.get(s1), vertice.get(s2), wt); } else {
		 * graph.setEdge(vertice.get(s1), new Its(s2), wt); } } else { if
		 * (vertice.containsKey(s2)) { graph.setEdge(new Its(s1), vertice.get(s2), wt);
		 * } else { graph.setEdge(new Its(s1), new Its(s2), wt); } }
		 */
	}

	// Cite Source!!!!!!!!!!!!!!!!!!!!
	private static double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515; // to mile
		return dist;
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

}