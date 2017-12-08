package spoj.dynamic_programming.mfish;

// http://www.spoj.com/problems/MFISH/en/

import java.util.Arrays;

public class MFISH_Top_Down_Linear_comlexity {

	public static void main(String[] args) {
		MFISH_1.testFewManualCases(MFISH_Top_Down_Linear_comlexity::solve);
		MFISH_1.testRandomCases(MFISH_Top_Down_Linear_comlexity::solve);
		MFISH_1.testRandomCases(MFISH_Top_Down_Linear_comlexity::solve, 50, 0.7, true);
		MFISH_1.testRandomCases(MFISH_Top_Down_Linear_comlexity::solve, 50, 0.1, true);
		MFISH_1.testRandomCases(MFISH_Top_Down_Linear_comlexity::solve, 50, 0.0, true);
	}

	static int solve(int[] fish, Boat[] boats) {
		Arrays.sort(boats,
				(b1, b2) -> Integer.compare(b1.anchor, b2.anchor));
		// We have to memoize only O(N) solutions of subproblems!
		int[] memoized = new int[fish.length + 1];
		Arrays.fill(memoized, NOT_INITIALIZED);

		// Get 1:1 mapping of river segments -> to boat indices
		int[] boatIndices = getBoatIndicesMapping(fish, boats);

		int[] cumulativeSumArray = new int[fish.length + 1];
		cumulativeSumArray[0] = 0;
		for (int i = 1; i < cumulativeSumArray.length; i++) {
			cumulativeSumArray[i] = cumulativeSumArray[i - 1] + fish[i - 1];
		}

		int result = solve(boats, cumulativeSumArray, 1, boatIndices, memoized);
		return result;
	}

	static final int NEGATIVE_INFINITY = -100000000;
	static final int NOT_INITIALIZED = -1;
	static final int NO_BOAT_CAN_BE_HERE = -1;

	static int solve(Boat[] boats, int[] cumulativeSumArray,
			int start,
			int[] boatIndices,
			int[] memoized) {

		// get index of current boat (from 1:1 mapping)
		int boat = boatIndices[start];

		if (boat == NO_BOAT_CAN_BE_HERE) {
			// in case, if no boat can be aligned
			// in the current segment of the river
			return NEGATIVE_INFINITY;
		}

		if (boat == boats.length) {
			// all boats successfully aligned
			return 0;
		}

		if (start > boats[boat].anchor) {
			// anchor of the boat is unreachable
			// from given start position
			return NEGATIVE_INFINITY;
		}

		if ((cumulativeSumArray.length - start) < boats[boat].length) {
			// boat can't fit into the river
			// (intersects the right boundary of the fish array)
			return NEGATIVE_INFINITY;
		}

		if (memoized[start] != NOT_INITIALIZED) {
			return memoized[start];
		}

		if ((start + boats[boat].length) <= boats[boat].anchor) {
			// given start position is too far from
			// anchor of current boat
			// so, the start position has to be closer
			return solve(boats, cumulativeSumArray, start + 1, boatIndices, memoized);
		}

		int nextBoatIndex;

		// Subproblem 1
		// we would like to check the gain, in case if we put current boat
		// into the next position on the river
		nextBoatIndex = ((start + 1) < boatIndices.length)
				? boatIndices[start + 1]
				: NO_BOAT_CAN_BE_HERE;
		int subProblem1 = (nextBoatIndex == boat)
				? solve(boats, cumulativeSumArray, start + 1, boatIndices, memoized)
				: NEGATIVE_INFINITY;

		int coverage =
				countFish(cumulativeSumArray, start, (start + boats[boat].length) - 1);

		// Subproblem 2
		// we would like to check the gain, in case if we put current boat
		// into the current position on the river
		// (the index of next boat should differ by 1 - from the index of current boat)
		nextBoatIndex = ((start + boats[boat].length) < boatIndices.length)
				? boatIndices[start + boats[boat].length]
				: NO_BOAT_CAN_BE_HERE;
		int subProblem2 = (nextBoatIndex == (boat + 1))
				? coverage + solve(boats, cumulativeSumArray, start + boats[boat].length, boatIndices, memoized)
				: coverage;

		memoized[start] = Math.max(subProblem1, subProblem2);
		return memoized[start];
	}

	// Calculating 1:1 mapping with O(N) complexity
	// from the index of river segment
	// to the index of boat, which can start at given segment
	static int[] getBoatIndicesMapping(int[] fish, Boat[] boats) {
		int[] boatIndices = new int[fish.length + 1];
		Arrays.fill(boatIndices, NO_BOAT_CAN_BE_HERE);

		int currBoatIdx = 0;
		for (int i = 0; i < boatIndices.length; i++) {
			boatIndices[i] = currBoatIdx;

			if (boats[currBoatIdx].anchor == i) {
				currBoatIdx++;

				if (currBoatIdx == boats.length) {
					break;
				}
			}
		}

		return boatIndices;
	}

	static int countFish(int[] cumulativeSumArray, int from, int to) {
		return cumulativeSumArray[to] - cumulativeSumArray[from - 1];
	}
}
