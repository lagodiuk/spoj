package spoj.dynamic_programming.mtree;

//http://www.spoj.com/problems/MTREE/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MTREE_0_Initial {

	public static void main1(String... args) {
		test1();
		test2();
		test3();
		test3_2();
	}

	private static void test3_2() {
		Vertex v1 = new Vertex(2);
		Vertex v2 = new Vertex(3);
		Vertex v3 = new Vertex(2);
		Vertex v4 = new Vertex(2);
		v1.children.add(v2);
		v1.children.add(v3);
		v2.children.add(v4);
		System.out.println(calc(v1));
	}

	private static void test3() {
		Vertex v1 = new Vertex(2);
		Vertex v2 = new Vertex(3);
		Vertex v3 = new Vertex(2);
		Vertex v4 = new Vertex(2);
		v1.children.add(v2);
		v2.children.add(v3);
		v2.children.add(v4);
		System.out.println(calc(v1));
	}

	private static void test2() {
		Vertex v1 = new Vertex(5);
		Vertex v2 = new Vertex(5);
		Vertex v3 = new Vertex(5);
		v1.children.add(v2);
		v1.children.add(v3);
		System.out.println(calc(v1));
	}

	private static void test1() {
		Vertex v1 = new Vertex(100);
		Vertex v2 = new Vertex(100);
		v1.children.add(v2);
		System.out.println(calc(v1));
	}

	static int calc(Vertex v) {
		return calc(v, 0, 0);
	}

	static int calc(Vertex v, int tgt, int ign) {
		v.tgt = (tgt * v.w) + v.w;
		v.ign = tgt + ign;
		v.suf = v.w;
		if (v.children.isEmpty()) {
			return v.tgt + v.ign;
		}

		int result = 0;
		int sufSum = 0;
		int sufSumSquares = 0;
		for (Vertex ch : v.children) {
			result += calc(ch, v.tgt, v.ign);
			sufSum += ch.suf;
			sufSumSquares += ch.suf * ch.suf;
		}
		v.suf += v.w * sufSum;

		if (v.children.size() > 1) {
			result -= (v.tgt + v.ign) * (v.children.size() - 1);

			int prod = 0;
			// xy + xz + yz = ((x + y + z)^2 - (x^2 + y^2 + z^2)) / 2
			// xy + xz + yz + xk + yk + zk = ((x + y + z + k)^2 - (x^2 + y^2 + z^2 + k^2)) / 2
			prod = ((sufSum * sufSum) - sufSumSquares) / 2;
			// for(Vertex v1 : v.children) {
			// for(Vertex v2 : v.children) {
			// if(v1 != v2) {
			// prod += v1.suf * v2.suf;
			// }
			// }
			// }
			// prod /= 2;
			result += prod;
		}
		return result;
	}

	static class Vertex {
		List<Vertex> children = new ArrayList<>();
		int w;

		Vertex(int w) {
			this.w = w;
		}

		int tgt;
		int ign;
		int suf;
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

	static long calculate(Edge[] edges) {
		Map<Integer, Map<Integer, Integer>> fromToWeight = new HashMap<>();
		for (Edge e : edges) {
			if (fromToWeight.get(e.from) == null) {
				fromToWeight.put(e.from, new HashMap<>());
			}
			if (fromToWeight.get(e.to) == null) {
				fromToWeight.put(e.to, new HashMap<>());
			}
			fromToWeight.get(e.from).put(e.to, e.weight);
			fromToWeight.get(e.to).put(e.from, e.weight);
		}
		boolean[] visited = new boolean[edges.length + 2];
		long[] tgt = new long[edges.length + 2];
		long[] ign = new long[edges.length + 2];
		long[] suf = new long[edges.length + 2];

		int startIdx = -1;
		int startWeight = -1;
		int leafIdx = -1;
		for (Edge e : edges) {
			if (fromToWeight.get(e.from).size() == 1) {
				startIdx = e.to;
				leafIdx = e.from;
				startWeight = e.weight;
				break;
			}
			if (fromToWeight.get(e.to).size() == 1) {
				startIdx = e.from;
				leafIdx = e.to;
				startWeight = e.weight;
				break;
			}
		}
		visited[leafIdx] = true;

		return calculate(startIdx, 0, 0, startWeight, fromToWeight, visited, tgt, ign, suf);
	}

	// O(N) - in case of tree
	static long calculate(int v, long tgt, long ign, int w,
			Map<Integer, Map<Integer, Integer>> fromToWeight,
			boolean[] visited, long[] tgtArr, long[] ignArr, long[] sufArr) {

		tgtArr[v] = (tgt * w) + w;
		ignArr[v] = tgt + ign;
		sufArr[v] = w;
		visited[v] = true;

		boolean hasUnvisitedChildren = false;
		for (int chIdx : fromToWeight.get(v).keySet()) {
			if (!visited[chIdx]) {
				hasUnvisitedChildren = true;
				break;
			}
		}

		if (!hasUnvisitedChildren) {
			return tgtArr[v] + ignArr[v];
		}

		long result = 0;
		long sufSum = 0;
		long sumSufSquares = 0;
		int visitedChildrenCount = 0;
		for (int chIdx : fromToWeight.get(v).keySet()) {
			if (!visited[chIdx]) {
				result += calculate(chIdx, tgtArr[v], ignArr[v], fromToWeight.get(v).get(chIdx), fromToWeight, visited, tgtArr, ignArr, sufArr);
				sufSum += sufArr[chIdx];
				sumSufSquares += sufArr[chIdx] * sufArr[chIdx];
				visitedChildrenCount++;
			}
		}
		sufArr[v] += w * sufSum;

		if (visitedChildrenCount > 1) {
			result -= (tgtArr[v] + ignArr[v]) * (visitedChildrenCount - 1);
			result += ((sufSum * sufSum) - sumSufSquares) / 2;
		}

		return result;
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

	public static void main(String... args) {
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
		for (int i = 0; i < 255; i++) {
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

	static Edge[] generateRandomTree(int depth, Random rnd) {
		List<Edge> edgesList = new ArrayList<>();
		generateRandomTree(depth, rnd, edgesList, 1);
		return edgesList.toArray(new Edge[]{});
	}

	static int generateRandomTree(int depth, Random rnd, List<Edge> edges, int idx) {
		if (depth == 0) {
			return idx;
		}

		int children = rnd.nextInt(4) + 1;
		int currIdx = idx;
		for (int i = 0; i < children; i++) {
			int weight = rnd.nextInt(10) + 1;
			edges.add(new Edge(currIdx, idx + 1, weight));
			idx = generateRandomTree(depth - 1, rnd, edges, idx + 1);
		}

		return idx;
	}
}