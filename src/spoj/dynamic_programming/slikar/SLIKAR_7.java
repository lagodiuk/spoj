package spoj.dynamic_programming.slikar;

import java.util.Arrays;

// http://www.spoj.com/problems/SLIKAR/

public class SLIKAR_7 {

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

		System.out.println();
		System.out.println(solve(new String[]{
				"0101000101010001",
				"1010001101010001",
				"0101011101010001",
				"1010111101010001",
				"0101011101010001",
				"1010001101010001",
				"0101000101010001",
				"1010000010100000",
				"0101000101010001",
				"1010001101010001",
				"0101011101010001",
				"1010111101010001",
				"0101011101010001",
				"1010001101010001",
				"0101000101010001",
				"1010000010100000",
		}));

		System.out.println();
		System.out.println(solve(new String[]{
				"01010001010100011111111100010001",
				"10100011010100011111111100010001",
				"01010111010100011111111100010001",
				"10101111010100011111111100010001",
				"01010111010100011111111100010001",
				"10100011010100011111111100010001",
				"01010001010100011111111100010001",
				"10100000101000001111111100010001",
				"01010001010100011111111100010001",
				"10100011010100011111111100010001",
				"01010111010100011111111100010001",
				"10101111010100011111111100010001",
				"01010111010100011111111100010001",
				"10100011010100011111111100010001",
				"01010001010100011111111100010001",
				"10100000101000001111111100010001",
				"01010001010100011111111100010001",
				"10100011010100011111111100010001",
				"01010111010100011111111100010001",
				"10101111010100011111111100010001",
				"01010111010100011111111100010001",
				"10100011010100011111111100010001",
				"01010001010100011111111100010001",
				"10100000101000001111111100010001",
				"01010001010100011111111100010001",
				"10100011010100011111111100010001",
				"01010111010100011111111100010001",
				"10101111010100011111111100010001",
				"01010111010100011111111100010001",
				"10100011010100011111111100010001",
				"01010001010100011111111100010001",
				"10100000101000001111111100010001",
		}));

		System.out.println();
		System.out.println(solve(new String[]{
				"0101000101010001111111110001000100001111000000001111111111111111",
				"1010001101010001111111110001000100001111000000001111111111111111",
				"0101011101010001111111110001000100001111000000001111111111111111",
				"1010111101010001111111110001000100001111000000001111111111111111",
				"0101011101010001111111110001000100001111000000001111111111111111",
				"1010001101010001111111110001000100001111000000001111111111111111",
				"0101000101010001111111110001000100001111000000001111111111111111",
				"1010000010100000111111110001000100001111000000001111111111111111",
				"0101000101010001111111110001000100001111000000001111111111111111",
				"1010001101010001111111110001000100001111000000001111111111111111",
				"0101011101010001111111110001000100001111000000001111111111111111",
				"1010111101010001111111110001000100001111000000001111111111111111",
				"0101011101010001111111110001000100001111000000001111111111111111",
				"1010001101010001111111110001000100001111000000001111111111111111",
				"0101000101010001111111110001000100001111000000001111111111111111",
				"1010000010100000111111110001000100001111000000001111111111111111",
				"0101000101010001111111110001000100001111000000001111111111111111",
				"1010001101010001111111110001000100001111000000001111111111111111",
				"0101011101010001111111110001000100001111000000001111111111111111",
				"1010111101010001111111110001000100001111000000001111111111111111",
				"0101011101010001111111110001000100001111000000001111111111111111",
				"1010001101010001111111110001000100001111000000001111111111111111",
				"0101000101010001111111110001000100001111000000001111111111111111",
				"1010000010100000111111110001000100001111000000001111111111111111",
				"0101000101010001111111110001000100001111000000001111111111111111",
				"1010001101010001111111110001000100001111000000001111111111111111",
				"0101011101010001111111110001000100001111000000001111111111111111",
				"1010111101010001111111110001000100001111000000001111111111111111",
				"0101011101010001111111110001000100001111000000001111111111111111",
				"1010001101010001111111110001000100001111000000001111111111111111",
				"0101000101010001111111110001000100001111000000001111111111111111",
				"1010000010100000111111110001000100001111000000001111111111111111",
				"0101000101010001111111110001000100001111000000001111111111111111",
				"1010001101010001111111110001000100001111000000001111111111111111",
				"0101011101010001111111110001000100001111000000001111111111111111",
				"1010111101010001111111110001000100001111000000001111111111111111",
				"0101011101010001111111110001000100001111000000001111111111111111",
				"1010001101010001111111110001000100001111000000001111111111111111",
				"0101000101010001111111110001000100001111000000001111111111111111",
				"1010000010100000111111110001000100001111000000001111111111111111",
				"0101000101010001111111110001000100001111000000001111111111111111",
				"1010001101010001111111110001000100001111000000001111111111111111",
				"0101011101010001111111110001000100001111000000001111111111111111",
				"1010111101010001111111110001000100001111000000001111111111111111",
				"0101011101010001111111110001000100001111000000001111111111111111",
				"1010001101010001111111110001000100001111000000001111111111111111",
				"0101000101010001111111110001000100001111000000001111111111111111",
				"1010000010100000111111110001000100001111000000001111111111111111",
				"0101000101010001111111110001000100001111000000001111111111111111",
				"1010001101010001111111110001000100001111000000001111111111111111",
				"0101011101010001111111110001000100001111000000001111111111111111",
				"1010111101010001111111110001000100001111000000001111111111111111",
				"0101011101010001111111110001000100001111000000001111111111111111",
				"1010001101010001111111110001000100001111000000001111111111111111",
				"0101000101010001111111110001000100001111000000001111111111111111",
				"1010000010100000111111110001000100001111000000001111111111111111",
				"0101000101010001111111110001000100001111000000001111111111111111",
				"1010001101010001111111110001000100001111000000001111111111111111",
				"0101011101010001111111110001000100001111000000001111111111111111",
				"1010111101010001111111110001000100001111000000001111111111111111",
				"0101011101010001111111110001000100001111000000001111111111111111",
				"1010001101010001111111110001000100001111000000001111111111111111",
				"0101000101010001111111110001000100001111000000001111111111111111",
				"1010000010100000111111110001000100001111000000001111111111111111",
		}));
	}

	static final int solve(String[] rows) {
		int[][] count_1s = count_1s(rows);
		int log2W = Arrays.binarySearch(pow2, rows.length);
		int[][][] memoized = new int[log2W + 1][rows.length + 1][rows.length + 1];
		for (int[][] slice : memoized) {
			for (int[] row : slice) {
				Arrays.fill(row, NOT_INITIALZIED);
			}
		}
		int min_diff = solve(0, 0, log2W, rows, count_1s, memoized);

		int tree_size = pow2[log2W + 1] + 2;
		binary_quad_tree_x = new int[tree_size];
		binary_quad_tree_y = new int[tree_size];
		binary_quad_tree_x_white = new int[tree_size];
		binary_quad_tree_y_white = new int[tree_size];
		binary_quad_tree_x_black = new int[tree_size];
		binary_quad_tree_y_black = new int[tree_size];
		binary_quad_tree_color = new char[tree_size];

		binary_quad_tree_x[0] = 0;
		binary_quad_tree_y[0] = 0;
		binary_quad_tree_color[0] = NO_COLOR;
		trace_back(0, 0, log2W, rows, count_1s, memoized, 0);
		for (int x = 0; x < rows.length; x++) {
			for (int y = 0; y < rows.length; y++) {
				System.out.print(getColor(x, y, rows.length, 0));
			}
			System.out.println();
		}
		return min_diff;
	}

	private static final char NO_COLOR = 'x';
	private static final char WHITE = '1';
	private static final char BLACK = '0';

	// This can be refactored
	static final void trace_back(int x, int y, int log2W, String[] rows, int[][] count_1s, int[][][] memoized, int qt_idx) {
		if (log2W == 0) {
			binary_quad_tree_color[qt_idx] = rows[x].charAt(y);
			return;
		}

		int newLog2W = log2W - 1;
		int newW = pow2[newLog2W];

		for (int w = 0; w < QUADRANTS.length; w++) {
			int[] dxyw = QUADRANTS[w];
			for (int b = 0; b < QUADRANTS.length; b++) {
				int[] dxyb = QUADRANTS[b];
				if (w != b) {
					int currResult = diff(x + (dxyb[0] * newW), y + (dxyb[1] * newW), newW, rows, BLACK, count_1s);
					currResult += diff(x + (dxyw[0] * newW), y + (dxyw[1] * newW), newW, rows, WHITE, count_1s);
					for (int d = 0; d < QUADRANTS.length; d++) {
						int[] d1 = QUADRANTS[d];
						if ((d != b) && (d != w)) {
							currResult += solve(x + (d1[0] * newW), y + (d1[1] * newW), newLog2W, rows, count_1s, memoized);
						}
					}
					if (currResult == memoized[log2W][x][y]) {
						binary_quad_tree_x_black[qt_idx] = x + (dxyb[0] * newW);
						binary_quad_tree_y_black[qt_idx] = y + (dxyb[1] * newW);

						binary_quad_tree_x_white[qt_idx] = x + (dxyw[0] * newW);
						binary_quad_tree_y_white[qt_idx] = y + (dxyw[1] * newW);

						boolean left = true;
						for (int d = 0; d < QUADRANTS.length; d++) {
							int[] d1 = QUADRANTS[d];
							if ((d != b) && (d != w)) {
								int child_qt_idx;
								if (left) {
									child_qt_idx = (qt_idx << 1) + 1;
									left = false;
								} else {
									child_qt_idx = (qt_idx << 1) + 2;
								}
								binary_quad_tree_x[child_qt_idx] = x + (d1[0] * newW);
								binary_quad_tree_y[child_qt_idx] = y + (d1[1] * newW);
								binary_quad_tree_color[child_qt_idx] = NO_COLOR;
								trace_back(x + (d1[0] * newW), y + (d1[1] * newW), newLog2W, rows, count_1s, memoized, child_qt_idx);
							}
						}
						break;
					}
				}
			}
		}
	}

	static int[] binary_quad_tree_x;
	static int[] binary_quad_tree_y;
	static int[] binary_quad_tree_x_white;
	static int[] binary_quad_tree_y_white;
	static int[] binary_quad_tree_x_black;
	static int[] binary_quad_tree_y_black;
	static char[] binary_quad_tree_color;

	static final char getColor(int x, int y, int w, int qt_idx) {
		if (binary_quad_tree_color[qt_idx] != NO_COLOR) {
			return binary_quad_tree_color[qt_idx];
		}

		int child_w = w >>> 1;
		int x_sub_child_w = x - child_w;
		int y_sub_child_w = y - child_w;

		if ((binary_quad_tree_x_black[qt_idx] <= x) && (binary_quad_tree_x_black[qt_idx] > x_sub_child_w) &&
				(binary_quad_tree_y_black[qt_idx] <= y) && (binary_quad_tree_y_black[qt_idx] > y_sub_child_w)) {

			return BLACK;
		}

		if ((binary_quad_tree_x_white[qt_idx] <= x) && (binary_quad_tree_x_white[qt_idx] > x_sub_child_w) &&
				(binary_quad_tree_y_white[qt_idx] <= y) && (binary_quad_tree_y_white[qt_idx] > y_sub_child_w)) {

			return WHITE;
		}

		int left_qt_idx = (qt_idx << 1) + 1;
		if ((binary_quad_tree_x[left_qt_idx] <= x) && (binary_quad_tree_x[left_qt_idx] > x_sub_child_w) &&
				(binary_quad_tree_y[left_qt_idx] <= y) && (binary_quad_tree_y[left_qt_idx] > y_sub_child_w)) {

			return getColor(x, y, child_w, left_qt_idx);

		} else {
			int right_qt_idx = (qt_idx << 1) + 2;
			return getColor(x, y, child_w, right_qt_idx);
		}
	}

	public static final int NOT_INITIALZIED = -1;
	public static final int[][] QUADRANTS = {{0, 0}, {1, 0}, {0, 1}, {1, 1}};
	public static final int[] pow2 = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024};
	public static final int INFINITY = 100000000;

	// With memoization (top-down)
	static final int solve(int x, int y, int log2W, String[] rows, int[][] count_1s, int[][][] memoized) {
		if (log2W == 0) {
			return 0;
		}

		if (memoized[log2W][x][y] != NOT_INITIALZIED) {
			return memoized[log2W][x][y];
		}

		int newLog2W = log2W - 1;
		int newW = pow2[newLog2W];

		int result = INFINITY;
		for (int w = 0; w < QUADRANTS.length; w++) {
			int[] dxyw = QUADRANTS[w];
			for (int b = 0; b < QUADRANTS.length; b++) {
				int[] dxyb = QUADRANTS[b];
				if (w != b) {
					int currResult = diff(x + (dxyb[0] * newW), y + (dxyb[1] * newW), newW, rows, BLACK, count_1s);
					currResult += diff(x + (dxyw[0] * newW), y + (dxyw[1] * newW), newW, rows, WHITE, count_1s);
					for (int d = 0; d < QUADRANTS.length; d++) {
						int[] d1 = QUADRANTS[d];
						if ((d != b) && (d != w)) {
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
	static final int diff(int startRow, int startCol, int w, String[] rows, char chr, int[][] count_1s) {
		int up = (startRow > 0) ? count_1s[startRow - 1][(startCol + w) - 1] : 0;
		int left = (startCol > 0) ? count_1s[(startRow + w) - 1][startCol - 1] : 0;
		int left_up = (startRow > 0) && (startCol > 0) ? count_1s[startRow - 1][startCol - 1] : 0;
		int curr = count_1s[(startRow + w) - 1][(startCol + w) - 1];
		int count_1s_precalculated = (curr - left - up) + left_up;
		int count_0s_precalculated = (w * w) - count_1s_precalculated;
		int result = (chr == WHITE) ? count_0s_precalculated : count_1s_precalculated;
		return result;
	}

	// O(N^2)
	private static final int[][] count_1s(String[] rows) {
		int dim = rows.length;
		int[][] count = new int[dim][dim];
		for (int row = 0; row < dim; row++) {
			for (int col = 0; col < dim; col++) {
				int left = (col > 0) ? count[row][col - 1] : 0;
				int up = (row > 0) ? count[row - 1][col] : 0;
				int left_up = (row > 0) && (col > 0) ? count[row - 1][col - 1] : 0;
				int curr = rows[row].charAt(col) == WHITE ? 1 : 0;
				count[row][col] = ((left + up) - left_up) + curr;
			}
		}
		return count;
	}
}