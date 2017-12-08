package spoj.dynamic_programming.strcount;

// http://www.spoj.com/problems/STRCOUNT/

// Some interesting information can be found here: http://oeis.org/A048004

public class STRCOUNT_1 {

	public static void main(String[] args) {
		for (int n = 1; n <= 24; n++) {
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

		// 1110.....
		long left = 0;
		int other1 = n - k - 1;
		for (int x = 0; x <= Math.min(other1, k - 1); x++) {
			left += f(other1, x);
		}

		// .....0111
		long right = left;

		// 1110..111
		long left_right_both_k = f(other1, k);

		// ..01110...
		long middle = 0;
		int other2 = n - k - 2;
		for (int b = 0; b <= other2; b++) {

			int a = other2 - b;

			long l = 0;
			for (int x = 0; x <= Math.min(b, k); x++) {
				l += f(b, x);
			}

			long r = 0;
			for (int x = 0; x <= Math.min(a, k - 1); x++) {
				r += f(a, x);
			}

			middle += l * r;
		}

		return left + right + middle + left_right_both_k;
	}

}
