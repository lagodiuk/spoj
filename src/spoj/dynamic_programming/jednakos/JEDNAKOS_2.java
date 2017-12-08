package spoj.dynamic_programming.jednakos;

import java.util.Arrays;

// http://www.spoj.com/problems/JEDNAKOS/en/

public class JEDNAKOS_2 {

	public static void main(String[] args) {
		JEDNAKOS_Brute_Force.testManualCases(JEDNAKOS_2::solve);
		JEDNAKOS_Brute_Force.testRandomizedCases(JEDNAKOS_2::solve);
		JEDNAKOS_Brute_Force.testRandomizedCases(JEDNAKOS_2::solve, false, 1000, 100);
	}

	static int solve(String digitsStr, int target) {
		int[] digits = toDigitsArray(digitsStr);

		int[][] memoized = new int[digitsStr.length()][target + 1];
		for (int[] row : memoized) {
			Arrays.fill(row, -1);
		}

		int minAmountOfAdditionTerms = getMinAdditionTermsAmount(digits, 0, target, memoized);
		int minAmountOfAdditionOperators = minAmountOfAdditionTerms - 1;
		return minAmountOfAdditionOperators;
	}

	private static int[] toDigitsArray(String digitsStr) {
		int[] digits = new int[digitsStr.length()];
		for (int i = 0; i < digitsStr.length(); i++) {
			digits[i] = digitsStr.charAt(i) - '0';
		}
		return digits;
	}

	static final int INFINITY = 10000;

	// TODO: can be solved more efficiently using BFS
	static int getMinAdditionTermsAmount(int[] digits, int curr, int target, int[][] memoized) {
		if (curr == digits.length) {
			return (target == 0) ? 0 : INFINITY;
		}

		if (target < 0) {
			return INFINITY;
		}

		if (memoized[curr][target] != -1) {
			return memoized[curr][target];
		}

		int result = INFINITY;
		int currNum = digits[curr];
		int len = 1;
		while ((currNum <= target)
				&& ((curr + len) <= digits.length)) {

			result = Math.min(result, getMinAdditionTermsAmount(digits, curr + len, target - currNum, memoized) + 1);

			if ((curr + len) == digits.length) {
				break;
			}

			currNum = (currNum * 10) + digits[curr + len];
			len++;
		}

		memoized[curr][target] = result;
		return result;
	}
}
