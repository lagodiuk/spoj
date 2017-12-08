package spoj.dynamic_programming.scales;

// http://www.spoj.com/problems/SCALES/

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

public class SCALES_Brute_Force {

	public static void main(String[] args) {
		test_simple(SCALES_Brute_Force::solve);
	}

	static void test_simple(Function<int[], Integer> f) {
		int[][] data = new int[][]{
				{0, 0, 1, 0, 0, 0},
				{1, 0, 0, 1, 1, 0},
				{1, 0, 0, 0, 0, 0},
				{0, 1, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 1},
		};

		for (int[] input : data) {
			System.out.println(Arrays.toString(input));
			System.out.println(solve(input));
			System.out.println(f.apply(input));
			System.out.println();
		}
	}

	static void test(Function<int[], Integer> f) {
		Random r = new Random(1);

		for (int i = 0; i < 100; i++) {
			int[] input = new int[r.nextInt(8) + 1];
			for (int p = 0; p < input.length; p++) {
				input[p] = r.nextInt(2);
			}

			System.out.println(Arrays.toString(input));
			Integer r1 = f.apply(input);
			System.out.println(r1);
			int r2 = solve(input);
			System.out.println(r2);
			System.out.println();

			if (r1 != r2) {
				throw new RuntimeException();
			}
		}
	}

	static int solve(int[] right) {
		return solve(right, new int[right.length], right.length - 1);
	}

	static int solve(int[] right, int[] left, int position) {
		if (position < 0) {
			boolean equal = true;
			for (int i = 0; i < right.length; i++) {
				if (right[i] != left[i]) {
					equal = false;
					break;
				}
			}
			return equal ? 1 : 0;
		}

		left[position] = 0;
		int s1 = solve(right, left, position - 1);

		left[position] = 1;
		int s2 = solve(right, left, position - 1);

		int s3;
		left[position] = 0;
		int p = position;
		while ((p >= 0) && (right[p] == 1)) {
			right[p] = 0;
			p--;
		}

		if (p < 0) {
			s3 = 0;
			p = 0;
			while (p <= position) {
				right[p] = 1 - right[p];
				p++;
			}
		} else {
			right[p] = 1;
			s3 = solve(right, left, position - 1);
			while (p <= position) {
				right[p] = 1 - right[p];
				p++;
			}
		}

		return s1 + s2 + s3;
	}
}
