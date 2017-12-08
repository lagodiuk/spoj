package spoj.dynamic_programming.csubseqs;

import java.util.Arrays;

// http://www.spoj.com/problems/CSUBSEQS/

public class Calculate_Distinct_Subsequences_4D_Bottom_Up_3 {

	public static void main(String[] args) {
		Test.test_4D(Calculate_Distinct_Subsequences_4D_Bottom_Up_3::countDistinctSubsequences_4D);
		Test.randomizedTest(4, Calculate_Distinct_Subsequences_4D_Bottom_Up_3::countDistinctSubsequences_4D);
		Test.randomizedTest(4, Calculate_Distinct_Subsequences_4D_Bottom_Up_3::countDistinctSubsequences_4D, false, 50);
	}

	static int countDistinctSubsequences_4D(String... s) {
		return countDistinctSubsequences_4D(s[0], s[1], s[2], s[3]);
	}

	static int countDistinctSubsequences_4D(String s1, String s2, String s3, String s4) {
		int[][][][] mem = new int[s1.length() + 1][s2.length() + 1][s3.length() + 1][s4.length() + 1];

		for (int i1 = 0; i1 <= s1.length(); i1++) {
			for (int i2 = 0; i2 <= s2.length(); i2++) {
				for (int i3 = 0; i3 <= s3.length(); i3++) {
					mem[i1][i2][i3][0] = 1;
				}
			}
		}

		for (int i1 = 0; i1 <= s1.length(); i1++) {
			for (int i2 = 0; i2 <= s2.length(); i2++) {
				for (int i4 = 0; i4 <= s4.length(); i4++) {
					mem[i1][i2][0][i4] = 1;
				}
			}
		}

		for (int i1 = 0; i1 <= s1.length(); i1++) {
			for (int i3 = 0; i3 <= s3.length(); i3++) {
				for (int i4 = 0; i4 <= s4.length(); i4++) {
					mem[i1][0][i3][i4] = 1;
				}
			}
		}

		for (int i2 = 0; i2 <= s2.length(); i2++) {
			for (int i3 = 0; i3 <= s3.length(); i3++) {
				for (int i4 = 0; i4 <= s4.length(); i4++) {
					mem[0][i2][i3][i4] = 1;
				}
			}
		}

		int[] prev1 = new int[('z' - 'a') + 1];
		int[] prev2 = new int[('z' - 'a') + 1];
		int[] prev3 = new int[('z' - 'a') + 1];
		int[] prev4 = new int[('z' - 'a') + 1];

		Arrays.fill(prev1, -1);
		for (int i1 = 1; i1 <= s1.length(); i1++) {
			Arrays.fill(prev2, -1);
			for (int i2 = 1; i2 <= s2.length(); i2++) {
				Arrays.fill(prev3, -1);
				for (int i3 = 1; i3 <= s3.length(); i3++) {
					Arrays.fill(prev4, -1);
					for (int i4 = 1; i4 <= s4.length(); i4++) {

						if ((s1.charAt(i1 - 1) == s2.charAt(i2 - 1))
								&& (s1.charAt(i1 - 1) == s3.charAt(i3 - 1))
								&& (s1.charAt(i1 - 1) == s4.charAt(i4 - 1))) {

							mem[i1][i2][i3][i4] = 2 * mem[i1 - 1][i2 - 1][i3 - 1][i4 - 1];

							int ii1 = prev1[s1.charAt(i1 - 1) - 'a'];
							int ii2 = prev2[s2.charAt(i2 - 1) - 'a'];
							int ii3 = prev3[s3.charAt(i3 - 1) - 'a'];
							int ii4 = prev4[s4.charAt(i4 - 1) - 'a'];

							if ((ii1 >= 0) && (ii2 >= 0) && (ii3 >= 0) && (ii4 >= 0)) {
								mem[i1][i2][i3][i4] -= mem[ii1][ii2][ii3][ii4];
							}

						} else {

							mem[i1][i2][i3][i4] += mem[i1 - 1][i2][i3][i4];
							mem[i1][i2][i3][i4] += mem[i1][i2 - 1][i3][i4];
							mem[i1][i2][i3][i4] += mem[i1][i2][i3 - 1][i4];
							mem[i1][i2][i3][i4] += mem[i1][i2][i3][i4 - 1];

							mem[i1][i2][i3][i4] -= mem[i1 - 1][i2 - 1][i3][i4];
							mem[i1][i2][i3][i4] -= mem[i1 - 1][i2][i3 - 1][i4];
							mem[i1][i2][i3][i4] -= mem[i1 - 1][i2][i3][i4 - 1];
							mem[i1][i2][i3][i4] -= mem[i1][i2 - 1][i3 - 1][i4];
							mem[i1][i2][i3][i4] -= mem[i1][i2 - 1][i3][i4 - 1];
							mem[i1][i2][i3][i4] -= mem[i1][i2][i3 - 1][i4 - 1];

							mem[i1][i2][i3][i4] += mem[i1 - 1][i2 - 1][i3 - 1][i4];
							mem[i1][i2][i3][i4] += mem[i1 - 1][i2 - 1][i3][i4 - 1];
							mem[i1][i2][i3][i4] += mem[i1 - 1][i2][i3 - 1][i4 - 1];
							mem[i1][i2][i3][i4] += mem[i1][i2 - 1][i3 - 1][i4 - 1];

							mem[i1][i2][i3][i4] -= mem[i1 - 1][i2 - 1][i3 - 1][i4 - 1];
						}

						prev4[s4.charAt(i4 - 1) - 'a'] = i4 - 1;
					}
					prev3[s3.charAt(i3 - 1) - 'a'] = i3 - 1;
				}
				prev2[s2.charAt(i2 - 1) - 'a'] = i2 - 1;
			}
			prev1[s1.charAt(i1 - 1) - 'a'] = i1 - 1;
		}

		return mem[s1.length()][s2.length()][s3.length()][s4.length()] - 1;
	}
}
