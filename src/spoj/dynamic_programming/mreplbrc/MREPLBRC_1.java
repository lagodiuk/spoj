package spoj.dynamic_programming.mreplbrc;

// http://www.spoj.com/problems/MREPLBRC/

public class MREPLBRC_1 {

	public static void main(String[] args) {
		MREPLBRC_Brute_Force.testManyCases(MREPLBRC_1::solve);
	}

	static long solve(String s) {
		return f(s, 0, s.length() - 1);
	}

	// Top-down solution
	// TODO: add memoization
	static long f(String s, int i, int j) {
		if (i > j) {
			return 1;
		}

		long result = g(s, i, j);

		for (int k = i + 1; k < j; k += 2) {
			result += g(s, i, k) * f(s, k + 1, j);
		}

		return result;
	}

	static long g(String s, int i, int j) {

		if (((s.charAt(i) == '(') && (s.charAt(j) == ')'))
				|| ((s.charAt(i) == '[') && (s.charAt(j) == ']'))
				|| ((s.charAt(i) == '{') && (s.charAt(j) == '}'))
				|| ((s.charAt(i) == '?') && (s.charAt(j) == ')'))
				|| ((s.charAt(i) == '?') && (s.charAt(j) == ']'))
				|| ((s.charAt(i) == '?') && (s.charAt(j) == '}'))
				|| ((s.charAt(i) == '(') && (s.charAt(j) == '?'))
				|| ((s.charAt(i) == '[') && (s.charAt(j) == '?'))
				|| ((s.charAt(i) == '{') && (s.charAt(j) == '?'))) {

			return 1 * f(s, i + 1, j - 1);
		}

		if ((s.charAt(i) == '?') && (s.charAt(j) == '?')) {
			return 3 * f(s, i + 1, j - 1);
		}

		return 0;
	}
}
