package spoj.dynamic_programming.activ;

// http://www.spoj.com/problems/ACTIV/

import java.util.Arrays;

public class ACTIV_4 {

	public static void main(String[] args) {
		ACTIV_Brute_Force.testFewManualCases(ACTIV_4::solve);
		ACTIV_Brute_Force.testRandomizedCases(ACTIV_4::solve);
		ACTIV_Brute_Force.testRandomizedCases(ACTIV_4::solve, 20, true);
		ACTIV_Brute_Force.testRandomizedCases(ACTIV_4::solve, 1000, false);
	}

	static final int MOD = 100000000;

	static int solve(Activity[] activities) {
		Arrays.sort(activities, (a1, a2) -> Integer.compare(a1.start, a2.start));
		// "-1" because of excluding empty set
		int result = ((solveBottomUp(activities) - 1) + MOD) % MOD;
		return result;
	}

	static int solveBottomUp(Activity[] activities) {
		int[] memoized = new int[activities.length + 1];
		memoized[activities.length] = 1;

		int curr = activities.length - 1;
		while (curr >= 0) {

			int result = memoized[curr + 1];

			// Binary search for the leftmost activity,
			// which compatible with current
			int left = curr + 1;
			int right = activities.length;
			while (left < right) {
				int mid = (left + right) / 2;
				if (activities[mid].start < activities[curr].end) {
					left = mid + 1;
				} else {
					right = mid;
				}
			}

			result = (result + memoized[left]) % MOD;

			memoized[curr] = result;
			curr--;
		}

		return memoized[0];
	}
}
