package spoj.dynamic_programming.vocv;

import java.util.List;

// http://www.spoj.com/problems/VOCV/

// TODO: can be easily ported to C programming language
public class VOCV_3 {

	public static void main(String[] args) {
		VOCV_Brute_Force.testFewManualCases(VOCV_3::findMinLightsCount);
		VOCV_Brute_Force.testRandomized(VOCV_3::findMinLightsCount, 16, true);
	}

	static Result findMinLightsCount(List<Edge> input) {
		Edge[] edges = createArrayOfSortedEdges(input);
		Node[] nodes = initializeNodes(edges);
		findMinLightsCount(nodes[0], nodes, edges);
		Result result = new Result(nodes[0].minLights, nodes[0].variantsCount);
		return result;
	}

	static void findMinLightsCount(Node root, Node[] nodes, Edge[] edges) {

		root.visited = true;

		int includingCurrent = 1;
		int includingCurrentVariants = 1;

		int excludingCurrent = 0;
		int excludingCurrentVariants = 1;

		int currNodeIndex = edges[root.nextEdgeIndex].node1;
		while ((root.nextEdgeIndex < edges.length)
				&& (edges[root.nextEdgeIndex].node1 == currNodeIndex)) {

			Node nc = nodes[edges[root.nextEdgeIndex].node2];

			if (!nc.visited) {
				findMinLightsCount(nc, nodes, edges);

				includingCurrent += nc.minLights;
				includingCurrentVariants *= nc.variantsCount;

				excludingCurrent += nc.minLightsIncludingCurrent;
				excludingCurrentVariants *= nc.variantsCountIncludingCurrent;
			}

			root.nextEdgeIndex++;
		}

		if (includingCurrent == excludingCurrent) {
			root.minLights = includingCurrent;
			root.variantsCount = includingCurrentVariants + excludingCurrentVariants;

		} else if (includingCurrent < excludingCurrent) {
			root.minLights = includingCurrent;
			root.variantsCount = includingCurrentVariants;

		} else {
			root.minLights = excludingCurrent;
			root.variantsCount = excludingCurrentVariants;
		}

		root.minLightsIncludingCurrent = includingCurrent;
		root.variantsCountIncludingCurrent = includingCurrentVariants;
	}
	private static Node[] initializeNodes(Edge[] edges) {
		Node[] nodes = new Node[(edges.length / 2) + 1];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node();
		}

		nodes[0].nextEdgeIndex = 0;

		for (int i = 1; i < edges.length; i++) {
			if (edges[i].node1 != edges[i - 1].node1) {
				nodes[edges[i].node1].nextEdgeIndex = i;
			}
		}
		return nodes;
	}

	static Edge[] createArrayOfSortedEdges(List<Edge> input) {
		Edge[] tmp = new Edge[input.size() * 2];
		int idx = 0;
		for (Edge e : input) {
			// Create edges in both directions
			tmp[idx] = new Edge(e.node1 - 1, e.node2 - 1);
			tmp[idx + 1] = new Edge(e.node2 - 1, e.node1 - 1);
			idx += 2;
		}

		Edge[] result = sortEdgesByNode1(tmp);
		return result;
	}

	// Counting sort O(N)
	static Edge[] sortEdgesByNode1(Edge[] edges) {
		int[] nodeCount = new int[(edges.length / 2) + 1];

		// count occurrence of key: node1
		for (int i = edges.length - 1; i >= 0; i--) {
			Edge e = edges[i];
			nodeCount[e.node1]++;
		}

		// transform to cumulative sum
		for (int i = 1; i < nodeCount.length; i++) {
			nodeCount[i] += nodeCount[i - 1];
		}

		Edge[] result = new Edge[edges.length];
		for (int i = edges.length - 1; i >= 0; i--) {
			Edge e = edges[i];
			int insertionIndex = nodeCount[e.node1] - 1;
			result[insertionIndex] = e;
			nodeCount[e.node1]--;
		}
		return result;
	}

	static class Node {
		boolean visited;
		int nextEdgeIndex;

		int minLights;
		int variantsCount;

		int minLightsIncludingCurrent;
		int variantsCountIncludingCurrent;
	}
}
