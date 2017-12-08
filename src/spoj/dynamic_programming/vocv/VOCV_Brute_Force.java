package spoj.dynamic_programming.vocv;

// http://www.spoj.com/problems/VOCV/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.Function;

public class VOCV_Brute_Force {

	public static void main(String[] args) {
		testFewManualCases(null);
		testRandomized(null);
	}

	static void testRandomized(Function<List<Edge>, Result> f) {
		testRandomized(f, 12, true);
	}

	static void testRandomized(Function<List<Edge>, Result> f, int maxNodesCount, boolean compareWithBruteForce) {
		Random rnd = new Random(1);

		for (int i = 0; i < 30; i++) {
			Node root = generateRandomTree(rnd, maxNodesCount);
			List<Edge> edges = collectEdges(rnd, root);
			evaluateSolution(edges, f, compareWithBruteForce);
		}
	}

	static List<Edge> collectEdges(Random rnd, Node root) {
		List<Edge> edges = new ArrayList<>();
		collectEdges(rnd, root, edges);
		return edges;
	}

	static void collectEdges(Random rnd, Node root, List<Edge> edges) {
		for (Node c : root.children) {
			if (rnd.nextDouble() > 0.5) {
				edges.add(new Edge(root.index + 1, c.index + 1));
			} else {
				edges.add(new Edge(c.index + 1, root.index + 1));
			}
			collectEdges(rnd, c, edges);
		}
	}

	static Node generateRandomTree(Random rnd, int maxNodesCount) {
		int available = rnd.nextInt(maxNodesCount) + 2;
		Node root = new Node();
		root.index = available;
		generateRandomTree(rnd, available, root);
		return root;
	}

	static int generateRandomTree(Random rnd, int available, Node root) {
		while (available > 0) {

			Node child = new Node();
			child.index = available - 1;
			available--;

			root.children.add(child);
			if (rnd.nextDouble() > 0.5) {
				available = generateRandomTree(rnd, available, child);
			}
		}
		return available;
	}

	static void testFewManualCases(Function<List<Edge>, Result> f) {
		evaluateSolution(Arrays.asList(
				new Edge(1, 2)), f, true);

		evaluateSolution(Arrays.asList(
				new Edge(1, 2),
				new Edge(2, 3)), f, true);

		evaluateSolution(Arrays.asList(
				new Edge(1, 2),
				new Edge(2, 3),
				new Edge(3, 4)), f, true);

		evaluateSolution(Arrays.asList(
				new Edge(1, 2),
				new Edge(2, 3),
				new Edge(3, 4),
				new Edge(3, 5),
				new Edge(5, 6)), f, true);

		evaluateSolution(Arrays.asList(
				new Edge(1, 2),
				new Edge(2, 3),
				new Edge(1, 4),
				new Edge(1, 5),
				new Edge(5, 6)), f, true);

		evaluateSolution(Arrays.asList(
				new Edge(2, 1),
				new Edge(2, 3),
				new Edge(4, 1),
				new Edge(1, 5),
				new Edge(6, 5)), f, true);
	}

	static void evaluateSolution(List<Edge> edges, Function<List<Edge>, Result> f, boolean compareWithBruteForce) {
		System.out.println(edges.size() + 1);
		for (Edge e : edges) {
			System.out.println(e.node1 + " " + e.node2);
		}

		if (compareWithBruteForce) {
			Result bruteForceResult = findMinLightsCount(edges);

			if (f != null) {
				Result testedResult = f.apply(edges);
				if (!bruteForceResult.equals(testedResult)) {
					System.out.println("Expected result:\n" + bruteForceResult);
					System.out.println("Actual result:\n" + testedResult);
					throw new RuntimeException("Different results!");
				}
			}

			System.out.println("Expected result:\n" + bruteForceResult);
			System.out.println();

		} else {
			Result testedResult = f.apply(edges);
			System.out.println("Tested result:\n" + testedResult);
			System.out.println();
		}
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

		TreeMap<Integer, Integer> lightsToVariantsCount = new TreeMap<>();
		findMinLightsCount(nodes, 0, lightsToVariantsCount);

		Result result = new Result(lightsToVariantsCount.firstKey(), lightsToVariantsCount.firstEntry().getValue());
		return result;
	}

	static void findMinLightsCount(Node[] nodes, int curr, TreeMap<Integer, Integer> lightsToVariantsCount) {
		if (curr == nodes.length) {
			if (allEdgesHighlighted(nodes[0])) {
				int amountOfSwitchedOnNodes = calculateSwitchedOnNodes(nodes);
				Integer variantsCount = lightsToVariantsCount.getOrDefault(amountOfSwitchedOnNodes, 0);
				lightsToVariantsCount.put(amountOfSwitchedOnNodes, variantsCount + 1);
			}
			return;
		}

		nodes[curr].isSwitchedOn = true;
		findMinLightsCount(nodes, curr + 1, lightsToVariantsCount);

		nodes[curr].isSwitchedOn = false;
		findMinLightsCount(nodes, curr + 1, lightsToVariantsCount);
	}

	private static int calculateSwitchedOnNodes(Node[] nodes) {
		int amountOfSwitchedOnNodes = 0;
		for (Node n : nodes) {
			if (n.isSwitchedOn) {
				amountOfSwitchedOnNodes++;
			}
		}
		return amountOfSwitchedOnNodes;
	}

	static boolean allEdgesHighlighted(Node root) {
		for (Node c : root.children) {
			if ((!root.isSwitchedOn) && (!c.isSwitchedOn)) {
				return false;
			}
		}

		for (Node c : root.children) {
			if (!allEdgesHighlighted(c)) {
				return false;
			}
		}

		return true;
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
		int index;
		List<Node> children = new ArrayList<>();
		boolean isSwitchedOn = false;
	}
}
