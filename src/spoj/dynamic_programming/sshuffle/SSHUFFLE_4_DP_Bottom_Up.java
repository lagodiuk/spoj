package spoj.dynamic_programming.sshuffle;

// http://www.spoj.com/problems/SSHUFFLE/

public class SSHUFFLE_4_DP_Bottom_Up {

	public static void main(String[] args) {
		SSHUFFLE_Testing.test(SSHUFFLE_4_DP_Bottom_Up::solve, 9, "ab".toCharArray(), 50);
		System.out.println("\n=========================================\n");
		SSHUFFLE_Testing.test(SSHUFFLE_4_DP_Bottom_Up::solve, 60, "abc".toCharArray(), 300, false, 20);
	}

	static final long NOT_INITIALIZED = -1;

	static long solve(String s1, String s2, String s3) {
		int[][] s3s1 = precalculateEqualCharsPositions(s1, s3);
		int[][] s3s2 = precalculateEqualCharsPositions(s2, s3);

		return solve(s1, s2, s3, s3s1, s3s2);
	}

	static long solve(String s1, String s2, String s3, int[][] s3s1, int[][] s3s2) {

		int len1 = s1.length();
		int len2 = s2.length();
		int len3 = s3.length();

		long[][][] memoized = new long[s1.length() + 1][s2.length() + 1][s3.length() + 1];
		for (int i1 = 0; i1 <= len1; i1++) {
			for (int i2 = 0; i2 <= len2; i2++) {
				memoized[i1][i2][len3] = 1;
			}
		}

		for (int i3 = len3 - 1; i3 >= 0; i3--) {
			for (int i1 = len1; i1 >= 0; i1--) {
				for (int i2 = len2; i2 >= 0; i2--) {

					long result = 0;

					if (i1 < len1) {
						if (s1.charAt(i1) == s3.charAt(i3)) {
							result += memoized[i1 + 1][i2][i3 + 1];
						}
						for (int ii1 = s3s1[i3][i1]; ii1 < len1; ii1 = s3s1[i3][ii1]) {
							result += memoized[ii1 + 1][i2][i3 + 1];
						}
					}

					if (i2 < len2) {
						if (s2.charAt(i2) == s3.charAt(i3)) {
							result += memoized[i1][i2 + 1][i3 + 1];
						}
						for (int ii2 = s3s2[i3][i2]; ii2 < len2; ii2 = s3s2[i3][ii2]) {
							result += memoized[i1][ii2 + 1][i3 + 1];
						}
					}

					memoized[i1][i2][i3] = result;
				}
			}
		}

		return memoized[0][0][0];
	}

	// result[i2][i1] is the smallest index ii1 inside s1, such that
	// (s2.charAt(i2) == s1.charAt(i1)) and (ii1 > i1)
	private static int[][] precalculateEqualCharsPositions(String s1, String s2) {
		int len1 = s1.length();
		int len2 = s2.length();

		int[][] s2s1 = new int[len2][len1];

		for (int i2 = len2 - 1; i2 >= 0; i2--) {
			s2s1[i2][len1 - 1] = len1;
		}

		for (int i3 = len2 - 1; i3 >= 0; i3--) {
			for (int i1 = len1 - 1; i1 > 0; i1--) {
				if (s1.charAt(i1) == s2.charAt(i3)) {
					s2s1[i3][i1 - 1] = i1;
				} else {
					s2s1[i3][i1 - 1] = s2s1[i3][i1];
				}
			}
		}

		return s2s1;
	}
}
