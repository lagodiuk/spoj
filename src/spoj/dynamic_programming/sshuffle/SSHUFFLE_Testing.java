package spoj.dynamic_programming.sshuffle;

import java.util.Random;

public class SSHUFFLE_Testing {

	static void test(Solver f, int maxLength, char[] availableChars, int numTests) {
		test(f, maxLength, availableChars, numTests, true, 2);
	}

	static void test(Solver f, int maxLength, char[] availableChars, int numTests, boolean compareWithBruteForce, int minLength) {
		checkResults("abc", "abc", "abc", f, compareWithBruteForce);
		checkResults("aa", "aa", "aa", f, compareWithBruteForce);
		checkResults("abbcd", "bccde", "abcde", f, compareWithBruteForce);
		checkResults("xxxxxxx", "xx", "xxx", f, compareWithBruteForce);

		Random rnd = new Random(0);

		for (int i = 0; i < numTests; i++) {
			String s1 = generateRandomString(rnd, availableChars, maxLength, minLength);
			String s2 = generateRandomString(rnd, availableChars, maxLength, minLength);
			String s3 = generateRandomString(rnd, availableChars, Math.min(s1.length(), s2.length()), minLength);
			checkResults(s1, s2, s3, f, compareWithBruteForce);
		}
	}

	static void checkResults(String s1, String s2, String s3, Solver f, boolean compareWithBruteForce) {

		System.out.println(s1 + " " + s2 + " " + s3);

		if (f != null) {
			if (compareWithBruteForce) {
				long expected = SSHUFFLE_Brute_Force.countWaysToObtainS3(s1, s2, s3);
				long actual = f.solve(s1, s2, s3);

				if (actual == expected) {
					System.out.println(actual);
					System.out.println();
				} else {
					System.out.println("Expected: " + expected);
					System.out.println("Actual: " + actual);
					throw new RuntimeException("Results are different!");
				}
			} else {
				long actual = f.solve(s1, s2, s3);
				System.out.println(actual);
				System.out.println();
				if (actual < 0) {
					System.out.println(s1.length());
					System.out.println(s2.length());
					System.out.println(s3.length());
					throw new RuntimeException("Negative value result!");
				}
			}
		} else {
			long expected = SSHUFFLE_Brute_Force.countWaysToObtainS3(s1, s2, s3);
			System.out.println(expected);
			System.out.println();
		}
	}

	static String generateRandomString(Random rnd, char[] availableChars, int maxLength, int minLength) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < (rnd.nextInt(maxLength) + minLength); i++) {
			sb.append(availableChars[rnd.nextInt(availableChars.length)]);
		}
		return sb.toString();
	}

	interface Solver {
		long solve(String s1, String s2, String s3);
	}
}
