package spoj.dynamic_programming.minus;

// http://www.spoj.com/problems/MINUS/

public class MINUS_1_Not_Optimal {

	public static void main(String[] args) {
		System.out.println(solve(new int[]{12, 10, 4, 3, 5}, 4));
	}

	// Checks, whether we can put brackets and minus signs
	// between the items of array in order to obtain target
	static boolean solve(int[] arr, int target) {
		// Unfortunately, it seems that, even if we add memoization
		// the runtime complexity of this solution will be very big
		return f(arr, 0, arr.length - 1, target);
	}

	static boolean f(int[] arr, int l, int r, int target) {
		if (l == r) {
			return (arr[l] == target);
		}

		int min = 0;
		int max = 0;
		for (int i = l; i <= r; i++) {
			min -= arr[i];
			max += arr[i];
		}

		for (int x = min; x <= max; x++) {
			for (int m = l; m < r; m++) {
				if (f(arr, l, m, x) && f(arr, m + 1, r, x - target)) {
					return true;
				}
			}
		}

		return false;
	}
}
