package spoj.dynamic_programming.mreplbrc;

// http://www.spoj.com/problems/MREPLBRC/

public class MREPLBRC_3 {

	public static void main(String[] args) {
		MREPLBRC_Brute_Force.testManyCases(MREPLBRC_3::solve);
	}

	// Bottom-up solution
	static long solve(String s) {
		long[][] f = new long[s.length()][s.length()];

		for (int i = s.length() - 2; i >= 0; i--) {
			for (int j = i + 1; j < s.length(); j++) {
				f[i][j] = 0;
				for (int k = i + 1; k <= j; k += 2) {
					long left = ((i + 1) > (k - 1)) ? 1 : f[i + 1][k - 1];
					long right = ((k + 1) > j) ? 1 : f[k + 1][j];
					f[i][j] += coeff(s, i, k) * left * right;
				}
			}
		}

		return f[0][s.length() - 1];
	}

	static long coeff(String s, int i, int j) {

		if ((s.charAt(i) == '?') && (s.charAt(j) == '?')) {
			return 3;
		}

		if (((s.charAt(i) == '(') && (s.charAt(j) == ')'))
				|| ((s.charAt(i) == '[') && (s.charAt(j) == ']'))
				|| ((s.charAt(i) == '{') && (s.charAt(j) == '}'))
				|| ((s.charAt(i) == '?') && (s.charAt(j) == ')'))
				|| ((s.charAt(i) == '?') && (s.charAt(j) == ']'))
				|| ((s.charAt(i) == '?') && (s.charAt(j) == '}'))
				|| ((s.charAt(i) == '(') && (s.charAt(j) == '?'))
				|| ((s.charAt(i) == '[') && (s.charAt(j) == '?'))
				|| ((s.charAt(i) == '{') && (s.charAt(j) == '?'))) {

			return 1;
		}

		return 0;
	}
}
