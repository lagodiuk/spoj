package spoj.dynamic_programming.najkraci;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class Compare_1_and_2 {

	public static void main(String[] args) {

		// test(NAJKRACI_1_Floyd_Warshall::solve, NAJKRACI_2_Dijkstra_Not_Optimal::solve);
		// test(NAJKRACI_1_Floyd_Warshall::solve, NAJKRACI_3_Dijkstra_Dynamic_Programming::solve);
		// test(NAJKRACI_1_Floyd_Warshall::solve, NAJKRACI_4_Dijkstra_Dynamic_Programming::solve);
		// test(NAJKRACI_1_Floyd_Warshall::solve, NAJKRACI_5_Dijkstra_Dynamic_Programming::solve);

		// test(NAJKRACI_1_Floyd_Warshall::solve, NAJKRACI_8_Dijkstra_Dynamic_Programming::solve);

		// test(NAJKRACI_1_Floyd_Warshall::solve, NAJKRACI_10_Dijkstra_Dynamic_Programming::solve);

		test(null, NAJKRACI_10_Dijkstra_Dynamic_Programming::solve);
	}

	static void test(BiFunction<Integer, Edge[], int[]> f1, BiFunction<Integer, Edge[], int[]> f2) {
		Random rnd = new Random(1);

		for (int test = 0; test < 1000; test++) {

			int maxWeight = 3;

			int verticesCnt = rnd.nextInt(1500) + 2;
			int edgesCnt = rnd.nextInt(5000) + 1;

			// int verticesCnt = rnd.nextInt(150) + 2;
			// int edgesCnt = rnd.nextInt(555) + 1;

			// int verticesCnt = rnd.nextInt(5) + 2;
			// int edgesCnt = rnd.nextInt(10) + 1;

			Edge[] edges = new Edge[edgesCnt];
			for (int i = 0; i < edgesCnt; i++) {
				int from = rnd.nextInt(verticesCnt) + 1;
				int to = rnd.nextInt(verticesCnt) + 1;
				while (from == to) {
					to = rnd.nextInt(verticesCnt) + 1;
				}
				int baseWeight = rnd.nextInt(maxWeight) + 1;
				for (int j = 0; (j < (rnd.nextInt(4) + 1)) && (i < edgesCnt); j++, i++) {
					int w = rnd.nextInt(maxWeight) + 1;
					if (rnd.nextBoolean()) {
						w = baseWeight;
					}
					edges[i] = new Edge(from, to, w);
				}
				i--;
			}
			List<Edge> edgesList = Arrays.asList(edges);
			Collections.shuffle(edgesList);
			edges = edgesList.toArray(edges);

			long startTime = System.currentTimeMillis();
			compare(f1, f2, verticesCnt, edges);
			long endTime = System.currentTimeMillis();
			System.out.println("Time: " + (endTime - startTime) + " ms");
			System.out.println();
		}
	}

	static void compare(BiFunction<Integer, Edge[], int[]> f1, BiFunction<Integer, Edge[], int[]> f2, int verticesCnt, Edge[] edges) {
		System.out.println(verticesCnt + " " + edges.length);
		for (Edge e : edges) {
			System.out.println((e.from + 1) + " " + (e.to + 1) + " " + e.weight);
		}

		if (f1 == null) {
			int[] r2 = f2.apply(verticesCnt, edges);
			for (int x : r2) {
				if ((x < 0) || (x > NAJKRACI_9_Dijkstra_Dynamic_Programming.MOD)) {
					throw new RuntimeException("Incorrect result!");
				}
			}
			System.out.println(Arrays.toString(r2));
			System.out.println();
			return;
		}

		System.out.println("F1:");
		int[] r1 = f1.apply(verticesCnt, edges);
		System.out.println("F2:");
		int[] r2 = f2.apply(verticesCnt, edges);
		System.out.println("Done");
		if (!Arrays.equals(r1, r2)) {
			System.out.println(Arrays.toString(r1));
			System.out.println(Arrays.toString(r2));
			// f2.apply(verticesCnt, edges);
			throw new RuntimeException("Results are different!");
		}
		System.out.println(Arrays.toString(r1));
		System.out.println();
	}
}
