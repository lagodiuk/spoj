package spoj.dynamic_programming.mtree;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import spoj.dynamic_programming.mtree.MTREE_4_Stack_Based_MOD.Edge;

public class _MTREE_5_Final {

	public static void main(String[] args) throws Exception {
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
			PrintWriter pw = new PrintWriter(new FileWriter("/Users/yura/sandbox/code_interview_training/src/spoj/dynamic_programming/mtree/test/in_"
					+ String.format("%04d", i)));
			Edge[] edges = generateRandomTree(rnd.nextInt(7) + 1, rnd);
			pw.println(edges.length + 1);
			for (Edge e : edges) {
				pw.println(e.from + " " + e.to + " " + e.weight);
			}
			pw.flush();
			pw.close();

			long fast = calculate(edges);
			long slow = calculateFloydWarshall(edges);

			pw = new PrintWriter(new FileWriter("/Users/yura/sandbox/code_interview_training/src/spoj/dynamic_programming/mtree/test/out_"
					+ String.format("%04d", i)));
			pw.println(slow);
			pw.flush();
			pw.close();
			if (fast != slow) {
				System.out.println(fast + "\t" + slow + "\t" + edges.length);
				calculate(edges);
				calculateFloydWarshall(edges);
			}
		}
	}

	static final long MOD = 1000000007;

	// (2 * _2_INV_MOD) == 1 (mod MOD)
	static final long _2_INV_MOD = (MOD + 1) / 2;

	// sorted array with all edges (duplicated in both directions)
	// edges sorted by index of outgoing vertex
	static int[] edgeFrom;
	static int[] edgeTo;
	static int[] edgeWeight;

	static int verticesCount;

	// index in bidirEdges - of the current unvisited edge, which outcomes from given vertex
	static int[] unvisitedEdgeIdx;

	// if given vertex is visited
	static boolean[] vertexVisited;

	// forward: sum of all prefix-paths weights, ending in given vertex
	static long[] fwd;

	// forward: sum of all paths weights, which ends before given vertex
	static long[] exclCurr;

	// backward: sum of all prefix-paths weights, ending in given vertex
	static long[] bkwd;

	// backward: sum of all squared prefix-paths weights, ending in given vertex
	static long[] bkwdSqr;

	// amount of visited children, visited from given vertex
	static int[] visitedChildrenCnt;

	// sum of all path weights of the tree
	// (path weight is a product of all edge weights across the path)
	static long result;

	static int[] stack;
	static int stackTop;

	static long calculate(Edge[] edges) {

		verticesCount = edges.length + 1;
		edgeFrom = new int[(verticesCount - 1) * 2];
		edgeTo = new int[(verticesCount - 1) * 2];
		edgeWeight = new int[(verticesCount - 1) * 2];

		edges = toBidirectionEdges(edges);
		for (int i = 0; i < edges.length; i++) {
			edgeFrom[i] = edges[i].from;
			edgeTo[i] = edges[i].to;
			edgeWeight[i] = edges[i].weight;
		}

		result = 0;
		unvisitedEdgeIdx = new int[verticesCount + 1];
		vertexVisited = new boolean[verticesCount + 1];
		fwd = new long[verticesCount + 1];
		exclCurr = new long[verticesCount + 1];
		bkwd = new long[verticesCount + 1];
		bkwdSqr = new long[verticesCount + 1];
		visitedChildrenCnt = new int[verticesCount + 1];
		stack = new int[verticesCount + 1];
		stackTop = -1;

		sortEdgesByFrom();
		int[] tmp = new int[edgeFrom.length];
		System.arraycopy(edgeFrom, 0, tmp, 0, edgeFrom.length);
		Arrays.sort(tmp);
		if (!Arrays.equals(tmp, edgeFrom)) {
			throw new RuntimeException();
		}

		Arrays.fill(unvisitedEdgeIdx, Integer.MAX_VALUE);
		for (int i = 0; i < edgeFrom.length; i++) {
			unvisitedEdgeIdx[edgeFrom[i]] = Math.min(unvisitedEdgeIdx[edgeFrom[i]], i);
		}

		int startEdge = findLeafEdge();

		unvisitedEdgeIdx[edgeFrom[startEdge]]++;
		vertexVisited[edgeFrom[startEdge]] = true;
		fwd[edgeFrom[startEdge]] = 0;
		exclCurr[edgeFrom[startEdge]] = 0;
		traverseTreeDFS(startEdge);

		return result;
	}

	static void sortEdgesByFrom() {
		int[] newVertexFrom = new int[(verticesCount - 1) * 2];
		int[] newVertexTo = new int[(verticesCount - 1) * 2];
		int[] newEdgeWeight = new int[(verticesCount - 1) * 2];

		int[] valCount = new int[verticesCount + 1];
		for (int i = 0; i < edgeFrom.length; i++) {
			valCount[edgeFrom[i]]++;
		}
		valCount[0] = valCount[0] - 1;
		for (int i = 1; i < valCount.length; i++) {
			valCount[i] = valCount[i - 1] + valCount[i];
		}
		for (int i = edgeFrom.length - 1; i >= 0; i--) {
			newVertexFrom[valCount[edgeFrom[i]]] = edgeFrom[i];
			newVertexTo[valCount[edgeFrom[i]]] = edgeTo[i];
			newEdgeWeight[valCount[edgeFrom[i]]] = edgeWeight[i];
			valCount[edgeFrom[i]]--;
		}
		edgeFrom = newVertexFrom;
		edgeTo = newVertexTo;
		edgeWeight = newEdgeWeight;
	}

	static void traverseTreeDFS(int currEdge) {

		stack[++stackTop] = currEdge;

		LOOP : while (stackTop > -1) {

			currEdge = stack[stackTop--];
			int currVertex = edgeTo[currEdge];

			if (!vertexVisited[currVertex]) {

				int prevVertex = edgeFrom[currEdge];

				fwd[currVertex] = ((fwd[prevVertex] * edgeWeight[currEdge]) + edgeWeight[currEdge]) % MOD;
				exclCurr[currVertex] = (fwd[prevVertex] + exclCurr[prevVertex]) % MOD;
				vertexVisited[currVertex] = true;

				if (!hasUnvisitedChildren(currVertex)) {
					bkwd[currVertex] = edgeWeight[currEdge];
					result = (result + fwd[currVertex] + exclCurr[currVertex]) % MOD;
					continue LOOP;
				}

				bkwd[currVertex] = 0;
				bkwdSqr[currVertex] = 0;
				visitedChildrenCnt[currVertex] = 0;

				int childEdge = unvisitedEdgeIdx[currVertex];
				while (edgeFrom[childEdge] == currVertex) {
					if (!vertexVisited[edgeTo[childEdge]]) {
						visitedChildrenCnt[currVertex]++;
						stack[++stackTop] = currEdge;
						stack[++stackTop] = childEdge;
						continue LOOP;
					}
					unvisitedEdgeIdx[currVertex]++;
					childEdge = unvisitedEdgeIdx[currVertex];
				}

			} else {

				int childEdge = unvisitedEdgeIdx[currVertex];
				int childVertex = edgeTo[childEdge];

				bkwd[currVertex] = (bkwd[currVertex] + bkwd[childVertex]) % MOD;
				bkwdSqr[currVertex] = ((bkwd[childVertex] * bkwd[childVertex]) + bkwdSqr[currVertex]) % MOD;

				unvisitedEdgeIdx[currVertex]++;
				childEdge = unvisitedEdgeIdx[currVertex];

				while (edgeFrom[childEdge] == currVertex) {
					if (!vertexVisited[edgeTo[childEdge]]) {
						visitedChildrenCnt[currVertex]++;
						stack[++stackTop] = currEdge;
						stack[++stackTop] = childEdge;
						continue LOOP;
					}
					unvisitedEdgeIdx[currVertex]++;
					childEdge = unvisitedEdgeIdx[currVertex];
				}

				if (visitedChildrenCnt[currVertex] > 1) {
					result = (result - ((fwd[currVertex] + exclCurr[currVertex]) * (visitedChildrenCnt[currVertex] - 1))) % MOD;
					if (result < 0) {
						result += MOD;
					}
					result = (result + ((((((bkwd[currVertex] * bkwd[currVertex]) - bkwdSqr[currVertex])) % MOD) * _2_INV_MOD))) % MOD;
					if (result < 0) {
						result += MOD;
					}
				}

				bkwd[currVertex] = ((bkwd[currVertex] * edgeWeight[currEdge]) + edgeWeight[currEdge]) % MOD;
			}
		}
	}

	static int findLeafEdge() {
		for (int i = 1; i < fwd.length; i++) {
			int idx = unvisitedEdgeIdx[i];
			if ((idx == (edgeFrom.length - 1))
					|| (edgeFrom[idx] != edgeFrom[idx + 1])) {

				return idx;
			}
		}
		throw new RuntimeException("Unreachable code");
	}

	static boolean hasUnvisitedChildren(int v) {
		int idx = unvisitedEdgeIdx[v];

		while ((idx < edgeFrom.length)
				&& (edgeFrom[idx] == v)) {

			if (!vertexVisited[edgeTo[idx]]) {
				return true;
			}

			unvisitedEdgeIdx[v]++;
			idx = unvisitedEdgeIdx[v];
		}
		return false;
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
							dist[i][j] = (dist[i][k] * dist[k][j]) % MOD;
						}
					}
				}
			}
		}

		long result = 0;
		for (int i = 0; i < dist.length; i++) {
			for (int j = i; j < dist.length; j++) {
				if ((dist[i][j] != ABSENT) && (i != j)) {
					result = (result + dist[i][j]) % MOD;
				}
			}
		}
		return result % MOD;
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
			int weight = rnd.nextInt(1001);
			if (rnd.nextDouble() > 0.5) {
				edges.add(new Edge(currIdx, idx + 1, weight));
			} else {
				edges.add(new Edge(idx + 1, currIdx, weight));
			}
			idx = generateRandomTree(depth - 1, rnd, edges, idx + 1);
		}

		return idx;
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

}
