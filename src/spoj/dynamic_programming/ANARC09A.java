package spoj.dynamic_programming;

import java.util.Arrays;
import java.util.Random;

// http://www.spoj.com/problems/ANARC09A/

public class ANARC09A {

	private static final int INFINITY = 10000;

	public static void main(String[] args) {
		System.out.println(solveRecursive(")(") + "\t" + solveNonRecursive(")("));
		System.out.println(solveRecursive("()()()") + "\t" + solveNonRecursive("()()()"));
		System.out.println(solveRecursive("((()") + "\t" + solveNonRecursive("((()"));
		System.out.println(solveRecursive("(()()()())") + "\t" + solveNonRecursive("(()()()())"));
		System.out.println(solveRecursive("))(()(") + "\t" + solveNonRecursive("))(()("));
		System.out.println(solveRecursive(")())") + "\t" + solveNonRecursive(")())"));
		System.out.println(solveRecursive("((((((((") + "\t" + solveNonRecursive("(((((((("));
		System.out.println(solveRecursive("))))))))") + "\t" + solveNonRecursive("))))))))"));
		System.out.println(solveRecursive(")))))))(") + "\t" + solveNonRecursive(")))))))("));
		System.out.println(solveRecursive("))))))()") + "\t" + solveNonRecursive("))))))()"));

		StringBuilder sb = new StringBuilder();
		Random r = new Random(101);
		for (int i = 0; i < 2000; i++) {
			if (r.nextDouble() > 0.5) {
				sb.append('(');
			} else {
				sb.append(')');
			}
		}
		System.out.println(solveRecursive(sb.toString()) + "\t" + solveNonRecursive(sb.toString()));
	}

	// Recursive solution with memoization
	static int solveRecursive(String s) {
		int[][] memoized = new int[s.length()][s.length()];
		for (int i = 0; i < s.length(); i++) {
			Arrays.fill(memoized[i], -1);
		}

		int result = solve(s, 0, 0, memoized);

		return result;
	}

	static int solve(String s, int index, int stackSize, int[][] memoized) {

		if (stackSize > (s.length() - index)) {
			return INFINITY;
		}

		if (index == s.length()) {
			return 0;
		}

		if (memoized[index][stackSize] != -1) {
			return memoized[index][stackSize];
		}

		int result = 0;

		if (stackSize == 0) {

			if (s.charAt(index) == '(') {
				result = solve(s, index + 1, 1, memoized);

			} else {
				result = solve(s, index + 1, 1, memoized) + 1;
			}

		} else {
			if (s.charAt(index) == '(') {

				result = Math.min(
						solve(s, index + 1, stackSize + 1, memoized),
						solve(s, index + 1, stackSize - 1, memoized) + 1);

			} else {
				result = Math.min(
						solve(s, index + 1, stackSize + 1, memoized) + 1,
						solve(s, index + 1, stackSize - 1, memoized));
			}
		}

		memoized[index][stackSize] = result;

		return result;
	}

	static int solveNonRecursive(String s) {
		int[][] memoized = new int[s.length() + 1][s.length() + 1];

		for (int idx = 1; idx <= s.length(); idx++) {
			for (int stackSize = idx; stackSize >= 0; stackSize -= 2) {
				if (s.charAt(idx - 1) == '(') {
					if (stackSize == 0) {
						memoized[idx][stackSize] = memoized[idx - 1][stackSize + 1] + 1;
					} else {
						memoized[idx][stackSize] = memoized[idx - 1][stackSize - 1];
						if (((stackSize + 1) <= (idx - 1))
								&& ((memoized[idx - 1][stackSize + 1] + 1) < memoized[idx - 1][stackSize - 1])) {
							memoized[idx][stackSize] = memoized[idx - 1][stackSize + 1] + 1;
						}
					}
				} else {
					if (stackSize == 0) {
						memoized[idx][stackSize] = memoized[idx - 1][stackSize + 1];
					} else {
						memoized[idx][stackSize] = memoized[idx - 1][stackSize - 1] + 1;
						if (((stackSize + 1) <= (idx - 1))
								&& (memoized[idx - 1][stackSize + 1] < (memoized[idx - 1][stackSize - 1] + 1))) {
							memoized[idx][stackSize] = memoized[idx - 1][stackSize + 1];
						}
					}
				}
			}
		}

		return memoized[s.length()][0];
	}
}
