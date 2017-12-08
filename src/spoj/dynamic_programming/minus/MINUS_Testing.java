package spoj.dynamic_programming.minus;

// http://www.spoj.com/problems/MINUS/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class MINUS_Testing {

	static final int MAX_ITEMS = 101;
	static final int MAX_VALUE = 101;

	static void testFewManualCases(BiFunction<int[], Integer, List<Integer>> solution) {
		Object[][] tests = {
				{new int[]{12, 10, 4, 3, 5}, 4},
				{new int[]{1, 1, 1, 1, 1, 1, 1}, 3},
				{new int[]{1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0}, -1}
		};

		for (Object[] test : tests) {

			int[] arr = (int[]) test[0];
			int target = (int) test[1];

			System.out.println("Array length: " + arr.length);
			System.out.println("Target: " + target);
			System.out.println("Array:");
			System.out.println(Arrays.toString(arr).replaceAll("[\\[\\],]", " ").trim());

			List<Integer> subtractcionOrder = solution.apply(arr, target);
			verifySolution(arr, target, subtractcionOrder);

			System.out.println("Result: " + subtractcionOrder.toString().replaceAll("[\\[\\],]", " ").trim());
			System.out.println();
		}
	}

	static void testRandomized(BiFunction<int[], Integer, List<Integer>> solution) {
		Random rnd = new Random(0);

		for (int test = 0; test < 20; test++) {
			int[] arr = new int[rnd.nextInt(MAX_ITEMS) + 1];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = rnd.nextInt(MAX_VALUE);
			}

			for (int j = 0; j < 5; j++) {
				int target = randomDifference(arr, 0, arr.length - 1, rnd);

				System.out.println("Array length: " + arr.length);
				System.out.println("Target: " + target);
				System.out.println("Array:");
				System.out.println(Arrays.toString(arr).replaceAll("[\\[\\],]", " ").trim());

				List<Integer> subtractcionOrder = solution.apply(arr, target);
				verifySolution(arr, target, subtractcionOrder);

				System.out.println("Result: " + subtractcionOrder.toString().replaceAll("[\\[\\],]", " ").trim());
				System.out.println();
			}
		}
	}

	static int randomDifference(int[] arr, int L, int R, Random rnd) {
		if (L == R) {
			return arr[L];
		}

		int mid = rnd.nextInt(R - L) + L;
		return randomDifference(arr, L, mid, rnd) - randomDifference(arr, mid + 1, R, rnd);
	}

	static void verifySolution(int[] arr, int target, List<Integer> subtractcionOrder) {
		List<Integer> nums = new ArrayList<>();
		for (int item : arr) {
			nums.add(item);
		}

		for (int idx : subtractcionOrder) {
			// because result is 1-based
			idx = idx - 1;
			int tmp = nums.get(idx) - nums.get(idx + 1);
			nums.remove(idx + 1);
			nums.set(idx, tmp);
		}

		if (nums.size() > 1) {
			throw new RuntimeException("Size is greater than 1!");
		}

		if (nums.get(0) != target) {
			throw new RuntimeException("Expected result is not achieved!");
		}
	}
}
