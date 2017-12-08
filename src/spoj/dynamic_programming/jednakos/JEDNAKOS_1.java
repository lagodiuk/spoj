package spoj.dynamic_programming.jednakos;

// http://www.spoj.com/problems/JEDNAKOS/en/

public class JEDNAKOS_1 {

	public static void main(String[] args) {
		JEDNAKOS_Brute_Force.testManualCases(JEDNAKOS_1::solve);
		JEDNAKOS_Brute_Force.testRandomizedCases(JEDNAKOS_1::solve);
	}

	static int solve(String digitsStr, int target) {
		int[] digits = toDigitsArray(digitsStr);
		int minAmountOfAdditionTerms = getMinAdditionTermsAmount(digits, 0, target);
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

	// TODO: add memoization
	// TODO: can be solved more efficiently using BFS
	static int getMinAdditionTermsAmount(int[] digits, int curr, int target) {
		if (curr == digits.length) {
			return (target == 0) ? 0 : INFINITY;
		}

		if (target < 0) {
			return INFINITY;
		}

		int result = INFINITY;
		int currNum = digits[curr];
		int len = 1;
		while ((currNum <= target)
				&& ((curr + len) <= digits.length)) {

			result = Math.min(result, getMinAdditionTermsAmount(digits, curr + len, target - currNum) + 1);

			if ((curr + len) == digits.length) {
				break;
			}

			currNum = (currNum * 10) + digits[curr + len];
			len++;
		}
		return result;
	}
}
