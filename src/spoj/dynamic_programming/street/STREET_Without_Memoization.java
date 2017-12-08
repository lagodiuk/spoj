package spoj.dynamic_programming.street;

// http://www.spoj.com/problems/STREET/

import java.util.Arrays;
import java.util.Random;

public class STREET_Without_Memoization {

	public static void main(String[] args) {
		test_original_example(null);
	}

	public static void test_randomized(SolutionSolver solver) {
		Random rnd = new Random(1);
		int maxSize = 20;

		for (int i = 0; i < 50; i++) {
			int[] maxHeight = new int[rnd.nextInt(maxSize) + 1];
			int maxWidth = rnd.nextInt(maxHeight.length) + 1;
			int availableBuildings = rnd.nextInt(maxHeight.length) + 1;

			for (int j = 0; j < maxHeight.length; j++) {
				maxHeight[j] = rnd.nextInt(100);
			}

			System.out.println(maxHeight.length + " " + availableBuildings + " " + maxWidth);
			System.out.println(Arrays.toString(maxHeight).replaceAll("[\\[\\],]", " "));

			int s1 = solve(maxHeight, maxWidth, availableBuildings);
			System.out.println(s1);

			System.out.println();

			int s2 = solver.solve(maxHeight, maxWidth, availableBuildings);
			if (s1 != s2) {
				throw new RuntimeException();
			}
		}
	}

	public static void test_original_example(SolutionSolver solver) {
		System.out.println(solve(new int[]{7, 3, 12, 11, 13, 4, 8, 6, 6, 20}, 4, 2));
		if (solver != null) {
			System.out.println(solver.solve(new int[]{7, 3, 12, 11, 13, 4, 8, 6, 6, 20}, 4, 2));
		}

		System.out.println();

		System.out.println(solve(new int[]{7, 3, 12, 11, 13, 4, 8, 6, 6, 20}, 4, 3));
		if (solver != null) {
			System.out.println(solver.solve(new int[]{7, 3, 12, 11, 13, 4, 8, 6, 6, 20}, 4, 3));
		}
	}

	static interface SolutionSolver {
		int solve(int[] maxHeight, int maxWidth, int availableBuildings);
	}

	static int solve(int[] maxHeight, int maxWidth, int availableBuildings) {
		return solve(maxHeight, maxWidth, 0, availableBuildings);
	}

	static int solve(int[] maxHeight, int maxWidth, int currStartIdx, int availableBuildings) {
		if ((currStartIdx >= maxHeight.length) || (availableBuildings == 0)) {
			return 0;
		}

		int result = 0;

		int minHeight = Integer.MAX_VALUE;
		for (int i = 0; (i < maxWidth) && ((currStartIdx + i) < maxHeight.length); i++) {

			minHeight = Math.min(minHeight, maxHeight[currStartIdx + i]);
			result = Math.max(result,
					((i + 1) * minHeight) + solve(maxHeight, maxWidth, currStartIdx + i + 1, availableBuildings - 1));

		}
		result = Math.max(result,
				solve(maxHeight, maxWidth, currStartIdx + 1, availableBuildings));

		return result;
	}

}
