package spoj.dynamic_programming.activ;

// http://www.spoj.com/problems/ACTIV/

import java.util.Arrays;

public class ACTIV_1 {

	public static void main(String[] args) {
		ACTIV_Brute_Force.testFewManualCases(ACTIV_1::solve);
		ACTIV_Brute_Force.testRandomizedCases(ACTIV_1::solve);
	}

	static int solve(Activity[] activities) {
		Arrays.sort(activities, (a1, a2) -> {
			int x = Integer.compare(a1.start, a2.start);
			return x != 0 ? x : Integer.compare(a1.end, a2.end);
		});
		// "-1" because of excluding empty set
		int result = solve(activities, 0) - 1;
		return result;
	}

	// TODO: add memoization (and implement bottom-up Dynamic Programming solution)
	static int solve(Activity[] activities, int curr) {
		if (curr == activities.length) {
			return 1;
		}

		int result = solve(activities, curr + 1);

		// TODO: use binary search instead of linear search
		for (int i = curr + 1; i <= activities.length; i++) {
			if ((i == activities.length)
					|| (activities[i].start >= activities[curr].end)) {

				result += solve(activities, i);
				break;
			}
		}

		return result;
	}
}
