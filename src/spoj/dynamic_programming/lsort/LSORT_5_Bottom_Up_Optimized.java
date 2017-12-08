package spoj.dynamic_programming.lsort;

// http://www.spoj.com/problems/LSORT/

public class LSORT_5_Bottom_Up_Optimized {

	public static void main(String[] args) {
		LSORT_Brute_Force.verifyManualTestCases(LSORT_5_Bottom_Up_Optimized::solve);
		LSORT_Brute_Force.verifyRandomizedTestCases(LSORT_5_Bottom_Up_Optimized::solve);
		LSORT_Brute_Force.verifyRandomizedTestCases(LSORT_5_Bottom_Up_Optimized::solve, false, 1000);
	}

	static final int MAX_SIZE = 1005;
	static final int INF = 2000000000;

	static final int[] arr = new int[MAX_SIZE];
	static int arrLength = 0;

	static final int[] position = new int[MAX_SIZE];
	static final int[][] countLeft = new int[MAX_SIZE][MAX_SIZE];
	static final int[][] countRight = new int[MAX_SIZE][MAX_SIZE];
	static final int[][] memoized = new int[MAX_SIZE][MAX_SIZE];

	static int solve(int[] arr2) {
		System.arraycopy(arr2, 0, arr, 0, arr2.length);
		arrLength = arr2.length;

		for (int i = 0; i < arrLength; i++) {
			position[arr[i]] = i + 1;
		}
		initializeCountLeft();
		initializeCountRight();

		int result = solve();
		return result;
	}

	static int solve() {
		int sortedIntervalNewLength;
		int s1;
		int s2;
		for (int from = 1; from <= arrLength; from++) {
			for (int to = arrLength; to >= from; to--) {

				if ((from == 1) && (to == arrLength)) {
					memoized[from][to] = 0;

				} else {

					sortedIntervalNewLength = (to - from) + 2;

					s1 = (from == 1) ? INF : memoized[from - 1][to] + (countLeft[from - 1][to] * sortedIntervalNewLength);
					s2 = (to == arrLength) ? INF : memoized[from][to + 1] + (countRight[to + 1][from] * sortedIntervalNewLength);

					memoized[from][to] = Math.min(s1, s2);
				}
			}
		}

		int result = INF;
		int r;
		for (int i = 0; i < arrLength; i++) {
			r = memoized[arr[i]][arr[i]] + (i + 1);
			result = Math.min(result, r);
		}

		return result;
	}

	private static void initializeCountLeft() {
		int to;
		int from;
		for (int num = 1; num < arrLength; num++) {

			from = num + 1;
			to = from;

			countLeft[num][to] = (position[to] < position[num])
					? position[num] - 1
					: position[num];

			for (to = from + 1; to <= arrLength; to++) {

				countLeft[num][to] = (position[to] < position[num])
						? countLeft[num][to - 1] - 1
						: countLeft[num][to - 1];
			}
		}
	}

	private static void initializeCountRight() {
		int to;
		int from;
		for (int num = arrLength; num > 1; num--) {

			to = num - 1;
			from = to;

			countRight[num][from] = (position[from] < position[num])
					? position[num] - 1
					: position[num];

			for (from = to - 1; from >= 1; from--) {

				countRight[num][from] = (position[from] < position[num])
						? countRight[num][from + 1] - 1
						: countRight[num][from + 1];
			}
		}
	}
}
