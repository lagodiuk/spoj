package spoj.dynamic_programming.seqpar;

// http://www.spoj.com/problems/SEQPAR/

public class SEQPAR_6_Bottom_Up {

	public static void main(String[] args) {
		SEQPAR_BruteForce.testFewManualCases(SEQPAR_6_Bottom_Up::solve);
		SEQPAR_BruteForce.testRandomized(SEQPAR_6_Bottom_Up::solve);
	}

	private static final int INFINITY = 10000000;

	/**
	 * O(N) memoization table
	 */
	static int solve(int[] arr, int count) {
		int[][] memoized = new int[arr.length + 1][2];

		for (int curr = arr.length; curr >= 0; curr--) {
			memoized[curr][0] = INFINITY;
		}
		memoized[arr.length][0] = -INFINITY;

		int pr = 0;
		int cr = 1;

		for (int segments = 1; segments <= count; segments++) {
			for (int curr = arr.length; curr >= 0; curr--) {

				int result = INFINITY;
				for (int i = curr + 1; i <= arr.length; i++) {
					int currSum = sum(arr, curr, i);
					int subProblem = memoized[i][pr];
					int incl = Math.max(currSum, subProblem);
					result = Math.min(incl, result);
				}

				memoized[curr][cr] = result;
			}
			pr ^= 1;
			cr ^= 1;
		}

		int result = memoized[0][pr];
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
