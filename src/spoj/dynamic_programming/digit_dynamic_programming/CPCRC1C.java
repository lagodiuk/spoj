package spoj.dynamic_programming.digit_dynamic_programming;

// http://www.spoj.com/problems/CPCRC1C/

// Idea of solution is taken from:
// http://stackoverflow.com/questions/22394257/how-to-count-integers-between-large-a-and-b-with-a-certain-property/22394258#22394258

public class CPCRC1C {

	public static void main(String[] args) {
		System.out.println(sumDigits(1, 10));
		System.out.println(sumDigits(100, 777));
		System.out.println();
		System.out.println(sumDigits(1, 1000000000));
		System.out.println(sumDigits(987, 12345));
	}

	static long sumDigits(int from, int to) {
		return sumDigitsStartingFromZero(to) - sumDigitsStartingFromZero(from - 1);
	}

	static long sumDigitsStartingFromZero(long targetNum) {
		targetNum++;
		int[] target = toArrayOfDigits(targetNum);
		Long[][][][] memoized =
				new Long[target.length + 1][target.length + 1][target.length + 1][(target.length + 1) * 10];

		return sumDigits(0, target.length, target.length, 0, new int[target.length], target, memoized);
	}

	static long sumDigits(int position, int leftmostSmaller, int leftmostGreater, int currSum, int[] buffer, int[] target, Long[][][][] memoized) {
		if (position == target.length) {
			if (leftmostSmaller < leftmostGreater) {
				return currSum;
			} else {
				return 0;
			}
		}

		// Optimization (?)
		// if ((leftmostSmaller > leftmostGreater) && (position >= leftmostSmaller)) {
		// return 0;
		// }

		if (memoized[position][leftmostGreater][leftmostSmaller][currSum] != null) {
			return memoized[position][leftmostGreater][leftmostSmaller][currSum];
		}

		long result = 0;
		for (int digit = 0; digit <= 9; digit++) {
			int newLeftmostSmaller = leftmostSmaller;
			int newLeftmostGreater = leftmostGreater;

			buffer[position] = digit;

			if ((buffer[position] < target[position]) && (position < leftmostSmaller)) {
				newLeftmostSmaller = position;
			}

			if ((buffer[position] > target[position]) && (position < leftmostGreater)) {
				newLeftmostGreater = position;
			}

			result += sumDigits(position + 1, newLeftmostSmaller, newLeftmostGreater, currSum + digit, buffer, target, memoized);
		}

		memoized[position][leftmostGreater][leftmostSmaller][currSum] = result;

		return result;
	}

	private static int[] toArrayOfDigits(long targetNum) {
		char[] digits = Long.toString(targetNum).toCharArray();
		int target[] = new int[digits.length];
		for (int i = 0; i < target.length; i++) {
			target[i] = digits[i] - '0';
		}
		return target;
	}
}
