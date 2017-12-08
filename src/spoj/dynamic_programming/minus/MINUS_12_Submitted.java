package spoj.dynamic_programming.minus;

// http://www.spoj.com/problems/MINUS/

import java.util.Arrays;
import java.util.Scanner;

public class MINUS_12_Submitted {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int testsNum = sc.nextInt();
		for (int test = 0; test < testsNum; test++) {
			length = sc.nextInt();
			int target = sc.nextInt();

			for (int i = 0; i < length; i++) {
				arr[i] = sc.nextInt();
			}

			solve(target);
		}
	}

	static final void solve(int target) {
		calculatePartialDifferences();

		for (int[] row : mem) {
			Arrays.fill(row, NOT_INITIALIZED);
		}
		solve(0, target);

		Arrays.fill(openBracketCount, 0);
		Arrays.fill(closeBracketCount, 0);
		backtrack(0, target);

		colletOrder();
		System.out.println();
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
	private static final void calculatePartialDifferences() {
		for (int c = 0; c < length; c++) {
			partDiff[c][c] = arr[c];
			for (int r = c + 1; r < length; r++) {
				partDiff[c][r] = partDiff[c][r - 1] - arr[r];
			}
		}
	}

	static final int NOT_INITIALIZED = 0;
	static final int FALSE = 1;
	static final int TRUE = 2;

	static final int MAX_ITEMS = 101;
	static final int MAX_VALUE = 101;
	static final int OFFSET = MAX_ITEMS * MAX_VALUE;

	static final int[][] partDiff = new int[MAX_ITEMS][MAX_ITEMS];
	static final int[][] mem = new int[MAX_ITEMS][(OFFSET * 2) + 2];
	static final int[] arr = new int[MAX_ITEMS];
	static int length;
	static final int[] openBracketCount = new int[MAX_ITEMS];
	static final int[] closeBracketCount = new int[MAX_ITEMS];

	static final int solve(int col, int target) {
		if (col == length) {
			mem[col][target + OFFSET] = (target == 0) ? TRUE : FALSE;
			return mem[col][target + OFFSET];
		}
		if (mem[col][target + OFFSET] != NOT_INITIALIZED) {
			return mem[col][target + OFFSET];
		}
		for (int r = col; r < length; r++) {
			if (solve(r + 1, partDiff[col][r] - target) == TRUE) {
				mem[col][target + OFFSET] = TRUE;
				return mem[col][target + OFFSET];
			}
		}
		mem[col][target + OFFSET] = FALSE;
		return mem[col][target + OFFSET];
	}

	static final void backtrack(int col, int target) {
		if (col == length) {
			return;
		}
		for (int r = col; r < length; r++) {
			if (mem[r + 1][(partDiff[col][r] - target) + OFFSET] == TRUE) {
				backtrack(r + 1, partDiff[col][r] - target);

				for (int i = col + 1; i <= r; i++) {
					openBracketCount[col] += 1;
					closeBracketCount[i] += 1;
				}

				if ((r + 1) < length) {
					openBracketCount[col] += 1;
					closeBracketCount[length - 1] += 1;
				}
				return;
			}
		}
		throw new RuntimeException();
	}

	private static final void colletOrder() {
		int curr = 0;
		for (int i = 0; i < length; i++) {
			if (openBracketCount[i] > 0) {
				curr += 1;
				continue;
			}

			if ((closeBracketCount[i] > 0)) {
				while (closeBracketCount[i] > 0) {
					System.out.println(curr);
					closeBracketCount[i]--;
					curr -= 1;
				}
				curr += 1;
			}
		}
	}
}
