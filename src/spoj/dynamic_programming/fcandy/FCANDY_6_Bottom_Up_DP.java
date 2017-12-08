package spoj.dynamic_programming.fcandy;

// http://www.spoj.com/problems/FCANDY/

import java.util.HashSet;

public class FCANDY_6_Bottom_Up_DP {

	public static void main(String[] args) {
		FCANDY_1_Top_Down.evaluateExample(FCANDY_6_Bottom_Up_DP::solve);
		FCANDY_1_Top_Down.evaluateRandomized(FCANDY_6_Bottom_Up_DP::solve, true, 6);
		// FCANDY_1_Top_Down.evaluateRandomized(FCANDY_6_Bottom_Up_DP::solve, false, 50);
		FCANDY_1_Top_Down.evaluateRandomized(FCANDY_6_Bottom_Up_DP::solve, false, 100, 50, 50);
	}

	static int solve(Candies[] candies) {

		int totalCalories = 0;
		for (Candies ca : candies) {
			totalCalories += ca.calories * ca.count;
		}

		HashSet<Integer> prev = new HashSet<>(totalCalories);
		HashSet<Integer> curr = new HashSet<>(totalCalories);

		prev.add(0);

		for (int i = 0; i < candies.length; i++) {
			curr.clear();

			int count = candies[i].count;
			int calories = candies[i].calories;

			for (int bag1 : prev) {

				for (int x = 0; x <= count; x++) {

					int newBag1 = bag1 + (x * calories);

					if ((2 * newBag1) <= totalCalories) {
						curr.add(newBag1);
					}
				}
			}

			HashSet<Integer> tmp = prev;
			prev = curr;
			curr = tmp;
		}

		int result = 0;
		for (int x : prev) {
			result = Math.max(result, x);
		}

		return totalCalories - (2 * result);
	}
}
