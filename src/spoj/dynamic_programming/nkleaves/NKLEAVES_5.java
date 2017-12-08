package spoj.dynamic_programming.nkleaves;

// http://www.spoj.com/problems/NKLEAVES/en/

public class NKLEAVES_5 {

	public static void main(String[] args) {
		NKLEAVES_Brute_Force.testFewManualCases(NKLEAVES_5::solve);
		NKLEAVES_Brute_Force.testRandomlyGeneratedCases(NKLEAVES_5::solve);
	}

	static int solve(int[] leaves, int heaps) {
		int[] leftToRightAcc = new int[leaves.length];
		leftToRightAcc[leaves.length - 1] = leaves[leaves.length - 1];
		for (int i = leaves.length - 2; i >= 0; i--) {
			leftToRightAcc[i] = leaves[i] + leftToRightAcc[i + 1];
		}

		int cov = selectBiggestCoverage(leftToRightAcc, 0, heaps - 1);

		int result = -cov;
		for (int i = 0; i < leaves.length; i++) {
			result += i * leaves[i];
		}
		return result;
	}

	static int selectBiggestCoverage(int[] leftToRightAcc, int currPos, int cnt) {

		if (cnt == 0) {
			return 0;
		}

		if (currPos == leftToRightAcc.length) {
			return cnt == 0 ? 0 : -10000000;
		}

		int result = selectBiggestCoverage(leftToRightAcc, currPos + 1, cnt);
		for (int i = currPos + 1; i <= leftToRightAcc.length; i++) {
			result = Math.max(result, selectBiggestCoverage(leftToRightAcc, i, cnt - 1)
					+ (currPos * (leftToRightAcc[currPos] - (i == leftToRightAcc.length ? 0 : leftToRightAcc[i]))));
		}
		return result;
	}
}
