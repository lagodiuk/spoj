package spoj.dynamic_programming.lsort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

// http://www.spoj.com/problems/LSORT/

public class LSORT_Brute_Force {

	public static void main(String[] args) {
		verifyManualTestCases(null);
		verifyRandomizedTestCases(null);
	}

	static void verifyRandomizedTestCases(Function<int[], Integer> solver) {
		verifyRandomizedTestCases(solver, true, 9);
	}

	static void verifyRandomizedTestCases(Function<int[], Integer> solver, boolean compareWithBruteForce, int maxArrayLength) {
		Random rnd = new Random(0);
		int testsNum = 50;

		for (int test = 0; test < testsNum; test++) {
			List<Integer> list = new ArrayList<>();
			int len = rnd.nextInt(maxArrayLength) + 1;
			for (int i = 1; i <= len; i++) {
				list.add(i);
			}
			Collections.shuffle(list, rnd);

			int[] arr = new int[len];
			for (int i = 0; i < len; i++) {
				arr[i] = list.get(i);
			}

			verifySolution(arr, solver, compareWithBruteForce);
		}
	}

	static void verifyManualTestCases(Function<int[], Integer> solver) {
		verifySolution(new int[]{4, 1, 3, 2}, solver, true);
		verifySolution(new int[]{1}, solver, true);
		verifySolution(new int[]{1, 2}, solver, true);
		verifySolution(new int[]{1, 2, 3}, solver, true);
		verifySolution(new int[]{1, 3, 2}, solver, true);
		verifySolution(new int[]{1, 3, 2, 4}, solver, true);
		verifySolution(new int[]{4, 2, 3, 1}, solver, true);
		verifySolution(new int[]{1, 2, 3, 4}, solver, true);
		verifySolution(new int[]{2, 1}, solver, true);
		verifySolution(new int[]{2, 1, 3}, solver, true);
		verifySolution(new int[]{2, 1, 3, 4}, solver, true);
	}

	static void verifySolution(int[] arr, Function<int[], Integer> solver, boolean compareWithBruteForce) {
		System.out.println(arr.length);
		for (int x : arr) {
			System.out.print(x + " ");
		}
		System.out.println();

		if (solver == null) {
			int result = solve(arr);
			System.out.println("Result: " + result);
		} else {
			if (compareWithBruteForce) {
				int bruteForceResult = solve(arr.clone());
				int testedResult = solver.apply(arr.clone());
				if (testedResult != bruteForceResult) {
					System.out.println("Expected result: " + bruteForceResult);
					System.out.println("Actual result: " + testedResult);
					throw new RuntimeException("Different results!");
				} else {
					System.out.println("Result: " + testedResult);
				}
			} else {
				int result = solver.apply(arr);
				System.out.println("Result: " + result);
			}
		}

		System.out.println();
	}

	static int solve(int[] arr) {
		int result = INF;
		for (int i = 0; i < arr.length; i++) {
			ArrayList<Integer> source = new ArrayList<>();
			for (int j = 0; j < arr.length; j++) {
				if (j != i) {
					source.add(arr[j]);
				}
			}

			ArrayList<Integer> target = new ArrayList<>();
			target.add(arr[i]);

			int r = solve(source, target) + (i + 1);
			result = Math.min(result, r);
		}
		return result;
	}

	static final int INF = 10000000;

	static int solve(List<Integer> source, List<Integer> target) {
		if (source.isEmpty()) {
			return 0;
		}

		int left = target.get(0);
		int right = target.get(target.size() - 1);

		int leftResult = INF;
		for (int i = 0; i < source.size(); i++) {
			if (source.get(i) == (left - 1)) {
				source.remove(i);
				target.add(0, left - 1);
				leftResult = solve(source, target) + ((i + 1) * target.size());
				target.remove(0);
				source.add(i, left - 1);
				break;
			}
		}

		int rightResult = INF;
		for (int i = 0; i < source.size(); i++) {
			if (source.get(i) == (right + 1)) {
				source.remove(i);
				target.add(right + 1);
				rightResult = solve(source, target) + ((i + 1) * target.size());
				target.remove(target.size() - 1);
				source.add(i, right + 1);
				break;
			}
		}

		return Math.min(leftResult, rightResult);
	}
}
