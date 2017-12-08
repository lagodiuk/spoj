package spoj.dynamic_programming.vocv;

import java.util.List;

// http://www.spoj.com/problems/VOCV/

// TODO: Non-recursive variant of solution, which can be easily ported to C programming language
public class VOCV_4 {

	public static void main(String[] args) {
		VOCV_Brute_Force.testFewManualCases(VOCV_4::findMinLightsCount);
		VOCV_Brute_Force.testRandomized(VOCV_4::findMinLightsCount, 16, true);
		VOCV_Brute_Force.testRandomized(VOCV_4::findMinLightsCount, 100, false);
	}

	static Result findMinLightsCount(List<Edge> input) {
		Edge[] edges = createArrayOfSortedEdges(input);
		Node[] nodes = initializeNodes(edges);
		findMinLightsCount(nodes[0], nodes, edges);
		Result result = new Result(nodes[0].minLights, nodes[0].variantsCount);
		return result;
	}

	static void findMinLightsCount(Node curr, Node[] nodes, Edge[] edges) {
		while (true) {
			curr.visited = true;

			if ((curr.nextEdgeIndex < edges.length)
					&& (edges[curr.nextEdgeIndex].node1 == edges[curr.firstEdgeIndex].node1)) {

				updateCurrentNode(curr, nodes, edges);

				if (nodes[edges[curr.nextEdgeIndex].node2].visited) {
					curr.parentNodeIndex = edges[curr.nextEdgeIndex].node2;
					curr.nextEdgeIndex++;
				} else {
					curr.nextEdgeIndex++;
					curr = nodes[edges[curr.nextEdgeIndex - 1].node2];
				}

			} else {

				updateCurrentNode(curr, nodes, edges);

				if (curr.minLightsIncludingCurrent == curr.minLightsExcludingCurrent) {
					curr.minLights = curr.minLightsIncludingCurrent;
					curr.variantsCount = curr.variantsCountIncludingCurrent + curr.variantsCountExcludingCurrent;

				} else if (curr.minLightsIncludingCurrent < curr.minLightsExcludingCurrent) {
					curr.minLights = curr.minLightsIncludingCurrent;
					curr.variantsCount = curr.variantsCountIncludingCurrent;

				} else {
					curr.minLights = curr.minLightsExcludingCurrent;
					curr.variantsCount = curr.variantsCountExcludingCurrent;
				}

				if (curr.parentNodeIndex == -1) {
					break;
				} else {
					curr = nodes[curr.parentNodeIndex];
				}
			}
		}
	}

	private static void updateCurrentNode(Node curr, Node[] nodes, Edge[] edges) {
		if (((curr.nextEdgeIndex - 1) >= curr.firstEdgeIndex)
				&& (edges[curr.nextEdgeIndex - 1].node2 != curr.parentNodeIndex)) {

			Node prevChild = nodes[edges[curr.nextEdgeIndex - 1].node2];

			curr.minLightsIncludingCurrent += prevChild.minLights;
			curr.variantsCountIncludingCurrent *= prevChild.variantsCount;

			curr.minLightsExcludingCurrent += prevChild.minLightsIncludingCurrent;
			curr.variantsCountExcludingCurrent *= prevChild.variantsCountIncludingCurrent;
		}
	}

	private static Node[] initializeNodes(Edge[] edges) {
		Node[] nodes = new Node[(edges.length / 2) + 1];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node();
		}

		nodes[0].nextEdgeIndex = 0;
		nodes[0].firstEdgeIndex = 0;

		for (int i = 1; i < edges.length; i++) {
			if (edges[i].node1 != edges[i - 1].node1) {
				nodes[edges[i].node1].nextEdgeIndex = i;
				nodes[edges[i].node1].firstEdgeIndex = i;
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
		int firstEdgeIndex;
		int nextEdgeIndex;
		int parentNodeIndex = -1;

		int minLights;
		int variantsCount;

		int minLightsIncludingCurrent = 1;
		int variantsCountIncludingCurrent = 1;

		int minLightsExcludingCurrent = 0;
		int variantsCountExcludingCurrent = 1;
	}
}
