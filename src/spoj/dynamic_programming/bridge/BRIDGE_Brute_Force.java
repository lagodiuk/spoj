package spoj.dynamic_programming.bridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

// http://www.spoj.com/problems/BRIDGE/

public class BRIDGE_Brute_Force {

	public static void testRandomized(Function<Bridge[], Integer> f) {
		testRandomized(f, true, 20);
	}

	public static void testRandomized(Function<Bridge[], Integer> f, boolean compareWithBruteForce, int maxBridgesCount) {
		Random rnd = new Random(1);

		for (int i = 0; i < 30; i++) {
			int size = rnd.nextInt(maxBridgesCount) + 1;

			Bridge[] bridges = new Bridge[size];
			for (int j = 0; j < size; j++) {
				bridges[j] = new Bridge(rnd.nextInt(1001) - rnd.nextInt(1001), rnd.nextInt(1001) - rnd.nextInt(1001));
			}

			verifyResults(bridges, f, compareWithBruteForce);
		}
	}

	public static void testFewManualCases(Function<Bridge[], Integer> f) {
		testFewManualCases(f, true);
	}

	static void testFewManualCases(Function<Bridge[], Integer> f, boolean compareWithBruteForce) {
		verifyResults(new Bridge[]{
				new Bridge(2, 6),
				new Bridge(5, 4),
				new Bridge(8, 1),
				new Bridge(10, 2)
		}, f, compareWithBruteForce);

		verifyResults(new Bridge[]{
				new Bridge(5, 6),
				new Bridge(3, 4),
				new Bridge(10, 1)
		}, f, compareWithBruteForce);

		verifyResults(new Bridge[]{
				new Bridge(1, 3),
				new Bridge(2, 4),
				new Bridge(3, 5),
				new Bridge(4, 6),
				new Bridge(5, 1),
				new Bridge(6, 2)
		}, f, compareWithBruteForce);

		verifyResults(new Bridge[]{
				new Bridge(1, 1),
				new Bridge(2, 1),
				new Bridge(2, 1)
		}, f, compareWithBruteForce);

		verifyResults(new Bridge[]{
				new Bridge(0, 1),
				new Bridge(1, 2),
				new Bridge(1, -1)
		}, f, compareWithBruteForce);

		verifyResults(new Bridge[]{
				new Bridge(0, 1),
				new Bridge(1, 3),
				new Bridge(1, 2)
		}, f, compareWithBruteForce);
	}

	static void verifyResults(Bridge[] bridges, Function<Bridge[], Integer> f, boolean compareWithBruteForce) {
		System.out.println(bridges.length);
		for (Bridge b : bridges) {
			System.out.print(b.from + " ");
		}
		System.out.println();
		for (Bridge b : bridges) {
			System.out.print(b.to + " ");
		}
		System.out.println();

		int testedSolution = f.apply(bridges);

		if (compareWithBruteForce) {
			int brureForceSolution = solve(bridges);

			if (brureForceSolution != testedSolution) {
				System.out.println("Expected result: " + brureForceSolution);
				System.out.println("Actual result: " + testedSolution);
				f.apply(bridges);
				throw new RuntimeException("Results are different!");
			}
		}

		System.out.println("Result:\n" + testedSolution);
		System.out.println();
	}

	static int solve(Bridge[] bridges) {
		int cnt = solve(bridges, 0, new boolean[bridges.length]);
		return cnt;
	}

	static int solve(Bridge[] bridges, int idx, boolean[] isUsed) {
		if (idx == bridges.length) {

			List<Integer> used = new ArrayList<>();
			for (int i = 0; i < bridges.length; i++) {
				if (isUsed[i]) {
					used.add(i);
				}
			}

			for (int i : used) {
				for (int j : used) {
					if (i != j) {
						if ((bridges[i].from > bridges[j].from)
								&& (bridges[i].to < bridges[j].to)) {

							return 0;
						}

						if ((bridges[i].from < bridges[j].from)
								&& (bridges[i].to > bridges[j].to)) {

							return 0;
						}
					}
				}
			}

			return used.size();
		}

		isUsed[idx] = true;
		int including = solve(bridges, idx + 1, isUsed);

		isUsed[idx] = false;
		int excluding = solve(bridges, idx + 1, isUsed);

		return Math.max(including, excluding);
	}
}
