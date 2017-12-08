package spoj.dynamic_programming.musket;

// http://www.spoj.com/problems/MUSKET/

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MUSKET_2 {

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

		memoized = new Set[A.length + 1][A.length + 1][A.length + 1];

		Set<Integer> result = new HashSet<>();
		for (int i = 0; i <= A.length; i++) {
			result.addAll(getWinners(i, 0, A.length - 1, A));
		}

		return result;
	}

	static Set[][][] memoized;

	static Set<Integer> getWinners(int shift, int l, int r, int[][] A) {

		if (memoized[shift][l][r] != null) {
			return memoized[shift][l][r];
		}

		Set<Integer> winners = new HashSet<>();

		if (l == r) {
			winners.add((l + shift) % A.length);
			memoized[shift][l][r] = winners;
			return winners;
		}

		for (int k = l; k < r; k++) {
			Set<Integer> wl = getWinners(shift, l, k, A);
			Set<Integer> wr = getWinners(shift, k + 1, r, A);

			for (int il : wl) {
				for (int ir : wr) {
					if (A[il][ir] == 1) {
						winners.add(il);
					} else {
						winners.add(ir);
					}
				}
			}
		}

		memoized[shift][l][r] = winners;
		return winners;
	}
}
