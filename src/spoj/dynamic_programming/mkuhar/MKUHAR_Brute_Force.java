package spoj.dynamic_programming.mkuhar;

// http://www.spoj.com/problems/MKUHAR/

public class MKUHAR_Brute_Force {

	public static void main(String[] args) {
		MKUHAR_Brute_Force_2.test(MKUHAR_Brute_Force::solve);
	}

	static int solve(int money, Ingredient[] ingredients) {
		int[] amount = new int[ingredients.length];
		for (int i = 0; i < ingredients.length; i++) {
			amount[i] = ingredients[i].available;
		}
		return solve(money, 0, ingredients, amount);
	}

	static int solve(int money, int idx, Ingredient[] ingredients, int[] amount) {
		if (idx == ingredients.length) {
			int result = Integer.MAX_VALUE;
			for (int i = 0; i < ingredients.length; i++) {
				result = Math.min(result, amount[i] / ingredients[i].needed);
			}
			return result;
		}

		int result = Integer.MIN_VALUE;

		if (money >= ingredients[idx].cost_small) {
			amount[idx] += ingredients[idx].size_small;
			result = Math.max(result, solve(money - ingredients[idx].cost_small, idx, ingredients, amount));
			amount[idx] -= ingredients[idx].size_small;
		}

		if (money >= ingredients[idx].cost_big) {
			amount[idx] += ingredients[idx].size_big;
			result = Math.max(result, solve(money - ingredients[idx].cost_big, idx, ingredients, amount));
			amount[idx] -= ingredients[idx].size_big;
		}

		result = Math.max(result, solve(money, idx + 1, ingredients, amount));

		return result;
	}
}
