package spoj.dynamic_programming.scales;

// http://www.spoj.com/problems/SCALES/

public class SCALES_DP_Top_Down {

	public static void main(String[] args) {
		SCALES_Brute_Force.test_simple(SCALES_DP_Top_Down::solve);
		SCALES_Brute_Force.test(SCALES_DP_Top_Down::solve);
	}

	static int solve(int[] weight) {
		return solve(weight, weight.length - 1, 0);
	}

	static int solve(int[] weight, int position, int carry) {
		if (position < 0) {
			return (carry == 0) ? 1 : 0;
		}

		if ((weight[position] == 1) && (carry == 1)) {
			return solve(weight, position - 1, 1);
		}

		if ((weight[position] == 0) && (carry == 0)) {
			return solve(weight, position - 1, 0);
		}

		if (((weight[position] == 1) && (carry == 0))
				|| ((weight[position] == 0) && (carry == 1))) {
			return solve(weight, position - 1, 0) + solve(weight, position - 1, 1);
		}

		throw new RuntimeException();
	}
}
