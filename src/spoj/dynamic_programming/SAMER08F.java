package spoj.dynamic_programming;

// http://www.spoj.com/problems/SAMER08F/

public class SAMER08F {

	public static void main(String[] args) {
		System.out.println(squaresDP(1));
		System.out.println(squaresDP(2));
		System.out.println(squaresDP(3));
		System.out.println(squaresDP(8));
	}

	// Exponential complexity
	static int squares(int n) {

		if (n == 0) {
			return 0;
		}

		if (n == 1) {
			return 1;
		}

		return ((2 * squares(n - 1)) - squares(n - 2)) + (2 * (n - 1)) + 1;
	}

	// O(N)
	static int squaresDP(int n) {
		int prevPrev = 0;
		int prev = 1;
		for (int i = 2; i <= n; i++) {
			int curr = ((2 * prev) - prevPrev) + (2 * (i - 1)) + 1;
			prevPrev = prev;
			prev = curr;
		}
		return prev;
	}
}
