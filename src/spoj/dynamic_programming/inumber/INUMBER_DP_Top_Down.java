package spoj.dynamic_programming.inumber;

import java.util.Arrays;

// http://www.spoj.com/problems/INUMBER/

public class INUMBER_DP_Top_Down {

	public static void main(String[] args) {
		for (int i = 1; i <= 1000; i++) {
			System.out.println(i);
			solve(i);
			System.out.println();
		}
	}

	static final int MAX_LEN = 121;
	static final int MAX_NUM = 1001;
	static final int INF = 1000000;

	static int[][][] mem = new int[MAX_NUM][MAX_NUM][MAX_LEN];

	static void solve(int num) {
		for (int[][] x : mem) {
			for (int[] y : x) {
				Arrays.fill(y, 0);
			}
		}

		minLen(num, 0, 0, num);
		backtrack(num, 0, 0, num);
		System.out.println();
	}

	static int minLen(int sum, int mod, int pos, int num) {
		if ((sum < 0) || (pos >= MAX_LEN) || (((MAX_LEN - pos) * 9) < sum)) {
			return INF;
		}

		if ((sum == 0) && (mod == 0)) {
			return 0;
		}

		if (mem[sum][mod][pos] != 0) {
			return mem[sum][mod][pos];
		}

		mem[sum][mod][pos] = INF;

		for (int i = 0; i <= 9; i++) {
			mem[sum][mod][pos] = Math.min(mem[sum][mod][pos],
					minLen(sum - i, ((mod * 10) + i) % num, pos + 1, num) + 1);
		}

		return mem[sum][mod][pos];
	}

	static void backtrack(int sum, int mod, int pos, int num) {
		if ((sum == 0) && (mod == 0)) {
			return;
		}

		for (int i = 0; i <= 9; i++) {
			if ((sum >= i)
					&& (mem[sum][mod][pos]
						== (mem[sum - i][((mod * 10) + i) % num][pos + 1] + 1))) {

				System.out.print(i);
				backtrack(sum - i, ((mod * 10) + i) % num, pos + 1, num);
				return;
			}
		}

		throw new RuntimeException();
	}

}
