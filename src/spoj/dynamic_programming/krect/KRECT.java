package spoj.dynamic_programming.krect;

// http://www.spoj.com/problems/KRECT/en/

// Solution is very similar to solution of problem about:
// "Maximum sum rectangle in a 2D matrix"

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class KRECT {

	public static void main(String[] args) {
		char[][] board = new char[][]{
				"CED".toCharArray(),
				"CEB".toCharArray(),
				"CBC".toCharArray(),
				"DDA".toCharArray()
		};

		for (int i = 1; i < 8; i++) {
			test(board, i);
		}

		Random rnd = new Random(1);
		for (int i = 0; i < 30; i++) {
			board = generateRandomBoard(rnd);
			for (int j = 1; j < Math.max(board.length, board[0].length); j++) {
				test(board, j);
			}
		}

		// Upper bound:
		board = new char[100][100];
		for (char[] row : board) {
			for (int i = 0; i < row.length; i++) {
				row[i] = 'A';
			}
		}
		printProblemDescription(board, 1);
		int r3 = solve3(board, 1);
		System.out.println(r3);
	}

	static void test(char[][] board, int kDifferent) {
		printProblemDescription(board, kDifferent);

		Set<Integer> results = new HashSet<>();

		int r1 = solve(board, kDifferent);
		System.out.println(r1);
		results.add(r1);

		int r2 = solve2(board, kDifferent);
		System.out.println(r2);
		results.add(r2);

		int r3 = solve3(board, kDifferent);
		System.out.println(r3);
		results.add(r3);

		System.out.println();

		if (results.size() != 1) {
			throw new RuntimeException("Different results detected!");
		}
	}

	private static void printProblemDescription(char[][] board, int kDifferent) {
		System.out.println(board.length + " " + board[0].length + " " + kDifferent);
		for (char[] row : board) {
			for (int i = 0; i < row.length; i++) {
				System.out.print(row[i]);
			}
			System.out.println();
		}
	}

	// O(H^2 * W^2) - optimized
	static int solve3(char[][] board, int kDifferent) {
		int height = board.length;
		int width = board[0].length;

		int result = 0;

		int[] aggregatedRows = new int[width];

		for (int yMin = 0; yMin < height; yMin++) {
			Arrays.fill(aggregatedRows, 0);

			for (int yMax = yMin; yMax < height; yMax++) {
				for (int x = 0; x < width; x++) {
					aggregatedRows[x] |= 1 << (board[yMax][x] - 'A');
				}

				for (int xMin = 0; xMin < width; xMin++) {
					int chars = 0;

					for (int xMax = xMin; xMax < width; xMax++) {
						chars |= aggregatedRows[xMax];

						// count 1-bits
						int cardinality = cardinality_ConstantTime(chars);

						if (cardinality == kDifferent) {
							result++;
						}
					}
				}
			}
		}

		return result;
	}

	// Complexity: O(number of non-zero bits)
	static int cardinality_LinearTime(int num) {
		int cardinality = 0;
		while (num != 0) {
			num &= (num - 1);
			cardinality++;
		}
		return cardinality;
	}

	// Contains bits count for 8-bit integers
	static int[] lookupTable = new int[256];
	static {
		for (int i = 0; i < 256; i++) {
			lookupTable[i] = cardinality_LinearTime(i);
		}
	}

	// Complexity: O(1)
	static int cardinality_ConstantTime(int num) {
		int cardinality = 0;
		cardinality += lookupTable[num & 0xff];
		cardinality += lookupTable[(num >> 8) & 0xff];
		cardinality += lookupTable[(num >> 16) & 0xff];
		cardinality += lookupTable[(num >> 24) & 0xff];
		return cardinality;
	}

	// Solution is very similar to solution of problem about:
	// "Maximum sum rectangle in a 2D matrix"
	//
	// Complexity:
	// O(H * (W + H * (W + W * W * X))) =
	// = O(H * W + H^2 * (W + W^2 * X)) =
	// = O(H * W + H^2 * W + H^2 * W^2 * X) =
	// = O(H^2 * W^2 * X)
	// W - Width
	// H - Height
	// X - Complexity of method BitSet.cardinality
	static int solve2(char[][] board, int kDifferent) {
		int height = board.length;
		int width = board[0].length;

		int result = 0;

		BitSet[] aggregatedRows = new BitSet[width];

		for (int yMin = 0; yMin < height; yMin++) {
			for (int x = 0; x < width; x++) {
				aggregatedRows[x] = new BitSet();
			}

			for (int yMax = yMin; yMax < height; yMax++) {
				for (int x = 0; x < width; x++) {
					aggregatedRows[x].set(board[yMax][x] - 'A');
				}

				for (int xMin = 0; xMin < width; xMin++) {
					BitSet chars = new BitSet();
					for (int xMax = xMin; xMax < width; xMax++) {
						chars.or(aggregatedRows[xMax]);
						if (chars.cardinality() == kDifferent) {
							result++;
						}
					}
				}
			}
		}

		return result;
	}

	// Complexity: O(W^3 * H^3)
	// W - Width
	// H - Height
	static int solve(char[][] board, int kDifferent) {
		int height = board.length;
		int width = board[0].length;

		int result = 0;

		for (int y1 = 0; y1 < height; y1++) {
			for (int x1 = 0; x1 < width; x1++) {
				for (int y2 = y1; y2 < height; y2++) {
					for (int x2 = x1; x2 < width; x2++) {

						BitSet chars = new BitSet(26);
						for (int h = y1; h <= y2; h++) {
							for (int w = x1; w <= x2; w++) {
								chars.set(board[h][w] - 'A');
							}
						}

						if (chars.cardinality() == kDifferent) {
							result++;
						}
					}
				}
			}
		}

		return result;
	}

	static char[][] generateRandomBoard(Random rnd) {
		// char[][] board = new char[rnd.nextInt(80) + 1][rnd.nextInt(80) + 1];
		char[][] board = new char[rnd.nextInt(30) + 1][rnd.nextInt(30) + 1];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = (char) ('A' + rnd.nextInt(26));
			}
		}
		return board;
	}
}
