package spoj.dynamic_programming.najkraci;

public class Edge {

	final int from;
	final int to;
	final int weight;

	public Edge(int from, int to, int weight) {
		this.from = from - 1;
		this.to = to - 1;
		this.weight = weight;
	}
}
