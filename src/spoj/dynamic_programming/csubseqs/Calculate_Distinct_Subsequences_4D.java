package spoj.dynamic_programming.csubseqs;

// http://www.spoj.com/problems/CSUBSEQS/

import java.util.Arrays;

public class Calculate_Distinct_Subsequences_4D {

	public static void main(String[] args) {
		Test.test_4D(Calculate_Distinct_Subsequences_4D::countDistinctSubsequences_4D);
		Test.randomizedTest(4, Calculate_Distinct_Subsequences_4D::countDistinctSubsequences_4D);
	}

	static int countDistinctSubsequences_4D(String... s) {
		return countDistinctSubsequences_4D(s[0], s[1], s[2], s[3]);
	}

	static int countDistinctSubsequences_4D(String s1, String s2, String s3, String s4) {
		int[][][][] mem = new int[s1.length()][s2.length()][s3.length()][s4.length()];
		for (int[][][] slice3d : mem) {
			for (int[][] slice2d : slice3d) {
				for (int[] slice1d : slice2d) {
					Arrays.fill(slice1d, -1);
				}
			}
		}
		return countDistinctSubsequences4(s1, s2, s3, s4, s1.length() - 1, s2.length() - 1, s3.length() - 1, s4.length() - 1, mem) - 1;
	}

	static int countDistinctSubsequences4(String s1, String s2, String s3, String s4, int i1, int i2, int i3, int i4, int[][][][] mem) {

		if ((i1 < 0) || (i2 < 0) || (i3 < 0) || (i4 < 0)) {
			return 1;
		}

		if (mem[i1][i2][i3][i4] != -1) {
			return mem[i1][i2][i3][i4];
		}

		if ((s1.charAt(i1) == s2.charAt(i2))
				&& (s1.charAt(i1) == s3.charAt(i3))
				&& (s1.charAt(i1) == s4.charAt(i4))) {

			int res = countDistinctSubsequences4(s1, s2, s3, s4, i1 - 1, i2 - 1, i3 - 1, i4 - 1, mem) * 2;

			int ii1 = findPrevOccurence(s1, i1);
			int ii2 = findPrevOccurence(s2, i2);
			int ii3 = findPrevOccurence(s3, i3);
			int ii4 = findPrevOccurence(s4, i4);
			if ((ii1 >= 0) && (ii2 >= 0) && (ii3 >= 0) && (ii4 >= 0)) {
				res -= countDistinctSubsequences4(s1, s2, s3, s4, ii1 - 1, ii2 - 1, ii3 - 1, ii4 - 1, mem);
			}

			mem[i1][i2][i3][i4] = res;
			return res;
		}

		// Using "Inclusion-Exclusion" principle

		int res = 0;

		// Include states with odd amount of "-1"
		res += countDistinctSubsequences4(s1, s2, s3, s4, i1 - 1, i2, i3, i4, mem);
		res += countDistinctSubsequences4(s1, s2, s3, s4, i1, i2 - 1, i3, i4, mem);
		res += countDistinctSubsequences4(s1, s2, s3, s4, i1, i2, i3 - 1, i4, mem);
		res += countDistinctSubsequences4(s1, s2, s3, s4, i1, i2, i3, i4 - 1, mem);

		// Exclude states with even amount of "-1"
		res -= countDistinctSubsequences4(s1, s2, s3, s4, i1 - 1, i2 - 1, i3, i4, mem);
		res -= countDistinctSubsequences4(s1, s2, s3, s4, i1 - 1, i2, i3 - 1, i4, mem);
		res -= countDistinctSubsequences4(s1, s2, s3, s4, i1 - 1, i2, i3, i4 - 1, mem);
		res -= countDistinctSubsequences4(s1, s2, s3, s4, i1, i2 - 1, i3 - 1, i4, mem);
		res -= countDistinctSubsequences4(s1, s2, s3, s4, i1, i2 - 1, i3, i4 - 1, mem);
		res -= countDistinctSubsequences4(s1, s2, s3, s4, i1, i2, i3 - 1, i4 - 1, mem);

		// Include states with odd amount of "-1"
		res += countDistinctSubsequences4(s1, s2, s3, s4, i1 - 1, i2 - 1, i3 - 1, i4, mem);
		res += countDistinctSubsequences4(s1, s2, s3, s4, i1 - 1, i2 - 1, i3, i4 - 1, mem);
		res += countDistinctSubsequences4(s1, s2, s3, s4, i1 - 1, i2, i3 - 1, i4 - 1, mem);
		res += countDistinctSubsequences4(s1, s2, s3, s4, i1, i2 - 1, i3 - 1, i4 - 1, mem);

		// Exclude states with even amount of "-1"
		res -= countDistinctSubsequences4(s1, s2, s3, s4, i1 - 1, i2 - 1, i3 - 1, i4 - 1, mem);

		mem[i1][i2][i3][i4] = res;
		return res;
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
