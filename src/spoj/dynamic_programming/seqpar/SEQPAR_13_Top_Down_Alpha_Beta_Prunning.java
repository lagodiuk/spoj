package spoj.dynamic_programming.seqpar;

import java.util.Arrays;

// http://www.spoj.com/problems/SEQPAR/

public class SEQPAR_13_Top_Down_Alpha_Beta_Prunning {

	public static void main(String[] args) {
		SEQPAR_BruteForce.testFewManualCases(SEQPAR_13_Top_Down_Alpha_Beta_Prunning::solve);
		SEQPAR_BruteForce.testRandomized(SEQPAR_13_Top_Down_Alpha_Beta_Prunning::solve);
		SEQPAR_BruteForce.testRandomized(SEQPAR_13_Top_Down_Alpha_Beta_Prunning::solve, 1000, 50, false);
	}

	private static final int INFINITY = 10000000;

	static int solve(int[] arr, int count) {
		int[][] memoized = new int[arr.length][count + 1];
		for (int[] row : memoized) {
			Arrays.fill(row, -1);
		}

		SumOnInterval[][] sois = getSumsOnIntervals_Optimized(arr, count);

		int result = solve(arr, 0, count, memoized, sois);
		printMemoizedMatrix(memoized);
		return result;
	}

	private static SumOnInterval[][] getSumsOnIntervals(int[] arr, int count) {
		SumOnInterval[][] sois = new SumOnInterval[arr.length][];
		for (int start = 0; start <= (arr.length - 1); start++) {
			sois[start] = new SumOnInterval[arr.length - start];
			int currSum = 0;
			for (int i = start + 1; i <= arr.length; i++) {
				currSum += arr[i - 1];
				sois[start][i - start - 1] = new SumOnInterval(currSum, i);
			}
			Arrays.sort(sois[start], (s1, s2) -> Integer.compare(s1.sum, s2.sum));
		}
		return sois;
	}

	private static SumOnInterval[][] getSumsOnIntervals_Optimized(int[] arr, int count) {
		int d = (arr.length - count) + 1;
		SumOnInterval[][] sois = new SumOnInterval[arr.length][];
		for (int start = 0; start <= (arr.length - 1); start++) {
			sois[start] = new SumOnInterval[Math.min(arr.length - start, d)];
			int currSum = 0;
			for (int i = start + 1; i <= Math.min(arr.length, start + d); i++) {
				currSum += arr[i - 1];
				sois[start][i - start - 1] = new SumOnInterval(currSum, i);
			}
			Arrays.sort(sois[start], (s1, s2) -> Integer.compare(s1.sum, s2.sum));
		}
		return sois;
	}

	private static void printMemoizedMatrix(int[][] memoized) {
		for (int[] row : memoized) {
			for (int x : row) {
				if (x == INFINITY) {
					System.out.printf("%5s", "i");
				} else if (x == -1) {
					System.out.printf("%5s", ".");
				} else {
					System.out.printf("%5d", x);
				}
			}
			System.out.println();
		}
	}

	/**
	 * 2 variables represents state
	 * With memoization O(N^2)
	 * Pruning branches without solutions
	 */
	static int solve(int[] arr, int curr, int segments, int[][] memoized, SumOnInterval[][] sois) {
		if ((segments == 0) && (curr == arr.length)) {
			return -INFINITY;
		}

		if ((segments <= 0) || (curr >= arr.length)) {
			return INFINITY;
		}

		if (memoized[curr][segments] != -1) {
			return memoized[curr][segments];
		}

		// Pruning of branches without solution:
		// we need to iterate only while (i <= (arr.length - segments) + 1)
		// int left = curr + 1;
		int right = (arr.length - segments) + 1;
		// List<SumOnInterval> sums = new ArrayList<>();
		// int currSum = 0;
		// for (int i = left; i <= right; i++) {
		// currSum += arr[i - 1];
		// sums.add(new SumOnInterval(currSum, i));
		// }
		// Collections.sort(sums, (s1, s2) -> Integer.compare(s1.sum, s2.sum));

		int result = INFINITY;
		if (sois[curr] != null) {
			for (SumOnInterval soi : sois[curr]) {
				if (soi.sum >= result) {
					break;
				}
				if (soi.end <= right) {
					int subProblem = solve(arr, soi.end, segments - 1, memoized, sois);
					int incl = Math.max(soi.sum, subProblem);
					result = Math.min(incl, result);
				}
			}
		}

		memoized[curr][segments] = result;
		return result;
	}

	static class SumOnInterval {
		int sum;
		int end;
		public SumOnInterval(int sum, int end) {
			this.sum = sum;
			this.end = end;
		}
		@Override
		public String toString() {
			return "(" + this.sum + ", " + this.end + ")";
		}
	}
}
