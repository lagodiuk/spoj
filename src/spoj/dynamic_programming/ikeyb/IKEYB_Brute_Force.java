package spoj.dynamic_programming.ikeyb;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class IKEYB_Brute_Force {

	public static void main(String[] args) {
		// Example of snippet for generation of combinations
		// (8 - 1)!/((3 - 1)! * (8 - 3)!) = 21 - different combinations
		// enumerateAllKeyLettersCount(new int[3], 0, 8,
		// (int[] arr) -> {
		// System.out.println(Arrays.toString(arr));
		// return null;
		// });

		printSolution(new char[]{'a', 'b', 'c', 'd'},
				new int[]{1, 1, 1, 1},
				new char[]{'1', '2', '3'});

		printSolution("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray(),
				new int[]{3371, 589, 1575, 1614, 6212, 971, 773, 1904, 2989, 123, 209, 1588, 1513, 2996, 3269, 1080, 121, 2726, 3083, 4368, 1334, 518, 752,
						427, 733, 871},
				"23456789".toCharArray());
	}

	static void verifyRandomTestCases(SolutionFunctionInterface f) {
		Random rnd = new Random(1);

		for (int testNum = 0; testNum < 30; testNum++) {

			// Can't be tested with brute force approach
			// int keysCnt = rnd.nextInt(90) + 1;
			// int lettersCnt = keysCnt + rnd.nextInt(91 - keysCnt);

			// Testing brute force approach with smaller limits
			int keysCnt = rnd.nextInt(10) + 1;
			int lettersCnt = keysCnt + rnd.nextInt(20);

			char[] letters = new char[lettersCnt];
			for (int i = 0; i < lettersCnt; i++) {
				letters[i] = (char) (rnd.nextInt(127 - 33) + 33);
			}

			int[] frequencies = new int[lettersCnt];
			for (int i = 0; i < lettersCnt; i++) {
				frequencies[i] = rnd.nextInt(100001);
			}

			char[] keys = new char[keysCnt];
			for (int i = 0; i < keysCnt; i++) {
				keys[i] = (char) (rnd.nextInt(127 - 33) + 33);
			}

			compareWithBruteForceSolution(letters, frequencies, keys, f);
		}
	}

	static void verifySpecificTestCases(SolutionFunctionInterface f) {
		compareWithBruteForceSolution(
				"abcd".toCharArray(),
				new int[]{1, 1, 1, 1},
				"123".toCharArray(),
				f);

		compareWithBruteForceSolution(
				"ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray(),
				new int[]{3371, 589, 1575, 1614, 6212, 971, 773, 1904, 2989, 123, 209, 1588, 1513, 2996, 3269, 1080, 121, 2726, 3083, 4368, 1334, 518, 752,
						427, 733, 871},
				"23456789".toCharArray(),
				f);
	}

	static void compareWithBruteForceSolution(char[] letters, int[] frequencies, char[] keys, SolutionFunctionInterface f) {

		System.out.println(keys.length + " " + letters.length);
		System.out.println(new String(keys));
		System.out.println(new String(letters));
		for (int frq : frequencies) {
			System.out.println(frq);
		}

		List<String> testedSolution = f.solve(letters, frequencies, keys);
		List<String> bruteForceSolution = solve(letters, frequencies, keys);
		if (!bruteForceSolution.equals(testedSolution)) {
			throw new RuntimeException();
		}

		System.out.println();
		System.out.println("Solution:");
		for (String s : testedSolution) {
			System.out.println(s);
		}
		System.out.println();
		System.out.println();
	}

	static void printSolution(char[] letters, int[] frequencies, char[] keys) {
		List<String> result = solve(letters, frequencies, keys);

		for (String s : result) {
			System.out.println(s);
		}
		System.out.println();
	}

	// Exponential complexity
	private static List<String> solve(char[] letters, int[] frequencies, char[] keys) {
		Solution solution = new Solution();
		solution.keyLettersCount = new int[keys.length];
		solution.cost = Long.MAX_VALUE;

		// Exponential complexity
		enumerateAllKeyLettersCount(new int[keys.length], 0, letters.length,
				(int[] keyLettersCount) -> {

					long cost = calculateCost(keyLettersCount, frequencies);

					if (cost < solution.cost) {

						solution.cost = cost;
						System.arraycopy(keyLettersCount, 0, solution.keyLettersCount, 0, keyLettersCount.length);

					} else if (cost == solution.cost) {

						boolean betterThanExistingSolution = true;

						for (int i = keyLettersCount.length - 1; i >= 0; i--) {
							if (keyLettersCount[i] < solution.keyLettersCount[i]) {
								betterThanExistingSolution = false;
								break;
							}
						}

						if (betterThanExistingSolution) {
							System.arraycopy(keyLettersCount, 0, solution.keyLettersCount, 0, keyLettersCount.length);
						}

					}

					return null;
				});

		List<String> result = solution.generateResultAssignmentOfLetters(letters, keys);
		return result;
	}

	/**
	 * Enumerating all possible variants of assignment of letters to keys.
	 *
	 * By putting (k - 1) possible delimiters (delimiters which between k keys) -
	 * over (n - 1) available positions (delimiters between n letters).
	 *
	 * Amount of possible combinations:
	 * C = (n - 1)! / ((k - 1)! * (n - 1 - k + 1)!) = (n - 1)! / ((k - 1)! * (n - k)!)
	 */
	static void enumerateAllKeyLettersCount(int[] keyLettersCount, int currKeyIdx, int lettersAvailable, Function<int[], Void> callback) {

		if (currKeyIdx == keyLettersCount.length) {
			if (lettersAvailable == 0) {
				callback.apply(keyLettersCount);
			}
			return;
		}

		if ((lettersAvailable <= 0) || (lettersAvailable < (keyLettersCount.length - currKeyIdx))) {
			return;
		}

		for (int i = 1; i <= lettersAvailable; i++) {
			keyLettersCount[currKeyIdx] = i;
			enumerateAllKeyLettersCount(keyLettersCount, currKeyIdx + 1, lettersAvailable - i, callback);
		}
	}

	static long calculateCost(int[] keyLettersCount, int[] frequencies) {
		long cost = 0;

		int letterIdx = 0;
		for (int count : keyLettersCount) {
			for (int weight = 1; weight <= count; weight++) {
				cost += weight * frequencies[letterIdx];
				letterIdx++;
			}
		}

		return cost;
	}

	static class Solution {
		int[] keyLettersCount;
		long cost;

		List<String> generateResultAssignmentOfLetters(char[] letters, char[] keys) {
			List<String> result = new ArrayList<>();

			int letterIdx = 0;
			int keyIdx = 0;

			for (int count : this.keyLettersCount) {
				String keyAssignment = keys[keyIdx] + ": ";

				for (int weight = 1; weight <= count; weight++) {
					keyAssignment += letters[letterIdx];
					letterIdx++;
				}

				result.add(keyAssignment);
				keyIdx++;
			}

			return result;
		}
	}

	static interface SolutionFunctionInterface {
		List<String> solve(char[] letters, int[] frequencies, char[] keys);
	}
}
