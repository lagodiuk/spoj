package spoj.dynamic_programming;

// http://www.spoj.com/problems/MARTIAN/

public class MARTIAN {

	static int[][] yeyenum;
	static int[][] bloggium;

	// 1)
	// Exponential complexity
	static long calculate(int eastIndex, int southIndex) {
		if ((eastIndex < 0) || (southIndex < 0)) {
			return 0;
		}

		// TODO: pre-calculate (cumulative-sum-array)
		long sumYeyenum = 0;
		for (int e = eastIndex; e >= 0; e--) {
			sumYeyenum += yeyenum[southIndex][e];
		}

		// TODO: pre-calculate (cumulative-sum-array)
		long sumBloggium = 0;
		for (int s = southIndex; s >= 0; s--) {
			sumBloggium += bloggium[s][eastIndex];
		}

		// TODO: use memoization
		return Math.max(
				calculate(eastIndex - 1, southIndex) + sumBloggium,
				calculate(eastIndex, southIndex - 1) + sumYeyenum);
	}

	// 2)
	// Bottom-up Dynamic Programming solution (based on exponential solution 1)
	// O(height * width) complexity
	static long calculate2(int eastIndex, int southIndex) {
		// transforming yeyenum - to "East-to-West" cumulative-sum-array
		for (int s = 0; s <= southIndex; s++) {
			for (int e = 1; e <= eastIndex; e++) {
				yeyenum[s][e] += yeyenum[s][e - 1];
			}
		}

		// transforming bloggium - to "South-to-North" cumulative-sum-array
		for (int e = 0; e <= eastIndex; e++) {
			for (int s = 1; s <= southIndex; s++) {
				bloggium[s][e] += bloggium[s - 1][e];
			}
		}

		// allocating array with "left" and "up" margins:
		// result[0][i] == 0 - for each i
		// result[j][0] == 0 - for each j
		//
		// by this reason, all indices of array "result" - are shifted by 1
		// (in comparison to indices of arrays "yeyenum" and "bloggium")
		// so, for given cell "result[s][w]" - corresponding cells are:
		// "yeyenum[s - 1][e - 1]" and "bloggium[s - 1][e - 1]"
		//
		// array with margins helps to process edge cases in homogeneous way
		long[][] result = new long[southIndex + 2][eastIndex + 2];

		for (int s = 1; s <= (southIndex + 1); s++) {
			for (int e = 1; e <= (eastIndex + 1); e++) {
				result[s][e] = Math.max(
						result[s - 1][e] + yeyenum[s - 1][e - 1],
						result[s][e - 1] + bloggium[s - 1][e - 1]);
			}
		}

		return result[southIndex + 1][eastIndex + 1];
	}

	public static void main(String[] args) {
		yeyenum = new int[][]{
				{0, 0, 10, 9},
				{1, 3, 10, 0},
				{4, 2, 1, 3},
				{1, 1, 20, 0}
		};

		bloggium = new int[][]{
				{10, 0, 0, 0},
				{1, 1, 1, 30},
				{0, 0, 5, 5},
				{5, 10, 10, 10}
		};

		System.out.println(calculate(3, 3));
		System.out.println(calculate2(3, 3));
	}
}
