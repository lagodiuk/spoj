package spoj.dynamic_programming.lsort;

import java.util.Arrays;

// http://www.spoj.com/problems/LSORT/

public class LSORT_3_Top_Down {

	public static void main(String[] args) {
		LSORT_Brute_Force.verifyManualTestCases(LSORT_3_Top_Down::solve);
		LSORT_Brute_Force.verifyRandomizedTestCases(LSORT_3_Top_Down::solve);
		LSORT_Brute_Force.verifyRandomizedTestCases(LSORT_3_Top_Down::solve, false, 1000);
	}

	static int solve(int[] arr) {
		int[] position = getNumberPositions(arr);
		int[][] countLeft = getCountLeft(arr, position);
		int[][] countRight = getCountRight(arr, position);

		int[][] memoized = new int[arr.length + 1][arr.length + 1];
		for (int[] row : memoized) {
			Arrays.fill(row, -1);
		}

		int result = INF;
		for (int i = 0; i < arr.length; i++) {
			int r = solve(arr[i], arr[i], arr, countLeft, countRight, memoized) + (i + 1);
			result = Math.min(result, r);
		}
		return result;
	}

	static final int INF = 2000000000;

	static int solve(int from, int to, int[] arr, int[][] countLeft, int[][] countRight, int[][] memoized) {

		if ((from == 1) && (to == arr.length)) {
			return 0;
		}

		if ((from < 1) || (to > arr.length)) {
			return INF;
		}

		if (memoized[from][to] != -1) {
			return memoized[from][to];
		}

		int s1 = solve(from - 1, to, arr, countLeft, countRight, memoized) + (countLeft[from - 1][to] * ((to - from) + 1 + 1));
		int s2 = solve(from, to + 1, arr, countLeft, countRight, memoized) + (countRight[to + 1][from] * ((to - from) + 1 + 1));

		memoized[from][to] = Math.min(s1, s2);
		return memoized[from][to];
	}

	private static int[] getNumberPositions(int[] arr) {
		int[] position = new int[arr.length + 1];
		for (int i = 0; i < arr.length; i++) {
			position[arr[i]] = i + 1;
		}
		return position;
	}

	private static int[][] getCountLeft(int[] arr, int[] position) {
		int[][] countLeft = new int[arr.length + 1][arr.length + 1];
		for (int num = 1; num < arr.length; num++) {

			int from = num + 1;
			int to = from;

			countLeft[num][to] = (position[to] < position[num])
					? position[num] - 1
					: position[num];

			for (to = from + 1; to <= arr.length; to++) {

				countLeft[num][to] = (position[to] < position[num])
						? countLeft[num][to - 1] - 1
						: countLeft[num][to - 1];
			}
		}
		return countLeft;
	}

	private static int[][] getCountRight(int[] arr, int[] position) {
		int[][] countRight = new int[arr.length + 2][arr.length + 1];
		for (int num = arr.length; num > 1; num--) {

			int to = num - 1;
			int from = to;

			countRight[num][from] = (position[from] < position[num])
					? position[num] - 1
					: position[num];

			for (from = to - 1; from >= 1; from--) {

				countRight[num][from] = (position[from] < position[num])
						? countRight[num][from + 1] - 1
						: countRight[num][from + 1];
			}
		}
		return countRight;
	}
}
