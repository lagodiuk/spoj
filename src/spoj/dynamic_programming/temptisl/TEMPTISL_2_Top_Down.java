package spoj.dynamic_programming.temptisl;

import java.util.Arrays;

// http://www.spoj.com/problems/TEMPTISL/

public class TEMPTISL_2_Top_Down {

	public static void main(String[] args) {
		System.out.println(solve(8, 4, 1, 5));
		System.out.println(solve(2, 2, 1, 3));
	}

	static int solve(int vertexCount, int target, int curr, int steps) {
		int[][] memoized = new int[steps + 1][vertexCount];
		int result = solveNormalized(vertexCount, ((target - curr) + vertexCount) % vertexCount, 0, steps, memoized);

		for (int[] row : memoized) {
			System.out.println(Arrays.toString(row));
		}

		return result;
	}

	// TODO: develop bottom-up solution
	// TODO: it seems that we can rely on symmetry principle:
	// there are always 2 different positions, which has the same distance to the target vertex
	static int solveNormalized(int vertexCount, int target, int curr, int steps, int[][] memoized) {
		if (steps == 0) {
			return target == curr ? 1 : 0;
		}

		memoized[steps][curr] = solveNormalized(vertexCount, target, (curr + 1) % vertexCount, steps - 1, memoized) +
				solveNormalized(vertexCount, target, ((curr - 1) + vertexCount) % vertexCount, steps - 1, memoized);

		return memoized[steps][curr];
	}

}
