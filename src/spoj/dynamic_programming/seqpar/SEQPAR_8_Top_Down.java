package spoj.dynamic_programming.seqpar;

import java.util.Arrays;

// http://www.spoj.com/problems/SEQPAR/

public class SEQPAR_8_Top_Down {

	public static void main(String[] args) {
		SEQPAR_BruteForce.testFewManualCases(SEQPAR_8_Top_Down::solve);
		SEQPAR_BruteForce.testRandomized(SEQPAR_8_Top_Down::solve);
	}

	private static final int INFINITY = 10000000;

	static int solve(int[] arr, int count) {
		int[][] memoized = new int[arr.length][count + 1];
		for (int[] row : memoized) {
			Arrays.fill(row, -1);
		}
		int result = solve(arr, 0, count, memoized);
		for (int[] row : memoized) {
			for (int x : row) {
				if (x == INFINITY) {
					System.out.printf("%3s", "i");
				} else if (x == -1) {
					System.out.printf("%3s", ".");
				} else {
					System.out.printf("%3d", x);
				}
			}
			System.out.println();
		}
		return result;
	}

	/**
	 * 2 variables represents state
	 * With memoization O(N^2)
	 * Pruning branches without solutions
	 */
	static int solve(int[] arr, int curr, int segments, int[][] memoized) {
		if ((segments == 0) && (curr == arr.length)) {
			return -INFINITY;
		}

		if ((segments <= 0) || (curr >= arr.length)) {
			return INFINITY;
		}

		if (memoized[curr][segments] != -1) {
			return memoized[curr][segments];
		}

		int result = INFINITY;
		// Pruning of branches without solution:
		// we need to iterate only while (i <= (arr.length - segments) + 1)
		for (int i = curr + 1; i <= ((arr.length - segments) + 1); i++) {
			int currSum = sum(arr, curr, i);
			int subProblem = solve(arr, i, segments - 1, memoized);
			int incl = Math.max(currSum, subProblem);
			result = Math.min(incl, result);
		}

		memoized[curr][segments] = result;
		return result;
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
