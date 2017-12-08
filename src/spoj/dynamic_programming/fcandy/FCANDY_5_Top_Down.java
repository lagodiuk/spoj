package spoj.dynamic_programming.fcandy;

// http://www.spoj.com/problems/FCANDY/

public class FCANDY_5_Top_Down {

	public static void main(String[] args) {
		FCANDY_1_Top_Down.evaluateExample(FCANDY_5_Top_Down::solve);
		FCANDY_1_Top_Down.evaluateRandomized(FCANDY_5_Top_Down::solve, true, 6);
	}

	static int solve(Candies[] candies) {
		int totalCalories = 0;
		for (Candies ca : candies) {
			totalCalories += ca.calories * ca.count;
		}
		int result = totalCalories - solve(0, 0, 0, candies, totalCalories);
		return result;
	}

	static int solve(int bag1, int i, int x, Candies[] candies, int totalCalories) {

		if ((2 * bag1) > totalCalories) {
			return -10000000;
		}

		if (i >= candies.length) {
			return 2 * bag1;
		}

		if (x > candies[i].count) {
			return -10000000;
		}

		int result = Math.max(
				solve(bag1 + candies[i].calories, i, x + 1, candies, totalCalories),
				solve(bag1, i + 1, 0, candies, totalCalories));

		return result;
	}
}
