package spoj.dynamic_programming.jednakos;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

// http://www.spoj.com/problems/JEDNAKOS/en/

public class JEDNAKOS_Brute_Force {

	public static void main(String[] args) {
		testManualCases(null);
		testRandomizedCases(null);
	}

	static void testRandomizedCases(BiFunction<String, Integer, Integer> tested) {
		testRandomizedCases(tested, true, 7, 100);
	}

	static void testRandomizedCases(BiFunction<String, Integer, Integer> tested, boolean compareWithBruteForce, int maxTermsAmount, int maxTermValue) {
		Random rnd = new Random(1);

		for (int testCase = 0; testCase < 500; testCase++) {
			int target = 0;
			List<String> terms = new ArrayList<>();
			for (int i = 0; i < (rnd.nextInt(maxTermsAmount) + 1); i++) {
				int term = rnd.nextInt(maxTermValue);
				String zeroPrefix = "";
				if (rnd.nextDouble() > 0.5) {
					for (int j = 0; j < rnd.nextInt(5); j++) {
						zeroPrefix += "0";
					}
				}
				terms.add(zeroPrefix + Integer.toString(term));
				target += term;
			}
			testSolution(String.join("", terms), target, tested, compareWithBruteForce);
		}
	}

	static void testManualCases(BiFunction<String, Integer, Integer> tested) {
		testSolution("143175", 120, tested, true);
		testSolution("5025", 30, tested, true);
		testSolution("999899", 125, tested, true);
		testSolution("75001200", 87, tested, true);
		testSolution("9998000099000000", 125, tested, true);
	}

	static void testSolution(String digitsStr, int target, BiFunction<String, Integer, Integer> tested, boolean compareWithBruteForce) {
		System.out.println(digitsStr + "=" + target);

		if (compareWithBruteForce) {
			int bruteForceResult = solve(digitsStr, target);

			if (tested != null) {
				int testedResult = tested.apply(digitsStr, target);

				System.out.println("Expected result: " + bruteForceResult);
				System.out.println("Tested result: " + testedResult);

				if (testedResult != bruteForceResult) {
					throw new RuntimeException("Different results!");
				}
			} else {
				System.out.println("Result: " + bruteForceResult);
			}

		} else {
			int testedResult = tested.apply(digitsStr, target);
			System.out.println("Result: " + testedResult);
		}

		System.out.println();
	}

	static final int INFINITY = 10000;

	static int result = INFINITY;

	static int solve(String digitsStr, int target) {
		result = INFINITY;
		checkAllCombinations(digitsStr, 0, new ArrayList<>(), target);
		return result - 1;
	}

	static void checkAllCombinations(String digitsStr, int startPos, List<Integer> delimiters, int target) {
		if (startPos == digitsStr.length()) {
			BigInteger tmp = new BigInteger(Integer.toString(target));
			int left = 0;
			for (int right : delimiters) {
				tmp = tmp.subtract(new BigInteger(digitsStr.substring(left, right)));
				left = right;
			}
			if (tmp.compareTo(BigInteger.ZERO) == 0) {
				result = Math.min(result, delimiters.size());
			}
			return;
		}

		for (int i = startPos + 1; i <= digitsStr.length(); i++) {
			delimiters.add(i);
			checkAllCombinations(digitsStr, i, delimiters, target);
			delimiters.remove(delimiters.size() - 1);
		}
	}
}
