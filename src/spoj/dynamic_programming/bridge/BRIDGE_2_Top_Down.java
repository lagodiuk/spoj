package spoj.dynamic_programming.bridge;

// http://www.spoj.com/problems/BRIDGE/

import java.util.Arrays;

public class BRIDGE_2_Top_Down {

	public static void main(String[] args) {
		BRIDGE_Brute_Force.testFewManualCases(BRIDGE_2_Top_Down::solve);
		BRIDGE_Brute_Force.testRandomized(BRIDGE_2_Top_Down::solve);
	}

	static int solve(Bridge[] bridges) {
		Arrays.sort(bridges, (b1, b2) -> {
			int x = Integer.compare(b1.from, b2.from);
			return (x != 0) ? x : Integer.compare(b1.to, b2.to);
		});

		int cnt = solve(bridges, -1, 0);
		return cnt;
	}

	static int solve(Bridge[] bridges, int prev, int curr) {

		if (curr == bridges.length) {
			return 0;
		}

		if ((prev != -1) && (bridges[curr].to < bridges[prev].to)) {
			return solve(bridges, prev, curr + 1);
		}

		int excludingCurrent = solve(bridges, prev, curr + 1);
		int includingCurrent = solve(bridges, curr, curr + 1) + 1;
		int result = Math.max(excludingCurrent, includingCurrent);
		return result;
	}
}
