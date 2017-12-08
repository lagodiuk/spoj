package spoj.dynamic_programming.streduce;

// http://www.spoj.com/problems/STREDUCE/

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class STREDUCE_Submitted {

	public static void main(String[] args) throws Exception {
		String s = new BufferedReader(new InputStreamReader(System.in)).readLine();
		buildParseTable(s);
		solve(s);
		System.out.println(memoized[0][s.length() - 1]);
	}

	static final boolean[][][] parseTable = new boolean[305][305][2];
	static final int[][] memoized = new int[305][305];
	static final int INFINITY = 10000000;

	static void buildParseTable(String s) {

		for (int l = s.length() - 1; l >= 0; l--) {

			parseTable[l][l][s.charAt(l) - 'a'] = true;

			for (int r = l + 1; r <= (s.length() - 1); r++) {

				if ((s.charAt(l) == s.charAt(r))
						&& (((r - l) % 2) == 0)
						&& (parseTable[l + 1][r - 1][0] || parseTable[l + 1][r - 1][1])) {

					// a proper subtree with a root in the middle
					parseTable[l][r][s.charAt((r + l) / 2) - 'a'] = true;
				}

				// otherwise - root in the middle
				// however, left and right subtrees are present too
				for (int x = l + 1; (x <= (r - 1)) && !(parseTable[l][r][0] && parseTable[l][r][1]); x++) {

					parseTable[l][r][s.charAt(x) - 'a'] =
							((parseTable[l][x - 1][0] && parseTable[x + 1][r][0])
							|| (parseTable[l][x - 1][1] && parseTable[x + 1][r][1]))
									? true
									: parseTable[l][r][s.charAt(x) - 'a'];
				}
			}
		}
	}

	static void solve(String s) {

		for (int l = s.length() - 1; l >= 0; l--) {
			for (int r = l; r <= (s.length() - 1); r++) {

				if (parseTable[l][r][0] || parseTable[l][r][1]) {

					memoized[l][r] = 1;

				} else {

					memoized[l][r] = INFINITY;

					for (int x = l; x <= (r - 1); x++) {
						memoized[l][r] = Math.min(
								memoized[l][r],
								memoized[l][x] + memoized[x + 1][r]);
					}
				}
			}
		}
	}
}
