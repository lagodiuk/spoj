package spoj.dynamic_programming.service;

// http://www.spoj.com/problems/SERVICE/

import java.util.Arrays;
import java.util.Random;
import java.util.function.BiFunction;

public class SERVICE_DP_Simple {

	public static final int POSITIVE_INFINITY = 2000002;

	public static void test(BiFunction<int[][], int[], Integer> f) {
		Random rnd = new Random(1);

		int maxCities = 15;
		int maxOrders = 40;
		int maxCost = 100;
		int testsNum = 100;

		for (int test = 0; test < testsNum; test++) {

			int citiesCount = rnd.nextInt(maxCities - 3) + 3;
			int[][] costMatrix = new int[citiesCount][citiesCount];
			for (int i = 0; i < citiesCount; i++) {
				for (int j = 0; j < citiesCount; j++) {
					costMatrix[i][j] = rnd.nextInt(maxCost);
				}
				costMatrix[i][i] = 0;
			}

			int[] orders = new int[rnd.nextInt(maxOrders) + 10];
			for (int i = 0; i < orders.length; i++) {
				orders[i] = rnd.nextInt(citiesCount) + 1;
			}

			System.out.println(citiesCount + " " + orders.length);
			for (int i = 0; i < citiesCount; i++) {
				for (int j = 0; j < citiesCount; j++) {
					System.out.printf("%3d", costMatrix[i][j]);
				}
				System.out.println();
			}
			for (int i = 0; i < orders.length; i++) {
				System.out.print(orders[i] + " ");
			}
			System.out.println();

			int c1 = minCost(costMatrix, orders);
			System.out.println(c1);

			Integer c2 = f.apply(costMatrix, orders);
			System.out.println(c2);

			System.out.println();

			if (c1 != c2) {
				throw new RuntimeException();
			}
		}
	}

	public static void test_example_from_problem_description(BiFunction<int[][], int[], Integer> f) {
		int[][] costMatrix = new int[][]{
				{0, 1, 1, 1, 1},
				{1, 0, 2, 3, 2},
				{1, 1, 0, 4, 1},
				{2, 1, 5, 0, 1},
				{4, 2, 3, 4, 0}
		};
		int[] orders = new int[]{4, 2, 4, 1, 5, 4, 3, 2, 1};

		System.out.println(minCost(costMatrix, orders));
		System.out.println(f.apply(costMatrix, orders));
		System.out.println();
	}

	static int minCost(int[][] cost, int[] orders) {
		int[][][][] memoized = new int[cost.length][cost.length][cost.length][orders.length];
		for (int i = 0; i < cost.length; i++) {
			for (int j = 0; j < cost.length; j++) {
				for (int k = 0; k < cost.length; k++) {
					Arrays.fill(memoized[i][j][k], -1);
				}
			}
		}

		return minCost(0, 1, 2, 0, cost, orders, memoized);
	}

	static int minCost(int firtsPos, int secondPos, int thirdPos, int orderIdx, int[][] cost, int[] orders, int[][][][] memoized) {
		if (orderIdx == orders.length) {
			return 0;
		}

		if ((firtsPos == secondPos) || (firtsPos == thirdPos) || (secondPos == thirdPos)) {
			// It is not allowed that two service workers are present at the same location
			return POSITIVE_INFINITY;
		}

		if (memoized[firtsPos][secondPos][thirdPos][orderIdx] != -1) {
			return memoized[firtsPos][secondPos][thirdPos][orderIdx];
		}

		int nextLocation = orders[orderIdx] - 1;

		memoized[firtsPos][secondPos][thirdPos][orderIdx] = Math.min(Math.min(
				minCost(nextLocation, secondPos, thirdPos, orderIdx + 1, cost, orders, memoized) + cost[firtsPos][nextLocation],
				minCost(firtsPos, nextLocation, thirdPos, orderIdx + 1, cost, orders, memoized) + cost[secondPos][nextLocation]),
				minCost(firtsPos, secondPos, nextLocation, orderIdx + 1, cost, orders, memoized) + cost[thirdPos][nextLocation]);

		return memoized[firtsPos][secondPos][thirdPos][orderIdx];
	}
}
