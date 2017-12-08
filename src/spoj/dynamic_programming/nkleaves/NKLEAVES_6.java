package spoj.dynamic_programming.nkleaves;

import java.util.Arrays;

// http://www.spoj.com/problems/NKLEAVES/en/

public class NKLEAVES_6 {

	public static void main(String[] args) {
		NKLEAVES_Brute_Force.testFewManualCases(NKLEAVES_6::solve);
		NKLEAVES_Brute_Force.testRandomlyGeneratedCases(NKLEAVES_6::solve);
		NKLEAVES_Brute_Force.testRandomlyGeneratedCases(NKLEAVES_6::solve, 100, false);
	}

	static int solve(int[] leaves, int heaps) {
		int[] leftToRightAcc = new int[leaves.length];
		leftToRightAcc[leaves.length - 1] = leaves[leaves.length - 1];
		for (int i = leaves.length - 2; i >= 0; i--) {
			leftToRightAcc[i] = leaves[i] + leftToRightAcc[i + 1];
		}

		int cov = getBiggestCoveredArea(heaps, leftToRightAcc);
		// getBiggestCoveredArea2(heaps, leftToRightAcc);

		int result = -cov;
		for (int i = 0; i < leaves.length; i++) {
			result += i * leaves[i];
		}
		return result;
	}

	private static int getBiggestCoveredArea(int rectangles, int[] leftToRightAcc) {
		int[][] memoized = new int[rectangles][leftToRightAcc.length + 1];
		for (int[] row : memoized) {
			Arrays.fill(row, -1);
		}
		int cov = selectBiggestCoverage(leftToRightAcc, 0, rectangles - 1, memoized);

		System.out.println();
		System.out.println("Array size: " + leftToRightAcc.length);
		System.out.println(Arrays.toString(leftToRightAcc));
		System.out.println("Coverage: " + cov);
		for (int[] row : memoized) {
			for (int x : row) {
				if (x < 0) {
					System.out.printf("%9s", ".");
				} else {
					System.out.printf("%9d", x);
				}
			}
			System.out.println();
		}
		System.out.println();
		return cov;
	}

	static int selectBiggestCoverage(int[] leftToRightAcc, int currPos, int cnt, int[][] memoized) {

		if (memoized[cnt][currPos] != -1) {
			return memoized[cnt][currPos];
		}

		if (cnt == 0) {
			memoized[cnt][currPos] = 0;
			return memoized[cnt][currPos];
		}

		if (currPos == leftToRightAcc.length) {
			memoized[cnt][currPos] = cnt == 0 ? 0 : -10000000;
			return memoized[cnt][currPos];
		}

		int result = selectBiggestCoverage(leftToRightAcc, currPos + 1, cnt, memoized);
		for (int i = currPos + 1; i <= leftToRightAcc.length; i++) {
			int deltaCnt = leftToRightAcc[currPos] - (i == leftToRightAcc.length ? 0 : leftToRightAcc[i]);
			int area = currPos * deltaCnt;
			result = Math.max(result, selectBiggestCoverage(leftToRightAcc, i, cnt - 1, memoized) + area);
		}

		memoized[cnt][currPos] = result;
		return result;
	}

	// //////////////////////////

	static int selectBiggestCoverage2(int[] leftToRightAcc, int currPos, int cnt, int[][] memoized) {

		if (memoized[cnt][currPos] != -1) {
			return memoized[cnt][currPos];
		}

		if (cnt == 0) {
			memoized[cnt][currPos] = 0;
			return memoized[cnt][currPos];
		}

		if (currPos == leftToRightAcc.length) {
			memoized[cnt][currPos] = cnt == 0 ? 0 : -10000000;
			return memoized[cnt][currPos];
		}

		int result = selectBiggestCoverage2(leftToRightAcc, currPos + 1, cnt, memoized);
		for (int i = currPos + 1; i <= ((leftToRightAcc.length - cnt) + 1); i++) {
			int deltaCnt = leftToRightAcc[currPos] - (i == leftToRightAcc.length ? 0 : leftToRightAcc[i]);
			int area = currPos * deltaCnt;
			int tmp = selectBiggestCoverage2(leftToRightAcc, i, cnt - 1, memoized) + area;
			result = Math.max(result, tmp);
		}

		memoized[cnt][currPos] = result;
		return result;
	}

	private static int getBiggestCoveredArea2(int rectangles, int[] leftToRightAcc) {
		int[][] memoized = new int[rectangles][leftToRightAcc.length + 1];
		for (int[] row : memoized) {
			Arrays.fill(row, -1);
		}
		int cov = selectBiggestCoverage2(leftToRightAcc, 0, rectangles - 1, memoized);

		System.out.println();
		System.out.println(Arrays.toString(leftToRightAcc));
		System.out.println("Coverage: " + cov);
		for (int[] row : memoized) {
			for (int x : row) {
				if (x < 0) {
					System.out.printf("%9s", ".");
				} else {
					System.out.printf("%9d", x);
				}
			}
			System.out.println();
		}
		System.out.println();
		return cov;
	}
}
