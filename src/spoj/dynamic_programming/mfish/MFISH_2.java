package spoj.dynamic_programming.mfish;

// http://www.spoj.com/problems/MFISH/en/

import java.util.Arrays;

public class MFISH_2 {

	public static void main(String[] args) {
		MFISH_1.testFewManualCases(MFISH_2::solve);
		MFISH_1.testRandomCases(MFISH_2::solve);
		MFISH_1.testRandomCases(MFISH_2::solve, 50, 0.7, true);
		MFISH_1.testRandomCases(MFISH_2::solve, 50, 0.1, true);
		MFISH_1.testRandomCases(MFISH_2::solve, 50, 0.0, true);
	}

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

		// just get rid of "-1" in array of board indices
		for (int i = boatIdxs.length - 2; i >= 0; i--) {
			if (boatIdxs[i] == -1) {
				boatIdxs[i] = boatIdxs[i + 1];
			}
		}

		// System.out.println(Arrays.toString(fish));
		// System.out.println(Arrays.toString(fishCovered));
		// System.out.println(Arrays.toString(boatIdx));

		int start = 1;
		int result = solve(boats, fishCovered, boatIdxs, start);
		// System.out.println(result);
		// System.out.println();

		return result;
	}

	static int solve(Boat[] boats, int[] fishCovered, int[] boatIdxs, int curr) {
		int boatIdx = boatIdxs[curr];
		Boat boat = boats[boatIdx];

		if (boatIdx == (boats.length - 1)) {
			// in case if we reached to the last boat - just select position
			// with the highest coverage
			int result = NEGATIVE_INFINITY;
			while ((curr < boatIdxs.length) && (boatIdx == boatIdxs[curr])) {
				result = Math.max(result, fishCovered[curr]);
				curr++;
			}
			return result;
		}

		int result = NEGATIVE_INFINITY;

		// position of the next boat
		int next = curr + boat.length;

		// if index of the next board differs only by 1 from the index of current boat
		if ((next < boatIdxs.length) && (boatIdxs[next] == (boatIdx + 1))) {
			result = fishCovered[curr] + solve(boats, fishCovered, boatIdxs, next);
		}

		// in case if the following position belongs to the current boat as well
		if (((curr + 1) < boatIdxs.length) && (boatIdxs[curr + 1] == boatIdx)) {
			result = Math.max(result, solve(boats, fishCovered, boatIdxs, curr + 1));
		}

		return result;
	}

	static final int NEGATIVE_INFINITY = -100000000;

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
