package spoj.dynamic_programming.csubseqs;

// http://www.spoj.com/problems/CSUBSEQS/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Count_Distinct_Subsequences_Brute_Force {

	static int countDistinctSubsequences(String... strs) {
		List<Set<String>> collectedSubseqs = new ArrayList<>();

		for (String s : strs) {
			Set<String> collected = new HashSet<>();
			getAllDistinctSubsequences(s, 0, new StringBuilder(), collected);
			collectedSubseqs.add(collected);
		}

		Set<String> intersection = new HashSet<>();

		Set<String> first = collectedSubseqs.get(0);
		for (String s : first) {
			boolean presentInAll = true;
			for (int i = 1; i < collectedSubseqs.size(); i++) {
				if (!collectedSubseqs.get(i).contains(s)) {
					presentInAll = false;
					break;
				}
			}
			if (presentInAll) {
				intersection.add(s);
			}
		}

		return intersection.size();
	}

	static void getAllDistinctSubsequences(String s, int idx, StringBuilder sb, Set<String> collected) {
		if (idx == s.length()) {
			if (sb.length() > 0) {
				collected.add(sb.toString());
			}
			return;
		}

		sb.append(s.charAt(idx));
		getAllDistinctSubsequences(s, idx + 1, sb, collected);
		sb.setLength(sb.length() - 1);

		getAllDistinctSubsequences(s, idx + 1, sb, collected);
	}
}
