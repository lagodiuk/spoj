package spoj.dynamic_programming.minus;

// http://www.spoj.com/problems/MINUS/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MINUS_5 {

	public static void main(String[] args) {
		solve(new int[]{12, 10, 4, 3, 5}, 4);

		solve(new int[]{1, 1, 1, 1, 1, 1, 1}, 3);
	}

	static void solve(int[] arr, int target) {
		/**
		 * mem[c][r] is defined for every r >= c
		 * mem[c][r] represents ((...(((arr[c] - arr[c+1]) - arr[c+2]) - arr[c+3]) ... ) - arr[r])
		 */
		int[][] mem = new int[arr.length][arr.length];
		for (int c = 0; c < arr.length; c++) {
			mem[c][c] = arr[c];
			for (int r = c + 1; r < arr.length; r++) {
				mem[c][r] = mem[c][r - 1] - arr[r];
			}
		}

		for (int[] row : mem) {
			System.out.println(Arrays.toString(row));
		}

		System.out.println(solve(0, target, mem));

		Map<Key, Boolean> mem2 = new HashMap<>();
		System.out.println(solveDebug(0, target, mem, mem2));

		result = "";
		solveDebugBacktrack(0, target, mem, arr, mem2);
		System.out.println(result);
	}

	static boolean solve(int col, int target, int[][] mem) {
		if (col == mem.length) {
			return target == 0;
		}
		for (int r = col; r < mem.length; r++) {
			if (solve(r + 1, mem[col][r] - target, mem)) {
				return true;
			}
		}
		return false;
	}

	static boolean solveDebug(int col, int target, int[][] mem, Map<Key, Boolean> mem2) {
		Key k = new Key(col, target);
		if (col == mem.length) {
			mem2.put(k, target == 0);
			return target == 0;
		}
		if (mem2.containsKey(k)) {
			return mem2.get(k);
		}
		for (int r = col; r < mem.length; r++) {
			if (solveDebug(r + 1, mem[col][r] - target, mem, mem2)) {
				mem2.put(k, true);
				return true;
			}
		}
		mem2.put(k, false);
		return false;
	}

	static String result;
	static void solveDebugBacktrack(int col, int target, int[][] mem, int[] arr, Map<Key, Boolean> mem2) {
		if (col == mem.length) {
			return;
		}
		for (int r = col; r < mem.length; r++) {
			Key k = new Key(r + 1, mem[col][r] - target);
			if (mem2.containsKey(k) && mem2.get(k)) {
				solveDebugBacktrack(r + 1, mem[col][r] - target, mem, arr, mem2);

				String left = arr[col] + "";;
				for (int i = col + 1; i <= r; i++) {
					left = "(" + left + " - " + arr[i] + ")";
				}

				if ((r + 1) < mem.length) {
					result = "(" + left + " - " + result + ")";
				} else {
					result = left;
				}
				return;
			}
		}
		throw new RuntimeException();
	}
	static class Key {
		final int col;
		final int target;
		public Key(int col, int target) {
			super();
			this.col = col;
			this.target = target;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + this.col;
			result = (prime * result) + this.target;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (this.getClass() != obj.getClass()) {
				return false;
			}
			Key other = (Key) obj;
			if (this.col != other.col) {
				return false;
			}
			if (this.target != other.target) {
				return false;
			}
			return true;
		}
	}
}
