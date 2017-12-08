package spoj.dynamic_programming.ikeyb;

// http://www.spoj.com/problems/IKEYB/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IKEYB_2_DP_Top_Down {

	public static void main(String[] args) {
		// printSolution(new char[]{'a', 'b', 'c', 'd'},
		// new int[]{1, 1, 1, 1},
		// new char[]{'1', '2', '3'});
		//
		// printSolution("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray(),
		// new int[]{3371, 589, 1575, 1614, 6212, 971, 773, 1904, 2989, 123, 209, 1588, 1513, 2996,
		// 3269, 1080, 121, 2726, 3083, 4368, 1334, 518, 752,
		// 427, 733, 871},
		// "23456789".toCharArray());

		IKEYB_Brute_Force.verifySpecificTestCases(IKEYB_2_DP_Top_Down::solve);
		IKEYB_Brute_Force.verifyRandomTestCases(IKEYB_2_DP_Top_Down::solve);
	}

	static void printSolution(char[] letters, int[] frequencies, char[] keys) {
		List<String> keysAssignment = solve(letters, frequencies, keys);

		for (String s : keysAssignment) {
			System.out.println(s);
		}
		System.out.println();
	}

	// Runtime complexity: O(K * L * L)
	// Memory consumption: O(K * L)
	private static List<String> solve(char[] letters, int[] frequencies, char[] keys) {

		long[][] mem = new long[keys.length + 1][letters.length + 1];
		for (long[] row : mem) {
			Arrays.fill(row, -1);
		}

		int[] cumulativeFreq = calculateCumulativeSumArray(frequencies);

		solve(cumulativeFreq, keys.length, letters.length - 1, mem);

		List<String> keysAssignment = new ArrayList<>();
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

	static final long INF = 1000000000;

	static long solve(int[] cumulativeFreq, int key, int curr, long[][] mem) {

		if (key == -1) {
			return INF;
		}

		if (curr == -1) {
			return (key == 0) ? 0 : INF;
		}

		if (mem[key][curr] != -1) {
			return mem[key][curr];
		}

		long result = INF;

		int next = curr - 1;
		long currKeyWeight = 0;
		while (next >= -1) {
			currKeyWeight += cumulativeFreq[curr + 1] - cumulativeFreq[next + 2];

			result = Math.min(result, currKeyWeight + solve(cumulativeFreq, key - 1, next, mem));

			next--;
		}

		mem[key][curr] = result;
		return result;
	}

	static void backtrack(int[] cumulativeFreq, int key, int curr, long[][] mem, char[] keys, char[] letters, List<String> keysAssignment) {

		if (curr == -1) {
			return;
		}

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

		backtrack(cumulativeFreq, key - 1, minNext, mem, keys, letters, keysAssignment);
	}
}
