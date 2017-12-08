package spoj.dynamic_programming.mkuhar;

// http://www.spoj.com/problems/MKUHAR/

public class MKUHAR_Binary_Search_6 {

	public static void main(String[] args) throws Exception {
		// MKUHAR_Brute_Force_2.test(MKUHAR_Binary_Search_6::solve, true, 20, 30, 300, 100);
		MKUHAR_Brute_Force_2.test(MKUHAR_Binary_Search_6::solve, false, 100, 100, 100000, 200);

		// BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// String s;
		// String[] parts;
		//
		// parts = br.readLine().split(" ");
		// int ingredients_cnt = Integer.parseInt(parts[0]);
		// int money = Integer.parseInt(parts[1]);
		// for (int i = 0; i < ingredients_cnt; i++) {
		// parts = br.readLine().split(" ");
		// INGREDIENTS[i][0] = Integer.parseInt(parts[0]);
		// INGREDIENTS[i][1] = Integer.parseInt(parts[1]);
		// INGREDIENTS[i][2] = Integer.parseInt(parts[2]);
		// INGREDIENTS[i][3] = Integer.parseInt(parts[3]);
		// INGREDIENTS[i][4] = Integer.parseInt(parts[4]);
		// INGREDIENTS[i][5] = Integer.parseInt(parts[5]);
		// }
		// System.out.println(solve(money, ingredients_cnt));
	}

	static final int solve(int money, Ingredient[] ingredients) {
		int ingredients_cnt = ingredients.length;
		for (int i = 0; i < ingredients_cnt; i++) {
			INGREDIENTS[i][0] = ingredients[i].needed;
			INGREDIENTS[i][1] = ingredients[i].available;
			INGREDIENTS[i][2] = ingredients[i].size_small;
			INGREDIENTS[i][3] = ingredients[i].cost_small;
			INGREDIENTS[i][4] = ingredients[i].size_big;
			INGREDIENTS[i][5] = ingredients[i].cost_big;
		}
		return solve(money, ingredients_cnt);
	}

	static final int solve(int money, int ingredients_cnt) {
		int amount = 1;
		int total_cost = 0;
		while (true) {
			total_cost = 0;
			for (int i = 0; i < ingredients_cnt; i++) {
				total_cost += get_min_cost(i, amount);
			}
			if (total_cost > money) {
				break;
			}
			amount <<= 1;
		}

		int left = amount >>> 1;
		int right = amount;

		while (left < right) {
			int mid = (left + right + 1) >>> 1;
			total_cost = 0;
			for (int i = 0; i < ingredients_cnt; i++) {
				total_cost += get_min_cost(i, mid);
			}
			if (total_cost > money) {
				right = mid - 1;
			} else {
				left = mid;
			}
		}

		return left;
	}

	static int[][] INGREDIENTS = new int[100][6];
	static final int INFINITY = 1000000;

	static final int get_min_cost(int ing, int amount) {
		int target = (amount * INGREDIENTS[ing][0]) - INGREDIENTS[ing][1];
		target = (target >= 0) ? target : 0;
		int min_cost = INFINITY;

		int max_big_amount = target + INGREDIENTS[ing][4];
		int curr_big_amount = 0;
		int curr_big_cost = 0;
		while (curr_big_amount <= max_big_amount) {
			int rest = target - curr_big_amount;
			rest = (rest >= 0) ? rest : 0;
			boolean perfect_fit = ((rest % INGREDIENTS[ing][2]) == 0);
			int small_count = (rest / INGREDIENTS[ing][2]) + (perfect_fit ? 0 : 1);
			int cost = curr_big_cost + (small_count * INGREDIENTS[ing][3]);
			min_cost = (min_cost < cost) ? min_cost : cost;
			if ((cost > min_cost) && perfect_fit) {
				break;
			}
			curr_big_amount += INGREDIENTS[ing][4];
			curr_big_cost += INGREDIENTS[ing][5];
		}

		return min_cost;
	}
}
