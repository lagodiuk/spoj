package spoj.dynamic_programming.seqpar;

// http://www.spoj.com/problems/SEQPAR/

public class SEQPAR_12_Bottom_Up {

	public static void main(String[] args) {
		SEQPAR_BruteForce.testFewManualCases(SEQPAR_12_Bottom_Up::solve);
		SEQPAR_BruteForce.testRandomized(SEQPAR_12_Bottom_Up::solve);
		SEQPAR_BruteForce.testRandomized(SEQPAR_12_Bottom_Up::solve, 1000, 1000, false);
	}

	private static final int INFINITY = 10000000;

	/**
	 * O(N) memoization table
	 * But, with very accurate boundaries -
	 * for purpose of avoiding of branches without solution
	 *
	 * And calculation of sum on the interval with O(1) complexity
	 */
	static int solve(int[] arr, int count) {
		int[][] memoized = new int[arr.length + 1][2];
		int prevSeg = 0;
		int currSeg = 1;

		for (int curr = arr.length; curr >= 0; curr--) {
			memoized[curr][prevSeg] = INFINITY;
		}
		memoized[arr.length][prevSeg] = -INFINITY;

		for (int segments = 1; segments <= count; segments++) {

			// Very accurate boundaries - for purpose of avoiding of branches without solution
			memoized[(arr.length - segments) + 1][currSeg] = INFINITY;
			if ((count - segments - 1) >= 0) {
				memoized[count - segments - 1][currSeg] = INFINITY;
			}

			// Very accurate boundaries - for purpose of avoiding of branches without solution
			for (int curr = arr.length - segments; curr >= (count - segments); curr--) {

				int result = INFINITY;

				// Pruning of branches without solution:
				// we need to iterate only while (i <= (arr.length - segments) + 1)
				int currSum = 0;
				for (int i = curr + 1; i <= ((arr.length - segments) + 1); i++) {
					// Calculation of sum on the interval with O(1) complexity
					currSum += arr[i - 1];
					int subProblem = memoized[i][prevSeg];
					int incl = Math.max(currSum, subProblem);
					result = Math.min(incl, result);
				}

				memoized[curr][currSeg] = result;
			}

			currSeg ^= 1;
			prevSeg ^= 1;
		}

		int result = memoized[0][prevSeg];
		return result;
	}
}
