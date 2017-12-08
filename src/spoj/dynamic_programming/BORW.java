package spoj.dynamic_programming;

import java.util.Arrays;
import java.util.Random;

// http://www.spoj.com/problems/BORW/

public class BORW {

	public static void main(String[] args) {
		System.out.println(min_unpainted_bottom_up(new int[]{1, 4, 2, 3, 3, 2, 4, 1}));
		System.out.println(min_unpainted_bottom_up(new int[]{7, 8, 1, 2, 4, 6, 3, 5, 2, 1, 8, 7}));

		Random rnd = new Random();
		for (int i = 0; i < 200; i++) {
			int[] arr = new int[rnd.nextInt(15) + 1];
			for (int j = 0; j < arr.length; j++) {
				arr[j] = Math.abs(rnd.nextInt(1000000)) + 1;
			}
			int r1 = min_unpainted(arr);
			int r2 = min_unpainted_bottom_up(arr);
			if (r1 != r2) {
				System.out.println(arr.length);
				System.out.println(Arrays.toString(arr).replaceAll("[\\[\\],]", " "));
				System.out.println(r1 + "\t" + r2);
				System.out.println();
			}
		}
	}

	static int min_unpainted(int[] arr) {
		return min_unpainted(arr, 0, -1, -1);
	}

	// TODO: add memoization, or implement bottom-up solution
	static int min_unpainted(int[] arr, int curr_idx, int prev_black_idx, int prev_white_idx) {
		if (curr_idx == arr.length) {
			return 0;
		}
		int ign = min_unpainted(arr, curr_idx + 1, prev_black_idx, prev_white_idx) + 1;
		int blk = ((prev_black_idx == -1) || (arr[prev_black_idx] < arr[curr_idx]))
				? min_unpainted(arr, curr_idx + 1, curr_idx, prev_white_idx)
				: Integer.MAX_VALUE;
		int wht = ((prev_white_idx == -1) || (arr[prev_white_idx] > arr[curr_idx]))
				? min_unpainted(arr, curr_idx + 1, prev_black_idx, curr_idx)
				: Integer.MAX_VALUE;
		return Math.min(ign, Math.min(blk, wht));
	}

	static int min_unpainted_bottom_up(int[] arr) {
		int[][][] mem = new int[arr.length + 2][arr.length + 2][arr.length + 2];

		for (int curr_idx = arr.length + 1; curr_idx >= 1; curr_idx--) {
			for (int prev_black_idx = 0; prev_black_idx <= curr_idx; prev_black_idx++) {
				for (int prev_white_idx = 0; prev_white_idx <= curr_idx; prev_white_idx++) {
					if (curr_idx == (arr.length + 1)) {
						mem[curr_idx][prev_black_idx][prev_white_idx] = 0;
					} else {
						int ign = mem[curr_idx + 1][prev_black_idx][prev_white_idx] + 1;
						int blk = ((prev_black_idx == 0) || (arr[prev_black_idx - 1] < arr[curr_idx - 1]))
								? mem[curr_idx + 1][curr_idx][prev_white_idx]
								: Integer.MAX_VALUE;
						int wht = ((prev_white_idx == 0) || (arr[prev_white_idx - 1] > arr[curr_idx - 1]))
								? mem[curr_idx + 1][prev_black_idx][curr_idx]
								: Integer.MAX_VALUE;

						mem[curr_idx][prev_black_idx][prev_white_idx] = Math.min(ign, Math.min(blk, wht));
					}
				}
			}
		}

		return mem[1][0][0];
	}
}
