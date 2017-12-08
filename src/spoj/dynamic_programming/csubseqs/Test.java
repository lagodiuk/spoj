package spoj.dynamic_programming.csubseqs;

// http://www.spoj.com/problems/CSUBSEQS/

import java.util.Random;
import java.util.function.Function;

public class Test {

	static void test_2D(Function<String[], Integer> tested) {
		compareResults(tested, new String[]{"aabb", "abab"}, true);
	}

	static void test_3D(Function<String[], Integer> tested) {
		compareResults(tested, new String[]{"aabb", "abab", "baba"}, true);
		compareResults(tested, new String[]{"aa", "aa", "aa"}, true);
		compareResults(tested, new String[]{"ab", "ab", "ab"}, true);
		compareResults(tested, new String[]{"abcd", "abcd", "abcd"}, true);
		compareResults(tested, new String[]{"abcde", "abcde", "abcde"}, true);
	}

	static void test_4D(Function<String[], Integer> tested) {
		compareResults(tested, new String[]{"aabb", "abab", "baba", "acba"}, true);
		compareResults(tested, new String[]{"aa", "aa", "aa", "aa"}, true);
		compareResults(tested, new String[]{"ab", "ab", "ab", "ab"}, true);
		compareResults(tested, new String[]{"abcd", "abcd", "abcd", "abcd"}, true);
		compareResults(tested, new String[]{"abcde", "abcde", "abcde", "abcde"}, true);
	}

	static void randomizedTest(int dimension, Function<String[], Integer> tested) {
		randomizedTest(dimension, tested, true, 10);
	}

	static void randomizedTest(int dimension, Function<String[], Integer> tested, boolean compareWithBruteForce, int maxLength) {

		Random rnd = new Random(1);

		for (int test = 0; test < 50; test++) {

			String[] strings = new String[dimension];
			for (int i = 0; i < dimension; i++) {
				strings[i] = generateRandomString(rnd, maxLength);
			}

			compareResults(tested, strings, compareWithBruteForce);
		}
	}

	private static void compareResults(Function<String[], Integer> tested, String[] strings, boolean compareWithBruteForce) {

		for (String s : strings) {
			System.out.println(s);
		}

		if (!compareWithBruteForce) {
			int res2 = tested.apply(strings);
			System.out.println("Result: " + res2);
			System.out.println();
			return;
		}

		int res1 = Count_Distinct_Subsequences_Brute_Force.countDistinctSubsequences(strings);
		int res2 = tested.apply(strings);

		if (res1 != res2) {
			System.out.println("Brute force: " + res1);
			System.out.println("Non brute force: " + res2);
			throw new RuntimeException("Different results!");
		}

		System.out.println("Result: " + res1);
		System.out.println();
	}

	private static String generateRandomString(Random rnd, int maxLength) {
		StringBuilder sb1 = new StringBuilder();
		int len1 = rnd.nextInt(maxLength) + 3;
		for (int i = 0; i < len1; i++) {
			sb1.append((char) (rnd.nextInt(('d' - 'a') + 1) + 'a'));
		}
		String str1 = sb1.toString();
		return str1;
	}
}
