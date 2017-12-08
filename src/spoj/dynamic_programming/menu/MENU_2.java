package spoj.dynamic_programming.menu;

import spoj.dynamic_programming.menu.MENU_BruteForce.Dish;

// http://www.spoj.com/problems/MENU/

public class MENU_2 {

	public static void main(String[] args) {
		MENU_BruteForce.test_compare_with_brute_force(MENU_2::benefit);
	}

	static double benefit(int budget, int days, Dish[] dishes) {
		double benefit = MENU_BruteForce.NEGATIVE_INFINITY;
		for (int i = 0; i < dishes.length; i++) {
			benefit = Math.max(benefit, benefit(budget, days, dishes, i, -1, false));
		}
		return benefit;
	}

	static double benefit(int budget, int days, Dish[] dishes, int curr, int prev, boolean prevEqualsPrevPrev) {

		if (budget < 0) {
			return MENU_BruteForce.NEGATIVE_INFINITY;
		}

		if (days == 0) {
			return 0;
		}

		double result = MENU_BruteForce.NEGATIVE_INFINITY;

		double coeff;
		if (curr == prev) {
			if (prevEqualsPrevPrev) {
				coeff = 0;
			} else {
				coeff = 0.5;
			}
		} else {
			coeff = 1;
		}

		for (int j = 0; j < dishes.length; j++) {
			double currResult = benefit(budget - dishes[curr].cost, days - 1, dishes, j, curr, curr == prev) + (coeff * dishes[curr].benefit);
			result = Math.max(result, currResult);
		}

		if (result < 0) {
			result = MENU_BruteForce.NEGATIVE_INFINITY;
		}

		return result;
	}

}
