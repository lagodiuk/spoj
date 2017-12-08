package spoj.dynamic_programming.fcandy;

// http://www.spoj.com/problems/FCANDY/

import java.util.Random;
import java.util.function.Function;

public class FCANDY_1_Top_Down {

	public static void main(String[] args) {
		evaluateExample(null);
		evaluateRandomized(null, true, 6);
	}

	static void evaluateRandomized(Function<Candies[], Integer> algorithm, boolean compareWithBruteForce, int maxCandiesCount) {
		evaluateRandomized(algorithm, compareWithBruteForce, maxCandiesCount, 20, 50);
	}

	static void evaluateRandomized(Function<Candies[], Integer> algorithm, boolean compareWithBruteForce, int maxCandiesCount, int maxCount, int maxCalories) {

		Random rnd = new Random(1);
		for (int i = 0; i < 50; i++) {
			Candies[] candies = new Candies[rnd.nextInt(maxCandiesCount) + 1];
			for (int j = 0; j < candies.length; j++) {
				candies[j] = new Candies(rnd.nextInt(maxCount) + 1, rnd.nextInt(maxCalories) + 1);
			}
			evaluateSolution(candies, algorithm, compareWithBruteForce);
		}
	}

	static void evaluateExample(Function<Candies[], Integer> algorithm) {
		evaluateSolution(new Candies[]{
				new Candies(3, 5),
				new Candies(3, 3),
				new Candies(1, 2),
				new Candies(3, 100)
		}, algorithm, true);
	}

	static void evaluateSolution(Candies[] candies, Function<Candies[], Integer> algorithm, boolean compareWithBruteForce) {
		System.out.println(candies.length);
		for (Candies ca : candies) {
			System.out.println(ca.count + " " + ca.calories);
		}
		if (algorithm == null) {
			System.out.println("Result: " + solve(candies));
		} else {
			if (!compareWithBruteForce) {
				System.out.println("Result: " + algorithm.apply(candies));
			} else {
				int bruteForceResult = solve(candies);
				int testedResult = algorithm.apply(candies);
				if (testedResult != bruteForceResult) {
					System.out.println("Expected result: " + bruteForceResult);
					System.out.println("Actual result: " + testedResult);
					throw new RuntimeException("Different results!");
				} else {
					System.out.println("Result: " + testedResult);
				}
			}
		}
		System.out.println();
	}

	static int solve(Candies[] candies) {
		int result = solve(0, 0, 0, candies);
		return result;
	}

	static int solve(int bag1, int bag2, int i, Candies[] candies) {
		if (i >= candies.length) {
			return Math.abs(bag1 - bag2);
		}

		int result = 10000000;

		int count = candies[i].count;
		int calories = candies[i].calories;
		for (int x = 0; x <= count; x++) {
			result = Math.min(result,
					solve(bag1 + (x * calories), bag2 + ((count - x) * calories), i + 1, candies));
		}

		return result;
	}
}
