package spoj.dynamic_programming.csubseqs;

// http://www.spoj.com/problems/CSUBSEQS/

public class Calculate_Distinct_Subsequences_1D {

	public static void main(String[] args) {
		Test.randomizedTest(1, Calculate_Distinct_Subsequences_1D::countDistinctSubsequences_1D);
	}

	static int countDistinctSubsequences_1D(String... s) {
		return countDistinctSubsequences_1D(s[0]);
	}

	static int countDistinctSubsequences_1D(String s) {

		int[] mem = new int[s.length()];

		for (int i = 0; i < s.length(); i++) {
			mem[i] = 2 * ((i > 0) ? mem[i - 1] : 1);

			int prevOccurence = findPrevOccurence(s, i);
			if (prevOccurence >= 0) {
				mem[i] -= (prevOccurence > 0) ? mem[prevOccurence - 1] : 1;
			}
		}

		return mem[s.length() - 1] - 1;
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
