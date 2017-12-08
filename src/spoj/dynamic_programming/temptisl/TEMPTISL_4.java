package spoj.dynamic_programming.temptisl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

// http://www.spoj.com/problems/TEMPTISL/

public class TEMPTISL_4 {

	public static void main(String[] args) throws Exception {
		String s;
		String[] parts;
		int vertexCnt;
		int steps;
		int startPos;
		int targetPos;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while ((s = br.readLine()) != null) {
			if (s.isEmpty()) {
				continue;
			}
			parts = s.split(" ");
			vertexCnt = Integer.parseInt(parts[0]);
			steps = Integer.parseInt(parts[1]);
			if ((vertexCnt == -1) || (steps == -1)) {
				break;
			}

			s = "";
			while (s.isEmpty()) {
				s = br.readLine();
			}
			parts = s.split(" ");
			startPos = Integer.parseInt(parts[0]);
			targetPos = Integer.parseInt(parts[1]);

			System.out.println(solve(vertexCnt, targetPos, startPos, steps));
		}
	}

	static final long solve(int vertexCount, int target, int curr, int steps) {
		return solveBottomUp(vertexCount, ((target - curr) + vertexCount) % vertexCount, steps);
	}

	static final int MAX_VERTEX_COUNT = 52;

	static final long[][] memoized = new long[2][MAX_VERTEX_COUNT];
	static int curr;
	static int prev;

	static final long solveBottomUp(int vertexCount, int target, int stepsCnt) {

		curr = 0;
		prev = 1;

		Arrays.fill(memoized[prev], 0);
		memoized[prev][target] = 1;

		for (int steps = 1; steps <= stepsCnt; steps++) {
			for (int vertex = 0; vertex < vertexCount; vertex++) {
				memoized[curr][vertex] = memoized[prev][(vertex + 1) % vertexCount] +
						memoized[prev][((vertex - 1) + vertexCount) % vertexCount];
			}
			curr ^= 1;
			prev ^= 1;
		}

		return memoized[prev][0];
	}
}
