package spoj.dynamic_programming.musket;

// http://www.spoj.com/problems/MUSKET/

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MUSKET_1 {

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
		int[] idx = new int[A.length];
		for (int i = 0; i < A.length; i++) {
			idx[i] = i;
		}

		Set<Integer> result = new HashSet<>();
		for (int i = 0; i <= A.length; i++) {
			result.addAll(getWinners(idx, 0, idx.length - 1, A));
			shiftRight(idx);
		}

		return result;
	}

	static void shiftRight(int[] arr) {
		int last = arr[arr.length - 1];
		int i = arr.length - 1;
		while (i > 0) {
			arr[i] = arr[i - 1];
			i--;
		}
		arr[0] = last;
	}

	static Set<Integer> getWinners(int[] idx, int l, int r, int[][] A) {
		Set<Integer> winners = new HashSet<>();

		if (l == r) {
			winners.add(idx[l]);
			return winners;
		}

		for (int k = l; k < r; k++) {
			Set<Integer> wl = getWinners(idx, l, k, A);
			Set<Integer> wr = getWinners(idx, k + 1, r, A);

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

		return winners;
	}
}
