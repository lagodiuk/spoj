package spoj.dynamic_programming.seqpar;

// http://www.spoj.com/problems/SEQPAR/

public class SEQPAR_7_Bottom_Up {

	public static void main(String[] args) {
		SEQPAR_BruteForce.testFewManualCases(SEQPAR_7_Bottom_Up::solve);
		SEQPAR_BruteForce.testRandomized(SEQPAR_7_Bottom_Up::solve);
	}

	private static final int INFINITY = 10000000;

	/**
	 * O(N) memoization table
	 * Without function for calculation of sum on the interval of array
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

				if (curr < arr.length) {

					int currSum = arr[curr];
					for (int i = curr + 1; i <= arr.length; i++) {

						int subProblem = memoized[i][pr];
						int incl = Math.max(currSum, subProblem);

						result = Math.min(incl, result);

						if (i < arr.length) {
							currSum += arr[i];
						}
					}
				}

				memoized[curr][cr] = result;
			}
			pr ^= 1;
			cr ^= 1;
		}

		int result = memoized[0][pr];
		return result;
	}
}
