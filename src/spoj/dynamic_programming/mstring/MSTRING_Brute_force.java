package spoj.dynamic_programming.mstring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;

// http://www.spoj.com/problems/MSTRING/en/

public class MSTRING_Brute_force {

	static void testRandomlyGeneratedCases(BiFunction<String, String, Integer> tested) {
		Random rnd = new Random(1);

		for (int testNum = 0; testNum < 30; testNum++) {
			int sourceStrLen = rnd.nextInt(12) + 1;

			List<Character> chars = new ArrayList<>();
			for (int i = 0; i < sourceStrLen; i++) {
				chars.add((char) (rnd.nextInt('z' - 'a') + 'a'));
			}

			String sourceStr = chars
					.stream()
					.map(chr -> chr.toString())
					.reduce((acc, chr) -> acc + chr)
					.get();

			Collections.shuffle(chars);
			chars.addAll(chars.subList(0, rnd.nextInt(chars.size())));
			String targetStr = chars
					.stream()
					.map(chr -> chr.toString())
					.reduce((acc, chr) -> acc + chr)
					.get();

			compareResultWithBruteForce(sourceStr, targetStr, tested);
		}
	}

	static void testFewManualCases(BiFunction<String, String, Integer> tested) {
		compareResultWithBruteForce("banana", "anbnaanbaan", tested);
		compareResultWithBruteForce("babab", "babba", tested);
		compareResultWithBruteForce("bab", "aba", tested);
		compareResultWithBruteForce("baba", "abab", tested);
	}

	static void compareResultWithBruteForce(String sourceStr, String targetStr, BiFunction<String, String, Integer> tested) {

		System.out.println(sourceStr);
		System.out.println(targetStr);

		int testedResult = tested.apply(sourceStr, targetStr);
		int bruteForceResult = solve(sourceStr, targetStr);

		if (testedResult != bruteForceResult) {
			throw new RuntimeException("Different results!");
		}

		System.out.println("Result: " + testedResult);
		System.out.println();
	}

	static final int INF = 100000;

	static int solve(String sourceStr, String targetStr) {
		Set<String> sourceSubsets = new HashSet<>();
		generateAllSubsets(sourceStr, 0, new StringBuilder(), sourceSubsets);

		Set<String> targetSubsets = new HashSet<>();
		generateAllSubsets(targetStr, 0, new StringBuilder(), targetSubsets);

		int minLength = INF;
		for (String s : sourceSubsets) {
			if (!targetSubsets.contains(s)) {
				minLength = Math.min(minLength, s.length());
			}
		}

		return minLength;
	}

	static void generateAllSubsets(String str, int pos, StringBuilder sb, Set<String> subsets) {
		if (pos == str.length()) {
			subsets.add(sb.toString());
			return;
		}

		sb.append(str.charAt(pos));
		generateAllSubsets(str, pos + 1, sb, subsets);
		sb.setLength(sb.length() - 1);

		generateAllSubsets(str, pos + 1, sb, subsets);
	}
}
