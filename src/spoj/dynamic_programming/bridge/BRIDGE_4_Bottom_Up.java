package spoj.dynamic_programming.bridge;

// http://www.spoj.com/problems/BRIDGE/

import java.util.Arrays;

public class BRIDGE_4_Bottom_Up {

	public static void main(String[] args) {
		BRIDGE_Brute_Force.testFewManualCases(BRIDGE_4_Bottom_Up::solve);
		BRIDGE_Brute_Force.testRandomized(BRIDGE_4_Bottom_Up::solve);
		BRIDGE_Brute_Force.testRandomized(BRIDGE_4_Bottom_Up::solve, false, 1000);
	}

	// Complexity:
	// O(N * log(N) + N^2) = O(N^2)
	// Memory consumption:
	// O(N)
	static int solve(Bridge[] bridges) {
		Arrays.sort(bridges, (b1, b2) -> {
			int x = Integer.compare(b1.from, b2.from);
			return (x != 0) ? x : Integer.compare(b1.to, b2.to);
		});

		int[][] memoized = new int[bridges.length + 1][2];
		int currMemoized = 0;
		int prevMemoized = 1;
		for (int curr = bridges.length - 1; curr >= 0; curr--) {
			for (int prev = -1; prev < bridges.length; prev++) {
				if ((prev != -1) && (bridges[curr].to < bridges[prev].to)) {
					memoized[prev + 1][currMemoized] = ((curr + 1) == bridges.length) ? 0 : memoized[prev + 1][prevMemoized];
				} else {
					memoized[prev + 1][currMemoized] = Math.max(
							((curr + 1) == bridges.length) ? 0 : memoized[prev + 1][prevMemoized],
							(((curr + 1) == bridges.length) ? 0 : memoized[curr + 1][prevMemoized]) + 1);
				}
			}
			currMemoized ^= 1;
			prevMemoized ^= 1;
		}

		return memoized[0][prevMemoized];
	}
}
