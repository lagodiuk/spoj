package spoj.dynamic_programming.nkleaves;

import java.util.Arrays;

// http://www.spoj.com/problems/NKLEAVES/en/

public class NKLEAVES_4 {

	public static void main(String[] args) {
		NKLEAVES_Brute_Force.testFewManualCases(NKLEAVES_4::solve);
		NKLEAVES_Brute_Force.testRandomlyGeneratedCases(NKLEAVES_4::solve);
		NKLEAVES_Brute_Force.testRandomlyGeneratedCases(NKLEAVES_4::solve, 1000, false);
	}

	static int solve(int[] leaves, int heaps) {
		int[][] memoized = new int[leaves.length][heaps + 1];
		for (int[] row : memoized) {
			Arrays.fill(row, -1);
		}
		return solve(leaves, leaves.length - 1, heaps, memoized);
	}

	static final int INF = 1000000000;

	static int solve(int[] leaves, int currPos, int heaps, int[][] memoized) {
		if (currPos == -1) {
			if (heaps == 0) {
				return 0;
			} else {
				return INF;
			}
		}

		if (heaps < 0) {
			return INF;
		}

		if (memoized[currPos][heaps] != -1) {
			return memoized[currPos][heaps];
		}

		int result = INF;
		for (int i = -1; i < currPos; i++) {
			result = Math.min(result, solve(leaves, i, heaps - 1, memoized) + cost(leaves, i + 1, currPos));
		}

		memoized[currPos][heaps] = result;
		return result;
	}

	static int cost(int[] leaves, int from, int to) {
		int result = 0;
		for (int i = from; i <= to; i++) {
			result += leaves[i] * (i - from);
		}
		return result;
	}
}
