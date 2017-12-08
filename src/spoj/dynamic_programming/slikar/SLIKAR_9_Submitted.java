package spoj.dynamic_programming.slikar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

// http://www.spoj.com/problems/SLIKAR/

public class SLIKAR_9_Submitted {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int rows_num = Integer.parseInt(br.readLine());
		String[] rows = new String[rows_num];
		for (int i = 0; i < rows_num; i++) {
			rows[i] = br.readLine();
		}

		solve(rows);
	}

	static final void solve(String[] rows) {
		int[][] count_1s = count_1s(rows);
		int log2W = Arrays.binarySearch(pow2, rows.length);
		int[][][] memoized = new int[log2W + 1][rows.length + 1][rows.length + 1];
		for (int[][] slice : memoized) {
			for (int[] row : slice) {
				Arrays.fill(row, NOT_INITIALZIED);
			}
		}
		int min_diff = solve(0, 0, log2W, rows, count_1s, memoized);

		System.out.println(min_diff);

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
		trace_back(log2W, rows, count_1s, memoized, 0);
		StringBuilder sb = new StringBuilder(rows.length + 1);
		for (int x = 0; x < rows.length; x++) {
			for (int y = 0; y < rows.length; y++) {
				sb.append(getColor(x, y, rows.length, 0));
			}
			System.out.println(sb.toString());
			sb.setLength(0);
		}
	}

	private static final char NO_COLOR = 'x';
	private static final char WHITE = '1';
	private static final char BLACK = '0';

	// This can be refactored
	static final void trace_back(final int log2W, final String[] rows, final int[][] count_1s, final int[][][] memoized, final int qt_idx) {

		final int x = binary_quad_tree_x[qt_idx];
		final int y = binary_quad_tree_y[qt_idx];

		if (log2W == 0) {
			binary_quad_tree_color[qt_idx] = rows[x].charAt(y);
			return;
		}

		final int newLog2W = log2W - 1;
		final int newW = pow2[newLog2W];

		final int left_idx = (qt_idx << 1) + 1;
		final int right_idx = left_idx + 1;

		binary_quad_tree_color[left_idx] = NO_COLOR;
		binary_quad_tree_color[right_idx] = NO_COLOR;

		int white_diff;
		int[] d1;
		int[] d2;
		int currResult;

		for (final int[] dxyw : QUADRANTS) {

			binary_quad_tree_x_white[qt_idx] = x + (dxyw[0] * newW);
			binary_quad_tree_y_white[qt_idx] = y + (dxyw[1] * newW);
			white_diff = diff(binary_quad_tree_x_white[qt_idx], binary_quad_tree_y_white[qt_idx], newW, rows, WHITE, count_1s);

			for (final int[] dxyb : QUADRANTS) {
				if (dxyw != dxyb) {

					binary_quad_tree_x_black[qt_idx] = x + (dxyb[0] * newW);
					binary_quad_tree_y_black[qt_idx] = y + (dxyb[1] * newW);

					currResult = diff(binary_quad_tree_x_black[qt_idx], binary_quad_tree_y_black[qt_idx], newW, rows, BLACK, count_1s);
					currResult += white_diff;

					d1 = null;
					d2 = null;
					for (final int[] d : QUADRANTS) {
						if ((d != dxyb) && (d != dxyw)) {
							if (d1 == null) {
								d1 = d;
							} else {
								d2 = d;
								break;
							}
						}
					}

					binary_quad_tree_x[left_idx] = x + (d1[0] * newW);
					binary_quad_tree_y[left_idx] = y + (d1[1] * newW);

					binary_quad_tree_x[right_idx] = x + (d2[0] * newW);
					binary_quad_tree_y[right_idx] = y + (d2[1] * newW);

					currResult += memoized[newLog2W][binary_quad_tree_x[left_idx]][binary_quad_tree_y[left_idx]];
					currResult += memoized[newLog2W][binary_quad_tree_x[right_idx]][binary_quad_tree_y[right_idx]];

					if (currResult == memoized[log2W][x][y]) {

						trace_back(newLog2W, rows, count_1s, memoized, left_idx);
						trace_back(newLog2W, rows, count_1s, memoized, right_idx);

						return;
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

	static final char getColor(final int x, final int y, final int w, final int qt_idx) {
		if (binary_quad_tree_color[qt_idx] != NO_COLOR) {
			return binary_quad_tree_color[qt_idx];
		}

		final int child_w = w >>> 1;
		final int x_sub_child_w = x - child_w;
		final int y_sub_child_w = y - child_w;

		if ((binary_quad_tree_x_black[qt_idx] <= x) && (binary_quad_tree_x_black[qt_idx] > x_sub_child_w) &&
				(binary_quad_tree_y_black[qt_idx] <= y) && (binary_quad_tree_y_black[qt_idx] > y_sub_child_w)) {

			return BLACK;
		}

		if ((binary_quad_tree_x_white[qt_idx] <= x) && (binary_quad_tree_x_white[qt_idx] > x_sub_child_w) &&
				(binary_quad_tree_y_white[qt_idx] <= y) && (binary_quad_tree_y_white[qt_idx] > y_sub_child_w)) {

			return WHITE;
		}

		final int left_qt_idx = (qt_idx << 1) + 1;
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
	static final int solve(final int x, final int y, final int log2W, final String[] rows, final int[][] count_1s, final int[][][] memoized) {

		if (log2W == 0) {
			memoized[log2W][x][y] = 0;
			return 0;
		}

		if (memoized[log2W][x][y] != NOT_INITIALZIED) {
			return memoized[log2W][x][y];
		}

		final int newLog2W = log2W - 1;
		final int newW = pow2[newLog2W];

		int white_diff;
		int currResult;
		int[] d1;
		int[] d2;

		int result = INFINITY;
		for (final int[] dxyw : QUADRANTS) {
			white_diff = diff(x + (dxyw[0] * newW), y + (dxyw[1] * newW), newW, rows, WHITE, count_1s);

			for (final int[] dxyb : QUADRANTS) {
				if (dxyw != dxyb) {
					currResult = diff(x + (dxyb[0] * newW), y + (dxyb[1] * newW), newW, rows, BLACK, count_1s);
					currResult += white_diff;

					d1 = null;
					d2 = null;
					for (final int[] d : QUADRANTS) {
						if ((d != dxyb) && (d != dxyw)) {
							if (d1 == null) {
								d1 = d;
							} else {
								d2 = d;
								break;
							}
						}
					}
					currResult += solve(x + (d1[0] * newW), y + (d1[1] * newW), newLog2W, rows, count_1s, memoized);
					currResult += solve(x + (d2[0] * newW), y + (d2[1] * newW), newLog2W, rows, count_1s, memoized);

					result = Math.min(result, currResult);
				}
			}
		}

		memoized[log2W][x][y] = result;
		return result;
	}

	// O(1)
	static final int diff(final int startRow, final int startCol, final int w, final String[] rows, final char chr, final int[][] count_1s) {
		final int up = (startRow > 0) ? count_1s[startRow - 1][(startCol + w) - 1] : 0;
		final int left = (startCol > 0) ? count_1s[(startRow + w) - 1][startCol - 1] : 0;
		final int left_up = (startRow > 0) && (startCol > 0) ? count_1s[startRow - 1][startCol - 1] : 0;
		final int curr = count_1s[(startRow + w) - 1][(startCol + w) - 1];
		final int count_1s_precalculated = (curr - left - up) + left_up;
		final int result = (chr == WHITE) ? ((w * w) - count_1s_precalculated) : count_1s_precalculated;
		return result;
	}

	// O(N^2)
	private static final int[][] count_1s(final String[] rows) {
		final int dim = rows.length;
		final int[][] count = new int[dim][dim];
		for (int row = 0; row < dim; row++) {
			for (int col = 0; col < dim; col++) {
				final int left = (col > 0) ? count[row][col - 1] : 0;
				final int up = (row > 0) ? count[row - 1][col] : 0;
				final int left_up = (row > 0) && (col > 0) ? count[row - 1][col - 1] : 0;
				final int curr = rows[row].charAt(col) == WHITE ? 1 : 0;
				count[row][col] = ((left + up) - left_up) + curr;
			}
		}
		return count;
	}
}