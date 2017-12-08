package spoj.dynamic_programming.slikar;

import java.util.Arrays;

// http://www.spoj.com/problems/SLIKAR/

public class SLIKAR_4 {

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
		QuadTree qt = new QuadTree(0, 0, rows.length);
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
	static void trace_back(int x, int y, int log2W, String[] rows, int[][] count_1s, int[][][] memoized, QuadTree qt) {
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
						qt.children = new QuadTree[2][2];
						qt.children[dxyb[0]][dxyb[1]] = new QuadTree(x + (dxyb[0] * newW), y + (dxyb[1] * newW), newW);
						qt.children[dxyb[0]][dxyb[1]].color = '0';

						qt.children[dxyw[0]][dxyw[1]] = new QuadTree(x + (dxyw[0] * newW), y + (dxyw[1] * newW), newW);
						qt.children[dxyw[0]][dxyw[1]].color = '1';

						for (int[] d1 : QUADRANTS) {
							if ((d1 != dxyb) && (d1 != dxyw)) {
								qt.children[d1[0]][d1[1]] = new QuadTree(x + (d1[0] * newW), y + (d1[1] * newW), newW);
								trace_back(x + (d1[0] * newW), y + (d1[1] * newW), newLog2W, rows, count_1s, memoized, qt.children[d1[0]][d1[1]]);
							}
						}
					}
				}
			}
		}
	}

	// This can be refactored
	// Actually, it will be sufficient just to use the Binary Tree!
	static class QuadTree {
		int x;
		int y;
		int w;
		QuadTree[][] children;
		char color;
		public QuadTree(int x, int y, int w) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.color = 'x'; // marker of no color
		}

		char getColor(int x, int y) {
			if (this.children == null) {
				return this.color;
			}

			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					if ((this.children[i][j].x <= x) && ((this.children[i][j].x + this.children[i][j].w) > x) &&
							(this.children[i][j].y <= y) && ((this.children[i][j].y + this.children[i][j].w) > y)) {

						return this.children[i][j].getColor(x, y);
					}
				}
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