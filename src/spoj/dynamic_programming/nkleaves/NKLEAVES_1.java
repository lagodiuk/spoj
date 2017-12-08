package spoj.dynamic_programming.nkleaves;

// http://www.spoj.com/problems/NKLEAVES/en/

public class NKLEAVES_1 {

	public static void main(String[] args) {
		NKLEAVES_Brute_Force.testFewManualCases(NKLEAVES_1::solve);
		NKLEAVES_Brute_Force.testRandomlyGeneratedCases(NKLEAVES_1::solve);
	}

	static int solve(int[] leaves, int heaps) {
		return solve(leaves, leaves.length - 1, heaps);
	}

	static final int INF = 1000000;

	static int solve(int[] leaves, int currPos, int heaps) {
		if (currPos == -1) {
			if (heaps == 0) {
				return 0;
			} else {
				return INF;
			}
		}

		int result = INF;
		for (int i = -1; i < currPos; i++) {
			result = Math.min(result, solve(leaves, i, heaps - 1) + cost(leaves, i + 1, currPos));
		}
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
