package spoj.dynamic_programming.lsort;

// http://www.spoj.com/problems/LSORT/

public class LSORT_1 {

	public static void main(String[] args) {
		LSORT_Brute_Force.verifyManualTestCases(LSORT_1::solve);
		LSORT_Brute_Force.verifyRandomizedTestCases(LSORT_1::solve);
	}

	static int solve(int[] arr) {
		int result = INF;
		for (int i = 0; i < arr.length; i++) {
			int r = solve(arr[i], arr[i], arr) + (i + 1);
			result = Math.min(result, r);
		}
		return result;
	}

	static final int INF = 10000000;

	// TODO: use memoization
	static int solve(int from, int to, int[] arr) {

		if ((from == 1) && (to == arr.length)) {
			return 0;
		}

		if ((from < 1) || (to > arr.length)) {
			return INF;
		}

		int s1 = solve(from - 1, to, arr) + cost(from - 1, from, to, arr);
		int s2 = solve(from, to + 1, arr) + cost(to + 1, from, to, arr);

		return Math.min(s1, s2);
	}

	// TODO: implement possibility to get cost with O(1) complexity
	// can be done via precalculation with O(N^2) complexity
	static int cost(int num, int from, int to, int[] arr) {
		int c = 0;
		for (int i = 0; i < arr.length; i++) {
			if ((arr[i] <= to) && (arr[i] >= from)) {
				continue;
			}
			c++;
			if (arr[i] == num) {
				break;
			}
		}
		return ((to - from) + 1 + 1) * c;
	}
}
