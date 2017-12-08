package spoj.dynamic_programming.bat1;

import java.util.Arrays;
import java.util.Random;

// http://www.spoj.com/problems/BAT1/

public class BAT1_1_Brute_Force {

	public static void main(String[] args) {
		test(null);
	}

	static final int NEGATIVE_INF = -1000000;

	static int solve(int batches, int batch_size, int money, int[] open_cost, int[][] cost, int[][] rating) {
		int result = solve(money, open_cost, cost, rating, 0, 0, false);
		return result;
	}

	static int solve(int money, int[] open_cost, int[][] cost, int[][] rating, int bucket_num, int item_num, boolean opened) {
		if (money < 0) {
			return NEGATIVE_INF;
		}

		int buckets_amount = cost.length;
		if (bucket_num == buckets_amount) {
			return 0;
		}

		int items_per_bucket = cost[bucket_num].length;
		if (item_num == items_per_bucket) {
			return NEGATIVE_INF;
		}

		int item_cost = cost[bucket_num][item_num];
		int item_rating = rating[bucket_num][item_num];
		if (!opened) {
			item_cost += open_cost[bucket_num];
		}
		int f1 = solve(money - item_cost, open_cost, cost, rating, bucket_num, item_num, true) + item_rating;

		int f2 = solve(money, open_cost, cost, rating, bucket_num, item_num + 1, opened);

		int f3 = solve(money, open_cost, cost, rating, bucket_num + 1, 0, false);

		return Math.max(f1, Math.max(f2, f3));
	}

	static interface Solver {
		int solve(int batches, int batch_size, int money, int[] open_cost, int[][] cost, int[][] rating);
	}

	static void test(Solver solver) {
		test(solver, true, 6, 6, 30, 20, 100);
	}

	static void test(Solver solver, boolean compareWithBruteForce, int max_batches, int max_batch_size, int max_money, int max_cost, int max_rating) {
		compareSolution(
				solver,
				compareWithBruteForce,
				2, 4, 20,
				new int[]{3, 4},
				new int[][]{
						{3, 2, 3, 2},
						{3, 2, 3, 5}
				},
				new int[][]{
						{3, 2, 3, 2},
						{4, 5, 6, 5}
				});

		compareSolution(
				solver,
				compareWithBruteForce,
				2, 2, 7,
				new int[]{2, 2},
				new int[][]{
						{2, 1},
						{1, 2}
				},
				new int[][]{
						{10, 2},
						{3, 10}
				});

		Random rnd = new Random(0);
		for (int test = 0; test < 40; test++) {
			int batches = rnd.nextInt(max_batches) + 1;
			int batch_size = rnd.nextInt(max_batch_size) + 1;
			int money = rnd.nextInt(max_money) + 1;
			int[] open_cost = new int[batches];
			int[][] cost = new int[batches][batch_size];
			int[][] rating = new int[batches][batch_size];
			for (int b = 0; b < batches; b++) {
				open_cost[b] = rnd.nextInt(max_cost) + 1;
				for (int it = 0; it < batch_size; it++) {
					cost[b][it] = rnd.nextInt(max_cost) + 1;
					rating[b][it] = rnd.nextInt(max_rating) + 1;
				}
			}
			compareSolution(solver, compareWithBruteForce, batches, batch_size, money, open_cost, cost, rating);
		}
	}

	static void compareSolution(
			Solver solver,
			boolean compareWithBruteForce,
			int batches,
			int batch_size,
			int money,
			int[] open_cost,
			int[][] cost,
			int[][] rating) {

		System.out.println(batches + " " + batch_size + " " + money);
		System.out.println(Arrays.toString(open_cost).replaceAll("[\\[\\],]", ""));
		for (int[] row : cost) {
			System.out.println(Arrays.toString(row).replaceAll("[\\[\\],]", ""));
		}
		for (int[] row : rating) {
			System.out.println(Arrays.toString(row).replaceAll("[\\[\\],]", ""));
		}

		if (solver == null) {
			int result = solve(batches, batch_size, money, open_cost, cost, rating);
			System.out.println("Result: " + result);
		} else {
			if (!compareWithBruteForce) {
				int result = solver.solve(batches, batch_size, money, open_cost, cost, rating);
				System.out.println("Result: " + result);
			} else {
				int expected_result = solve(batches, batch_size, money, open_cost, cost, rating);
				int actual_result = solver.solve(batches, batch_size, money, open_cost, cost, rating);
				if (expected_result != actual_result) {
					System.out.println("Expected result: " + expected_result);
					System.out.println("Actual result: " + actual_result);
					throw new RuntimeException("Results are different!");
				} else {
					System.out.println("Result: " + actual_result);
				}
			}
		}

		System.out.println();
	}
}
