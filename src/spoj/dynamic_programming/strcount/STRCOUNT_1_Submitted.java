package spoj.dynamic_programming.strcount;

// http://www.spoj.com/problems/STRCOUNT/

public class STRCOUNT_1_Submitted {

	static final long[][] f = new long[65][65];
	static final long[][] sum = new long[65][65];

	static int n;
	static int k;
	static int b;

	public static void main(String[] args) {

		for (n = 0; n <= 64; n++) {
			f[n][0] = 1;
			f[n][n] = 1;
			sum[n][0] = 1;
			sum[n][n] = 1 << n;
			for (k = n + 1; k <= 64; k++) {
				sum[n][k] = sum[n][n];
			}
		}
		for (n = 2; n <= 64; n++) {
			for (k = 1; k < n; k++) {
				f[n][k] = (sum[n - k - 1][k - 1] << 1) + f[n - k - 1][k];
				for (b = 0; b <= (n - k - 2); b++) {
					f[n][k] += sum[b][k] * sum[n - k - 2 - b][k - 1];
				}
				sum[n][k] = sum[n][k - 1] + f[n][k];
			}
		}

		for (int n = 1; n <= 64; n++) {
			for (int k = 0; k <= n; k++) {
				System.out.print(f[n][k]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
}
