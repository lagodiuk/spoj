package spoj.dynamic_programming.menu;

import spoj.dynamic_programming.menu.MENU_BruteForce.Dish;

// http://www.spoj.com/problems/MENU/

public class MENU_3 {

	public static void main(String[] args) {
		MENU_BruteForce.test_compare_with_brute_force(MENU_3::benefit);
	}

	static double benefit(int budget, int days, Dish[] dishes) {
		int[][][][][] memoized = initializeMemoizedArray(budget, days, dishes.length);

		int benefit = MENU_BruteForce.NEGATIVE_INFINITY;
		for (int i = 0; i < dishes.length; i++) {
			benefit = Math.max(benefit, benefit(dishes, budget, days, i, -1, false, memoized));
		}

		return benefit / 10.0;
	}

	static int benefit(Dish[] dishes, int budget, int days, int curr, int prev, boolean prevEqualsPrevPrev, int[][][][][] memoized) {

		if (budget < 0) {
			return MENU_BruteForce.NEGATIVE_INFINITY;
		}

		if (days == 0) {
			return 0;
		}

		if (memoized[budget][days][curr][prev + 1][prevEqualsPrevPrev ? 1 : 0] != -1) {
			return memoized[budget][days][curr][prev + 1][prevEqualsPrevPrev ? 1 : 0];
		}

		int result = MENU_BruteForce.NEGATIVE_INFINITY;

		double coeff;
		if (curr == prev) {
			coeff = prevEqualsPrevPrev ? 0 : 5;
		} else {
			coeff = 10;
		}

		for (int j = 0; j < dishes.length; j++) {
			int currResult =
					benefit(dishes, budget - dishes[curr].cost, days - 1, j, curr, curr == prev, memoized)
							+ (int) (coeff * dishes[curr].benefit);

			result = Math.max(result, currResult);
		}

		if (result < 0) {
			result = MENU_BruteForce.NEGATIVE_INFINITY;
		}

		memoized[budget][days][curr][prev + 1][prevEqualsPrevPrev ? 1 : 0] = result;
		return result;
	}

	private static int[][][][][] initializeMemoizedArray(int budget, int days, int dishesLength) {
		int[][][][][] memoized = new int[budget + 1][days + 1][dishesLength][dishesLength + 1][2];
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
