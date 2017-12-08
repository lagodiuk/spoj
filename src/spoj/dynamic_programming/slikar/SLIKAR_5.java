package spoj.dynamic_programming.slikar;

import java.util.Arrays;

// http://www.spoj.com/problems/SLIKAR/

public class SLIKAR_5 {

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
		int[][] count_1s = count_1s(rows);
		int log2W = Arrays.binarySearch(pow2, rows.length);
		int[][][] memoized = new int[log2W + 1][rows.length + 1][rows.length + 1];
		for (int[][] slice : memoized) {
			for (int[] row : slice) {
				Arrays.fill(row, NOT_INITIALZIED);
			}
		}
		int min_diff = solve(0, 0, log2W, rows, count_1s, memoized);
		BinaryQuadTree qt = new BinaryQuadTree(0, 0, rows.length);
		trace_back(0, 0, log2W, rows, count_1s, memoized, qt);
		for (int x = 0; x < rows.length; x++) {
			for (int y = 0; y < rows.length; y++) {
				System.out.print(qt.getColor(x, y) + "");
			}
			System.out.println();
		}
		return min_diff;
	}

	// This can be refactored
	static void trace_back(int x, int y, int log2W, String[] rows, int[][] count_1s, int[][][] memoized, BinaryQuadTree qt) {
		if (log2W == 0) {
			qt.color = rows[x].charAt(y);
			return;
		}

		int newLog2W = log2W - 1;
		int newW = pow2[newLog2W];

		for (int[] dxyw : QUADRANTS) {
			for (int[] dxyb : QUADRANTS) {
				if (dxyb != dxyw) {
					int currResult = diff(x + (dxyb[0] * newW), y + (dxyb[1] * newW), newW, rows, '0', count_1s);
					currResult += diff(x + (dxyw[0] * newW), y + (dxyw[1] * newW), newW, rows, '1', count_1s);
					for (int[] d1 : QUADRANTS) {
						if ((d1 != dxyb) && (d1 != dxyw)) {
							currResult += solve(x + (d1[0] * newW), y + (d1[1] * newW), newLog2W, rows, count_1s, memoized);
						}
					}
					if (currResult == memoized[log2W][x][y]) {
						qt.x_black = x + (dxyb[0] * newW);
						qt.y_black = y + (dxyb[1] * newW);

						qt.x_white = x + (dxyw[0] * newW);
						qt.y_white = y + (dxyw[1] * newW);

						boolean left = true;
						for (int[] d1 : QUADRANTS) {
							if ((d1 != dxyb) && (d1 != dxyw)) {
								if (left) {
									qt.left = new BinaryQuadTree(x + (d1[0] * newW), y + (d1[1] * newW), newW);
									trace_back(x + (d1[0] * newW), y + (d1[1] * newW), newLog2W, rows, count_1s, memoized, qt.left);
									left = false;
								} else {
									qt.right = new BinaryQuadTree(x + (d1[0] * newW), y + (d1[1] * newW), newW);
									trace_back(x + (d1[0] * newW), y + (d1[1] * newW), newLog2W, rows, count_1s, memoized, qt.right);
								}
							}
						}
					}
				}
			}
		}
	}

	static class BinaryQuadTree {

		int x;
		int y;
		int w;

		BinaryQuadTree left;
		BinaryQuadTree right;

		int x_white;
		int y_white;

		int x_black;
		int y_black;

		char color;

		public BinaryQuadTree(int x, int y, int w) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.color = 'x'; // marker of no color
		}

		char getColor(int x, int y) {
			if ((this.left == null) && (this.right == null)) {
				return this.color;
			}

			if ((this.left.x <= x) && ((this.left.x + this.left.w) > x) &&
					(this.left.y <= y) && ((this.left.y + this.left.w) > y)) {

				return this.left.getColor(x, y);
			}

			if ((this.right.x <= x) && ((this.right.x + this.right.w) > x) &&
					(this.right.y <= y) && ((this.right.y + this.right.w) > y)) {

				return this.right.getColor(x, y);
			}

			int child_w = this.w / 2;

			if ((this.x_black <= x) && ((this.x_black + child_w) > x) &&
					(this.y_black <= y) && ((this.y_black + child_w) > y)) {

				return '0';
			}

			if ((this.x_white <= x) && ((this.x_white + child_w) > x) &&
					(this.y_white <= y) && ((this.y_white + child_w) > y)) {

				return '1';
			}

			throw new RuntimeException();
		}
	}

	public static final int NOT_INITIALZIED = -1;
	public static final int[][] QUADRANTS = {{0, 0}, {1, 0}, {0, 1}, {1, 1}};
	public static final int[] pow2 = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512};
	public static final int INFINITY = 100000000;

	// With memoization (top-down)
	static int solve(int x, int y, int log2W, String[] rows, int[][] count_1s, int[][][] memoized) {
		if (log2W == 0) {
			return 0;
		}

		if (memoized[log2W][x][y] != NOT_INITIALZIED) {
			return memoized[log2W][x][y];
		}

		int newLog2W = log2W - 1;
		int newW = pow2[newLog2W];

		int result = INFINITY;
		for (int[] dxyw : QUADRANTS) {
			for (int[] dxyb : QUADRANTS) {
				if (dxyb != dxyw) {
					int currResult = diff(x + (dxyb[0] * newW), y + (dxyb[1] * newW), newW, rows, '0', count_1s);
					currResult += diff(x + (dxyw[0] * newW), y + (dxyw[1] * newW), newW, rows, '1', count_1s);
					for (int[] d1 : QUADRANTS) {
						if ((d1 != dxyb) && (d1 != dxyw)) {
							currResult += solve(x + (d1[0] * newW), y + (d1[1] * newW), newLog2W, rows, count_1s, memoized);
						}
					}
					result = Math.min(result, currResult);
				}
			}
		}

		memoized[log2W][x][y] = result;
		return result;
	}

	// O(1)
	static int diff(int startRow, int startCol, int w, String[] rows, char chr, int[][] count_1s) {
		int up = (startRow > 0) ? count_1s[startRow - 1][(startCol + w) - 1] : 0;
		int left = (startCol > 0) ? count_1s[(startRow + w) - 1][startCol - 1] : 0;
		int left_up = (startRow > 0) && (startCol > 0) ? count_1s[startRow - 1][startCol - 1] : 0;
		int curr = count_1s[(startRow + w) - 1][(startCol + w) - 1];
		int count_1s_precalculated = (curr - left - up) + left_up;
		int count_0s_precalculated = (w * w) - count_1s_precalculated;
		int result = (chr == '1') ? count_0s_precalculated : count_1s_precalculated;
		return result;
	}

	// O(N^2)
	private static int[][] count_1s(String[] rows) {
		int dim = rows.length;
		int[][] count = new int[dim][dim];
		for (int row = 0; row < dim; row++) {
			for (int col = 0; col < dim; col++) {
				int left = (col > 0) ? count[row][col - 1] : 0;
				int up = (row > 0) ? count[row - 1][col] : 0;
				int left_up = (row > 0) && (col > 0) ? count[row - 1][col - 1] : 0;
				int curr = rows[row].charAt(col) == '1' ? 1 : 0;
				count[row][col] = ((left + up) - left_up) + curr;
			}
		}
		return count;
	}
}