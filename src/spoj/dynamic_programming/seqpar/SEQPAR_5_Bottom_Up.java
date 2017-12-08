package spoj.dynamic_programming.seqpar;

import java.util.Arrays;

// http://www.spoj.com/problems/SEQPAR/

public class SEQPAR_5_Bottom_Up {

	public static void main(String[] args) {
		SEQPAR_BruteForce.testFewManualCases(SEQPAR_5_Bottom_Up::solve);
		SEQPAR_BruteForce.testRandomized(SEQPAR_5_Bottom_Up::solve);
	}

	private static final int INFINITY = 10000000;

	/**
	 * O(N^2) memoization table
	 */
	static int solve(int[] arr, int count) {
		int[][] memoized = new int[arr.length + 1][count + 1];
		for (int[] row : memoized) {
			Arrays.fill(row, -1);
		}

		for (int curr = arr.length; curr >= 0; curr--) {
			memoized[curr][0] = INFINITY;
		}
		memoized[arr.length][0] = -INFINITY;

		for (int segments = 1; segments <= count; segments++) {
			for (int curr = arr.length; curr >= 0; curr--) {

				int result = INFINITY;
				for (int i = curr + 1; i <= arr.length; i++) {
					int currSum = sum(arr, curr, i);
					int subProblem = memoized[i][segments - 1];
					int incl = Math.max(currSum, subProblem);
					result = Math.min(incl, result);
				}

				memoized[curr][segments] = result;
			}
		}

		int result = memoized[0][count];

		for (int[] row : memoized) {
			for (int x : row) {
				if (x == -INFINITY) {
					System.out.printf("%3s", "*");
				} else if (x == INFINITY) {
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

	// TODO: use cumulative sum array
	static int sum(int[] arr, int from, int to) {
		int sum = 0;
		for (int i = from; i < to; i++) {
			sum += arr[i];
		}
		return sum;
	}
}
