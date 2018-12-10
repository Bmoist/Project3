import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class StreetMapping extends JComponent {

	private HashMap<String, String[]> road = new HashMap<String, String[]>();
	private HashMap<String, double[]> coordinate = new HashMap<String, double[]>();
	private Graph graph;
	private Its target;
	private boolean drawMap = false;
	private static double dx, dy;
	private static double minx, miny;

	public StreetMapping() {
	}

	public StreetMapping(HashMap<String, String[]> rd, HashMap<String, double[]> coordinate) {
		setRoads(rd, coordinate);
		setScale();
	}

	public void setRoads(HashMap<String, String[]> rd, HashMap<String, double[]> coordinate) {
		road = rd;
		this.coordinate = coordinate;
	}

	public double getDiffX(HashMap<String, double[]> cd) {
		double max = 0;
		double min = Integer.MAX_VALUE;
		double temp;
		for (String s : cd.keySet()) {
			temp = cd.get(s)[0];
			if (max < temp) {
				max = temp;
			}
			if (min > temp) {
				min = temp;
			}
		}
		return max - min;
	}

	public double getDiffY(HashMap<String, double[]> cd) {
		double max = 0;
		double min = Integer.MAX_VALUE;
		double temp;
		for (String s : cd.keySet()) {
			temp = Math.abs(cd.get(s)[1]);
			if (max < temp) {
				max = temp;
			}
			if (min > temp) {
				min = temp;
			}
		}
		return max - min;
	}

	public double getMinx(HashMap<String, double[]> cd) {
		double min = Integer.MAX_VALUE;
		double temp;
		for (String s : cd.keySet()) {
			temp = cd.get(s)[0];
			if (min > temp) {
				min = temp;
			}
		}
		return min;
	}

	public double getMiny(HashMap<String, double[]> cd) {
		double min = Integer.MAX_VALUE;
		double temp;
		for (String s : cd.keySet()) {
			temp = Math.abs(cd.get(s)[1]);
			if (min > temp) {
				min = temp;
			}
		}
		return min;
	}

	public int scaleX(double x, double diff, double min) {
		double value = x - min;
		double ratio = this.getWidth() / diff;
		return (int) (value * ratio + 1);
	}

	public int scaleY(double y, double diff, double min) {
		double value = Math.abs(y) - min;
		double ratio = this.getHeight() / diff;
		return (int) (value * ratio + 1);
	}

	public int moveX(int y) {
		double ratio = (double) this.getWidth() / (double) this.getHeight();
		return (int) ((this.getHeight() - y) * ratio);
	}

	public int moveY(int x) {
		double ratio = (double) this.getHeight() / (double) this.getWidth();
		return (int) ((this.getWidth() - x) * ratio);
	}

	public void drawMap(Graphics g) {
		for (String element : road.keySet()) {
			String[] road = this.road.get(element);
			double[] c1 = coordinate.get(road[0]);
			double[] c2 = coordinate.get(road[1]);

			int[] xy1 = getXY(c1);
			int[] xy2 = getXY(c2);
			g.drawLine(xy1[0], xy1[1], xy2[0], xy2[1]);

		}
	}

	public int[] getXY(double[] c) {
		int xy[] = new int[2];
		int x1 = scaleX(c[0], dx, minx);
		int y1 = scaleY(c[1], dy, miny);
		int a1 = moveX(y1);
		int a2 = moveY(x1);
		xy[0] = a1;
		xy[1] = a2;
		return xy;

	}

	public void setScale() {
		dx = getDiffX(coordinate);
		dy = getDiffY(coordinate);
		minx = getMinx(coordinate);
		miny = getMiny(coordinate);
	}

	public void drawMap() {
		drawMap = true;
	}
	public void paintComponent(Graphics g) {
		/* Graphics2D g2 = (Graphics2D) g; */
		if(drawMap) {
			drawMap(g);
		}
	}

	public void drawRoad(String v1, String v2) {
		Graphics g = getGraphics();
		double[] c1 = coordinate.get(v1);
		double[] c2 = coordinate.get(v2);
		int[] xy1 = getXY(c1);
		int[] xy2 = getXY(c2);

		g.setColor(Color.RED);
		g.drawLine(xy1[0], xy1[1], xy2[0], xy2[1]);
		// ((Graphics2D) g).setStroke(new BasicStroke(2));
	}

}