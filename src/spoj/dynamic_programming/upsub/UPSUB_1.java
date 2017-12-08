package spoj.dynamic_programming.upsub;

import java.util.Arrays;

// http://www.spoj.com/problems/UPSUB/

public class UPSUB_1 {

	public static void main(String[] args) {
		findLongestSubsequence("abcbcbcd");
		findLongestSubsequence("dcbab");
		findLongestSubsequence("dcbax");
		findLongestSubsequence("zyxdcba");
		findLongestSubsequence("abcdabc");
	}

	static final int NEGATIVE_INFINITY = -100000;
	static final int NOT_INITIALIZED = 2 * NEGATIVE_INFINITY;

	static void findLongestSubsequence(String s) {
		int[][] memoized = new int[s.length() + 1][s.length() + 1];
		for (int[] row : memoized) {
			Arrays.fill(row, NOT_INITIALIZED);
		}
		char[] arr = s.toCharArray();
		findLongestSubsequence(arr, 0, -1, memoized);
		backtrack(arr, 0, -1, memoized, new StringBuilder());
		System.out.println();
	}

	static int findLongestSubsequence(char[] arr, int currIdx, int prevIdx, int[][] memoized) {

		if (memoized[prevIdx + 1][currIdx] != NOT_INITIALIZED) {
			return memoized[prevIdx + 1][currIdx];
		}

		if (currIdx == arr.length) {
			memoized[prevIdx + 1][currIdx] = 0;
			return memoized[prevIdx + 1][currIdx];
		}

		// excluding current character
		int subProblem1 = findLongestSubsequence(arr, currIdx + 1, prevIdx, memoized);

		// including current character
		int subProblem2 = ((prevIdx == -1) || (arr[currIdx] >= arr[prevIdx]))
				? findLongestSubsequence(arr, currIdx + 1, currIdx, memoized) + 1
				: NEGATIVE_INFINITY;

		memoized[prevIdx + 1][currIdx] = Math.max(subProblem1, subProblem2);
		return memoized[prevIdx + 1][currIdx];
	}

	static void backtrack(char[] arr, int currIdx, int prevIdx, int[][] memoized, StringBuilder stack) {
		if (currIdx == arr.length) {
			System.out.println(stack.toString());
			return;
		}

		// excluding current character
		if (memoized[prevIdx + 1][currIdx] == memoized[prevIdx + 1][currIdx + 1]) {
			backtrack(arr, currIdx + 1, prevIdx, memoized, stack);
		}

		// including current character
		if (memoized[prevIdx + 1][currIdx] == (memoized[currIdx + 1][currIdx + 1] + 1)) {
			stack.append(arr[currIdx]);
			backtrack(arr, currIdx + 1, currIdx, memoized, stack);
			stack.setLength(stack.length() - 1);
		}
	}
}
