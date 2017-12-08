package spoj.dynamic_programming.seqpar;

// http://www.spoj.com/problems/SEQPAR/

import java.util.HashMap;
import java.util.Map;

public class SEQPAR_16_AlphaBeta_Pruning_Min_Max_DP_HashMap {

	public static void main(String[] args) {
		SEQPAR_BruteForce.testFewManualCases(SEQPAR_16_AlphaBeta_Pruning_Min_Max_DP_HashMap::solve);
		SEQPAR_BruteForce.testRandomized(SEQPAR_16_AlphaBeta_Pruning_Min_Max_DP_HashMap::solve);
		SEQPAR_BruteForce.testRandomized(SEQPAR_16_AlphaBeta_Pruning_Min_Max_DP_HashMap::solve, 1000, 1000, false);
	}

	static int solve(int[] arr, int delims) {

		Map<State, Integer> memoized = new HashMap<>();

		int result = min(0, delims - 1, INFINITY, -INFINITY, arr, memoized);

		return result;
	}

	static final int INFINITY = 100000000;

	static int min(int l, int s, int minSoFar, int maxSoFar, int[] arr, Map<State, Integer> memoized) {

		if ((s == 0) && (l < arr.length)) {
			return sum(arr, l, arr.length - 1);
		}

		if (l >= arr.length) {
			return INFINITY;
		}

		State state = new State(l, s);

		if (memoized.containsKey(state)) {
			return memoized.get(state);
		}

		int result = INFINITY;
		for (int r = l; r < ((arr.length - s) + 1); r++) {
			result = Math.min(result, max(l, r, s, minSoFar, maxSoFar, arr, memoized));
			if (result <= maxSoFar) {
				return result;
			}
			minSoFar = Math.min(result, minSoFar);
		}

		memoized.put(state, result);
		return result;
	}

	static int max(int l, int r, int s, int minSoFar, int maxSoFar, int[] arr, Map<State, Integer> memoized) {
		int result = sum(arr, l, r);
		if (result >= minSoFar) {
			return result;
		}
		maxSoFar = Math.max(result, maxSoFar);
		result = Math.max(result, min(r + 1, s - 1, minSoFar, maxSoFar, arr, memoized));
		return result;
	}

	// TODO: use cumulative sum array
	static int sum(int[] arr, int from, int to) {
		int sum = 0;
		for (int i = from; i <= to; i++) {
			sum += arr[i];
		}
		return sum;
	}

	static class State {
		final int l;
		final int s;
		public State(int l, int s) {
			this.l = l;
			this.s = s;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + this.l;
			result = (prime * result) + this.s;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (this.getClass() != obj.getClass()) {
				return false;
			}
			State other = (State) obj;
			if (this.l != other.l) {
				return false;
			}
			if (this.s != other.s) {
				return false;
			}
			return true;
		}
	}
}
