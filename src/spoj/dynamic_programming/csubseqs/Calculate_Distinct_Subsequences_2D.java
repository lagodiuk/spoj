package spoj.dynamic_programming.csubseqs;

// http://www.spoj.com/problems/CSUBSEQS/

public class Calculate_Distinct_Subsequences_2D {

	public static void main(String[] args) {
		Test.test_2D(Calculate_Distinct_Subsequences_2D::countDistinctSubsequences_2D);
		Test.randomizedTest(2, Calculate_Distinct_Subsequences_2D::countDistinctSubsequences_2D);
	}

	static int countDistinctSubsequences_2D(String... s) {
		return countDistinctSubsequences_2D(s[0], s[1]);
	}

	static int countDistinctSubsequences_2D(String s1, String s2) {
		return countDistinctSubsequences2(s1, s2, s1.length() - 1, s2.length() - 1) - 1;
	}

	static int countDistinctSubsequences2(String s1, String s2, int i1, int i2) {

		if ((i1 < 0) || (i2 < 0)) {
			return 1;
		}

		if (s1.charAt(i1) == s2.charAt(i2)) {
			int cnt = countDistinctSubsequences2(s1, s2, i1 - 1, i2 - 1) * 2;

			int ii1 = findPrevOccurence(s1, i1);
			int ii2 = findPrevOccurence(s2, i2);
			if ((ii1 >= 0) && (ii2 >= 0)) {
				cnt -= countDistinctSubsequences2(s1, s2, ii1 - 1, ii2 - 1);
			}

			return cnt;
		}

		// Using "Inclusion-Exclusion" principle
		int res = 0;

		// Include states with odd amount of "-1"
		res += countDistinctSubsequences2(s1, s2, i1 - 1, i2);
		res += countDistinctSubsequences2(s1, s2, i1, i2 - 1);

		// Exclude states with even amount of "-1"
		res -= countDistinctSubsequences2(s1, s2, i1 - 1, i2 - 1);

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
