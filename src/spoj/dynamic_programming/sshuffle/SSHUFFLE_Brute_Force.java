package spoj.dynamic_programming.sshuffle;

import java.util.ArrayList;
import java.util.List;

// http://www.spoj.com/problems/SSHUFFLE/

public class SSHUFFLE_Brute_Force {

	public static void main(String[] args) {
		SSHUFFLE_Testing.test(null, 9, "ab".toCharArray(), 50);
	}

	static long countWaysToObtainS3(String s1, String s2, String s3) {
		List<String> allCombinations = generateAllCombinationsOfAllSubsequences(s1, s2);
		long count = 0;
		for (String s : allCombinations) {
			if (s.equals(s3)) {
				count++;
			}
		}
		return count;
	}

	static List<String> generateAllCombinationsOfAllSubsequences(String s1, String s2) {
		List<String> collected = new ArrayList<>();
		for (String sub1 : generateAllSubsequences(s1)) {
			for (String sub2 : generateAllSubsequences(s2)) {
				collected.addAll(generateAllCombinationsOfSubsequences(sub1, sub2));
			}
		}
		return collected;
	}

	static List<String> generateAllSubsequences(String s) {
		List<String> collected = new ArrayList<>();
		generateAllSubsequences(s, 0, new StringBuilder(), collected);
		return collected;
	}

	static void generateAllSubsequences(String s, int i, StringBuilder sb, List<String> collected) {
		if (i == s.length()) {
			collected.add(sb.toString());
			return;
		}

		generateAllSubsequences(s, i + 1, sb, collected);

		sb.append(s.charAt(i));
		generateAllSubsequences(s, i + 1, sb, collected);
		sb.setLength(sb.length() - 1);
	}

	static List<String> generateAllCombinationsOfSubsequences(String s1, String s2) {
		List<String> collected = new ArrayList<>();
		generateAllCombinationsOfSubsequences(s1, s2, 0, 0, new StringBuilder(), collected);
		return collected;

	}

	static void generateAllCombinationsOfSubsequences(String s1, String s2, int i1, int i2, StringBuilder sb, List<String> collected) {

		if ((i1 == s1.length()) && (i2 == s2.length())) {
			collected.add(sb.toString());
			return;
		}

		if (i1 < s1.length()) {
			sb.append(s1.charAt(i1));
			generateAllCombinationsOfSubsequences(s1, s2, i1 + 1, i2, sb, collected);
			sb.setLength(sb.length() - 1);
		}

		if (i2 < s2.length()) {
			sb.append(s2.charAt(i2));
			generateAllCombinationsOfSubsequences(s1, s2, i1, i2 + 1, sb, collected);
			sb.setLength(sb.length() - 1);
		}
	}
}
