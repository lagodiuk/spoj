package spoj.dynamic_programming.seqpar;

import java.util.Arrays;
import java.util.Random;
import java.util.function.BiFunction;

// http://www.spoj.com/problems/SEQPAR/

public class SEQPAR_BruteForce {

	public static void main(String[] args) {
		testFewManualCases(null);
		testRandomized(null);
	}

	static void testRandomized(BiFunction<int[], Integer, Integer> tested) {
		testRandomized(tested, 30, 15, true);
	}

	static void testRandomized(BiFunction<int[], Integer, Integer> tested, int maxValue, int maxLength, boolean compareWithBruteForce) {
		Random rnd = new Random(1);

		for (int i = 0; i < 50; i++) {
			int[] arr = new int[rnd.nextInt(maxLength) + 1];
			for (int j = 0; j < arr.length; j++) {
				arr[j] = rnd.nextInt(maxValue) * (rnd.nextInt(2) == 1 ? 1 : -1);
			}

			int delims = rnd.nextInt(arr.length) + 1;
			testSolution(arr, delims, tested, compareWithBruteForce);
		}
	}

	static void testFewManualCases(BiFunction<int[], Integer, Integer> tested) {
		testSolution(new int[]{1, 1, 1, 3, 2, 2, 1, 3, 1}, 1, tested, true);
		testSolution(new int[]{1, 1, 1, 3, 2, 2, 1, 3, 1}, 2, tested, true);
		testSolution(new int[]{1, 1, 1, 3, 2, 2, 1, 3, 1}, 3, tested, true);
		testSolution(new int[]{1, 1, 1, 3, 2, 2, 1, 3, 1}, 4, tested, true);
	}

	static void testSolution(int[] arr, int delims, BiFunction<int[], Integer, Integer> tested, boolean compareWithBruteForce) {
		System.out.println(arr.length + " " + delims);
		System.out.println(Arrays.toString(arr).replaceAll("[,\\[\\]]", " ").trim());

		if (tested == null) {
			int result = solve(arr, delims);
			System.out.println("Result: " + result);
			System.out.println();

		} else if (!compareWithBruteForce) {
			int result = tested.apply(arr, delims);
			System.out.println("Result: " + result);
			System.out.println();

		} else {
			int expectedResult = solve(arr, delims);
			int actualResult = tested.apply(arr, delims);
			if (expectedResult != actualResult) {
				System.out.println("Expected result: " + expectedResult);
				System.out.println("Actual result: " + actualResult);
				throw new RuntimeException("Results are different!");
			}
			System.out.println("Result: " + expectedResult);
			System.out.println();
		}
	}

	static int solve(int[] arr, int delims) {
		return solve(arr, new int[delims - 1], 1, 0);
	}

	private static final int INFINITY = 10000000;

	static int solve(int[] arr, int[] delims, int currArrIdx, int currDelimIdx) {
		if (currDelimIdx == delims.length) {
			int result = -INFINITY;
			int prev = 0;
			for (int curr : delims) {
				result = Math.max(result, sum(arr, prev, curr));
				prev = curr;
			}
			result = Math.max(result, sum(arr, prev, arr.length));
			return result;
		}

		if (currArrIdx == arr.length) {
			return INFINITY;
		}

		int s1 = solve(arr, delims, currArrIdx + 1, currDelimIdx);

		delims[currDelimIdx] = currArrIdx;
		int s2 = solve(arr, delims, currArrIdx + 1, currDelimIdx + 1);

		return Math.min(s1, s2);
	}

	// TODO: use cumulative sum array
	static int sum(int[] arr, int from, int to) {
		int sum = 0;
		for (int i = from; i < to; i++) {
			sum += arr[i];
		}
		return sum;
	}
}
