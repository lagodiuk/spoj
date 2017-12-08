package spoj.dynamic_programming.mdolls;

// http://www.spoj.com/problems/MDOLLS/en/

import java.util.Arrays;
import java.util.TreeSet;

public class MDOLLS_4_Greedy_Non_Recursive {

	public static void main(String[] args) {
		MDOLLS_1_Brute_force.testFewManualCases(MDOLLS_4_Greedy_Non_Recursive::solve);
		MDOLLS_1_Brute_force.testRandomlyGeneratedCases(MDOLLS_4_Greedy_Non_Recursive::solve);
		MDOLLS_1_Brute_force.testRandomlyGeneratedCases(MDOLLS_4_Greedy_Non_Recursive::solve, true, 8, 10);
		// MDOLLS_1_Brute_force.testRandomlyGeneratedCases(MDOLLS_4_Greedy::solve, false, 10000,
		// 10000);
	}

	static int solve(Doll[] dolls) {
		Arrays.sort(dolls, (d1, d2) -> {
			return Integer.compare(d1.height, d2.height);
		});

		int result = solve(dolls, dolls.length - 1, new TreeSet<>());
		return result;
	}

	// Non-recursive
	static int solve(Doll[] dolls, int curr, TreeSet<Integer> topLevel) {

		while (curr != -1) {

			Doll currDoll = dolls[curr];
			boolean nestedIntoCurrentDoll = false;

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
					nestedIntoCurrentDoll = true;
				}
			}

			if (!nestedIntoCurrentDoll) {
				// add current doll as a new top level doll
				topLevel.add(curr);
			}

			curr--;
		}

		return topLevel.size();
	}
}
