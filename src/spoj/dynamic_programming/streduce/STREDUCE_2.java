package spoj.dynamic_programming.streduce;

// http://www.spoj.com/problems/STREDUCE/

public class STREDUCE_2 {

	public static void main(String[] args) {
		String s = "baaba";
		// String s = "bbababb";
		// String s =
		// "abbabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaababba";
		System.out.println(s.length());
		buildParseTable(s);
		solve(s);
		System.out.println(parseTable[0][s.length() - 1][0] || parseTable[0][s.length() - 1][1]);
		System.out.println(memoised[0][s.length() - 1]);
	}

	static boolean[][][] parseTable;

	static void buildParseTable(String s) {
		parseTable = new boolean[s.length()][s.length()][2];

		for (int l = s.length() - 1; l >= 0; l--) {

			parseTable[l][l][s.charAt(l) - 'a'] = true;

			for (int r = l + 1; r <= (s.length() - 1); r++) {

				if ((s.charAt(l) == s.charAt(r))
						&& (((r - l) % 2) == 0)
						&& (parseTable[l + 1][r - 1][0] || parseTable[l + 1][r - 1][1])) {

					// a proper subtree with a root in the middle
					int c = s.charAt((r + l) / 2) - 'a';
					parseTable[l][r][c] = true;
				}

				// otherwise - root in the middle
				// however, left and right subtrees are present too
				for (int x = l + 1; x <= (r - 1); x++) {

					if ((parseTable[l][x - 1][0] && parseTable[x + 1][r][0])
							|| (parseTable[l][x - 1][1] && parseTable[x + 1][r][1])) {

						int c = s.charAt(x) - 'a';
						parseTable[l][r][c] = true;
					}

					if (parseTable[l][r][0] && parseTable[l][r][1]) {
						break;
					}
				}
			}
		}
	}

	static int[][] memoised;
	static final int INFINITY = 10000000;

	static void solve(String s) {
		memoised = new int[s.length()][s.length()];

		for (int l = s.length() - 1; l >= 0; l--) {
			for (int r = l; r <= (s.length() - 1); r++) {

				if (parseTable[l][r][0] || parseTable[l][r][1]) {

					memoised[l][r] = 1;

				} else {

					memoised[l][r] = INFINITY;

					for (int x = l; x <= (r - 1); x++) {
						memoised[l][r] = Math.min(
								memoised[l][r],
								memoised[l][x] + memoised[x + 1][r]);
					}
				}
			}
		}
	}
}
