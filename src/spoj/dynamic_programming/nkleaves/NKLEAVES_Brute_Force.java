package spoj.dynamic_programming.nkleaves;

// http://www.spoj.com/problems/NKLEAVES/en/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class NKLEAVES_Brute_Force {

	public static void main(String[] args) {
		System.out.println(solve(new int[]{1, 20, 3, 40, 5}, 3));
		System.out.println(solve(new int[]{1, 20, 3, 40, 5}, 2));
		System.out.println(solve(new int[]{1, 2, 3, 4, 5}, 2));
		System.out.println(solve(new int[]{1, 1, 1, 1, 1}, 1));
		System.out.println(solve(new int[]{1, 1, 1, 1, 1}, 2));
		System.out.println(solve(new int[]{1, 1, 1, 1, 1}, 3));
		System.out.println(solve(new int[]{1, 1, 1, 1, 1}, 4));
		System.out.println(solve(new int[]{1, 1, 1, 1, 1}, 5));
	}

	static void testRandomlyGeneratedCases(BiFunction<int[], Integer, Integer> f) {
		testRandomlyGeneratedCases(f, 8, true);
	}

	static void testRandomlyGeneratedCases(BiFunction<int[], Integer, Integer> f, int maxLeavesAmount, boolean compareWithBruteForce) {
		Random rnd = new Random(1);

		for (int test = 0; test < 30; test++) {
			int[] leaves = new int[rnd.nextInt(maxLeavesAmount) + 1];
			for (int i = 0; i < leaves.length; i++) {
				leaves[i] = rnd.nextInt(1000) + 1;
			}

			for (int heaps = 1; heaps <= Math.min(leaves.length, 10); heaps++) {
				verify(leaves, heaps, f, compareWithBruteForce);
			}
		}
	}

	static void testFewManualCases(BiFunction<int[], Integer, Integer> f) {
		verify(new int[]{1, 20, 3, 40, 5}, 3, f, true);
		verify(new int[]{1, 20, 3, 40, 5}, 2, f, true);
		verify(new int[]{1, 2, 3, 4, 5}, 2, f, true);
		verify(new int[]{1, 1, 1, 1, 1}, 1, f, true);
		verify(new int[]{1, 1, 1, 1, 1}, 2, f, true);
		verify(new int[]{1, 1, 1, 1, 1}, 3, f, true);
		verify(new int[]{1, 1, 1, 1, 1}, 4, f, true);
		verify(new int[]{1, 1, 1, 1, 1}, 5, f, true);
	}

	static void verify(int[] leaves, int heaps, BiFunction<int[], Integer, Integer> f, boolean compareWithBruteForce) {
		System.out.println(Arrays.toString(leaves));
		System.out.println(heaps);

		if (compareWithBruteForce) {
			int bruteForceResult = solve(leaves, heaps);
			int testedResult = f.apply(leaves, heaps);

			System.out.println("Expected result: " + bruteForceResult);
			if (bruteForceResult != testedResult) {
				System.out.println("Actual result: " + testedResult);
				f.apply(leaves, heaps);
				throw new RuntimeException("Different results!");
			}
		} else {
			int testedResult = f.apply(leaves, heaps);
			System.out.println("Actual result: " + testedResult);
		}

		System.out.println();
	}

	static int solve(int[] leaves, int heaps) {
		return solve(leaves, 1, heaps, new ArrayList<>());
	}

	static int solve(int[] leaves, int currPos, int heaps, List<Integer> heapsPositions) {
		if ((currPos == leaves.length) && (heaps > 1)) {
			return Integer.MAX_VALUE;
		}

		if (heaps == 1) {
			int cost = 0;
			int left = 0;
			heapsPositions.add(leaves.length);
			for (int right : heapsPositions) {
				for (int j = left; j < right; j++) {
					cost += leaves[j] * (j - left);
				}
				left = right;
			}
			heapsPositions.remove(heapsPositions.size() - 1);
			return cost;
		}

		int result = Integer.MAX_VALUE;
		for (int i = currPos; i < leaves.length; i++) {
			heapsPositions.add(i);
			result = Math.min(result, solve(leaves, i + 1, heaps - 1, heapsPositions));
			heapsPositions.remove(heapsPositions.size() - 1);
		}
		return result;
	}
}
