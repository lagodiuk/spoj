package spoj.dynamic_programming.mfish;

// http://www.spoj.com/problems/MFISH/en/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class MFISH_1 {

	public static void main(String[] args) {
		testFewManualCases(null);
		testRandomCases(null);
	}

	static void testRandomCases(BiFunction<int[], Boat[], Integer> tested) {
		testRandomCases(tested, 30, 0.5, true);
	}

	static void testRandomCases(BiFunction<int[], Boat[], Integer> tested, int maxRiverLength, double boatProbabilityThreshold, boolean compareWithBruteForce) {
		Random rnd = new Random(1);
		int testsNum = 100;

		for (int test = 0; test < testsNum; test++) {
			int[] fish = generateRiver(maxRiverLength, rnd);
			Boat[] boats = generateBoats(boatProbabilityThreshold, rnd, fish.length);
			verifySolution(fish, boats, tested, compareWithBruteForce);
		}
	}

	private static Boat[] generateBoats(double boatProbabilityThreshold, Random rnd, int riverLength) {
		List<Boat> boatsList = new ArrayList<>();

		int initialMargin = rnd.nextInt((riverLength / 3) + 1);

		int currPos = initialMargin;
		while (currPos < riverLength) {
			if (rnd.nextDouble() > boatProbabilityThreshold) {
				// EITHER add new boat

				int maxLength = riverLength - currPos;
				if ((rnd.nextDouble() > 0.5) && (maxLength > (riverLength / 2))) {
					// avoid very large boats
					maxLength = (riverLength / 2) + 1;
				}

				Boat b = new Boat(currPos + 1, rnd.nextInt(maxLength) + 1);
				boatsList.add(b);
				currPos += b.length;
			} else {
				// OR just shift to the next position
				currPos++;
			}
		}

		if (boatsList.isEmpty()) {
			// avoid possibility of generation of empty list of boats
			return generateBoats(boatProbabilityThreshold, rnd, riverLength);
		}

		Boat[] boats = boatsList.toArray(new Boat[]{});
		return boats;
	}

	private static int[] generateRiver(int maxRiverLength, Random rnd) {
		int[] fish = new int[rnd.nextInt(maxRiverLength) + 1];
		for (int i = 0; i < fish.length; i++) {
			fish[i] = rnd.nextInt(100) + 1;
		}
		return fish;
	}

	static void testFewManualCases(BiFunction<int[], Boat[], Integer> tested) {
		verifySolution(new int[]{2, 5, 3, 4, 7, 6, 2, 1, 3, 8, 5}, new Boat[]{
				new Boat(1, 1)
		}, tested, true);

		verifySolution(new int[]{2, 5, 3, 4, 7, 6, 2, 1, 3, 8, 5}, new Boat[]{
				new Boat(2, 3)
		}, tested, true);

		verifySolution(new int[]{2, 5, 3, 4, 7, 6, 2, 1, 3, 8, 5}, new Boat[]{
				new Boat(10, 2)
		}, tested, true);

		verifySolution(new int[]{2, 5, 3, 4, 7, 6, 2, 1, 3, 8, 5}, new Boat[]{
				new Boat(10, 3)
		}, tested, true);

		verifySolution(new int[]{2, 5, 3, 4, 7, 6, 2, 1, 3, 8, 5}, new Boat[]{
				new Boat(10, 4)
		}, tested, true);

		verifySolution(new int[]{2, 5, 3, 4, 7, 6, 2, 1, 3, 8, 5}, new Boat[]{
				new Boat(8, 3),
				new Boat(3, 2),
		}, tested, true);

		verifySolution(new int[]{3, 2, 4, 7, 2, 1, 3, 6, 1, 2, 6, 4, 1}, new Boat[]{
				new Boat(5, 7),
				new Boat(11, 4),
		}, tested, true);

		verifySolution(new int[]{1, 1, 6, 4, 4, 1, 1, 3, 10, 1, 1}, new Boat[]{
				new Boat(2, 3),
				new Boat(6, 4),
				new Boat(10, 2),
		}, tested, true);

		verifySolution(new int[]{1, 1, 1, 1, 1, 1, 1}, new Boat[]{
				new Boat(2, 2),
				new Boat(3, 3),
				new Boat(6, 2),
		}, tested, true);
	}

	static void verifySolution(int[] fish, Boat[] boats, BiFunction<int[], Boat[], Integer> tested, boolean compareWithBruteForce) {
		System.out.println(fish.length);
		for (int x : fish) {
			System.out.print(x + " ");
		}
		System.out.println();
		System.out.println(boats.length);
		for (Boat b : boats) {
			System.out.println(b.anchor + " " + b.length);
		}

		if (tested == null) {
			System.out.println("Result: " + solve(fish.clone(), boats.clone()));
		} else {
			if (compareWithBruteForce) {
				int bruteForceResult = solve(fish.clone(), boats.clone());
				int testedResult = tested.apply(fish.clone(), boats.clone());
				if (testedResult != bruteForceResult) {
					System.out.println("Expected result: " + bruteForceResult);
					System.out.println("Actual result: " + testedResult);
					throw new RuntimeException("Results are different!");
				} else {
					System.out.println("Result: " + testedResult);
				}
			} else {
				int testedResult = tested.apply(fish.clone(), boats.clone());
				System.out.println("Result: " + testedResult);
			}
		}
		System.out.println();
	}

	static int solve(int[] fish, Boat[] boats) {
		Arrays.sort(boats, (b1, b2) -> Integer.compare(b1.anchor, b2.anchor));
		int[] fishAcc = calculateCumulativePreffixSum(fish);
		int result = solve(boats, fishAcc, 1, 0);
		return result;
	}

	static final int NEGATIVE_INFINITY = -100000000;

	// TODO: develop algorithm with linear time complexity
	static int solve(Boat[] boats, int[] fishAcc, int start, int boat) {
		if (boat == boats.length) {
			return 0;
		}

		if (start > boats[boat].anchor) {
			return NEGATIVE_INFINITY;
		}

		if ((fishAcc.length - start) < boats[boat].length) {
			return NEGATIVE_INFINITY;
		}

		int result = NEGATIVE_INFINITY;
		int from = Math.max(start, boats[boat].minPos);
		int to = Math.min((fishAcc.length - boats[boat].length) + 1, boats[boat].anchor + 1);
		for (int i = from; i < to; i++) {
			int subProblem = solve(boats, fishAcc, i + boats[boat].length, boat + 1);
			if (subProblem != NEGATIVE_INFINITY) {
				subProblem += countFish(fishAcc, i, (i + boats[boat].length) - 1);
			}
			result = Math.max(result, subProblem);
		}
		return result;
	}

	static int[] calculateCumulativePreffixSum(int[] fish) {
		int[] fishAcc = new int[fish.length + 1];
		fishAcc[0] = 0;
		for (int i = 1; i < fishAcc.length; i++) {
			fishAcc[i] = fishAcc[i - 1] + fish[i - 1];
		}
		return fishAcc;
	}

	static int countFish(int[] fishAcc, int from, int to) {
		return fishAcc[to] - fishAcc[from - 1];
	}

}
