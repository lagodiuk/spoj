package spoj.dynamic_programming.menu;

import spoj.dynamic_programming.menu.MENU_BruteForce.Dish;

// http://www.spoj.com/problems/MENU/

public class MENU_5 {

	public static void main(String[] args) {
		// System.out.println(benefit(5, 2, new Dish[]{
		// new Dish(3, 5)
		// }));
		//
		// System.out.println(benefit(20, 3, new Dish[]{
		// new Dish(2, 5),
		// new Dish(18, 6),
		// new Dish(1, 1),
		// new Dish(3, 3),
		// new Dish(2, 3)
		// }));

		// MENU_BruteForce.test_compare_with_brute_force(MENU_4::benefit);
		MENU_BruteForce.test_big(MENU_5::benefit);
	}

	static double benefit(int budget, int days, Dish[] dishes) {
		int[][][][][] memoized = initializeMemoizedArray(budget, days, dishes.length);

		int maxBenefit = MENU_BruteForce.NEGATIVE_INFINITY;
		int startDish = -1;
		int startBudget = -1;

		for (int currDish = 0; currDish < dishes.length; currDish++) {
			for (int currBudget = 0; currBudget <= budget; currBudget++) {

				int currBenefit = benefit(dishes, currBudget, days, currDish, false, false, memoized);

				if ((currBenefit > maxBenefit)
						|| ((currBenefit == maxBenefit) && (currBudget < startBudget))) {

					startBudget = currBudget;
					startDish = currDish;
					maxBenefit = currBenefit;
				}
			}
		}

		if (maxBenefit > MENU_BruteForce.NEGATIVE_INFINITY) {
			backtrack(dishes, startBudget, days, startDish, false, false, memoized);
			System.out.println();
		}

		return maxBenefit > MENU_BruteForce.NEGATIVE_INFINITY ? maxBenefit / 10.0 : maxBenefit;
	}

	static int benefit(Dish[] dishes, int budget, int days, int curr, boolean prevEqualsCurr, boolean prevEqualsPrevPrev, int[][][][][] memoized) {

		if (budget < 0) {
			return MENU_BruteForce.NEGATIVE_INFINITY;
		}

		if (days == 0) {
			return 0;
		}

		if (memoized[budget][days][curr][prevEqualsCurr ? 1 : 0][prevEqualsPrevPrev ? 1 : 0] != -1) {
			return memoized[budget][days][curr][prevEqualsCurr ? 1 : 0][prevEqualsPrevPrev ? 1 : 0];
		}

		int result = MENU_BruteForce.NEGATIVE_INFINITY;

		double coeff;
		if (prevEqualsCurr) {
			coeff = prevEqualsPrevPrev ? 0 : 5;
		} else {
			coeff = 10;
		}

		for (int j = 0; j < dishes.length; j++) {
			int currResult =
					benefit(dishes, budget - dishes[curr].cost, days - 1, j, curr == j, prevEqualsCurr, memoized)
							+ (int) (coeff * dishes[curr].benefit);

			result = Math.max(result, currResult);
		}

		if (result < 0) {
			result = MENU_BruteForce.NEGATIVE_INFINITY;
		}

		memoized[budget][days][curr][prevEqualsCurr ? 1 : 0][prevEqualsPrevPrev ? 1 : 0] = result;
		return result;
	}

	static void backtrack(Dish[] dishes, int budget, int days, int curr, boolean prevEqualsCurr, boolean prevEqualsPrevPrev, int[][][][][] memoized) {

		if (days == 0) {
			return;
		}

		System.out.print((curr + 1) + " ");

		double coeff;
		if (prevEqualsCurr) {
			coeff = prevEqualsPrevPrev ? 0 : 5;
		} else {
			coeff = 10;
		}

		for (int j = 0; j < dishes.length; j++) {

			int candidate;
			if (days == 1) {
				candidate = (int) (coeff * dishes[curr].benefit);
			} else {
				candidate = memoized[budget - dishes[curr].cost][days - 1][j][curr == j ? 1 : 0][prevEqualsCurr ? 1 : 0]
						+ (int) (coeff * dishes[curr].benefit);
			}

			if (memoized[budget][days][curr][prevEqualsCurr ? 1 : 0][prevEqualsPrevPrev ? 1 : 0] == candidate) {

				backtrack(dishes, budget - dishes[curr].cost, days - 1, j, curr == j, prevEqualsCurr, memoized);
				return;
			}
		}

		throw new RuntimeException();
	}

	private static int[][][][][] initializeMemoizedArray(int budget, int days, int dishesLength) {
		int[][][][][] memoized = new int[budget + 1][days + 1][dishesLength][2][2];
		for (int i = 0; i < memoized.length; i++) {
			for (int i1 = 0; i1 < memoized[0].length; i1++) {
				for (int i2 = 0; i2 < memoized[0][0].length; i2++) {
					for (int i3 = 0; i3 < memoized[0][0][0].length; i3++) {
						for (int i4 = 0; i4 < memoized[0][0][0][0].length; i4++) {
							memoized[i][i1][i2][i3][i4] = -1;
						}
					}
				}
			}
		}
		return memoized;
	}
}
