package spoj.dynamic_programming.pruball;

// http://www.spoj.com/problems/PRUBALL/

import java.util.Arrays;

public class PRUBALL_1 {

	public static void main(String[] args) {
		reset();
		System.out.println(solve(2, 1, 10));

		reset();
		System.out.println(solve(2, 1, 100));

		reset();
		System.out.println(solve(2, 1, 300));
	}

	private static void reset() {
		for (int i = 0; i < memoized.length; i++) {
			for (int j = 0; j < memoized[0].length; j++) {
				Arrays.fill(memoized[i][j], -1);
			}
		}
	}

	static int[][][] memoized = new int[100][1000][1000];

	static int solve(int balls, int minFloor, int maxFloor) {
		if (balls == 1) {
			return (maxFloor - minFloor) + 1;
		}

		if (memoized[balls][minFloor][maxFloor] != -1) {
			return memoized[balls][minFloor][maxFloor];
		}

		int result = Integer.MAX_VALUE;
		for (int i = minFloor + 1; i < maxFloor; i++) {
			result = Math.min(result, Math.max(solve(balls - 1, minFloor, i - 1), solve(balls, i + 1, maxFloor)));
		}

		memoized[balls][minFloor][maxFloor] = result + 1;

		return memoized[balls][minFloor][maxFloor];
	}
}
