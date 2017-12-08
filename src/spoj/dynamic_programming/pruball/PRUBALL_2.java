package spoj.dynamic_programming.pruball;

// http://www.spoj.com/problems/PRUBALL/

import java.util.Arrays;

public class PRUBALL_2 {

	public static void main(String[] args) {
		reset();
		System.out.println(solve(2, 10));
		System.out.println(solve(2, 100));
		System.out.println(solve(2, 300));
		System.out.println(solve(25, 900));
	}

	private static void reset() {
		for (int i = 0; i < memoized.length; i++) {
			Arrays.fill(memoized[i], -1);
		}
	}

	static int[][] memoized = new int[100][1000];

	static int solve(int balls, int floors) {

		if ((floors < 1) || (balls < 1)) {
			return 0;
		}

		if (balls == 1) {
			return floors;
		}

		if (memoized[balls][floors] != -1) {
			return memoized[balls][floors];
		}

		int result = 100000;
		for (int i = 1; i <= floors; i++) {
			result = Math.min(result,
					Math.max(solve(balls - 1, i - 1),
							solve(balls, floors - i)));
		}

		memoized[balls][floors] = result + 1;

		return memoized[balls][floors];
	}
}
