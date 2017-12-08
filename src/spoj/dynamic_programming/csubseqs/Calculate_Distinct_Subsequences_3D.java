package spoj.dynamic_programming.csubseqs;

// http://www.spoj.com/problems/CSUBSEQS/

import java.util.Arrays;

public class Calculate_Distinct_Subsequences_3D {

	public static void main(String[] args) {
		Test.test_3D(Calculate_Distinct_Subsequences_3D::countDistinctSubsequences_3D);
		Test.randomizedTest(3, Calculate_Distinct_Subsequences_3D::countDistinctSubsequences_3D);
	}

	static int countDistinctSubsequences_3D(String... s) {
		return countDistinctSubsequences_3D(s[0], s[1], s[2]);
	}

	static int countDistinctSubsequences_3D(String s1, String s2, String s3) {
		int[][][] mem = new int[s1.length()][s2.length()][s3.length()];
		for (int[][] slice : mem) {
			for (int[] row : slice) {
				Arrays.fill(row, -1);
			}
		}
		return countDistinctSubsequences3(s1, s2, s3, s1.length() - 1, s2.length() - 1, s3.length() - 1, mem) - 1;
	}

	static int countDistinctSubsequences3(String s1, String s2, String s3, int i1, int i2, int i3, int[][][] mem) {

		if ((i1 < 0) || (i2 < 0) || (i3 < 0)) {
			return 1;
		}

		if (mem[i1][i2][i3] != -1) {
			return mem[i1][i2][i3];
		}

		if ((s1.charAt(i1) == s2.charAt(i2))
				&& (s1.charAt(i1) == s3.charAt(i3))) {

			int res = countDistinctSubsequences3(s1, s2, s3, i1 - 1, i2 - 1, i3 - 1, mem) * 2;

			int ii1 = findPrevOccurence(s1, i1);
			int ii2 = findPrevOccurence(s2, i2);
			int ii3 = findPrevOccurence(s3, i3);
			if ((ii1 >= 0) && (ii2 >= 0) && (ii3 >= 0)) {
				res -= countDistinctSubsequences3(s1, s2, s3, ii1 - 1, ii2 - 1, ii3 - 1, mem);
			}

			mem[i1][i2][i3] = res;
			return res;
		}

		// Using "Inclusion-Exclusion" principle

		int res = 0;

		// Include states with odd amount of "-1"
		res += countDistinctSubsequences3(s1, s2, s3, i1 - 1, i2, i3, mem);
		res += countDistinctSubsequences3(s1, s2, s3, i1, i2 - 1, i3, mem);
		res += countDistinctSubsequences3(s1, s2, s3, i1, i2, i3 - 1, mem);

		// Exclude states with even amount of "-1"
		res -= countDistinctSubsequences3(s1, s2, s3, i1 - 1, i2 - 1, i3, mem);
		res -= countDistinctSubsequences3(s1, s2, s3, i1 - 1, i2, i3 - 1, mem);
		res -= countDistinctSubsequences3(s1, s2, s3, i1, i2 - 1, i3 - 1, mem);

		// Include states with odd amount of "-1"
		res += countDistinctSubsequences3(s1, s2, s3, i1 - 1, i2 - 1, i3 - 1, mem);

		mem[i1][i2][i3] = res;
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
