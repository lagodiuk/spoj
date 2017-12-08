package spoj.dynamic_programming.activ;

// http://www.spoj.com/problems/ACTIV/

import java.util.Arrays;

public class ACTIV_2 {

	public static void main(String[] args) {
		ACTIV_Brute_Force.testFewManualCases(ACTIV_2::solve);
		ACTIV_Brute_Force.testRandomizedCases(ACTIV_2::solve);
	}

	static int solve(Activity[] activities) {
		Arrays.sort(activities, (a1, a2) -> {
			int x = Integer.compare(a1.start, a2.start);
			return x != 0 ? x : Integer.compare(a1.end, a2.end);
		});
		// "-1" because of excluding empty set
		int result = solveBottomUp(activities) - 1;
		return result;
	}

	static int solveBottomUp(Activity[] activities) {
		int[] memoized = new int[activities.length + 1];
		memoized[activities.length] = 1;

		int curr = activities.length - 1;
		while (curr >= 0) {

			int result = memoized[curr + 1];

			// TODO: use binary search instead of linear search
			for (int i = curr + 1; i <= activities.length; i++) {
				if ((i == activities.length)
						|| (activities[i].start >= activities[curr].end)) {

					result += memoized[i];
					break;
				}
			}
			memoized[curr] = result;
			curr--;
		}

		return memoized[0];
	}
}
