package spoj.dynamic_programming.mdolls;

// http://www.spoj.com/problems/MDOLLS/en/

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class MDOLLS_2_Brute_force {

	public static void main(String[] args) {
		MDOLLS_1_Brute_force.testFewManualCases(MDOLLS_2_Brute_force::solve);
		MDOLLS_1_Brute_force.testRandomlyGeneratedCases(MDOLLS_2_Brute_force::solve);
	}

	static int solve(Doll[] dolls) {
		Arrays.sort(dolls, (d1, d2) -> {
			return Integer.compare(d1.height, d2.height);
		});

		int result = solve(dolls, 0, new TreeSet<>());
		return result;
	}

	static int solve(Doll[] dollsSortedByHeight, int curr, Set<Integer> topLevel) {

		if (curr == dollsSortedByHeight.length) {
			System.out.println("___" + topLevel.toString());
			return topLevel.size();
		}

		System.out.println(topLevel.toString() + " " + curr);

		Doll currDoll = dollsSortedByHeight[curr];
		int result = Integer.MAX_VALUE;

		if (!topLevel.isEmpty()) {

			for (int topLevelIdx : topLevel.toArray(new Integer[]{})) {
				Doll existingTopLevelDoll = dollsSortedByHeight[topLevelIdx];

				if ((currDoll.width > existingTopLevelDoll.width)
						&& (currDoll.height > existingTopLevelDoll.height)) {

					// nest existing top level doll into current doll
					topLevel.remove(topLevelIdx);
					topLevel.add(curr);
					result = Math.min(result, solve(dollsSortedByHeight, curr + 1, topLevel));
					topLevel.add(topLevelIdx);
					topLevel.remove(curr);
				}
			}
		}

		if (result == Integer.MAX_VALUE) {
			// add current doll as a new top level doll
			topLevel.add(curr);
			result = Math.min(result, solve(dollsSortedByHeight, curr + 1, topLevel));
			topLevel.remove(curr);
		}

		return result;
	}
}
