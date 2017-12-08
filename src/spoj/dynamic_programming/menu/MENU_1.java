package spoj.dynamic_programming.menu;

import spoj.dynamic_programming.menu.MENU_BruteForce.Dish;

// http://www.spoj.com/problems/MENU/

public class MENU_1 {

	public static void main(String[] args) {

		System.out.println(benefit(5, 2, new Dish[]{
				new Dish(3, 5)
		}));

		System.out.println(MENU_BruteForce.benefit(5, 2, new Dish[]{
				new Dish(3, 5)
		}));

		System.out.println(benefit(20, 3, new Dish[]{
				new Dish(2, 5),
				new Dish(18, 6),
				new Dish(1, 1),
				new Dish(3, 3),
				new Dish(2, 3)
		}));

		System.out.println(MENU_BruteForce.benefit(20, 3, new Dish[]{
				new Dish(2, 5),
				new Dish(18, 6),
				new Dish(1, 1),
				new Dish(3, 3),
				new Dish(2, 3)
		}));
	}

	static double benefit(int budget, int days, Dish[] dishes) {
		double benefit = MENU_BruteForce.NEGATIVE_INFINITY;
		for (int i = 0; i < dishes.length; i++) {
			benefit = Math.max(benefit, benefit(budget, days, dishes, i, -1, -2));
		}
		return benefit;
	}

	static double benefit(int budget, int days, Dish[] dishes, int curr, int prev, int prevPrev) {

		if (budget < 0) {
			return MENU_BruteForce.NEGATIVE_INFINITY;
		}

		if (days == 0) {
			return 0;
		}

		double result = MENU_BruteForce.NEGATIVE_INFINITY;

		double coeff;
		if (curr == prev) {
			if (prev == prevPrev) {
				coeff = 0;
			} else {
				coeff = 0.5;
			}
		} else {
			coeff = 1;
		}

		for (int j = 0; j < dishes.length; j++) {
			double currResult = benefit(budget - dishes[curr].cost, days - 1, dishes, j, curr, prev) + (coeff * dishes[curr].benefit);
			result = Math.max(result, currResult);
		}

		if (result < 0) {
			result = MENU_BruteForce.NEGATIVE_INFINITY;
		}

		return result;
	}
}
