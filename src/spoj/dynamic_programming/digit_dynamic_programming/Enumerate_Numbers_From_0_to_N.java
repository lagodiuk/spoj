package spoj.dynamic_programming.digit_dynamic_programming;

import java.util.Arrays;

public class Enumerate_Numbers_From_0_to_N {

	public static void main(String[] args) {
		// (N + 1)
		print(new int[]{1, 2, 2});
	}

	static void print(int[] target) {
		print(0, target.length, target.length, new int[target.length], target);
	}

	// http://stackoverflow.com/questions/22394257/how-to-count-integers-between-large-a-and-b-with-a-certain-property/22394258#22394258
	static void print(int position, int leftmostSmaller, int leftmostGreater, int[] buffer, int[] target) {
		if (position == target.length) {
			if (leftmostSmaller < leftmostGreater) {
				System.out.println(Arrays.toString(buffer));
			}
			return;
		}

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

			print(position + 1, newLeftmostSmaller, newLeftmostGreater, buffer, target);
		}
	}
}
