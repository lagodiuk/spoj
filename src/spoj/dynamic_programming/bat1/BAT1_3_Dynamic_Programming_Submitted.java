package spoj.dynamic_programming.bat1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

// http://www.spoj.com/problems/BAT1/

public class BAT1_3_Dynamic_Programming_Submitted {

	public static void main(String[] args) throws Exception {
		FastScanner sc = new FastScanner(new InputStreamReader(System.in));
		int testsNum = sc.nextInt();
		for (int test = 0; test < testsNum; test++) {
			buckets_amount = sc.nextInt();
			items_per_bucket = sc.nextInt();
			int money = sc.nextInt();
			for (int b = 0; b < buckets_amount; b++) {
				open_cost[b] = sc.nextInt();
			}
			for (int b = 0; b < buckets_amount; b++) {
				for (int ib = 0; ib < items_per_bucket; ib++) {
					cost[b][ib] = sc.nextInt();
				}
			}
			for (int b = 0; b < buckets_amount; b++) {
				for (int ib = 0; ib < items_per_bucket; ib++) {
					rating[b][ib] = sc.nextInt();
				}
			}
			int result = solve(money);
			System.out.println(result);
		}
	}

	static final int NEGATIVE_INF = -1000000;
	static final int MAX_MONEY = 10002;
	static final int MAX_BATCHES = 21;
	static final int MAX_BATCH_SIZE = 21;
	static final int NOT_INITIALIZED = -1;
	static final int[][][][] memoized = new int[2][MAX_BATCHES][MAX_BATCH_SIZE][MAX_MONEY];
	static final int[] open_cost = new int[MAX_BATCHES];
	static final int[][] cost = new int[MAX_BATCHES][MAX_BATCH_SIZE];
	static final int[][] rating = new int[MAX_BATCHES][MAX_BATCH_SIZE];

	static int buckets_amount;
	static int items_per_bucket;

	static final int solve(final int money) {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j <= buckets_amount; j++) {
				for (int k = 0; k <= items_per_bucket; k++) {
					Arrays.fill(memoized[i][j][k], NOT_INITIALIZED);
				}
			}
		}
		int result = solve(money, 0, 0, 0);
		return result;
	}

	static final int solve(final int money, final int bucket_num, final int item_num, final int opened) {

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

		int f1;
		if (opened == 0) {
			f1 = solve(money - cost[bucket_num][item_num] - open_cost[bucket_num], bucket_num, item_num, 1) + rating[bucket_num][item_num];
		} else {
			f1 = solve(money - cost[bucket_num][item_num], bucket_num, item_num, 1) + rating[bucket_num][item_num];
		}

		int f2 = solve(money, bucket_num, item_num + 1, opened);

		int f3 = solve(money, bucket_num + 1, 0, 0);

		memoized[opened][bucket_num][item_num][money] = Math.max(f1, Math.max(f2, f3));
		return memoized[opened][bucket_num][item_num][money];
	}

	// http://neerc.ifmo.ru/trains/information/Template.java
	static final class FastScanner {
		final BufferedReader br;
		StringTokenizer st;

		FastScanner(InputStreamReader in) throws Exception {
			this.br = new BufferedReader(in);
		}

		final String next() {
			while ((this.st == null) || !this.st.hasMoreTokens()) {
				try {
					this.st = new StringTokenizer(this.br.readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return this.st.nextToken();
		}

		final int nextInt() {
			return Integer.parseInt(this.next());
		}
	}
}
