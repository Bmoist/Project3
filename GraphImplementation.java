import java.util.*;

class Graph {
	private HashMap<String, Its> vertice = new HashMap<String, Its>();

	public Graph() {
	}

	public Graph(Its[] it) {
		for (Its v : it) {
			this.addVertice(v.s(), v);
		}
	}

	public Its getVertice(String s) {
		return vertice.get(s);
	}

	public int count() {
		return vertice.size();
	}

	public HashMap<String, Its> getVertice() {
		return vertice;
	}

	public void addVertice(String s, Its it) {
		vertice.put(s, it);
	}

	public Its first(Its a) {
		return a.first();
	}

	public Its next(Its a, Its b) {
		if (isEdge(a, b)) {
			return a.next();
		} else {
			System.out.println("Not an edge: calling next");
			return null;
		}
	}

	public double weight(Its a, Its b) {
		if (!isEdge(a, b)) {
			System.out.println("Not an edge: calling weight");
			return 0;
		}
		return a.getWeight(b);
	}

	public void setEdge(Its a, Its b, double wt) {
		assert wt != 0;
		if (!vertice.containsKey(b.s())) {
			vertice.put(b.s(), b);
		}
		if (!vertice.containsKey(a.s())) {
			vertice.put(a.s(), a);
		}
		a.setWeight(b, wt);
	}

	public void delEdge(Its a, Its b) {
		a.deleteEdge(b);
	}

	public boolean isEdge(Its a, Its b) {
		int count = 0;
		for (Its c : a.getNeighbor()) {
			count++;
			if (c == b) {
				a.setPointer(count);
				return true;
			}
		}
		return false;
	}

	public void mark(Its a, int v) {
		a.mark(v);
	}

	public int getMark(Its a) {
		return a.getMark();
	}

	public void printGraph() {
		for (String s : vertice.keySet()) {
			System.out.println(s + ": " + vertice.get(s).d());
		}
	}
}

class Its implements Comparable<Its> {
	private String intersection = "";
	private int pointer = 0;
	private int mark = 0;
	private double d = 0;		// d as distance
	private Its p = null;		// p as parent
	private LinkedList<Its> neighbor = new LinkedList<Its>();
	private HashMap<Its, Double> weight = new HashMap<Its, Double>();

	public Its(String it) {
		intersection = it;
	}

	public Its(String s, Its it, double wt) {
		intersection = s;
		addNeighbor(it, wt);
	}

	public double d() {
		return d;
	}

	public Its p() {
		return p;
	}

	public String s() {
		return intersection;
	}

	public int getMark() {
		return mark;
	}

	public int getPointer() {
		return pointer;
	}

	public void setD(double a) {
		d = a;
	}

	public void setP(Its it) {
		p = it;
	}

	public String printP(Its it) {		// it as target
		if (it.p == null) {		// base case: reached the source node
			return "Path: ";
		} else {
			return printP(it.p) + " " + it.p.s();
		}
	}

	public void mark(int n) {
		mark = n;
	}

	public void setPointer(int n) {
		pointer = n;
	}

	public LinkedList<Its> getNeighbor() {
		return neighbor;
	}

	public void addNeighbor(Its it, double wt) {
		neighbor.add(it);
		it.neighbor.add(this);
		weight.put(it, wt);
		it.weight.put(this, wt);
	}

	public double getWeight(Its it) {
		return weight.get(it);
	}

	public void setWeight(Its it, double wt) {
		assert wt != 0;
		addNeighbor(it, wt);
	}

	public void deleteEdge(Its it) {
		neighbor.remove(it);
		it.neighbor.remove(this);
		weight.remove(it);
		it.weight.remove(this);
	}

	public Its first() {
		return neighbor.get(0);
	}

	public Its next() {
		return neighbor.get(pointer);
	}

	public Its neighbor(int n) {
		return neighbor.get(n);
	}

	@Override
	public int compareTo(Its it) {
		if (this.d < it.d) {
			return -1;
		} else if (this.d > it.d) {
			return 1;
		}
		return 0;
	}
}