package spoj.dynamic_programming.bat3;

// http://www.spoj.com/problems/BAT3/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class BAT3_Brute_Force {

	public static void main(String[] args) {
		System.out.println(solve(4, new int[]{6, 3, 5, 2, 4, 5}));
		System.out.println(solve(2, new int[]{6, 4, 8, 7, 6, 5, 4, 3, 2, 1}));
		System.out.println();

		test(null, 10, true);
	}

	static void test(BiFunction<Integer, int[], Integer> tested, int maxLen, boolean compareWithBruteForce) {
		Random rnd = new Random(0);

		for (int test = 0; test < 100; test++) {
			int[] height = new int[rnd.nextInt(maxLen) + 1];
			for (int i = 0; i < height.length; i++) {
				height[i] = rnd.nextInt(1000);
			}

			int miniBATPosition = rnd.nextInt(height.length);

			compareWithBruteForce(miniBATPosition, height, tested, compareWithBruteForce);
		}
	}

	static void compareWithBruteForce(int miniBATPosition, int[] height, BiFunction<Integer, int[], Integer> tested, boolean compareWithBruteForce) {
		System.out.println(height.length + " " + miniBATPosition);
		System.out.println(Arrays.toString(height).replaceAll("[\\[\\],]", ""));
		if (tested == null) {
			System.out.println("Result: " + solve(miniBATPosition, height));
		} else {
			if (!compareWithBruteForce) {
				System.out.println("Result: " + tested.apply(miniBATPosition, height));
			} else {
				int testedResult = tested.apply(miniBATPosition, height);
				int bruteForceResult = solve(miniBATPosition, height);

				if (testedResult != bruteForceResult) {
					System.out.println("Tested result: " + testedResult);
					System.out.println("Brute force result: " + bruteForceResult);
					throw new RuntimeException("Different results!");
				}

				System.out.println("Result: " + testedResult);
			}
		}
		System.out.println();
	}

	static int solve(int miniBATPosition, int[] height) {
		return solve(miniBATPosition, height, 0, new ArrayList<>());
	}

	static int solve(int miniBATPosition, int[] height, int pos, List<Integer> indices) {
		if (pos == height.length) {
			if (isAllowed(miniBATPosition, height, indices)) {
				return indices.size();
			} else {
				return -100000;
			}
		}

		indices.add(pos);
		int result = solve(miniBATPosition, height, pos + 1, indices);
		indices.remove(indices.size() - 1);

		result = Math.max(result, solve(miniBATPosition, height, pos + 1, indices));

		return result;
	}

	static boolean isAllowed(int miniBATPosition, int[] height, List<Integer> indices) {
		for (int i = 1; i < indices.size(); i++) {
			if ((indices.get(i - 1) != miniBATPosition)
					&& (height[indices.get(i)] >= height[indices.get(i - 1)])) {
				return false;
			}
		}
		return true;
	}

}
