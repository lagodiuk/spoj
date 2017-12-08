package spoj.dynamic_programming.bridge;

// http://www.spoj.com/problems/BRIDGE/

import java.util.Arrays;

public class BRIDGE_1_Top_Down {

	public static void main(String[] args) {
		BRIDGE_Brute_Force.testFewManualCases(BRIDGE_1_Top_Down::solve);
		BRIDGE_Brute_Force.testRandomized(BRIDGE_1_Top_Down::solve);
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

		int firstNonConflictingWithCurr = curr + 1;
		while ((firstNonConflictingWithCurr < bridges.length)
				&& (bridges[firstNonConflictingWithCurr].to < bridges[curr].to)) {

			firstNonConflictingWithCurr++;
		}

		int firstNonConflictingWithPrev = curr + 1;
		while ((prev != -1) && (firstNonConflictingWithPrev < bridges.length)
				&& (bridges[firstNonConflictingWithPrev].to < bridges[prev].to)) {

			firstNonConflictingWithPrev++;
		}

		int excludingCurrent = solve(bridges, prev, firstNonConflictingWithPrev);
		int includingCurrent = solve(bridges, curr, firstNonConflictingWithCurr) + 1;
		int result = Math.max(excludingCurrent, includingCurrent);
		return result;
	}
}
