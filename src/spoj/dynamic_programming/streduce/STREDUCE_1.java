package spoj.dynamic_programming.streduce;

// http://www.spoj.com/problems/STREDUCE/

public class STREDUCE_1 {

	public static void main(String[] args) {
		System.out.println(solve("baaba"));
		System.out.println(solve("baababaaba"));
		System.out.println(solve("baababbaaba"));
		System.out.println(solve("aababaa"));
		System.out.println(solve("babab"));
		System.out.println(solve("baababaababbaba"));
	}

	static int solve(String s) {
		int result = solve(s, 0, s.length() - 1);
		return result;
	}

	static final int INFINITY = 10000000;

	static int solve(String s, int left, int right) {

		if (parse(s, left, right, 'a')
				|| parse(s, left, right, 'b')) {

			return 1;
		}

		int result = INFINITY;
		for (int x = left; x <= (right - 1); x++) {

			int subProblem1 = solve(s, left, x);
			int subProblem2 = solve(s, x + 1, right);

			result = Math.min(result, subProblem1 + subProblem2);
		}
		return result;
	}

	static boolean parse(String s, int left, int right, char expectedChar) {

		if (left == right) {

			return (s.charAt(left) == expectedChar)
					? true
					: false;
		}

		if ((s.charAt(left) == s.charAt(right))
				&& (((right - left) % 2) == 0)
				&& (s.charAt((right + left) / 2) == expectedChar)
				&& (parse(s, left + 1, right - 1, 'a') || parse(s, left + 1, right - 1, 'b'))) {

			// a proper subtree with a root in the middle
			return true;
		}

		// otherwise - root in the middle
		// however, left and right subtrees are present too
		for (int x = left + 1; x <= (right - 1); x++) {

			if (s.charAt(x) != expectedChar) {
				continue;
			}

			if (parse(s, left, x - 1, 'a')
					&& parse(s, x + 1, right, 'a')) {
				return true;
			}

			if (parse(s, left, x - 1, 'b')
					&& parse(s, x + 1, right, 'b')) {
				return true;
			}
		}
		// Otherwise
		return false;
	}
}
