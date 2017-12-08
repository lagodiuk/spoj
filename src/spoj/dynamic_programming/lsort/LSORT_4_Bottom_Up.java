package spoj.dynamic_programming.lsort;

// http://www.spoj.com/problems/LSORT/

public class LSORT_4_Bottom_Up {

	public static void main(String[] args) {
		LSORT_Brute_Force.verifyManualTestCases(LSORT_4_Bottom_Up::solve);
		LSORT_Brute_Force.verifyRandomizedTestCases(LSORT_4_Bottom_Up::solve);
		LSORT_Brute_Force.verifyRandomizedTestCases(LSORT_4_Bottom_Up::solve, false, 1000);
	}

	static int solve(int[] arr) {
		int[] position = getNumberPositions(arr);
		int[][] countLeft = getCountLeft(arr, position);
		int[][] countRight = getCountRight(arr, position);
		int result = solve(arr, countLeft, countRight);
		return result;
	}

	static final int INF = 2000000000;

	static int solve(int[] arr, int[][] countLeft, int[][] countRight) {
		int[][] memoized = new int[arr.length + 1][arr.length + 1];

		for (int from = 1; from <= arr.length; from++) {
			for (int to = arr.length; to >= from; to--) {
				if ((from == 1) && (to == arr.length)) {
					memoized[from][to] = 0;
				} else {
					int s1 = (from == 1) ? INF : memoized[from - 1][to] + (countLeft[from - 1][to] * ((to - from) + 1 + 1));
					int s2 = (to == arr.length) ? INF : memoized[from][to + 1] + (countRight[to + 1][from] * ((to - from) + 1 + 1));

					memoized[from][to] = Math.min(s1, s2);
				}
			}
		}

		int result = INF;
		for (int i = 0; i < arr.length; i++) {
			int r = memoized[arr[i]][arr[i]] + (i + 1);
			result = Math.min(result, r);
		}

		return result;
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
