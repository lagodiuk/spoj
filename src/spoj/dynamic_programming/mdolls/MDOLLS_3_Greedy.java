package spoj.dynamic_programming.mdolls;

// http://www.spoj.com/problems/MDOLLS/en/

import java.util.Arrays;
import java.util.TreeSet;

public class MDOLLS_3_Greedy {

	public static void main(String[] args) {
		MDOLLS_1_Brute_force.testFewManualCases(MDOLLS_3_Greedy::solve);
		MDOLLS_1_Brute_force.testRandomlyGeneratedCases(MDOLLS_3_Greedy::solve);
	}

	static int solve(Doll[] dolls) {
		Arrays.sort(dolls, (d1, d2) -> {
			return Integer.compare(d1.height, d2.height);
		});

		int result = solve(dolls, dolls.length - 1, new TreeSet<>());
		return result;
	}

	static int solve(Doll[] dolls, int curr, TreeSet<Integer> topLevel) {

		if (curr == -1) {
			// System.out.println("___" + topLevel.toString());
			return topLevel.size();
		}

		// System.out.println(topLevel.toString() + " " + curr);

		Doll currDoll = dolls[curr];
		int result = Integer.MAX_VALUE;

		if (!topLevel.isEmpty()) {

			int bestCandidateIdx = -1;
			Doll bestCandidate = null;

			// Yay!
			// It seems, that we can make a greedy choice here:
			// Choose the doll with the smallest width (which also fits into current doll)
			for (int topLevelIdx : topLevel) {
				Doll existingTopLevelDoll = dolls[topLevelIdx];

				if ((currDoll.width < existingTopLevelDoll.width)
						&& (currDoll.height < existingTopLevelDoll.height)) {

					if (bestCandidateIdx == -1) {
						bestCandidateIdx = topLevelIdx;
						bestCandidate = existingTopLevelDoll;
					} else {
						if (bestCandidate.width > existingTopLevelDoll.width) {
							bestCandidateIdx = topLevelIdx;
							bestCandidate = existingTopLevelDoll;
						}
					}
				}
			}

			if (bestCandidateIdx != -1) {
				// nest existing top level doll into current doll
				topLevel.remove(bestCandidateIdx);
				topLevel.add(curr);
				result = solve(dolls, curr - 1, topLevel);
				topLevel.remove(curr);
				topLevel.add(bestCandidateIdx);
			}
		}

		if (result == Integer.MAX_VALUE) {
			// add current doll as a new top level doll
			topLevel.add(curr);
			result = solve(dolls, curr - 1, topLevel);
			topLevel.remove(curr);
		}

		return result;
	}
}
