package spoj.dynamic_programming.scales;

// http://www.spoj.com/problems/SCALES/

public class SCALES_Bottom_Up {

	public static void main(String[] args) {
		System.out.println(solve(new int[]{0, 0, 0, 0, 0, 1}));
		SCALES_Brute_Force.test_simple(SCALES_DP_Top_Down::solve);
		SCALES_Brute_Force.test(SCALES_DP_Top_Down::solve);
	}

	static int solve(int[] weight) {
		int prev_carry_0 = 1;
		int prev_carry_1 = 0;
		int pos = 0;

		int curr_carry_0;
		int curr_carry_1;

		while (pos < weight.length) {
			if (weight[pos] == 1) {
				curr_carry_1 = prev_carry_1;
				curr_carry_0 = prev_carry_0 + prev_carry_1;
			} else {
				curr_carry_0 = prev_carry_0;
				curr_carry_1 = prev_carry_0 + prev_carry_1;
			}
			prev_carry_0 = curr_carry_0;
			prev_carry_1 = curr_carry_1;
			pos++;
		}

		return prev_carry_0;
	}
}
