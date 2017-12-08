package spoj.dynamic_programming.vocv;

// http://www.spoj.com/problems/VOCV/

import java.util.ArrayList;
import java.util.List;

public class VOCV_1 {

	public static void main(String[] args) {
		VOCV_Brute_Force.testFewManualCases(VOCV_1::findMinLightsCount);
		VOCV_Brute_Force.testRandomized(VOCV_1::findMinLightsCount);
	}

	static Result findMinLightsCount(List<Edge> edges) {
		Node[] nodes = new Node[edges.size() + 1];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node();
		}

		for (Edge e : edges) {
			nodes[e.node1 - 1].children.add(nodes[e.node2 - 1]);
			nodes[e.node2 - 1].children.add(nodes[e.node1 - 1]);
		}

		transformToDirectedTree(nodes[0]);
		findMinLightsCount(nodes[0]);

		Result result = new Result(nodes[0].minLights, nodes[0].variantsCount);
		return result;
	}

	static void findMinLightsCount(Node n) {
		if ((n.minLights != -1) && (n.variantsCount != -1)) {
			return;
		}

		for (Node nc : n.children) {
			findMinLightsCount(nc);
		}

		// // Seems, that we don't need to execute this:
		// for (Node nc : n.children) {
		// for (Node nnc : nc.children) {
		// findMinLightsCount(nnc);
		// }
		// }

		int includingCurrent = 1;
		int includingCurrentVariants = 1;
		for (Node nc : n.children) {
			includingCurrent += nc.minLights;
			includingCurrentVariants *= nc.variantsCount;
		}

		int excludingCurrent = 0;
		int excludingCurrentVariants = 1;
		for (Node nc : n.children) {
			excludingCurrent += 1;
			for (Node nnc : nc.children) {
				excludingCurrent += nnc.minLights;
				excludingCurrentVariants *= nnc.variantsCount;
			}
		}

		if (includingCurrent == excludingCurrent) {
			n.minLights = includingCurrent;
			n.variantsCount = includingCurrentVariants + excludingCurrentVariants;
		} else if (includingCurrent < excludingCurrent) {
			n.minLights = includingCurrent;
			n.variantsCount = includingCurrentVariants;

		} else {
			n.minLights = excludingCurrent;
			n.variantsCount = excludingCurrentVariants;
		}
	}

	static void transformToDirectedTree(Node root) {
		for (Node c : root.children) {
			int rootIdx = -1;
			for (int i = 0; i < c.children.size(); i++) {
				if (c.children.get(i) == root) {
					rootIdx = i;
					break;
				}
			}
			c.children.remove(rootIdx);
			transformToDirectedTree(c);
		}
	}

	static class Node {
		List<Node> children = new ArrayList<>();
		int minLights = -1;
		int variantsCount = -1;
	}
}
