package spoj.dynamic_programming;

// http://www.spoj.com/problems/ANARC05H

public class ANARC05H {

	// TODO: use cumulative sum array
	static long sum(int left, int right, int[] digits) {
		long sum = 0;
		for (int i = left; i <= right; i++) {
			sum += digits[i];
		}
		return sum;
	}

	// Exponential complexity
	// TODO: use memoization
	static long variants(int left, long prev, int[] digits) {
		if (left == digits.length) {
			return 1;
		}

		long result = 0;

		for (int k = left; k < digits.length; k++) {
			long preffixSum = sum(left, k, digits);

			if (preffixSum >= prev) {
				result += variants(k + 1, preffixSum, digits);
			}
		}

		return result;
	}

	static long variants(int[] digits) {
		return variants(0, 0, digits);
	}

	public static void main(String[] args) {
		System.out.println(variants(new int[]{6, 3, 5}));
		System.out.println(variants(new int[]{1, 1, 1, 7}));
		System.out.println(variants(new int[]{9, 8, 7, 6}));
		System.out.println(variants(new int[]{0, 0, 0}));
		System.out.println(variants(new int[]{0, 0, 0, 0}));
		System.out.println(variants(new int[]{0, 0, 0, 0, 0}));
	}
}
