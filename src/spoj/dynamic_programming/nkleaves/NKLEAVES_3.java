package spoj.dynamic_programming.nkleaves;

import java.util.LinkedList;
import java.util.List;

public class NKLEAVES_3 {

	public static void main(String[] args) {
		NKLEAVES_Brute_Force.testFewManualCases(NKLEAVES_3::solve);
		NKLEAVES_Brute_Force.testRandomlyGeneratedCases(NKLEAVES_3::solve);
	}

	static int solve(int[] leaves, int heaps) {
		int acc = calcAcc(leaves);
		int[] leftToRightAcc = calculateLeftToRightAcc(leaves);

		List<Integer> idxs = new LinkedList<>();
		for (int i = 1; i < heaps; i++) {
			idxs.add(i);
		}

		int r = solve(leftToRightAcc, heaps, idxs);

		return acc - r;
	}

	static int solve(int[] leftToRightAcc, int pos, List<Integer> idxs) {

		int sm = 0;
		int prev = 0;
		for (int curr : idxs) {
			sm += curr * leftToRightAcc[curr];
			sm -= prev * leftToRightAcc[curr];
			prev = curr;
		}

		if (pos == leftToRightAcc.length) {
			return sm;
		}

		idxs.add(pos);
		for (int i = 0; i < (idxs.size() - 1); i++) {
			int removed = idxs.remove(i);
			sm = Math.max(sm, solve(leftToRightAcc, pos + 1, idxs));
			idxs.add(i, removed);
		}
		idxs.remove(idxs.size() - 1);

		return sm;
	}

	private static int[] calculateLeftToRightAcc(int[] leaves) {
		int[] leftToRightAcc = new int[leaves.length];
		leftToRightAcc[leaves.length - 1] = leaves[leaves.length - 1];
		for (int i = leaves.length - 2; i >= 0; i--) {
			leftToRightAcc[i] = leaves[i] + leftToRightAcc[i + 1];
		}
		return leftToRightAcc;
	}

	private static int calcAcc(int[] leaves) {
		int acc = 0;
		for (int i = 0; i < leaves.length; i++) {
			acc += leaves[i] * i;
		}
		return acc;
	}
}
