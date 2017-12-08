package spoj.dynamic_programming.mkbudget;

import java.util.Random;

public class MKBUDGET_Brute_Force {

	static int solve(int[] count, int hire, int salary, int severance) {
		int minCount = Integer.MAX_VALUE;
		int maxCount = Integer.MIN_VALUE;
		for (int x : count) {
			minCount = Math.min(minCount, x);
			maxCount = Math.max(maxCount, x);
		}
		return solve(count, hire, salary, severance, minCount, maxCount, new int[count.length], 0);
	}

	static int solve(int[] count, int hire, int salary, int severance, int minCount, int maxCount,
			int[] curr, int idx) {

		if (idx == count.length) {
			if (curr[0] < count[0]) {
				return Integer.MAX_VALUE;
			}
			int cost = (curr[0] * hire) + (curr[0] * salary);
			for (int i = 1; i < count.length; i++) {
				if (curr[i] < count[i]) {
					return Integer.MAX_VALUE;
				}
				cost += curr[i] * salary;
				if (curr[i] > curr[i - 1]) {
					cost += (curr[i] - curr[i - 1]) * hire;
				}
				if (curr[i] < curr[i - 1]) {
					cost += (curr[i - 1] - curr[i]) * severance;
				}
			}
			return cost;
		}

		int cost = Integer.MAX_VALUE;
		for (int cnt = minCount; cnt <= maxCount; cnt++) {
			curr[idx] = cnt;
			cost = Math.min(cost, solve(count, hire, salary, severance, minCount, maxCount, curr, idx + 1));
		}
		return cost;
	}

	static int[] generateRandomCountArray(Random rnd) {
		int[] result = new int[rnd.nextInt(5) + 3];
		for (int i = 0; i < result.length; i++) {
			result[i] = rnd.nextInt(10) + 5;
		}
		return result;
	}
}
