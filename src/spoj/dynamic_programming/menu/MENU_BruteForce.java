package spoj.dynamic_programming.menu;

import java.util.Arrays;
import java.util.Random;

// http://www.spoj.com/problems/MENU/

public class MENU_BruteForce {

	public static final int NEGATIVE_INFINITY = -1000000;

	public static void test_big(TriFunction<Integer, Integer, Dish[], Double> f) {
		Random rnd = new Random(1);
		int maxDishes = 50;
		int maxBenefit = 200;
		int maxCost = 10;
		int maxDays = 21;

		testInLoop(f, rnd, maxDishes, maxBenefit, maxCost, maxDays, false);
	}

	public static void test_compare_with_brute_force(TriFunction<Integer, Integer, Dish[], Double> f) {
		Random rnd = new Random(1);
		int maxDishes = 12;
		int maxBenefit = 200;
		int maxCost = 10;
		int maxDays = 9;

		testInLoop(f, rnd, maxDishes, maxBenefit, maxCost, maxDays, true);

		System.out.println();
		System.out.println("Done!");
	}

	private static void testInLoop(TriFunction<Integer, Integer, Dish[], Double> f, Random rnd, int maxDishes, int maxBenefit, int maxCost, int maxDays,
			boolean executeBruteForce) {

		for (int i = 0; i < 50; i++) {

			Dish[] dishes = new Dish[rnd.nextInt(maxDishes) + 1];
			for (int d = 0; d < dishes.length; d++) {
				dishes[d] = new Dish(rnd.nextInt(maxCost) + 1, rnd.nextInt(maxBenefit) + 1);
			}

			int budget = rnd.nextInt(10 * maxCost);
			int days = rnd.nextInt(maxDays) + 1;

			System.out.println("Dishes: " + Arrays.toString(dishes));
			System.out.println("Budget: " + budget);
			System.out.println("Days: " + days);

			if (executeBruteForce) {
				double b1 = MENU_BruteForce.benefit(budget, days, dishes);
				System.out.println(b1);

				double b2 = f.apply(budget, days, dishes);
				System.out.println(b2);

				if (b1 != b2) {
					System.out.println("RESULTS ARE DIFFERENT!");
					System.exit(0);
				}

			} else {
				Double b2 = f.apply(budget, days, dishes);
				System.out.println(b2);
			}

			System.out.printf("%d %d %d\n", days, dishes.length, budget);
			for (Dish d : dishes) {
				System.out.printf("%d %d\n", d.cost, (int) d.benefit);
			}

			System.out.println();
		}
	}

	static interface TriFunction<A, B, C, R> {
		R apply(A a, B b, C c);
	}

	static double benefit(int budget, int days, Dish[] dishes) {
		return benefit(budget, days, dishes, new int[days], 0);
	}

	static double benefit(int budget, int days, Dish[] dishes, int[] dishIdx, int currDay) {
		if (currDay == days) {

			int totalCost = 0;
			for (int d : dishIdx) {
				totalCost += dishes[d].cost;
			}
			if (totalCost > budget) {
				return NEGATIVE_INFINITY;
			}

			double benefit = 0;
			int i = 0;
			while (i < days) {

				int start = i;
				while ((i < days) && (dishIdx[i] == dishIdx[start])) {
					i++;
				}
				int cnt = i - start;

				double coefficient;
				if (cnt == 1) {
					coefficient = 1;
				} else {
					coefficient = 1.5;
				}

				benefit += coefficient * dishes[dishIdx[start]].benefit;
			}

			return benefit;
		}

		double benefit = NEGATIVE_INFINITY;
		for (int i = 0; i < dishes.length; i++) {
			dishIdx[currDay] = i;
			benefit = Math.max(benefit, benefit(budget, days, dishes, dishIdx, currDay + 1));
		}
		return benefit;
	}

	public static class Dish {
		int cost;
		double benefit;
		public Dish(int cost, double benefit) {
			this.cost = cost;
			this.benefit = benefit;
		}
		@Override
		public String toString() {
			return String.format("(%d, %.1f)", this.cost, this.benefit);
		}
	}
}
