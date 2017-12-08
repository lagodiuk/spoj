package spoj.dynamic_programming.minus;

// http://www.spoj.com/problems/MINUS/

import java.util.Arrays;

public class MINUS_7 {

	public static void main(String[] args) {
		solve(new int[]{12, 10, 4, 3, 5}, 4);

		solve(new int[]{1, 1, 1, 1, 1, 1, 1}, 3);

		solve(new int[]{1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0}, -1);
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

		int[][] mem2 = new int[MAX_ITEMS][(OFFSET * 2) + 2];
		System.out.println(solveDebug(0, target, mem, mem2) == TRUE);

		NumberDescription[] brackets = new NumberDescription[arr.length];
		for (int i = 0; i < brackets.length; i++) {
			brackets[i] = new NumberDescription();
		}
		solveDebugBacktrack(0, target, mem, mem2, brackets);
		printWithBrackets(arr, brackets);

		int curr = 0;
		for (int i = 0; i < arr.length; i++) {
			if (brackets[i].openBracketCount > 0) {
				curr += 1;
				continue;
			}

			if ((brackets[i].closeBracketCount > 0)) {
				while (brackets[i].closeBracketCount > 0) {
					System.out.print(curr + " ");
					brackets[i].closeBracketCount--;
					curr -= 1;
				}
				curr += 1;
			}
		}
		System.out.println();

		System.out.println();
	}

	private static void printWithBrackets(int[] arr, NumberDescription[] brackets) {
		for (int i = 0; i < brackets.length; i++) {
			if ((brackets[i].openBracketCount > 0)
					&& (brackets[i].closeBracketCount > 0)) {
				throw new RuntimeException();
			}

			if (brackets[i].openBracketCount > 0) {
				if ((i > 0) && (brackets[i - 1].closeBracketCount > 0)) {
					System.out.print(" - ");
				}
				for (int j = 0; j < brackets[i].openBracketCount; j++) {
					System.out.print("(");
				}
				System.out.print(arr[i] + " - ");
			}
			if (brackets[i].closeBracketCount > 0) {
				if (brackets[i - 1].closeBracketCount > 0) {
					System.out.print(" - ");
				}
				System.out.print(arr[i]);
				for (int j = 0; j < brackets[i].closeBracketCount; j++) {
					System.out.print(")");
				}
			}
		}
		System.out.println();
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

	static final int NOT_INITIALIZED = 0;
	static final int FALSE = 1;
	static final int TRUE = 2;

	static final int MAX_ITEMS = 101;
	static final int MAX_VALUE = 101;
	static final int OFFSET = MAX_ITEMS * MAX_VALUE;

	static int solveDebug(int col, int target, int[][] mem, int[][] mem2) {
		if (col == mem.length) {
			mem2[col][target + OFFSET] = (target == 0) ? TRUE : FALSE;
			return mem2[col][target + OFFSET];
		}
		if (mem2[col][target + OFFSET] != NOT_INITIALIZED) {
			return mem2[col][target + OFFSET];
		}
		for (int r = col; r < mem.length; r++) {
			if (solveDebug(r + 1, mem[col][r] - target, mem, mem2) == TRUE) {
				mem2[col][target + OFFSET] = TRUE;
				return mem2[col][target + OFFSET];
			}
		}
		mem2[col][target + OFFSET] = FALSE;
		return mem2[col][target + OFFSET];
	}

	static void solveDebugBacktrack(int col, int target, int[][] mem, int[][] mem2, NumberDescription[] brackets) {
		if (col == mem.length) {
			return;
		}
		for (int r = col; r < mem.length; r++) {
			if (mem2[r + 1][(mem[col][r] - target) + OFFSET] == TRUE) {
				solveDebugBacktrack(r + 1, mem[col][r] - target, mem, mem2, brackets);

				for (int i = col + 1; i <= r; i++) {
					brackets[col].openBracketCount += 1;
					brackets[i].closeBracketCount += 1;
				}

				if ((r + 1) < mem.length) {
					brackets[col].openBracketCount += 1;
					brackets[brackets.length - 1].closeBracketCount += 1;
				}
				return;
			}
		}
		throw new RuntimeException();
	}

	static class NumberDescription {
		int openBracketCount;
		int closeBracketCount;
	}
}
