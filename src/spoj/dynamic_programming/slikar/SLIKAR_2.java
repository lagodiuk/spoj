package spoj.dynamic_programming.slikar;

import java.util.Arrays;

// http://www.spoj.com/problems/SLIKAR/

public class SLIKAR_2 {

	public static void main(String... args) {
		System.out.println(solve(new String[]{
				"01010001",
				"10100011",
				"01010111",
				"10101111",
				"01010111",
				"10100011",
				"01010001",
				"10100000",
		}));
	}

	static int solve(String[] rows) {
		int log2W = Arrays.binarySearch(pow2, rows.length);
		return solve(0, 0, log2W, rows);
	}

	public static final int[][] QUADRANTS = {{0, 0}, {1, 0}, {0, 1}, {1, 1}};
	public static final int[] pow2 = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512};
	public static final int INFINITY = 100000000;

	// TODO add memoization - it is needed to memoize (512 * 512 * log2(512)) values
	static int solve(int x, int y, int log2W, String[] rows) {
		if (log2W == 0) {
			return 0;
		}

		int newLog2W = log2W - 1;
		int newW = pow2[newLog2W];

		int result = INFINITY;
		for (int[] dxyw : QUADRANTS) {
			for (int[] dxyb : QUADRANTS) {
				if (dxyb != dxyw) {
					int currResult = diff(x + (dxyb[0] * newW), y + (dxyb[1] * newW), newW, rows, '0');
					currResult += diff(x + (dxyw[0] * newW), y + (dxyw[1] * newW), newW, rows, '1');
					for (int[] d1 : QUADRANTS) {
						if ((d1 != dxyb) && (d1 != dxyw)) {
							currResult += solve(x + (d1[0] * newW), y + (d1[1] * newW), newLog2W, rows);
						}
					}
					result = Math.min(result, currResult);
				}
			}
		}
		return result;
	}

	// TODO this can be precalculated with O(N^2) complexity
	static int diff(int x, int y, int w, String[] rows, char col) {
		int result = 0;
		for (int i = x; i < (x + w); i++) {
			for (int j = y; j < (y + w); j++) {
				if (rows[i].charAt(j) != col) {
					result++;
				}
			}
		}
		return result;
	}
}