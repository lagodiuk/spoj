package spoj.dynamic_programming.fcandy;

// http://www.spoj.com/problems/FCANDY/

public class FCANDY_13_Pisinger_Algorithm {

	static final int MAX_CALORIES = 205;
	static final int MAX_NUMBER = 505;
	static final int MAX_CANDIES_TYPES = 105;

	static final int[] PREV = new int[MAX_CALORIES * 2];
	static final int[] CURR = new int[MAX_CALORIES * 2];
	static final int[] ALL_CANDIES_ARRAY = new int[MAX_CANDIES_TYPES * MAX_NUMBER];

	public static void main(String[] args) {
		FCANDY_1_Top_Down.evaluateExample(FCANDY_13_Pisinger_Algorithm::solve1);
		FCANDY_1_Top_Down.evaluateRandomized(FCANDY_13_Pisinger_Algorithm::solve1, true, 6);
		FCANDY_1_Top_Down.evaluateRandomized(FCANDY_13_Pisinger_Algorithm::solve1, false, 100, 50, 50);
		FCANDY_1_Top_Down.evaluateRandomized(FCANDY_13_Pisinger_Algorithm::solve1, false, 100, 500, 200);
	}

	static final int solve1(Candies[] candies) {

		int totalCalories = calculateTotalalories(candies);

		int idx = 0;
		for (Candies ca : candies) {
			for (int i = 0; i < ca.count; i++) {
				ALL_CANDIES_ARRAY[idx] = ca.calories;
				idx++;
			}
		}
		int n = idx;

		int maxSubsetSum = subsetSumPisinger(ALL_CANDIES_ARRAY, n, totalCalories / 2);
		return totalCalories - (2 * maxSubsetSum);
	}

	static final int calculateTotalalories(Candies[] candies) {
		int totalCalories = 0;
		for (Candies ca : candies) {
			totalCalories += ca.calories * ca.count;
		}
		return totalCalories;
	}

	static int subsetSumPisinger(int[] w, int n, int c) {

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

		int[] prev = PREV;
		for (int m = (c - r) + 1; m <= c; m++) {
			prev[m - offs] = 0;
		}
		for (int m = c + 1; m <= (c + r); m++) {
			prev[m - offs] = 1;
		}
		prev[w_ - offs] = b;

		int[] curr = CURR;
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
