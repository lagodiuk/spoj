package spoj.dynamic_programming.ikeyb;

// http://www.spoj.com/problems/IKEYB/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IKEYB_4_DP_Bottom_Up_NonRecursive_Backtrack {

	public static void main(String[] args) {
		IKEYB_Brute_Force.verifySpecificTestCases(IKEYB_4_DP_Bottom_Up_NonRecursive_Backtrack::solve);
		IKEYB_Brute_Force.verifyRandomTestCases(IKEYB_4_DP_Bottom_Up_NonRecursive_Backtrack::solve);
	}

	static void printSolution(char[] letters, int[] frequencies, char[] keys) {
		List<String> keysAssignment = solve(letters, frequencies, keys);

		for (String s : keysAssignment) {
			System.out.println(s);
		}
		System.out.println();
	}

	static final long INF = 1000000000;
	static final int MAX_LETTERS = 91;

	static long[][] mem = new long[MAX_LETTERS][MAX_LETTERS];
	static List<String> keysAssignment = new ArrayList<>(MAX_LETTERS);

	// Runtime complexity: O(K * L * L)
	// Memory consumption: O(K * L)
	private static List<String> solve(char[] letters, int[] frequencies, char[] keys) {

		int[] cumulativeFreq = calculateCumulativeSumArray(frequencies);

		for (int keyIdx = 0; keyIdx <= keys.length; keyIdx++) {
			for (int currLetterIdx = letters.length - 1; currLetterIdx >= 0; currLetterIdx--) {

				mem[keyIdx][currLetterIdx] = INF;

				int nextLetterIdx = currLetterIdx - 1;
				long currKeyWeight = 0;

				while (nextLetterIdx >= -1) {
					currKeyWeight += cumulativeFreq[currLetterIdx + 1] - cumulativeFreq[nextLetterIdx + 2];

					long subProblemSolution = ((nextLetterIdx == -1)
							? (((keyIdx - 1) == 0) ? 0 : INF)
							: (((keyIdx - 1) == -1) ? INF : mem[keyIdx - 1][nextLetterIdx]));

					mem[keyIdx][currLetterIdx] = Math.min(
							mem[keyIdx][currLetterIdx],
							currKeyWeight + subProblemSolution);

					nextLetterIdx--;
				}
			}
		}

		keysAssignment.clear();
		backtrack(cumulativeFreq, keys.length, letters.length - 1, mem, keys, letters, keysAssignment);
		Collections.reverse(keysAssignment);

		return keysAssignment;
	}

	private static int[] calculateCumulativeSumArray(int[] frequencies) {
		int[] cumulativeFreq = new int[frequencies.length + 1];
		for (int i = 1; i < cumulativeFreq.length; i++) {
			cumulativeFreq[i] = cumulativeFreq[i - 1] + frequencies[i - 1];
		}
		return cumulativeFreq;
	}

	// Non-recursive
	static void backtrack(int[] cumulativeFreq, int key, int curr, long[][] mem, char[] keys, char[] letters, List<String> keysAssignment) {
		while (curr != -1) {

			int minNext = -1;

			int next = curr - 1;
			long currKeyWeight = 0;
			while (next >= -1) {
				currKeyWeight += cumulativeFreq[curr + 1] - cumulativeFreq[next + 2];

				long subProblem = (next == -1)
						? (((key - 1) == 0) ? 0 : INF)
						: (((key - 1) == -1) ? INF : mem[key - 1][next]);

				if (mem[key][curr] == (currKeyWeight + subProblem)) {
					minNext = next;
				}

				next--;
			}

			StringBuilder sb = new StringBuilder().append(keys[key - 1]).append(": ");
			for (int j = minNext + 1; j <= curr; j++) {
				sb.append(letters[j]);
			}
			keysAssignment.add(sb.toString());

			curr = minNext;
			key--;
		}
	}
}
