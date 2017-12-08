package spoj.dynamic_programming.seqpar;

import java.util.Arrays;

// http://www.spoj.com/problems/SEQPAR/

public class SEQPAR_10_Bottom_Up {

	public static void main(String[] args) {
		SEQPAR_BruteForce.testFewManualCases(SEQPAR_10_Bottom_Up::solve);
		SEQPAR_BruteForce.testRandomized(SEQPAR_10_Bottom_Up::solve);
	}

	private static final int INFINITY = 10000000;

	/**
	 * O(N^2) memoization table
	 * But, with very accurate boundaries -
	 * for purpose of avoiding of branches without solution
	 *
	 * TODO: use O(N) memory
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

			// Very accurate boundaries - for purpose of avoiding of branches without solution
			memoized[(arr.length - segments) + 1][segments] = INFINITY;
			if ((count - segments - 1) >= 0) {
				memoized[count - segments - 1][segments] = INFINITY;
			}

			// Very accurate boundaries - for purpose of avoiding of branches without solution
			for (int curr = arr.length - segments; curr >= (count - segments); curr--) {

				int result = INFINITY;

				// Pruning of branches without solution:
				// we need to iterate only while (i <= (arr.length - segments) + 1)
				for (int i = curr + 1; i <= ((arr.length - segments) + 1); i++) {
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
