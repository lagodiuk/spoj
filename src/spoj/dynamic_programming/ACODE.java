// http://www.spoj.com/problems/ACODE/

package spoj.dynamic_programming;

public class ACODE {

	public static void main(String[] args) {
		System.out.println(solve("25114"));
		System.out.println(solve("1111111111"));
		System.out.println(solve("3333333333"));
		System.out.println(solve("110"));
		System.out.println(solve("100"));
		System.out.println(solve("1000"));
		System.out.println(solve("1001"));
		System.out.println(solve("1024"));
		System.out.println(solve("1020"));
		System.out.println(solve("99"));
		System.out.println(solve("1111"));
		System.out.println(solve("136517"));
		System.out.println(solve("2262"));
	}

	static long solve(String s) {
		System.out.println(solve2(s));
		return solve(s, 0);
	}

	// TODO: Either add memoization or transform to bottom-up Dynamic Programming
	static long solve(String s, int pos) {
		if (pos == s.length()) {
			return 1;
		}

		if ((digitAt(s, pos) == 0)) {
			return 0;
		}

		long result = solve(s, pos + 1);

		if ((pos < (s.length() - 1)) && ((digitAt(s, pos) == 1) || ((digitAt(s, pos) == 2) && (digitAt(s, pos + 1) <= 6)))) {
			result += solve(s, pos + 2);
		}

		return result;
	}

	static int digitAt(String s, int i) {
		return s.charAt(i) - '0';
	}

	// Bottom-up solution
	static long solve2(String s) {
		long prevPrev = 0;
		long prev = 1;
		long curr;

		for (int pos = s.length() - 1; pos >= 0; pos--) {
			if ((digitAt(s, pos) == 0)) {
				curr = 0;
			} else {
				curr = prev;
				if ((pos < (s.length() - 1)) && ((digitAt(s, pos) == 1) || ((digitAt(s, pos) == 2) && (digitAt(s, pos + 1) <= 6)))) {
					curr += prevPrev;
				}
			}

			prevPrev = prev;
			prev = curr;

			if ((prev == 0) && (prevPrev == 0)) {
				break;
			}
		}

		return prev;
	}
}
