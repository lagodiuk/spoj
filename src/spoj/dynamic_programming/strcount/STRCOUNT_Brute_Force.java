package spoj.dynamic_programming.strcount;

// http://www.spoj.com/problems/STRCOUNT/

public class STRCOUNT_Brute_Force {

	public static void main(String[] args) {
		for (int n = 1; n <= 26; n++) {
			int[] distribution = f(n);
			for (int k = 0; k <= n; k++) {
				System.out.printf("%7d ", distribution[k]);
			}
			System.out.println();
		}
	}

	static int[] f(int n) {
		int[] distribution = new int[n + 1];
		int[] binary = new int[n];
		int[] temp = new int[n];
		all_combinations(n, 0, binary, temp, distribution);
		return distribution;
	}

	static void all_combinations(int n, int idx, int[] binary, int[] temp, int[] distribution) {
		if (idx == n) {
			int cnt_1s = longest_substr_of_1s(binary, temp);
			distribution[cnt_1s]++;
			return;
		}

		binary[idx] = 1;
		all_combinations(n, idx + 1, binary, temp, distribution);

		binary[idx] = 0;
		all_combinations(n, idx + 1, binary, temp, distribution);
	}

	static int longest_substr_of_1s(int[] binary, int[] temp) {
		temp[0] = binary[0];
		for (int i = 1; i < binary.length; i++) {
			if (binary[i] == 1) {
				temp[i] = 1 + temp[i - 1];
			} else {
				temp[i] = 0;
			}
		}
		int result = 0;
		for (int i = 0; i < binary.length; i++) {
			result = Math.max(result, temp[i]);
		}
		return result;
	}
}
