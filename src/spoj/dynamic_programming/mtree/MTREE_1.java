package spoj.dynamic_programming.mtree;

// http://www.spoj.com/problems/MTREE/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MTREE_1 {

	public static void main(String[] args) {
		System.out.println(calculate(new Edge[]{
				new Edge(3, 2, 100),
				new Edge(2, 1, 100),
		}));

		System.out.println(calculate(new Edge[]{
				new Edge(1, 2, 5),
				new Edge(1, 3, 5),
				new Edge(1, 4, 5),
		}));

		System.out.println(calculate(new Edge[]{
				new Edge(1, 2, 2),
				new Edge(2, 3, 3),
				new Edge(4, 3, 2),
				new Edge(5, 3, 2),
		}));

		System.out.println(calculate(new Edge[]{
				new Edge(1, 2, 1),
				new Edge(2, 3, 1),
				new Edge(3, 4, 1),
		}));

		System.out.println(calculate(new Edge[]{
				new Edge(1, 2, 1),
				new Edge(2, 3, 1),
				new Edge(3, 4, 1),
				new Edge(1, 5, 1),
				new Edge(5, 6, 1),
				new Edge(6, 7, 1),
		}));

		System.out.println(calculate(new Edge[]{
				new Edge(1, 2, 1),
				new Edge(2, 3, 1),
				new Edge(3, 4, 1),
				new Edge(4, 5, 1),
				new Edge(5, 6, 1),
				new Edge(6, 7, 1),
		}));

		Random rnd = new Random(1);
		for (int i = 0; i < 1255; i++) {
			Edge[] edges = generateRandomTree(7, rnd);
			long fast = calculate(edges);
			long slow = calculateFloydWarshall(edges);
			if (fast != slow) {
				System.out.println(fast + "\t" + slow + "\t" + edges.length);
				calculate(edges);
				calculateFloydWarshall(edges);
			}
		}
	}

	// sorted array with all edges (duplicated in both directions)
	static Edge[] bidirEdges;

	// index in bidirEdges - of the first edge, which outcomes from given vertex
	static int[] edgeStartIdxs;

	// if given vertex is visited
	static boolean[] visited;

	// forward: sum of all prefix-paths, ending in given vertex
	static long[] tgtArr;

	// forward: sum of all paths, which ends before given vertex
	static long[] ignArr;

	// backward: sum of all prefix-paths, ending in given vertex
	static long[] sufArr;

	// sum of all path weights of the tree
	// (path weight is a product of all edge weights across the path)
	static long result;

	static long calculate(Edge[] edges) {

		result = 0;
		edgeStartIdxs = new int[edges.length + 2];
		visited = new boolean[edges.length + 2];
		tgtArr = new long[edges.length + 2];
		ignArr = new long[edges.length + 2];
		sufArr = new long[edges.length + 2];

		bidirEdges = toBidirectionEdges(edges);
		Arrays.sort(bidirEdges, (e1, e2) -> Integer.compare(e1.from, e2.from));

		Arrays.fill(edgeStartIdxs, Integer.MAX_VALUE);
		for (int i = 0; i < bidirEdges.length; i++) {
			edgeStartIdxs[bidirEdges[i].from] = Math.min(edgeStartIdxs[bidirEdges[i].from], i);
		}

		Edge startEdge = findLeafEdge();

		visited[startEdge.from] = true;
		calculate(startEdge.to, 0, 0, startEdge.weight);

		return result;
	}

	static void calculate(int v, long tgt, long ign, int w) {

		tgtArr[v] = (tgt * w) + w;
		ignArr[v] = tgt + ign;
		sufArr[v] = w;
		visited[v] = true;

		if (!hasUnvisitedChildren(v)) {
			result += tgtArr[v] + ignArr[v];
			return;
		}

		long sufSum = 0;
		long sufSqrSum = 0;
		int visitedChildrenCnt = 0;
		int idx = edgeStartIdxs[v];

		while (bidirEdges[idx].from == v) {
			if (!visited[bidirEdges[idx].to]) {
				visitedChildrenCnt++;
				calculate(bidirEdges[idx].to, tgtArr[v], ignArr[v], bidirEdges[idx].weight);
				sufSum += sufArr[bidirEdges[idx].to];
				sufSqrSum += sufArr[bidirEdges[idx].to] * sufArr[bidirEdges[idx].to];
			}
			idx++;
		}
		sufArr[v] += sufSum * w;

		if (visitedChildrenCnt > 1) {
			result -= (tgtArr[v] + ignArr[v]) * (visitedChildrenCnt - 1);
			result += ((sufSum * sufSum) - sufSqrSum) / 2;
		}
	}

	private static Edge findLeafEdge() {
		for (int i = 1; i < tgtArr.length; i++) {
			int idx = edgeStartIdxs[i];
			if ((idx == (bidirEdges.length - 1))
					|| (bidirEdges[idx].from != bidirEdges[idx + 1].from)) {

				return bidirEdges[idx];
			}
		}
		throw new RuntimeException("Unreachable code");
	}

	private static boolean hasUnvisitedChildren(int v) {
		int idx = edgeStartIdxs[v];

		while ((idx < bidirEdges.length)
				&& (bidirEdges[idx].from == v)) {

			if (!visited[bidirEdges[idx].to]) {
				return true;
			}
			idx++;
		}
		return false;
	}

	private static Edge[] toBidirectionEdges(Edge[] edges) {
		Edge[] bidirEdges = new Edge[edges.length * 2];
		int ins = 0;
		for (int i = 0; i < edges.length; i++) {
			bidirEdges[ins] = edges[i];
			bidirEdges[ins + 1] = new Edge(edges[i].to, edges[i].from, edges[i].weight);
			ins += 2;
		}
		return bidirEdges;
	}

	static class Edge {
		int from;
		int to;
		int weight;
		Edge(int from, int to, int weight) {
			this.from = from;
			this.to = to;
			this.weight = weight;
		}
	}

	// O(N^3)
	static long calculateFloydWarshall(Edge[] edges) {
		long ABSENT = Long.MAX_VALUE;
		long[][] dist = new long[edges.length + 2][edges.length + 2];
		for (int i = 0; i < dist.length; i++) {
			Arrays.fill(dist[i], ABSENT);
		}

		for (Edge e : edges) {
			dist[e.from][e.to] = e.weight;
			dist[e.to][e.from] = e.weight;
		}

		for (int k = 1; k < dist.length; k++) {
			for (int i = 1; i < dist.length; i++) {
				for (int j = 1; j < dist.length; j++) {
					if ((dist[i][k] != ABSENT) && (dist[k][j] != ABSENT) && (i != j)) {
						if (dist[i][j] == ABSENT) {
							dist[i][j] = dist[i][k] * dist[k][j];
						} else {
							long prod = dist[i][k] * dist[k][j];
							if ((dist[i][k] < dist[i][j]) && (dist[k][j] < dist[i][j]) && (prod > 0)) {
								dist[i][j] = Math.min(dist[i][j], prod);
							}
						}
					}
				}
			}
		}

		long result = 0;
		for (int i = 0; i < dist.length; i++) {
			for (int j = i; j < dist.length; j++) {
				if ((dist[i][j] != ABSENT) && (i != j)) {
					result += dist[i][j];
				}
			}
		}
		return result;
	}

	static Edge[] generateRandomTree(int depth, Random rnd) {
		List<Edge> edgesList = new ArrayList<>();
		generateRandomTree(depth, rnd, edgesList, 1);
		return edgesList.toArray(new Edge[]{});
	}

	static int generateRandomTree(int depth, Random rnd, List<Edge> edges, int idx) {
		if (depth == 0) {
			return idx;
		}

		int children = rnd.nextInt(4) + ((edges.size() == 0) ? 1 : 0);
		int currIdx = idx;
		for (int i = 0; i < children; i++) {
			int weight = rnd.nextInt(10);
			edges.add(new Edge(currIdx, idx + 1, weight));
			idx = generateRandomTree(depth - 1, rnd, edges, idx + 1);
		}

		return idx;
	}
}
