package spoj.dynamic_programming.vocv;

public class Edge {

	int node1;
	int node2;

	public Edge(int node1, int node2) {
		this.node1 = node1;
		this.node2 = node2;
	}

	@Override
	public String toString() {
		return "(" + this.node1 + ", " + this.node2 + ")";
	}
}
