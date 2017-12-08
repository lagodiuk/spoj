package spoj.dynamic_programming.digit_dynamic_programming;

// http://www.spoj.com/problems/PARTIT/

import java.util.Arrays;

public class PARTIT {

	public static void main(String[] args) {
		int arrSize = 10;
		int num = 18;

		// generate(arrSzie, num, 1317);

		int count = calculate(arrSize, num);
		for (int kth = 1; kth <= count; kth++) {
			printKth(0, 1, 0, kth, arrSize, num);
		}
	}

	static void generate(int arrSize, int targetSum, int kth) {
		calculate(arrSize, targetSum);
		printKth(0, 1, 0, kth, arrSize, targetSum);
	}

	static int calculate(int arrSize, int targetSum) {
		for (int i = 0; i < dp.length; i++) {
			for (int j = 0; j < dp[i].length; j++) {
				Arrays.fill(dp[i][j], -1);
			}
		}
		return calculate(0, 1, 0, targetSum, arrSize);
	}

	private static void printKth(int pos, int prev, int sum, int kth, int arrSize, int targetSum) {
		if (pos == arrSize) {
			return;
		}

		for (int i = prev; i <= maxValue; i++) {
			if (((sum + i) <= maxSum) && (dp[pos + 1][i][sum + i] != -1)) {

				if (dp[pos + 1][i][sum + i] >= kth) {
					System.out.print(i + " ");
					printKth(pos + 1, i, sum + i, kth, arrSize, targetSum);
					return;
				}

				if (dp[pos + 1][i][sum + i] < kth) {
					kth -= dp[pos + 1][i][sum + i];
					continue;
				}
			}
		}

		System.out.println(targetSum - sum);
	}

	static final int maxSum = 220;
	static final int maxArrSize = 10;
	static final int maxValue = 220;
	static int[][][] dp = new int[maxArrSize + 1][maxValue + 1][maxSum + 1];
	static int calculate(int pos, int prev, int sum, int targetSum, int arrSize) {
		if (sum > targetSum) {
			return 0;
		}
		if (pos == arrSize) {
			return (sum == targetSum) ? 1 : 0;
		}

		if (dp[pos][prev][sum] == -1) {
			dp[pos][prev][sum] = 0;
			for (int i = prev; i <= maxValue; i++) {
				dp[pos][prev][sum] += calculate(pos + 1, i, sum + i, targetSum, arrSize);
			}
		}
		return dp[pos][prev][sum];
	}
}
