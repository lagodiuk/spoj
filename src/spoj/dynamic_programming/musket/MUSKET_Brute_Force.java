package spoj.dynamic_programming.musket;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class MUSKET_Brute_Force {

	public static int[][] generateTestMatrix() {
		int[][] A = new int[][]{
				{1, 1, 1, 1, 1, 0, 1},
				{0, 1, 0, 1, 1, 0, 0},
				{0, 1, 1, 1, 1, 1, 1},
				{0, 0, 0, 1, 1, 0, 1},
				{0, 0, 0, 0, 1, 0, 1},
				{1, 1, 0, 1, 1, 1, 1},
				{0, 1, 0, 0, 0, 0, 1}
		};
		// Expected solution is [0, 2, 5]
		return A;
	}

	public static int[][] generateRandomMatrix(Random rnd) {
		int dim = rnd.nextInt(7) + 2;
		int[][] A = new int[dim][dim];

		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				A[i][j] = (rnd.nextDouble() > 0.5) ? 1 : 0;
				A[j][i] = 1 - A[i][j];
			}
		}

		for (int i = 0; i < dim; i++) {
			A[i][i] = 1;
		}

		return A;
	}

	public static void printMatrix(int[][] A) {
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A.length; j++) {
				System.out.print(A[i][j]);
			}
			System.out.println();
		}
	}

	public static Set<Integer> solve_brute_force(int[][] A) {
		Set<Integer> winners = new TreeSet<>();
		test_brute_force(A, new int[A.length], 0, new BitSet(A.length), winners);
		return winners;
	}

	// Complexity: O(N!)
	public static void test_brute_force(int[][] A, int[] order, int pos, BitSet used, Set<Integer> winners) {
		if (pos == A.length) {
			Set<Integer> live = new HashSet<>();
			for (int i = 0; i < A.length; i++) {
				live.add(i);
			}

			for (int x : order) {
				if (live.size() == 1) {
					break;
				}

				int from = x;
				int to = x + 1;

				while (!live.contains(from)) {
					from--;
					if (from < 0) {
						from = A.length - 1;
					}
				}

				while (!live.contains(to)) {
					to++;
					if (to > (A.length - 1)) {
						to = 0;
					}
				}

				if (A[from][to] == 1) {
					live.remove(to);
				} else {
					live.remove(from);
				}
			}

			winners.add(live.iterator().next());
			return;
		}

		for (int i = 0; i < A.length; i++) {
			if (!used.get(i)) {
				used.set(i);
				order[pos] = i;
				test_brute_force(A, order, pos + 1, used, winners);
				used.clear(i);
			}
		}
	}
}
