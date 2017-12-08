package spoj.dynamic_programming;

// http://www.spoj.com/problems/DSUBSEQ/

import java.util.Set;
import java.util.TreeSet;

public class DSUBSEQ {

	public static void main(String[] args) {
		test("A");
		test("AA");
		test("AAA");
		test("AABBB");
		test("ABAB");
		test("ABABAAAB");
		test("ABABAABABABABBBABAB");
		System.out.println(calculateUniqueSubseq("ABBAAABABABABABBABABABABABA"));
		System.out.println(calculateUniqueSubseq("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ"));
	}

	static void test(String s) {
		System.out.println(generateAllSubseq(s).size() + "\t" + calculateUniqueSubseq(s));
	}

	static Set<String> generateAllSubseq(String s) {
		Set<String> set = new TreeSet<>();
		generateAllSubseq(0, s, new StringBuilder(), set);
		return set;
	}

	static void generateAllSubseq(int idx, String s, StringBuilder sb, Set<String> set) {
		if (idx == s.length()) {
			set.add(sb.toString());
			return;
		}

		generateAllSubseq(idx + 1, s, sb, set);

		sb.append(s.charAt(idx));
		generateAllSubseq(idx + 1, s, sb, set);
		sb.setLength(sb.length() - 1);
	}

	// F(N) = 2 * F(N - 1) - F(t - 1)
	// where t is maximal index <= N
	// such that s.charAt(t) == s.charAt(N)
	// if s.charAt(N) != s.charAt(t) for every t <= N
	// then F(N) = 2 * F(N - 1)
	//
	// Complexity of solution is O(N^2)
	// TODO: reduce complexity by using HashMap
	static long calculateUniqueSubseq(String s) {
		long[] mem = new long[s.length() + 1];
		mem[0] = 1;
		for (int i = 0; i < s.length(); i++) {
			mem[i + 1] = (mem[i] * 2) % 1000000007;

			// TODO: instead of this loop - just memoize in HashMap the last position of each
			// character
			for (int j = i - 1; j >= 0; j--) {
				if (s.charAt(j) == s.charAt(i)) {
					mem[i + 1] -= mem[j];
					if (mem[i + 1] < 0) {
						mem[i + 1] += 1000000007;
					}
					break;
				}
			}
		}
		return mem[s.length()];
	}
}
