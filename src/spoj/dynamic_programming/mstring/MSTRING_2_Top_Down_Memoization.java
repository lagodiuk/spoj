package spoj.dynamic_programming.mstring;

import java.util.Arrays;

// http://www.spoj.com/problems/MSTRING/en/

public class MSTRING_2_Top_Down_Memoization {

	public static void main(String[] args) {
		MSTRING_Brute_force.testFewManualCases(MSTRING_2_Top_Down_Memoization::solve);
		MSTRING_Brute_force.testRandomlyGeneratedCases(MSTRING_2_Top_Down_Memoization::solve);
	}

	static final int INF = 100000;

	static int solve(String sourceStr, String targetStr) {
		int[][] nextLetterPos = getNextLetterPositionsMapping(targetStr);

		int[][] memoized = new int[sourceStr.length()][targetStr.length() + 1];
		for (int[] row : memoized) {
			Arrays.fill(row, -1);
		}

		int result = solve(sourceStr, 0, 0, nextLetterPos, memoized);
		return result;
	}

	static int solve(String sourceStr, int srcPos, int tgtPos, int[][] nextLetterPos, int[][] memoized) {
		if (tgtPos == INF) {
			return 0;
		}

		if (srcPos == sourceStr.length()) {
			return INF;
		}

		if (memoized[srcPos][tgtPos] != -1) {
			return memoized[srcPos][tgtPos];
		}

		memoized[srcPos][tgtPos] = Math.min(solve(sourceStr, srcPos + 1, tgtPos, nextLetterPos, memoized),
				solve(sourceStr, srcPos + 1, nextLetterPos[tgtPos][sourceStr.charAt(srcPos) - 'a'], nextLetterPos, memoized) + 1);

		return memoized[srcPos][tgtPos];
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
