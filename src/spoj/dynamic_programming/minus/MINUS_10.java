package spoj.dynamic_programming.minus;

// http://www.spoj.com/problems/MINUS/

import java.util.ArrayList;
import java.util.List;

public class MINUS_10 {

	public static void main(String[] args) {
		MINUS_Testing.testFewManualCases(MINUS_10::solve);
		MINUS_Testing.testRandomized(MINUS_10::solve);
	}

	static List<Integer> solve(int[] arr, int target) {
		int[][] partDiff = calculatePartialDifferences(arr);

		int[][] mem = new int[MAX_ITEMS][(OFFSET * 2) + 2];

		// Bottom-up approach is slow!
		// solveBottomUp(partDiff, mem);

		// Top-down is much faster in this case
		solveTopDown(0, target, partDiff, mem);

		NumberDescription[] brackets = new NumberDescription[arr.length];
		for (int i = 0; i < brackets.length; i++) {
			brackets[i] = new NumberDescription();
		}
		backtrack(0, target, partDiff, mem, brackets);

		List<Integer> result = colletOrder(arr, brackets);
		return result;
	}

	/**
	 * Calculates all partial differences of all subarrays of arr
	 *
	 * partDiff[c][r] is defined for every r >= c
	 * partDiff[c][r] represents (arr[c] - arr[c+1] - arr[c+2] - arr[c+3] - ... - arr[r])
	 *
	 * Or, if we use the brackets to denote the order of the subtractions:
	 * partDiff[c][r] is ((...(((arr[c] - arr[c+1]) - arr[c+2]) - arr[c+3]) - ... ) - arr[r])
	 */
	private static int[][] calculatePartialDifferences(int[] arr) {
		int[][] partDiff = new int[arr.length][arr.length];
		for (int c = 0; c < arr.length; c++) {
			partDiff[c][c] = arr[c];
			for (int r = c + 1; r < arr.length; r++) {
				partDiff[c][r] = partDiff[c][r - 1] - arr[r];
			}
		}
		return partDiff;
	}

	static final int NOT_INITIALIZED = 0;
	static final int FALSE = 1;
	static final int TRUE = 2;

	static final int MAX_ITEMS = 101;
	static final int MAX_VALUE = 101;
	static final int OFFSET = MAX_ITEMS * MAX_VALUE;

	static int solveTopDown(int col, int target, int[][] partDiff, int[][] mem) {
		if (col == partDiff.length) {
			mem[col][target + OFFSET] = (target == 0) ? TRUE : FALSE;
			return mem[col][target + OFFSET];
		}
		if (mem[col][target + OFFSET] != NOT_INITIALIZED) {
			return mem[col][target + OFFSET];
		}
		for (int r = col; r < partDiff.length; r++) {
			if (solveTopDown(r + 1, partDiff[col][r] - target, partDiff, mem) == TRUE) {
				mem[col][target + OFFSET] = TRUE;
				return mem[col][target + OFFSET];
			}
		}
		mem[col][target + OFFSET] = FALSE;
		return mem[col][target + OFFSET];
	}

	static void solveBottomUp(int[][] partDiff, int[][] mem) {

		for (int target = -OFFSET; target <= OFFSET; target++) {
			mem[partDiff.length][target + OFFSET] = (target == 0) ? TRUE : FALSE;
		}

		for (int col = partDiff.length - 1; col >= 0; col--) {
			for (int target = -OFFSET; target <= OFFSET; target++) {
				mem[col][target + OFFSET] = FALSE;
				for (int r = col; r < partDiff.length; r++) {
					if ((((partDiff[col][r] - target) + OFFSET) < mem[r + 1].length)
							&& (((partDiff[col][r] - target) + OFFSET) >= 0)
							&& (mem[r + 1][(partDiff[col][r] - target) + OFFSET] == TRUE)) {

						mem[col][target + OFFSET] = TRUE;
						break;
					}
				}
			}
		}
	}
	static void backtrack(int col, int target, int[][] partDiff, int[][] mem, NumberDescription[] brackets) {
		if (col == partDiff.length) {
			return;
		}
		for (int r = col; r < partDiff.length; r++) {
			if (mem[r + 1][(partDiff[col][r] - target) + OFFSET] == TRUE) {
				backtrack(r + 1, partDiff[col][r] - target, partDiff, mem, brackets);

				for (int i = col + 1; i <= r; i++) {
					brackets[col].openBracketCount += 1;
					brackets[i].closeBracketCount += 1;
				}

				if ((r + 1) < partDiff.length) {
					brackets[col].openBracketCount += 1;
					brackets[brackets.length - 1].closeBracketCount += 1;
				}
				return;
			}
		}
		throw new RuntimeException();
	}

	private static List<Integer> colletOrder(int[] arr, NumberDescription[] brackets) {
		List<Integer> result = new ArrayList<>();
		int curr = 0;
		for (int i = 0; i < arr.length; i++) {
			if (brackets[i].openBracketCount > 0) {
				curr += 1;
				continue;
			}

			if ((brackets[i].closeBracketCount > 0)) {
				while (brackets[i].closeBracketCount > 0) {
					result.add(curr);
					brackets[i].closeBracketCount--;
					curr -= 1;
				}
				curr += 1;
			}
		}

		return result;
	}

	static class NumberDescription {
		int openBracketCount;
		int closeBracketCount;
	}
}
