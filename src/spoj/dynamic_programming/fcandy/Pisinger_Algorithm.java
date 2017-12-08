package spoj.dynamic_programming.fcandy;

public class Pisinger_Algorithm {

	public static void main(String[] args) {
		System.out.println(subsetSumPisinger(new int[]{6, 4, 2, 6, 4, 3}, 15));
	}

	// Some comments about this algorithm on Stackoverflow
	// http://stackoverflow.com/questions/18821453/bounded-knapsack-special-case-small-individual-item-weight-is-small-compared-t
	// http://stackoverflow.com/questions/9809436/fast-solution-to-subset-sum
	static int subsetSumPisinger(int[] w, int c) {
		int n = w.length;
		int w_ = 0;
		int b = 0;
		// 1-based indexing
		for (int i = 1; i <= n; i++) {
			b = i;
			if ((w_ + w[i - 1]) <= c) {
				w_ += w[i - 1];
			} else {
				break;
			}
		}
		if (b == n) {
			return w_;
		}

		int r = getMax(w);

		int offs = (c - r) + 1;

		int[] prev = new int[2 * r];
		for (int m = (c - r) + 1; m <= c; m++) {
			prev[m - offs] = 0;
		}
		for (int m = c + 1; m <= (c + r); m++) {
			prev[m - offs] = 1;
		}
		prev[w_ - offs] = b;

		int[] curr = new int[2 * r];
		for (int t = b; t <= n; t++) {

			// System.out.println(Arrays.toString(prev));

			for (int m = (c - r) + 1; m <= (c + r); m++) {
				curr[m - offs] = prev[m - offs];
			}
			for (int m = (c - r) + 1; m <= c; m++) {
				int m_p = m + w[t - 1];
				curr[m_p - offs] = Math.max(curr[m_p - offs], prev[m - offs]);
			}
			for (int m = c + w[t - 1]; m >= (c + 1); m--) {
				for (int j = curr[m - offs] - 1; j >= prev[m - offs]; j--) {
					int m_p = m - w[j - 1];
					curr[m_p - offs] = Math.max(curr[m_p - offs], j);
				}
			}
			int[] tmp = prev;
			prev = curr;
			curr = tmp;
		}
		// System.out.println(Arrays.toString(prev));

		for (int m = c; m >= ((c - r) + 1); m--) {
			if (prev[m - offs] != 0) {
				return m;
			}
		}

		throw new RuntimeException();
	}

	static int getMax(int[] arr) {
		int max = -1;
		for (int x : arr) {
			max = Math.max(max, x);
		}
		return max;
	}
}
