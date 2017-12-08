package spoj.dynamic_programming.mkuhar;

// http://www.spoj.com/problems/MKUHAR/

public class MKUHAR_Linear_Search_3 {

	public static void main(String[] args) {
		// MKUHAR_Brute_Force_2.test(MKUHAR_Linear_Search_3::solve, true, 20, 30, 300, 100);
		MKUHAR_Brute_Force_2.test(MKUHAR_Linear_Search_3::solve, false, 100, 100, 100000, 100);
	}

	static int solve(int money, Ingredient[] ingredients) {
		int amount = 0;
		while (true) {
			int total_cost = 0;
			for (Ingredient ing : ingredients) {
				total_cost += get_min_cost(ing, amount);
			}
			if (total_cost > money) {
				break;
			}
			amount++;
		}
		return amount - 1;
	}

	static final int INFINITY = 1000000;

	static int get_min_cost(Ingredient ing, int amount) {

		int target = Math.max((amount * ing.needed) - ing.available, 0);
		int min_cost = INFINITY;

		int max_small_amount = target + ing.size_small;
		int curr_small_amount = 0;
		int curr_small_cost = 0;
		while (curr_small_amount <= max_small_amount) {
			int rest = Math.max(target - curr_small_amount, 0);
			int big_count = rest / ing.size_big;
			boolean increased = false;
			if ((rest % ing.size_big) != 0) {
				big_count++;
				increased = true;
			}
			int cost = curr_small_cost + (big_count * ing.cost_big);
			min_cost = Math.min(min_cost, cost);
			if ((cost > min_cost) && !increased) {
				break;
			}
			curr_small_amount += ing.size_small;
			curr_small_cost += ing.cost_small;
		}

		return min_cost;
	}
}
