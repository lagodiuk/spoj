package spoj.dynamic_programming.mtree;

// http://www.spoj.com/problems/MTREE/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MTREE_2_Less_Local_variables {

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
			Edge[] edges = generateRandomTree(6, rnd);
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
	// edges sorted by index of outgoing vertex
	static Edge[] edges;

	// index in bidirEdges - of the current unvisited edge, which outcomes from given vertex
	static int[] unvisitedEdgeIdx;

	// if given vertex is visited
	static boolean[] vertexVisited;

	// forward: sum of all prefix-paths weights, ending in given vertex
	static long[] tgt;

	// forward: sum of all paths weights, which ends before given vertex
	static long[] ign;

	// backward: sum of all prefix-paths weights, ending in given vertex
	static long[] suf;

	// backward: sum of all squared prefix-paths weights, ending in given vertex
	static long[] sufSqr;

	static int[] visitedChildrenCnt;

	// sum of all path weights of the tree
	// (path weight is a product of all edge weights across the path)
	static long result;

	static long calculate(Edge[] oneDirectionEdges) {

		result = 0;
		unvisitedEdgeIdx = new int[oneDirectionEdges.length + 2];
		vertexVisited = new boolean[oneDirectionEdges.length + 2];
		tgt = new long[oneDirectionEdges.length + 2];
		ign = new long[oneDirectionEdges.length + 2];
		suf = new long[oneDirectionEdges.length + 2];
		sufSqr = new long[oneDirectionEdges.length + 2];
		visitedChildrenCnt = new int[oneDirectionEdges.length + 2];

		edges = toBidirectionEdges(oneDirectionEdges);
		Arrays.sort(edges, (e1, e2) -> Integer.compare(e1.from, e2.from));

		Arrays.fill(unvisitedEdgeIdx, Integer.MAX_VALUE);
		for (int i = 0; i < edges.length; i++) {
			unvisitedEdgeIdx[edges[i].from] = Math.min(unvisitedEdgeIdx[edges[i].from], i);
		}

		Edge startEdge = findLeafEdge();

		unvisitedEdgeIdx[startEdge.from]++;
		vertexVisited[startEdge.from] = true;
		tgt[startEdge.from] = 0;
		ign[startEdge.from] = 0;
		visit(startEdge);

		return result;
	}

	static void visit(Edge currEdge) {

		int currVertex = currEdge.to;
		int prevVertex = currEdge.from;

		tgt[currVertex] = (tgt[prevVertex] * currEdge.weight) + currEdge.weight;
		ign[currVertex] = tgt[prevVertex] + ign[prevVertex];
		vertexVisited[currVertex] = true;

		if (!hasUnvisitedChildren(currVertex)) {
			suf[currVertex] = currEdge.weight;
			result += tgt[currVertex] + ign[currVertex];
			return;
		}

		suf[currVertex] = 0;
		sufSqr[currVertex] = 0;
		visitedChildrenCnt[currVertex] = 0;

		Edge childEdge = edges[unvisitedEdgeIdx[currVertex]];
		while (childEdge.from == currVertex) {
			if (!vertexVisited[childEdge.to]) {
				visitedChildrenCnt[currVertex]++;
				visit(childEdge);
				suf[currVertex] += suf[childEdge.to];
				sufSqr[currVertex] += suf[childEdge.to] * suf[childEdge.to];
			}
			unvisitedEdgeIdx[currVertex]++;
			childEdge = edges[unvisitedEdgeIdx[currVertex]];
		}

		if (visitedChildrenCnt[currVertex] > 1) {
			result -= (tgt[currVertex] + ign[currVertex]) * (visitedChildrenCnt[currVertex] - 1);
			result += ((suf[currVertex] * suf[currVertex]) - sufSqr[currVertex]) / 2;
		}

		suf[currVertex] = (suf[currVertex] * currEdge.weight) + currEdge.weight;
	}

	private static Edge findLeafEdge() {
		for (int i = 1; i < tgt.length; i++) {
			int idx = unvisitedEdgeIdx[i];
			if ((idx == (edges.length - 1))
					|| (edges[idx].from != edges[idx + 1].from)) {

				return edges[idx];
			}
		}
		throw new RuntimeException("Unreachable code");
	}

	private static boolean hasUnvisitedChildren(int v) {
		int idx = unvisitedEdgeIdx[v];

		while ((idx < edges.length)
				&& (edges[idx].from == v)) {

			if (!vertexVisited[edges[idx].to]) {
				return true;
			}

			unvisitedEdgeIdx[v]++;
			idx = unvisitedEdgeIdx[v];
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
