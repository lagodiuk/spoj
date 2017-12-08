package spoj.dynamic_programming.seqpar;

// http://www.spoj.com/problems/SEQPAR/

public class SEQPAR_14_AlphaBeta_Pruning_Min_Max {

	public static void main(String[] args) {
		SEQPAR_BruteForce.testFewManualCases(SEQPAR_14_AlphaBeta_Pruning_Min_Max::solve);
		SEQPAR_BruteForce.testRandomized(SEQPAR_14_AlphaBeta_Pruning_Min_Max::solve);
	}

	static int solve(int[] arr, int delims) {
		int result = min(0, delims - 1, INFINITY, -INFINITY, arr);
		return result;
	}

	static final int INFINITY = 100000000;

	static int min(int l, int s, int minSoFar, int maxSoFar, int[] arr) {

		if ((s == 0) && (l < arr.length)) {
			return sum(arr, l, arr.length - 1);
		}

		if (l >= arr.length) {
			return INFINITY;
		}

		int result = INFINITY;
		for (int r = l; r < arr.length; r++) {
			result = Math.min(result, max(l, r, s, minSoFar, maxSoFar, arr));
			if (result <= maxSoFar) {
				return result;
			}
			minSoFar = Math.min(result, minSoFar);
		}
		return result;
	}

	static int max(int l, int r, int s, int minSoFar, int maxSoFar, int[] arr) {
		int result = sum(arr, l, r);
		if (result >= minSoFar) {
			return result;
		}
		maxSoFar = Math.max(result, maxSoFar);
		result = Math.max(result, min(r + 1, s - 1, minSoFar, maxSoFar, arr));
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
