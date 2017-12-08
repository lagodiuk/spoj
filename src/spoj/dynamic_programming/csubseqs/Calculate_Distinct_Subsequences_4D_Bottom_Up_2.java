package spoj.dynamic_programming.csubseqs;

// http://www.spoj.com/problems/CSUBSEQS/

public class Calculate_Distinct_Subsequences_4D_Bottom_Up_2 {

	public static void main(String[] args) {
		Test.test_4D(Calculate_Distinct_Subsequences_4D_Bottom_Up_2::countDistinctSubsequences_4D);
		Test.randomizedTest(4, Calculate_Distinct_Subsequences_4D_Bottom_Up_2::countDistinctSubsequences_4D);
		Test.randomizedTest(4, Calculate_Distinct_Subsequences_4D_Bottom_Up_2::countDistinctSubsequences_4D, false, 50);
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

		for (int i1 = 1; i1 <= s1.length(); i1++) {
			for (int i2 = 1; i2 <= s2.length(); i2++) {
				for (int i3 = 1; i3 <= s3.length(); i3++) {
					for (int i4 = 1; i4 <= s4.length(); i4++) {

						if ((s1.charAt(i1 - 1) == s2.charAt(i2 - 1))
								&& (s1.charAt(i1 - 1) == s3.charAt(i3 - 1))
								&& (s1.charAt(i1 - 1) == s4.charAt(i4 - 1))) {

							mem[i1][i2][i3][i4] = 2 * mem[i1 - 1][i2 - 1][i3 - 1][i4 - 1];

							int ii1 = findPrevOccurence(s1, i1 - 1);
							int ii2 = findPrevOccurence(s2, i2 - 1);
							int ii3 = findPrevOccurence(s3, i3 - 1);
							int ii4 = findPrevOccurence(s4, i4 - 1);
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
					}
				}
			}
		}

		return mem[s1.length()][s2.length()][s3.length()][s4.length()] - 1;
	}

	private static int findPrevOccurence(String s, int i) {
		int prevOccurence = i - 1;
		while (prevOccurence >= 0) {
			if (s.charAt(prevOccurence) == s.charAt(i)) {
				break;
			} else {
				prevOccurence--;
			}
		}
		return prevOccurence;
	}
}
