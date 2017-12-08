package spoj.dynamic_programming.helpbob;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// http://www.spoj.com/problems/HELPBOB/

public class HELPBOB_Brute_Force {

	public static void main(String[] args) {
		System.out.printf("%.4f%n", solve(new Pizza[]{
				new Pizza(80, 30, new int[]{})
		}));

		System.out.printf("%.4f%n", solve(new Pizza[]{
				new Pizza(200, 100, new int[]{2, 50}),
				new Pizza(200, 100, new int[]{}),
		}));

		System.out.printf("%.4f%n", solve(new Pizza[]{ // (100 + 50 + 20 + 300)/(100 + 100 + 100 +
														// 600)
						new Pizza(100, 100, new int[]{3, 50, 2, 50}),
						new Pizza(100, 100, new int[]{4, 50}),
						new Pizza(100, 100, new int[]{2, 40}),
						new Pizza(600, 600, new int[]{5, 10}),
						new Pizza(1000, 10, new int[]{1, 50}),
		}));
	}

	static double solve(Pizza[] pizzas) {
		System.out.println();
		variantsChecked = 0;
		double result = solve(pizzas, new HashSet<>(), 0, 0);
		// System.out.println(variantsChecked);
		return result;
	}

	static int variantsChecked = 0;

	static double solve(Pizza[] pizzas, Set<Integer> buyed, double totalPrice, double totalArea) {
		if (buyed.size() == pizzas.length) {
			// System.out.println(buyed);
			// System.out.println(totalPrice / totalArea);
			variantsChecked++;
			return totalPrice / totalArea;
		}

		double result = 100000000;
		if (!buyed.isEmpty()) {
			// System.out.println(buyed);
			// System.out.println(totalPrice / totalArea);
			result = Math.min(result, totalPrice / totalArea);
		}

		for (int i = 0; i < pizzas.length; i++) {
			if (!buyed.contains(i)) {

				double pizzaPrice = pizzas[i].price;
				double pizzaArea = pizzas[i].area;

				for (int buyedIdx : buyed) {
					pizzaPrice = (pizzaPrice * pizzas[buyedIdx].disc[i]) / 100.0;
				}

				buyed.add(i);

				result = Math.min(result, solve(pizzas, buyed, totalPrice + pizzaPrice, totalArea + pizzaArea));

				buyed.remove(i);
			}
		}

		return result;
	}

	public static class Pizza {
		final int price;
		final int area;
		final int[] disc = new int[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100};

		public Pizza(int price, int area, int[] discounts) {
			this.price = price;
			this.area = area;
			for (int i = 0; i < discounts.length; i += 2) {
				int idx = discounts[i] - 1;
				int discount = discounts[i + 1];
				this.disc[idx] = discount;
			}
		}

		@Override
		public String toString() {
			return "(p=" + this.price + ", a=" + this.area + ", d=" + Arrays.toString(this.disc) + ")";
		}
	}

}
