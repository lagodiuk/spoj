package spoj.dynamic_programming.bat3;

// http://www.spoj.com/problems/BAT3/

public class BAT3 {

	public static void main(String[] args) {
		System.out.println(solve(4, new int[]{6, 3, 5, 2, 4, 5}));
		System.out.println(solve(2, new int[]{6, 4, 8, 7, 6, 5, 4, 3, 2, 1}));
		System.out.println();

		BAT3_Brute_Force.test(BAT3::solve, 15, true);
		BAT3_Brute_Force.test(BAT3::solve, 100, false);
	}

	static int solve(int miniBATPosition, int[] height) {

		// Solution is very similar to the search of Longest Increasing Subsequence

		int[] memoized = new int[height.length];

		for (int curr = height.length - 1; curr >= 0; curr--) {

			memoized[curr] = 0;

			for (int i = curr + 1; i < height.length; i++) {

				if ((miniBATPosition == curr) || (height[i] < height[curr])) {

					memoized[curr] = Math.max(memoized[curr], memoized[i]);
				}
			}

			memoized[curr] += 1;
		}

		int result = -1;
		for (int i = 0; i < memoized.length; i++) {
			result = Math.max(result, memoized[i]);
		}

		return result;
	}
}
