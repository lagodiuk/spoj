package spoj.dynamic_programming.musket;

// http://www.spoj.com/problems/MUSKET/

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MUSKET_5 {

	public static void main(String[] args) {
		int[][] A = MUSKET_Brute_Force.generateTestMatrix();
		MUSKET_Brute_Force.printMatrix(A);
		System.out.println(MUSKET_Brute_Force.solve_brute_force(A));
		System.out.println(getWinners(A));
		System.out.println();

		Random rnd = new Random(1);
		for (int i = 0; i < 100; i++) {
			A = MUSKET_Brute_Force.generateRandomMatrix(rnd);
			Set<Integer> w1 = MUSKET_Brute_Force.solve_brute_force(A);
			Set<Integer> w2 = getWinners(A);
			// if (!w1.equals(w2)) {
			System.out.println(A.length);
			MUSKET_Brute_Force.printMatrix(A);
			System.out.println(w1);
			System.out.println(w2);
			System.out.println();
			// }
		}
	}

	static Set<Integer> getWinners(int[][] A) {

		boolean[] canWin = new boolean[A.length];

		for (int shift = 0; shift < A.length; shift++) {

			cleanMemoized();

			int shiftedLeftIdx = (0 + shift) % A.length;
			int shiftedRightIndex = ((A.length - 1) + shift) % A.length;

			if ((!canWin[shiftedLeftIdx])
					&& parentCanWin(shift, true, 1, A.length - 1, A)) {

				canWin[shiftedLeftIdx] = true;
			}

			if ((!canWin[shiftedRightIndex])
					&& parentCanWin(shift, false, 0, (A.length - 1) - 1, A)) {

				canWin[shiftedRightIndex] = true;
			}

			for (int j = 1; j <= (A.length - 2); j++) {

				int shiftedCurrIndex = (j + shift) % A.length;

				if (canWin[shiftedCurrIndex]) {
					continue;
				}

				if (parentCanWin(shift, false, 0, j - 1, A)
						&& parentCanWin(shift, true, j + 1, A.length - 1, A)) {

					canWin[shiftedCurrIndex] = true;
				}
			}
		}

		Set<Integer> result = new HashSet<>();
		for (int i = 0; i < canWin.length; i++) {
			if (canWin[i]) {
				result.add(i);
			}
		}
		return result;
	}

	static Boolean[][][] memoized = new Boolean[2][102][102];
	static void cleanMemoized() {
		for (int i = 0; i < memoized.length; i++) {
			for (int j = 0; j < memoized[i].length; j++) {
				Arrays.fill(memoized[i][j], null);
			}
		}
	}

	static boolean parentCanWin(int shift, boolean parentLeft, int l, int r, int[][] A) {

		if (memoized[parentLeft ? 1 : 0][l][r] != null) {
			return memoized[parentLeft ? 1 : 0][l][r];
		}

		int parent;
		if (parentLeft) {
			parent = l - 1;
		} else {
			parent = r + 1;
		}

		int realParentIdx = (parent + shift) % A.length;
		int realLeftIdx = (l + shift) % A.length;
		int realRightIdx = (r + shift) % A.length;

		if (r == l) {
			memoized[parentLeft ? 1 : 0][l][r] = A[realParentIdx][realLeftIdx] == 1;
			return memoized[parentLeft ? 1 : 0][l][r];
		}

		if ((A[realParentIdx][realLeftIdx] == 1)
				&& parentCanWin(shift, true, l + 1, r, A)) {

			memoized[parentLeft ? 1 : 0][l][r] = true;
			return memoized[parentLeft ? 1 : 0][l][r];
		}

		if ((A[realParentIdx][realRightIdx] == 1)
				&& parentCanWin(shift, false, l, r - 1, A)) {

			memoized[parentLeft ? 1 : 0][l][r] = true;
			return memoized[parentLeft ? 1 : 0][l][r];
		}

		for (int k = l + 1; k <= (r - 1); k++) {
			int realKIdx = (k + shift) % A.length;

			if ((A[realParentIdx][realKIdx] == 1)
					&& parentCanWin(shift, false, l, k - 1, A)
					&& parentCanWin(shift, true, k + 1, r, A)) {

				memoized[parentLeft ? 1 : 0][l][r] = true;
				return memoized[parentLeft ? 1 : 0][l][r];
			}
		}

		memoized[parentLeft ? 1 : 0][l][r] = false;
		return memoized[parentLeft ? 1 : 0][l][r];
	}
}
