package spoj.dynamic_programming;

// http://www.spoj.com/problems/TRT/

public class TRT {

	// 1) Exponential complexity
	static long findBestPrice(int[] arr, int left, int right, int time) {
		if (left == right) {
			return arr[left] * time;
		}

		return Math.max(
				(arr[left] * time) + findBestPrice(arr, left + 1, right, time + 1),
				(arr[right] * time) + findBestPrice(arr, left, right - 1, time + 1));
	}

	static long findBestPrice1(int[] arr) {
		return findBestPrice(arr, 0, arr.length - 1, 1);
	}

	// 2) Exponential complexity, but smaller amount of arguments
	// (we can calculate time, just using values of left and right and initial length of array)
	static long findBestPriceCalcTime(int[] arr, int left, int right) {
		int time = left + (arr.length - right);

		if (left == right) {
			return arr[left] * time;
		}

		return Math.max(
				(arr[left] * time) + findBestPriceCalcTime(arr, left + 1, right),
				(arr[right] * time) + findBestPriceCalcTime(arr, left, right - 1));
	}

	static long findBestPrice2(int[] arr) {
		return findBestPriceCalcTime(arr, 0, arr.length - 1);
	}

	// 3) Quadratic complexity (solution is same to variant 2 - but with memoization)
	static final long NOT_MEMOIZED = Long.MIN_VALUE;

	static long findBestPriceQuadratic(int[] arr, int left, int right, long[][] memoized) {
		if (memoized[left][right] != NOT_MEMOIZED) {
			return memoized[left][right];
		}

		int time = left + (arr.length - right);

		if (left == right) {
			memoized[left][right] = arr[left] * time;
			return memoized[left][right];
		}

		memoized[left][right] = Math.max(
				(arr[left] * time) + findBestPriceCalcTime(arr, left + 1, right),
				(arr[right] * time) + findBestPriceCalcTime(arr, left, right - 1));

		return memoized[left][right];
	}

	static long findBestPrice3(int[] arr) {
		long[][] memoized = new long[arr.length][arr.length];
		for (int i = 0; i < memoized.length; i++) {
			for (int j = 0; j < memoized.length; j++) {
				memoized[i][j] = NOT_MEMOIZED;
			}
		}

		return findBestPriceQuadratic(arr, 0, arr.length - 1, memoized);
	}

	public static void main(String[] args) {
		System.out.println(findBestPrice1(new int[]{1, 3, 1, 5, 2}));
		System.out.println(findBestPrice2(new int[]{1, 3, 1, 5, 2}));
		System.out.println(findBestPrice3(new int[]{1, 3, 1, 5, 2}));
	}
}
