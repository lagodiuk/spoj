package spoj.dynamic_programming.brdghrd;

// http://www.spoj.com/problems/BRDGHRD/
// Extension of the solution of http://www.spoj.com/problems/BRIDGE/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import spoj.dynamic_programming.bridge.BRIDGE_Brute_Force;
import spoj.dynamic_programming.bridge.Bridge;

public class BRIDGE_5_Bottom_Up {

	public static void main(String[] args) {
		BRIDGE_Brute_Force.testFewManualCases(BRIDGE_5_Bottom_Up::solve);
		BRIDGE_Brute_Force.testRandomized(BRIDGE_5_Bottom_Up::solve);
		BRIDGE_Brute_Force.testRandomized(BRIDGE_5_Bottom_Up::solve, false, 1000);
	}

	// O(N * log(N))
	static int solve(Bridge[] bridges) {
		Arrays.sort(bridges, (b1, b2) -> {
			int x = Integer.compare(b1.from, b2.from);
			return (x != 0) ? x : Integer.compare(b1.to, b2.to);
		});

		// As far as all bridges are sorted by Bridge.from
		// We just need to find the Longest Increasing Subsequence of Bridge.to
		List<Integer> lis = new ArrayList<>();
		for (Bridge b : bridges) {
			if (lis.isEmpty()) {
				lis.add(b.to);
			} else {
				int idx = binarySearchRightmost(lis, 0, lis.size() - 1, b.to);
				if ((idx + 1) < lis.size()) {
					lis.set(idx + 1, b.to);
				} else {
					lis.add(b.to);
				}
			}
		}

		return lis.size();
	}

	static int binarySearchRightmost(List<Integer> arr, int l, int r, int x) {
		while (l < r) {
			int m = (l + r + 1) / 2;
			int e = arr.get(m);
			if (e > x) {
				r = m - 1;
			} else {
				l = m;
			}
		}
		if (arr.get(l) > x) {
			l--;
		}
		return l;
	}
}
