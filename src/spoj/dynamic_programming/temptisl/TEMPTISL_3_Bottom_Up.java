package spoj.dynamic_programming.temptisl;

import java.util.Arrays;

// http://www.spoj.com/problems/TEMPTISL/

public class TEMPTISL_3_Bottom_Up {

	public static void main(String[] args) {
		System.out.println(solve(8, 4, 1, 5));
		System.out.println(solve(2, 2, 1, 3));
	}

	static int solve(int vertexCount, int target, int curr, int steps) {
		return solveBottomUp(vertexCount, ((target - curr) + vertexCount) % vertexCount, steps);
	}

	// TODO: it seems that we can rely on symmetry principle:
	// there are always 2 different positions, which has the same distance to the target vertex
	static int solveBottomUp(int vertexCount, int target, int stepsCnt) {
		int[][] memoized = new int[stepsCnt + 1][vertexCount];

		for (int steps = 0; steps <= stepsCnt; steps++) {
			for (int vertex = 0; vertex < vertexCount; vertex++) {
				if (steps == 0) {
					memoized[steps][vertex] = (target == vertex) ? 1 : 0;
				} else {
					memoized[steps][vertex] = memoized[steps - 1][(vertex + 1) % vertexCount] +
							memoized[steps - 1][((vertex - 1) + vertexCount) % vertexCount];
				}
			}
		}

		for (int[] row : memoized) {
			System.out.println(Arrays.toString(row));
		}

		return memoized[stepsCnt][0];
	}
}
