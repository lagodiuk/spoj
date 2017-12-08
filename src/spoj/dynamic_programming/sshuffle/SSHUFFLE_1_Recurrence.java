package spoj.dynamic_programming.sshuffle;

// http://www.spoj.com/problems/SSHUFFLE/

public class SSHUFFLE_1_Recurrence {

	public static void main(String[] args) {
		SSHUFFLE_Testing.test(SSHUFFLE_1_Recurrence::solve, 9, "ab".toCharArray(), 50);
	}

	static long solve(String s1, String s2, String s3) {
		return solve(s1, s2, s3, 0, 0, 0);
	}

	// TODO: add memoization
	static long solve(String s1, String s2, String s3, int i1, int i2, int i3) {
		if (i3 == s3.length()) {
			return 1;
		}

		long result = 0;

		// TODO: for every position i3 inside s3, we can precalculate the index of the same
		// character inside s1
		// so, we could iterate only over such positions inside s1, where s1.charAt(ii1) ==
		// s3.charAt(i3)
		for (int ii1 = i1; ii1 < s1.length(); ii1++) {
			if (s1.charAt(ii1) == s3.charAt(i3)) {
				result += solve(s1, s2, s3, ii1 + 1, i2, i3 + 1);
			}
		}

		// TODO: for every position i3 inside s3, we can precalculate the index of the same
		// character inside s2
		// so, we could iterate only over such positions inside s2, where s2.charAt(ii2) ==
		// s3.charAt(i3)
		for (int ii2 = i2; ii2 < s2.length(); ii2++) {
			if (s2.charAt(ii2) == s3.charAt(i3)) {
				result += solve(s1, s2, s3, i1, ii2 + 1, i3 + 1);
			}
		}

		return result;
	}

}
