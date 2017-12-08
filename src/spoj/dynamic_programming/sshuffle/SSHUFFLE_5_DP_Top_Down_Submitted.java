package spoj.dynamic_programming.sshuffle;

import java.util.Arrays;

// http://www.spoj.com/problems/SSHUFFLE/

public class SSHUFFLE_5_DP_Top_Down_Submitted {

	public static void main(String[] args) throws Exception {
		SSHUFFLE_Testing.test(SSHUFFLE_5_DP_Top_Down_Submitted::solve, 9, "ab".toCharArray(), 50);
		System.out.println("\n=========================================\n");
		SSHUFFLE_Testing.test(SSHUFFLE_5_DP_Top_Down_Submitted::solve, 60, "abc".toCharArray(), 300, false,
				20);

		// BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// int testsNum = Integer.parseInt(br.readLine());
		// for (int i = 0; i < testsNum; i++) {
		// String line = br.readLine();
		// int space1 = line.indexOf(' ');
		// int space2 = line.indexOf(' ', space1 + 1);
		//
		// String s1 = line.substring(0, space1);
		// String s2 = line.substring(space1 + 1, space2);
		// String s3 = line.substring(space2 + 1);
		//
		// long result = solve(s1, s2, s3);
		// System.out.println(result);
		// }
	}

	static final long NOT_INITIALIZED = -1;
	static final int MAX_LENGTH = 61;
	static final int[][] s3s1 = new int[MAX_LENGTH][MAX_LENGTH];
	static final int[][] s3s2 = new int[MAX_LENGTH][MAX_LENGTH];
	static final long[][][] memoized = new long[MAX_LENGTH + 1][MAX_LENGTH + 1][MAX_LENGTH + 1];

	static String s1;
	static String s2;
	static String s3;
	static int len1;
	static int len2;
	static int len3;

	static long solve(String str1, String str2, String str3) {
		s1 = str1;
		s2 = str2;
		s3 = str3;

		precalculateEqualCharsPositions(s1, s3, s3s1);
		precalculateEqualCharsPositions(s2, s3, s3s2);

		len1 = s1.length();
		len2 = s2.length();
		len3 = s3.length();

		for (int i1 = 0; i1 <= len1; i1++) {
			for (int i2 = 0; i2 <= len2; i2++) {
				Arrays.fill(memoized[i1][i2], NOT_INITIALIZED);
			}
		}

		return solve(0, 0, 0);
	}

	static final long solve(int i1, int i2, int i3) {

		if (i3 == len3) {
			return 1;
		}

		if (memoized[i1][i2][i3] != NOT_INITIALIZED) {
			return memoized[i1][i2][i3];
		}

		memoized[i1][i2][i3] = 0;

		if (i1 < len1) {
			memoized[i1][i2][i3] += (s1.charAt(i1) == s3.charAt(i3)) ? solve(i1 + 1, i2, i3 + 1) : 0;
			for (int ii1 = s3s1[i3][i1]; ii1 < len1; ii1 = s3s1[i3][ii1]) {
				memoized[i1][i2][i3] += solve(ii1 + 1, i2, i3 + 1);
			}
		}

		if (i2 < len2) {
			memoized[i1][i2][i3] += (s2.charAt(i2) == s3.charAt(i3)) ? solve(i1, i2 + 1, i3 + 1) : 0;
			for (int ii2 = s3s2[i3][i2]; ii2 < len2; ii2 = s3s2[i3][ii2]) {
				memoized[i1][i2][i3] += solve(i1, ii2 + 1, i3 + 1);
			}
		}

		return memoized[i1][i2][i3];
	}

	// result[i2][i1] is the smallest index ii1 inside s1, such that
	// (s2.charAt(i2) == s1.charAt(i1)) and (ii1 > i1)
	static final void precalculateEqualCharsPositions(String s1, String s2, int[][] s2s1) {
		int len1 = s1.length();
		int len2 = s2.length();

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
	}
}
