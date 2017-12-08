package spoj.dynamic_programming.seqpar;

// http://www.spoj.com/problems/SEQPAR/

public class SEQPAR_2_Top_Down {

	public static void main(String[] args) {
		SEQPAR_BruteForce.testFewManualCases(SEQPAR_2_Top_Down::solve);
		SEQPAR_BruteForce.testRandomized(SEQPAR_2_Top_Down::solve);
	}

	private static final int INFINITY = 10000000;

	static int solve(int[] arr, int count) {
		return solve(arr, 0, 1, count);
	}

	/**
	 * 3 variables represents state
	 */
	static int solve(int[] arr, int prev, int curr, int delims) {
		if ((delims == 0) && (curr == (arr.length + 1))) {
			return -INFINITY;
		}

		if ((delims == 0) || (curr == (arr.length + 1))) {
			return INFINITY;
		}

		int excl = solve(arr, prev, curr + 1, delims);

		int currSum = sum(arr, prev, curr);
		int incl = Math.max(currSum, solve(arr, curr, curr + 1, delims - 1));

		return Math.min(excl, incl);
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
