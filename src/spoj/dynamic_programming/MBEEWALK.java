package spoj.dynamic_programming;

// http://www.spoj.com/problems/MBEEWALK/

import java.util.Arrays;

public class MBEEWALK {

	public static void main(String[] args) {
		for (int i = 1; i <= MAX_STEPS; i++) {
			System.out.println(i + " -> " + solve(i));
		}
	}

	static int solve(int availableSteps) {
		return solve(0, 0, availableSteps, 0, 0);
	}

	static final int MAX_STEPS = 14;
	static int[][][] MEMOIZED = new int[MAX_STEPS + 1][(2 * MAX_STEPS) + 1][(2 * MAX_STEPS) + 1];
	static {
		for (int i = 0; i <= MAX_STEPS; i++) {
			for (int j = 0; j <= (MAX_STEPS * 2); j++) {
				Arrays.fill(MEMOIZED[i][j], -1);
			}
		}
	}

	// Complexity O(N^3)
	static int solve(int startX, int startY, int availableSteps, int x, int y) {
		if (availableSteps == 0) {
			return (startX == x) && (startY == y) ? 1 : 0;
		}

		if (MEMOIZED[availableSteps][x + MAX_STEPS][y + MAX_STEPS] != -1) {
			return MEMOIZED[availableSteps][x + MAX_STEPS][y + MAX_STEPS];
		}

		int result = solve(startX, startY, availableSteps - 1, x + 1, y)
				+ solve(startX, startY, availableSteps - 1, x - 1, y)
				+ solve(startX, startY, availableSteps - 1, x, y + 1)
				+ solve(startX, startY, availableSteps - 1, x, y - 1)
				+ solve(startX, startY, availableSteps - 1, x + 1, y + 1)
				+ solve(startX, startY, availableSteps - 1, x - 1, y - 1);

		MEMOIZED[availableSteps][x + MAX_STEPS][y + MAX_STEPS] = result;

		return result;
	}

}
