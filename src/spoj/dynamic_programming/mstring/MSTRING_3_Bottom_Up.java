package spoj.dynamic_programming.mstring;

import java.util.Arrays;

// http://www.spoj.com/problems/MSTRING/en/

public class MSTRING_3_Bottom_Up {

	public static void main(String[] args) {
		MSTRING_Brute_force.testFewManualCases(MSTRING_3_Bottom_Up::solve);
		MSTRING_Brute_force.testRandomlyGeneratedCases(MSTRING_3_Bottom_Up::solve);
	}

	static final int INF = 100000;

	// Runtime complexity: O(N * M)
	// Memory consumption: O(N * M)
	// Where:
	// N - length of sourceStr
	// M - length of targetStr
	static int solve(String sourceStr, String targetStr) {
		int[][] nextLetterPos = getNextLetterPositionsMapping(targetStr);

		int[][] memoized = new int[sourceStr.length()][targetStr.length() + 1];
		for (int[] row : memoized) {
			Arrays.fill(row, -1);
		}

		for (int srcPos = sourceStr.length() - 1; srcPos >= 0; srcPos--) {
			for (int tgtPos = targetStr.length(); tgtPos >= 0; tgtPos--) {

				int excludingCurrLetter = ((srcPos + 1) == sourceStr.length())
						? INF
						: memoized[srcPos + 1][tgtPos];

				int includingCurrLetter = ((nextLetterPos[tgtPos][sourceStr.charAt(srcPos) - 'a'] == INF)
						? 0
						: ((srcPos + 1) == sourceStr.length())
								? INF
								: memoized[srcPos + 1][nextLetterPos[tgtPos][sourceStr.charAt(srcPos) - 'a']])
						+ 1;

				memoized[srcPos][tgtPos] = Math.min(excludingCurrLetter, includingCurrLetter);
			}
		}

		int result = memoized[0][0];
		return result;
	}

	private static int[][] getNextLetterPositionsMapping(String targetStr) {
		int[][] nextLetterPos = new int[targetStr.length() + 1][26];
		for (int i = 0; i < 26; i++) {
			nextLetterPos[targetStr.length()][i] = INF;
		}
		for (int i = targetStr.length() - 1; i >= 0; i--) {
			for (int j = 0; j < 26; j++) {
				if (targetStr.charAt(i) == (j + 'a')) {
					nextLetterPos[i][j] = i + 1;
				} else {
					nextLetterPos[i][j] = nextLetterPos[i + 1][j];
				}
			}
		}
		return nextLetterPos;
	}
}
