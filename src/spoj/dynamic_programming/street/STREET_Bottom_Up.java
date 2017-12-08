package spoj.dynamic_programming.street;

// http://www.spoj.com/problems/STREET/

public class STREET_Bottom_Up {

	public static void main(String[] args) {
		STREET_Without_Memoization.test_original_example(STREET_Bottom_Up::solve);
		STREET_Without_Memoization.test_randomized(STREET_Bottom_Up::solve);
	}

	static int solve(int[] maxHeight, int maxWidth, int availableBuildings) {

		int[][] memoized = new int[maxHeight.length][availableBuildings + 1];

		for (int currStartIdx = maxHeight.length - 1; currStartIdx >= 0; currStartIdx--) {
			for (int b = 1; b <= availableBuildings; b++) {

				int minHeight = Integer.MAX_VALUE;

				for (int w = 0; (w < maxWidth) && ((currStartIdx + w) < maxHeight.length); w++) {

					minHeight = Math.min(minHeight, maxHeight[currStartIdx + w]);

					int subProblemSolution = (((currStartIdx + w + 1) < maxHeight.length) && (b > 1))
							? memoized[currStartIdx + w + 1][b - 1]
							: 0;

					memoized[currStartIdx][b] = Math.max(
							memoized[currStartIdx][b],
							subProblemSolution + ((w + 1) * minHeight));
				}

				int subProblemSolution = ((currStartIdx + 1) < maxHeight.length)
						? memoized[currStartIdx + 1][b]
						: 0;

				memoized[currStartIdx][b] = Math.max(
						memoized[currStartIdx][b],
						subProblemSolution);

			}
		}

		int result = 0;

		for (int b = 1; b <= availableBuildings; b++) {
			result = Math.max(result, memoized[0][b]);
		}

		return result;
	}

}
