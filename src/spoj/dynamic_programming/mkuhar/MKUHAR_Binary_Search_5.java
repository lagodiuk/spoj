package spoj.dynamic_programming.mkuhar;

// http://www.spoj.com/problems/MKUHAR/

public class MKUHAR_Binary_Search_5 {

	public static void main(String[] args) {
		// MKUHAR_Brute_Force_2.test(MKUHAR_Binary_Search_5::solve, true, 20, 30, 300, 100);
		MKUHAR_Brute_Force_2.test(MKUHAR_Binary_Search_5::solve, false, 100, 100, 100000, 100);
	}

	static int solve(int money, Ingredient[] ingredients) {
		int amount = 1;
		int total_cost = 0;
		while (true) {
			total_cost = 0;
			for (Ingredient ing : ingredients) {
				total_cost += get_min_cost(ing, amount);
			}
			if (total_cost > money) {
				break;
			}
			amount *= 2;
		}

		int left = amount / 2;
		int right = amount;

		while (left < right) {
			int mid = (left + right + 1) / 2;
			total_cost = 0;
			for (Ingredient ing : ingredients) {
				total_cost += get_min_cost(ing, mid);
			}
			if (total_cost <= money) {
				left = mid;
			} else {
				right = mid - 1;
			}
		}

		return left;
	}

	static final int INFINITY = 1000000;

	static int get_min_cost(Ingredient ing, int amount) {
		int target = Math.max((amount * ing.needed) - ing.available, 0);
		int min_cost = INFINITY;

		int max_big_amount = target + ing.size_big;
		int curr_big_amount = 0;
		int curr_big_cost = 0;
		while (curr_big_amount <= max_big_amount) {
			int rest = Math.max(target - curr_big_amount, 0);
			boolean perfect_fit = ((rest % ing.size_small) == 0);
			int small_count = (rest / ing.size_small) + (perfect_fit ? 0 : 1);
			int cost = curr_big_cost + (small_count * ing.cost_small);
			min_cost = Math.min(min_cost, cost);
			if ((cost > min_cost) && perfect_fit) {
				break;
			}
			curr_big_amount += ing.size_big;
			curr_big_cost += ing.cost_big;
		}

		return min_cost;
	}
}
