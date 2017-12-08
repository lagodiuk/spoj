package spoj.dynamic_programming.mfish;

// http://www.spoj.com/problems/MFISH/en/

import java.util.Arrays;

public class MFISH_3 {

	public static void main(String[] args) {
		MFISH_1.testFewManualCases(MFISH_3::solve);
		MFISH_1.testRandomCases(MFISH_3::solve);
		MFISH_1.testRandomCases(MFISH_3::solve, 50, 0.7, true);
		MFISH_1.testRandomCases(MFISH_3::solve, 50, 0.1, true);
		MFISH_1.testRandomCases(MFISH_3::solve, 50, 0.0, true);
		// Stress test (keep in mind, that output to console is relatively slow)
		MFISH_1.testRandomCases(MFISH_3::solve, 10000, 0.6, false);
		MFISH_1.testRandomCases(MFISH_3::solve, 10000, 0.1, false);
	}

	static final int NEGATIVE_INFINITY = -100000000;

	static int solve(int[] fish, Boat[] boats) {
		// all boats has to be sorted by anchor position
		Arrays.sort(boats, (b1, b2) -> Integer.compare(b1.anchor, b2.anchor));

		// will be used for calculation of covered fish
		int[] fishAcc = calculateCumulativePreffixSum(fish);

		// amount of fish, which covered by the boat, which starts at given position
		int[] fishCovered = new int[fishAcc.length];
		Arrays.fill(fishCovered, NEGATIVE_INFINITY);

		// the index of boat, which allowed to start at given position
		int[] boatIdxs = new int[fishAcc.length];
		Arrays.fill(boatIdxs, -1);

		// iterate over all boats
		for (int b = 0; b < boats.length; b++) {
			Boat boat = boats[b];
			// current boat can't start before anchor of the previous boat
			int from = Math.max(boat.minPos, ((b > 0) ? boats[b - 1].anchor + 1 : -1));
			// current boat can't finish after the end of the river
			int to = Math.min((fishAcc.length - boat.length) + 1, boat.anchor + 1);

			// just counting amount of covered fish
			// for every allowed start position
			for (int i = from; i < to; i++) {
				fishCovered[i] = countFish(fishAcc, i, (i + boat.length) - 1);
				// also, remember the index of the boat
				boatIdxs[i] = b;
			}
		}

		// just get rid of "-1" in array of boat start positions
		for (int i = boatIdxs.length - 2; i >= 0; i--) {
			if (boatIdxs[i] == -1) {
				boatIdxs[i] = boatIdxs[i + 1];
			}
		}

		// System.out.println(Arrays.toString(fishAcc));
		// System.out.println(Arrays.toString(fishCovered));
		// System.out.println(Arrays.toString(boatIdxs));

		int result = solve(boats, fishCovered, boatIdxs);
		// System.out.println(result);
		// System.out.println();

		return result;
	}

	static int solve(Boat[] boats, int[] fishCovered, int[] boatIdxs) {
		int[] result = new int[boatIdxs.length];

		// skip unreachable cells in the end of the river
		int curr = boatIdxs.length - 1;
		while (boatIdxs[curr] == -1) {
			curr--;
		}

		// now, we reached to the last boat
		int currBoatIdx = boatIdxs[curr];
		while ((curr >= 0) && (currBoatIdx == boatIdxs[curr])) {
			result[curr] = fishCovered[curr];
			result[curr] = Math.max(result[curr], (curr + 1) < result.length ? result[curr + 1] : 0);
			curr--;
		}

		while (curr >= 0) {
			currBoatIdx = boatIdxs[curr];
			if (currBoatIdx == -1) {
				break;
			}

			Boat boat = boats[currBoatIdx];

			result[curr] = NEGATIVE_INFINITY;
			int next = curr + boat.length;
			// if index of the next board differs only by 1 from the index of current boat
			if ((next < boatIdxs.length) && (boatIdxs[next] == (currBoatIdx + 1))) {
				result[curr] = fishCovered[curr] + result[next];
			}

			// in case if the following position belongs to the current boat as well
			if (((curr + 1) < boatIdxs.length) && (boatIdxs[curr + 1] == currBoatIdx)) {
				result[curr] = Math.max(result[curr], result[curr + 1]);
			}
			curr--;
		}

		return result[curr + 1];
	}

	static int[] calculateCumulativePreffixSum(int[] arr) {
		int[] arrAcc = new int[arr.length + 1];
		arrAcc[0] = 0;
		for (int i = 1; i < arrAcc.length; i++) {
			arrAcc[i] = arrAcc[i - 1] + arr[i - 1];
		}
		return arrAcc;
	}

	static int countFish(int[] fishAcc, int from, int to) {
		return fishAcc[to] - fishAcc[from - 1];
	}

}
