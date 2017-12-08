package spoj.dynamic_programming.nkleaves;

public class NKLEAVES_2 {

	public static void main(String[] args) {
		NKLEAVES_Brute_Force.testFewManualCases(NKLEAVES_2::solve);
		NKLEAVES_Brute_Force.testRandomlyGeneratedCases(NKLEAVES_2::solve);
	}

	static int solve(int[] leaves, int heaps) {
		int acc = calcAcc(leaves);

		int[] leftToRightAcc = new int[leaves.length];
		leftToRightAcc[leaves.length - 1] = leaves[leaves.length - 1];
		for (int i = leaves.length - 2; i >= 0; i--) {
			leftToRightAcc[i] = leaves[i] + leftToRightAcc[i + 1];
		}
		int r = solve(leftToRightAcc, heaps - 1, leaves.length - 1, -1);

		return acc - r;
	}

	static int solve(int[] leftToRightAcc, int heaps, int pos, int p2) {

		if (heaps < 0) {
			return -10000000;
		}

		if (pos < 1) {
			return heaps == 0 ? 0 : -10000000;
		}

		int endingHere = solve(leftToRightAcc, heaps - 1, pos - 1, pos) + leftToRightAcc[pos];
		int endingBeforeHere = solve(leftToRightAcc, heaps, pos - 1, p2) + ((p2 > -1) ? leftToRightAcc[p2] : 0);

		return Math.max(endingBeforeHere, endingHere);
	}

	private static int calcAcc(int[] leaves) {
		int acc = 0;
		for (int i = 0; i < leaves.length; i++) {
			acc += leaves[i] * i;
		}
		return acc;
	}
}
