package spoj.dynamic_programming.musket;

// http://www.spoj.com/problems/MUSKET/

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MUSKET_4 {

	public static void main(String[] args) {
		int[][] A = MUSKET_Brute_Force.generateTestMatrix();
		MUSKET_Brute_Force.printMatrix(A);
		System.out.println(MUSKET_Brute_Force.solve_brute_force(A));
		System.out.println(getWinners(A));
		System.out.println();

		Random rnd = new Random(1);
		for (int i = 0; i < 50; i++) {
			A = MUSKET_Brute_Force.generateRandomMatrix(rnd);
			Set<Integer> w1 = MUSKET_Brute_Force.solve_brute_force(A);
			Set<Integer> w2 = getWinners(A);
			if (!w1.equals(w2)) {
				MUSKET_Brute_Force.printMatrix(A);
				System.out.println(w1);
				System.out.println(w2);
				System.out.println();
			}
		}
	}

	static Set<Integer> getWinners(int[][] A) {
		Set<Integer> result = new HashSet<>();
		for (int shift = 0; shift < A.length; shift++) {

			if ((!result.contains((0 + shift) % A.length))
					&& parentCanWin(shift, true, 1, A.length - 1, A)) {
				result.add((0 + shift) % A.length);
			}

			if ((!result.contains(((A.length - 1) + shift) % A.length))
					&& parentCanWin(shift, false, 0, A.length - 1 - 1, A)) {
				result.add(((A.length - 1) + shift) % A.length);
			}

			for (int j = 1; j <= (A.length - 2); j++) {

				if (result.contains((j + shift) % A.length)) {
					continue;
				}

				if (parentCanWin(shift, false, 0, j - 1, A)
						&& parentCanWin(shift, true, j + 1, A.length - 1, A)) {
					result.add((j + shift) % A.length);
				}
			}
		}

		return result;
	}

	static boolean parentCanWin(int shift, boolean parentLeft, int l, int r, int[][] A) {
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
			return A[realParentIdx][realLeftIdx] == 1;
		}

		if ((A[realParentIdx][realLeftIdx] == 1)
				&& parentCanWin(shift, true, l + 1, r, A)) {

			return true;
		}

		if ((A[realParentIdx][realRightIdx] == 1)
				&& parentCanWin(shift, false, l, r - 1, A)) {

			return true;
		}

		for (int k = l + 1; k <= (r - 1); k++) {
			int realKIdx = (k + shift) % A.length;

			if ((A[realParentIdx][realKIdx] == 1)
					&& parentCanWin(shift, false, l, k - 1, A)
					&& parentCanWin(shift, true, k + 1, r, A)) {

				return true;
			}
		}

		return false;
	}
}
