package spoj.dynamic_programming.mdolls;

// http://www.spoj.com/problems/MDOLLS/en/

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class MDOLLS_5_Greedy {

	public static void main(String[] args) {
		MDOLLS_1_Brute_force.testFewManualCases(MDOLLS_5_Greedy::solve);
		MDOLLS_1_Brute_force.testRandomlyGeneratedCases(MDOLLS_5_Greedy::solve);
		MDOLLS_1_Brute_force.testRandomlyGeneratedCases(MDOLLS_5_Greedy::solve, true, 8, 10);
		MDOLLS_1_Brute_force.testRandomlyGeneratedCases(MDOLLS_5_Greedy::solve, false, 20000, 10000);
	}

	static int solve(Doll[] dolls) {
		Arrays.sort(dolls, (d1, d2) -> {
			return Integer.compare(d1.height, d2.height);
		});

		int result = solve(dolls, dolls.length - 1, new TreeMap<>());
		return result;
	}

	// Non-recursive
	static int solve(Doll[] dolls, int curr, TreeMap<Integer, Set<Integer>> topLevelWidthToIndex) {

		while (curr != -1) {

			Doll currDoll = dolls[curr];
			boolean nestedIntoCurrentDoll = false;

			if (!topLevelWidthToIndex.isEmpty()) {

				int bestCandidateIdx = -1;

				// Yay!
				// It seems, that we can make a greedy choice here:
				// Choose the doll with the smallest width (which also fits into current doll)
				SortedMap<Integer, Set<Integer>> subMap = topLevelWidthToIndex.tailMap(currDoll.width, false);
				for (int w : subMap.keySet()) {
					for (int id : subMap.get(w)) {
						Doll doll = dolls[id];
						if ((doll.width > currDoll.width) && (doll.height > currDoll.height)) {
							bestCandidateIdx = id;
							break;
						}
					}
					if (bestCandidateIdx != -1) {
						break;
					}
				}

				if (bestCandidateIdx != -1) {
					// nest existing top level doll into current doll
					Set<Integer> bucket = topLevelWidthToIndex.get(dolls[bestCandidateIdx].width);
					if (bucket.size() == 1) {
						topLevelWidthToIndex.remove(dolls[bestCandidateIdx].width);
					} else {
						bucket.remove(bestCandidateIdx);
					}

					Set<Integer> bucket2 = topLevelWidthToIndex.get(currDoll.width);
					if (bucket2 == null) {
						bucket2 = new HashSet<>();
					}
					bucket2.add(curr);
					topLevelWidthToIndex.put(currDoll.width, bucket2);

					nestedIntoCurrentDoll = true;
				}
			}

			if (!nestedIntoCurrentDoll) {
				// add current doll as a new top level doll
				Set<Integer> bucket2 = topLevelWidthToIndex.get(currDoll.width);
				if (bucket2 == null) {
					bucket2 = new HashSet<>();
				}
				bucket2.add(curr);
				topLevelWidthToIndex.put(currDoll.width, bucket2);
			}

			curr--;
		}

		int result = 0;
		for (Entry<Integer, Set<Integer>> e : topLevelWidthToIndex.entrySet()) {
			result += e.getValue().size();
		}
		return result;
	}
}
