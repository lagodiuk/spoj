package spoj.dynamic_programming.pruball;

// http://www.spoj.com/problems/PRUBALL/

public class PRUBALL_4_Bottom_Up {

	public static void main(String[] args) {
		System.out.println(solve(2, 10));
		System.out.println(solve(2, 100));
		System.out.println(solve(2, 300));
		System.out.println(solve(25, 900));

		System.out.println();
		PRUBALL_3_Top_Down.randomized_test(PRUBALL_4_Bottom_Up::solve);
	}

	static int[][] memoized = new int[101][1001];

	static int solve(int balls, int floors) {

		if (balls == 1) {
			return floors;
		}

		for (int b = 1; b <= balls; b++) {
			for (int f = 1; f <= floors; f++) {

				int result = 100000;

				for (int i = 1; i <= ((floors / 2) + 1); i++) {

					int left = ((i < 2) || (b < 2))
							? 0
							: ((b == 2) ? i - 1 : memoized[b - 1][i - 1]);

					int right = ((f < (i + 1)) || (b < 1))
							? 0
							: ((b == 1) ? f - i : memoized[b][f - i]);

					result = Math.min(result, Math.max(left, right));
				}

				memoized[b][f] = result + 1;
			}
		}

		return memoized[balls][floors];
	}
}
