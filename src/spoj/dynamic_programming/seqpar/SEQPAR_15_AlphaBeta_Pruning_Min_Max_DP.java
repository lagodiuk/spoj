package spoj.dynamic_programming.seqpar;

// http://www.spoj.com/problems/SEQPAR/

import java.util.Arrays;

public class SEQPAR_15_AlphaBeta_Pruning_Min_Max_DP {

	public static void main(String[] args) {
		SEQPAR_BruteForce.testFewManualCases(SEQPAR_15_AlphaBeta_Pruning_Min_Max_DP::solve);
		SEQPAR_BruteForce.testRandomized(SEQPAR_15_AlphaBeta_Pruning_Min_Max_DP::solve);
		SEQPAR_BruteForce.testRandomized(SEQPAR_15_AlphaBeta_Pruning_Min_Max_DP::solve, 1000, 50, false);
	}

	static int solve(int[] arr, int delims) {

		int[][] memoized = new int[arr.length][delims + 1];
		for (int[] row : memoized) {
			Arrays.fill(row, -1);
		}

		int result = min(0, delims - 1, INFINITY, -INFINITY, arr, memoized);

		for (int[] row : memoized) {
			for (int x : row) {
				if (x == INFINITY) {
					System.out.printf("%5s", "i");
				} else if (x == -1) {
					System.out.printf("%5s", ".");
				} else {
					System.out.printf("%5d", x);
				}
			}
			System.out.println();
		}

		return result;
	}

	static final int INFINITY = 100000000;

	static int min(int l, int s, int minSoFar, int maxSoFar, int[] arr, int[][] memoized) {

		if ((s == 0) && (l < arr.length)) {
			return sum(arr, l, arr.length - 1);
		}

		if (l >= arr.length) {
			return INFINITY;
		}

		if (memoized[l][s] != -1) {
			return memoized[l][s];
		}

		int result = INFINITY;
		for (int r = l; r < ((arr.length - s) + 1); r++) {
			result = Math.min(result, max(l, r, s, minSoFar, maxSoFar, arr, memoized));
			if (result <= maxSoFar) {
				return result;
			}
			minSoFar = Math.min(result, minSoFar);
		}

		memoized[l][s] = result;
		return result;
	}

	static int max(int l, int r, int s, int minSoFar, int maxSoFar, int[] arr, int[][] memoized) {
		int result = sum(arr, l, r);
		if (result >= minSoFar) {
			return result;
		}
		maxSoFar = Math.max(result, maxSoFar);
		result = Math.max(result, min(r + 1, s - 1, minSoFar, maxSoFar, arr, memoized));
		return result;
	}

	// TODO: use cumulative sum array
	static int sum(int[] arr, int from, int to) {
		int sum = 0;
		for (int i = from; i <= to; i++) {
			sum += arr[i];
		}
		return sum;
	}
}
