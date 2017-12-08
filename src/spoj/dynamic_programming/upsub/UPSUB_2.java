package spoj.dynamic_programming.upsub;


// http://www.spoj.com/problems/UPSUB/

public class UPSUB_2 {

	public static void main(String[] args) {
		findLongestSubsequence("abcbcbcd");
		findLongestSubsequence("dcbab");
		findLongestSubsequence("dcbax");
		findLongestSubsequence("zyxdcba");
		findLongestSubsequence("abcdabc");
	}

	static final int NEGATIVE_INFINITY = -100000;

	static void findLongestSubsequence(String s) {
		int[][] memoized = new int[s.length() + 1][s.length() + 1];
		char[] arr = s.toCharArray();
		findLongestSubsequence(arr, memoized);
		backtrack(arr, 0, -1, memoized, new StringBuilder());
		System.out.println();
	}

	static void findLongestSubsequence(char[] arr, int[][] memoized) {

		for (int currIdx = arr.length; currIdx >= 0; currIdx--) {
			for (int prevIdx = arr.length - 1; prevIdx >= -1; prevIdx--) {

				if (currIdx == arr.length) {
					memoized[prevIdx + 1][currIdx] = 0;

				} else {

					// excluding current character
					int subProblem1 = memoized[prevIdx + 1][currIdx + 1];

					// including current character
					int subProblem2 = ((prevIdx == -1) || (arr[currIdx] >= arr[prevIdx]))
							? memoized[currIdx + 1][currIdx + 1] + 1
							: NEGATIVE_INFINITY;

					memoized[prevIdx + 1][currIdx] = Math.max(subProblem1, subProblem2);
				}
			}
		}
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
