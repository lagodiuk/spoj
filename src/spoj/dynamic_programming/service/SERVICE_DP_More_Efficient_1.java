package spoj.dynamic_programming.service;

// http://www.spoj.com/problems/SERVICE/

import java.util.Arrays;

public class SERVICE_DP_More_Efficient_1 {

	public static void main(String[] args) {
		SERVICE_DP_Simple.test_example_from_problem_description(SERVICE_DP_More_Efficient_1::minCost);
		SERVICE_DP_Simple.test(SERVICE_DP_More_Efficient_2::minCost);
	}

	static int minCost(int[][] cost, int[] orders) {
		int[][][] memoized = new int[cost.length][cost.length][orders.length + 1];
		for (int i = 0; i < cost.length; i++) {
			for (int j = 0; j < cost.length; j++) {
				Arrays.fill(memoized[i][j], -1);
			}
		}

		return minCost(0, 1, -1, cost, orders, memoized);
	}

	static int minCost(int firtsPos, int secondPos, int curOrderIdx, int[][] cost, int[] orders, int[][][] memoized) {
		if (curOrderIdx == (orders.length - 1)) {
			return 0;
		}

		if (memoized[firtsPos][secondPos][curOrderIdx + 1] != -1) {
			return memoized[firtsPos][secondPos][curOrderIdx + 1];
		}

		int thirdPos = (curOrderIdx < 0) ? 2 : orders[curOrderIdx] - 1;

		if ((firtsPos == secondPos) || (firtsPos == thirdPos) || (secondPos == thirdPos)) {

			// It is not allowed that two service workers are present at the same location
			memoized[firtsPos][secondPos][curOrderIdx + 1] = SERVICE_DP_Simple.POSITIVE_INFINITY;

		} else {

			int nextOrderPos = orders[curOrderIdx + 1] - 1;

			memoized[firtsPos][secondPos][curOrderIdx + 1] = Math.min(Math.min(
					minCost(firtsPos, secondPos, curOrderIdx + 1, cost, orders, memoized) + cost[thirdPos][nextOrderPos],
					minCost(firtsPos, thirdPos, curOrderIdx + 1, cost, orders, memoized) + cost[secondPos][nextOrderPos]),
					minCost(thirdPos, secondPos, curOrderIdx + 1, cost, orders, memoized) + cost[firtsPos][nextOrderPos]);
		}

		return memoized[firtsPos][secondPos][curOrderIdx + 1];
	}

}
