package spoj.dynamic_programming.hist2;

import java.util.Arrays;

// http://www.spoj.com/problems/HIST2/

public class HIST2_1 {

	public static void main(String[] args) {
		calculate(new int[]{1, 2, 3, 4});
		calculate(new int[]{3, 4, 1, 2});
		calculate(new int[]{2, 6, 5});
		calculate(new int[]{1, 1, 1});
		calculate(new int[]{1});
		calculate(new int[]{10});
		calculate(new int[]{1, 0, 1, 0});
	}

	static void calculate(int[] arr) {
		for (int i = 0; i < memoized.length; i++) {
			for (int j = 0; j < memoized[i].length; j++) {
				Arrays.fill(memoized[i][j], -1);
			}
		}
		int result = calculate(arr, 0, 0, 2 * arr.length);
		System.out.println(result);
	}

	static final int MAX_VALUE = 100;
	static final int MAX_ARR_SIZE = 15;
	static int[][][] memoized = new int[MAX_ARR_SIZE][MAX_ARR_SIZE * 2][MAX_ARR_SIZE * 4 * MAX_VALUE];

	static int calculate(int[] arr, int idx, int plusMinus, int perimeterSoFar) {
		if (idx == arr.length) {
			return (plusMinus == 2) ? perimeterSoFar : -1;
		}

		int pmIdx = plusMinus + MAX_ARR_SIZE;
		int perIdx = perimeterSoFar + (2 * MAX_VALUE * MAX_ARR_SIZE);

		if (memoized[idx][pmIdx][perIdx] == -1) {

			memoized[idx][pmIdx][perIdx] =
					Math.max(calculate(arr, idx + 1, plusMinus, perimeterSoFar), Math.max(
							calculate(arr, idx + 1, plusMinus + 2, perimeterSoFar + (2 * arr[idx])),
							calculate(arr, idx + 1, plusMinus - 2, perimeterSoFar - (2 * arr[idx]))));
		}

		return memoized[idx][pmIdx][perIdx];
	}
}
