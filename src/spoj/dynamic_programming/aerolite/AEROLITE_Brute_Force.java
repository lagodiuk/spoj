package spoj.dynamic_programming.aerolite;

// http://www.spoj.com/problems/AEROLITE/

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class AEROLITE_Brute_Force {

	public static void main(String[] args) {
		Set<String> generated = generate(8, 5);
		System.out.println(generated.size());
		for (String s : generated) {
			System.out.println(s);
		}
	}

	static Set<String> generate(int N, int depth) {

		if ((N < depth) || (N <= 0)) {
			return Collections.emptySet();
		}

		if (depth == 1) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < N; i++) {
				sb.append("()");
			}
			if (sb.length() > 0) {
				LinkedHashSet<String> result = new LinkedHashSet<>();
				result.add(sb.toString());
				return result;
			} else {
				return Collections.emptySet();
			}
		}

		Set<String> result = new LinkedHashSet<>();

		Set<String> set0 = generate(N - 1, depth - 1);
		for (String s0 : set0) {
			result.add("(" + s0 + ")");
		}

		for (int N1 = 1; N1 < N; N1++) {
			for (int D1 = 1; D1 <= depth; D1++) {

				Set<String> set1 = generate(N1, D1);
				Set<String> set2 = generate(N - N1, depth);

				for (String s1 : set1) {
					for (String s2 : set2) {
						result.add(s1 + s2);
					}
				}

				Set<String> set3 = generate(N1, depth);
				Set<String> set4 = generate(N - N1, D1);

				for (String s3 : set3) {
					for (String s4 : set4) {
						result.add(s3 + s4);
					}
				}
			}
		}

		return result;
	}
}
