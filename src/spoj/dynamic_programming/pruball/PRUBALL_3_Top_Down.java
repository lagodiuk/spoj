package spoj.dynamic_programming.pruball;

// http://www.spoj.com/problems/PRUBALL/

import java.util.Arrays;
import java.util.Random;
import java.util.function.BiFunction;

public class PRUBALL_3_Top_Down {

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

	static int[][] memoized = new int[101][1001];

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
		for (int i = 1; i <= ((floors / 2) + 1); i++) {
			result = Math.min(result,
					Math.max(solve(balls - 1, i - 1),
							solve(balls, floors - i)));
		}

		memoized[balls][floors] = result + 1;

		return memoized[balls][floors];
	}

	static void randomized_test(BiFunction<Integer, Integer, Integer> solver) {
		Random rnd = new Random(1);

		reset();

		for (int i = 0; i < 100; i++) {
			int balls = rnd.nextInt(100) + 1;
			int floors = rnd.nextInt(1000) + 1;

			int s1 = solve(balls, floors);
			int s2 = solver.apply(balls, floors);

			System.out.println(balls + " " + floors);
			System.out.println(s2);

			if (s1 != s2) {
				throw new RuntimeException();
			}
		}
	}
}
