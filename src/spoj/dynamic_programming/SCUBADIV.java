package spoj.dynamic_programming;

// http://www.spoj.com/problems/SCUBADIV/

public class SCUBADIV {

	static int[] oxygen;
	static int[] nitrogen;
	static int[] weight;

	// Exponential complexity solution
	// TODO: add memoization
	static long optimalWeight(int i, int requiredOxygen, int requiredNitrogen) {
		if (i < 0) {
			if ((requiredOxygen > 0) || (requiredNitrogen > 0)) {
				return 10000000; // "Infinity"
			} else {
				return 0;
			}
		}

		// TODO: memoize[ballonIndex][requiredOxygen][requiredNitrogen]
		return Math.min(
				optimalWeight(i - 1, requiredOxygen, requiredNitrogen),
				optimalWeight(i - 1, requiredOxygen - oxygen[i], requiredNitrogen - nitrogen[i]) + weight[i]);
	}

	/**
	 * 3 36 120
	 * 10 25 129
	 * 5 50 250
	 * 1 45 130
	 * 4 20 119
	 * @param args
	 */
	public static void main(String[] args) {
		oxygen = new int[]{3, 10, 5, 1, 4};
		nitrogen = new int[]{36, 25, 50, 45, 20};
		weight = new int[]{120, 129, 250, 130, 119};
		System.out.println(optimalWeight(4, 5, 60));
	}
}
