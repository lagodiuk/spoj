package spoj.dynamic_programming.temptisl;

// http://www.spoj.com/problems/TEMPTISL/

public class TEMPTISL_1 {

	public static void main(String[] args) {
		System.out.println(solve(8, 4, 1, 5));
	}

	static int solve(int vertexCount, int target, int curr, int steps) {
		return solveNormalized(vertexCount, ((target - curr) + vertexCount) % vertexCount, 0, steps);
	}

	// TODO: use memoization, and develop bottom-up solution
	// TODO: it seems that we can rely on symmetry principle:
	// there are always 2 different positions, which has the same distance to the target vertex
	static int solveNormalized(int vertexCount, int target, int curr, int steps) {
		if (steps == 0) {
			return target == curr ? 1 : 0;
		}

		return solveNormalized(vertexCount, target, (curr + 1) % vertexCount, steps - 1) +
				solveNormalized(vertexCount, target, ((curr - 1) + vertexCount) % vertexCount, steps - 1);
	}

}
