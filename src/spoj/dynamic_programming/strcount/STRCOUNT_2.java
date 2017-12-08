package spoj.dynamic_programming.strcount;

// http://www.spoj.com/problems/STRCOUNT/

public class STRCOUNT_2 {

	public static void main(String[] args) {
		for (int n = 1; n <= 7; n++) {
			for (int k = 0; k <= n; k++) {
				System.out.printf("%7d ", f(n, k));
			}
			System.out.println();
		}
	}

	static long f(int n, int k) {

		if (k > n) {
			return 0;
		}
		if ((k == 0) || (k == n)) {
			return 1;
		}

		long result = 0;
		result += f(n - 1, k - 1) + f(n - k - 1, k) + f(n - k - 1, k - 1);

		for (int b = 0; b <= (n - k - 2); b++) {

			long s1 = 0;
			for (int x = 0; x <= k; x++) {
				s1 += f(b, x);
			}

			long s2 = 0;
			for (int x = 0; x <= (k - 2); x++) {
				s2 += f(n - k - 2 - b, x);
			}

			result += (s1 * f(n - k - 2 - b, k - 1)) + (f(b, k) * s2);
		}
		return result;
	}
}
