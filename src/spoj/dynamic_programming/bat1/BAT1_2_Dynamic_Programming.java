package spoj.dynamic_programming.bat1;

import java.util.Arrays;

// http://www.spoj.com/problems/BAT1/

public class BAT1_2_Dynamic_Programming {

	public static void main(String[] args) {
		BAT1_1_Brute_Force.test(BAT1_2_Dynamic_Programming::solve);
		BAT1_1_Brute_Force.test(BAT1_2_Dynamic_Programming::solve, false, 20, 20, 1000, 20, 100);
	}

	static final int NEGATIVE_INF = -1000000;
	static final int MAX_MONEY = 10002;
	static final int MAX_BATCHES = 20;
	static final int MAX_BATCH_SIZE = 20;
	static final int NOT_INITIALIZED = -1;
	static final int[][][][] memoized = new int[2][MAX_BATCHES][MAX_BATCH_SIZE][MAX_MONEY];

	static int buckets_amount;
	static int items_per_bucket;

	static int solve(int batches, int batch_size, int money, int[] open_cost, int[][] cost, int[][] rating) {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < MAX_BATCHES; j++) {
				for (int k = 0; k < MAX_BATCH_SIZE; k++) {
					Arrays.fill(memoized[i][j][k], NOT_INITIALIZED);
				}
			}
		}
		buckets_amount = batches;
		items_per_bucket = batch_size;
		int result = solve(money, open_cost, cost, rating, 0, 0, 0);
		return result;
	}

	static int solve(int money, int[] open_cost, int[][] cost, int[][] rating, int bucket_num, int item_num, int opened) {

		if (money < 0) {
			return NEGATIVE_INF;
		}

		if (bucket_num == buckets_amount) {
			return 0;
		}

		if (item_num == items_per_bucket) {
			return NEGATIVE_INF;
		}

		if (memoized[opened][bucket_num][item_num][money] != NOT_INITIALIZED) {
			return memoized[opened][bucket_num][item_num][money];
		}

		int item_cost = cost[bucket_num][item_num];
		int item_rating = rating[bucket_num][item_num];
		if (opened == 0) {
			item_cost += open_cost[bucket_num];
		}
		int f1 = solve(money - item_cost, open_cost, cost, rating, bucket_num, item_num, 1) + item_rating;

		int f2 = solve(money, open_cost, cost, rating, bucket_num, item_num + 1, opened);

		int f3 = solve(money, open_cost, cost, rating, bucket_num + 1, 0, 0);

		memoized[opened][bucket_num][item_num][money] = Math.max(f1, Math.max(f2, f3));
		return memoized[opened][bucket_num][item_num][money];
	}
}
