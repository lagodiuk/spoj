package spoj.dynamic_programming.fcandy;

// http://www.spoj.com/problems/FCANDY/

public class FCANDY_3_Top_Down {

	public static void main(String[] args) {
		FCANDY_1_Top_Down.evaluateExample(FCANDY_3_Top_Down::solve);
		FCANDY_1_Top_Down.evaluateRandomized(FCANDY_3_Top_Down::solve, true, 6);
	}

	static int solve(Candies[] candies) {
		int totalCalories = 0;
		for (Candies ca : candies) {
			totalCalories += ca.calories * ca.count;
		}
		int result = solve(0, 0, candies, totalCalories);
		return result;
	}

	static int solve(int bag1, int i, Candies[] candies, int totalCalories) {

		if ((2 * bag1) > totalCalories) {
			return 10000000;
		}

		if (i >= candies.length) {
			return totalCalories - (2 * bag1);
		}

		int result = 10000000;

		int count = candies[i].count;
		int calories = candies[i].calories;
		for (int x = 0; x <= count; x++) {
			result = Math.min(result,
					solve(bag1 + (x * calories), i + 1, candies, totalCalories));
		}

		return result;
	}
}
