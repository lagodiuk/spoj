package spoj.dynamic_programming.seqpar;

// http://www.spoj.com/problems/SEQPAR/

public class SEQPAR_3_Top_Down {

	public static void main(String[] args) {
		SEQPAR_BruteForce.testFewManualCases(SEQPAR_3_Top_Down::solve);
		SEQPAR_BruteForce.testRandomized(SEQPAR_3_Top_Down::solve);
	}

	private static final int INFINITY = 10000000;

	static int solve(int[] arr, int count) {
		return solve(arr, 0, count);
	}

	/**
	 * 2 variables represents state
	 */
	static int solve(int[] arr, int curr, int segments) {
		if ((segments == 0) && (curr == arr.length)) {
			return -INFINITY;
		}

		if ((segments == 0) || (curr == arr.length)) {
			return INFINITY;
		}

		int result = INFINITY;
		for (int i = curr + 1; i <= arr.length; i++) {
			int currSum = sum(arr, curr, i);
			int incl = Math.max(currSum, solve(arr, i, segments - 1));
			result = Math.min(incl, result);
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
