package spoj.dynamic_programming.hist2;

import java.util.Arrays;
import java.util.BitSet;
import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;

// http://www.spoj.com/problems/HIST2/

public class HIST2_2 {

	public static void main(String[] args) {
		calculate(new int[]{1, 2, 3, 4});
		calculate(new int[]{3, 4, 1, 2});
		calculate(new int[]{2, 6, 5});
		calculate(new int[]{1, 5, 2});
		calculate(new int[]{1, 3});
		calculate(new int[]{3, 1});
		calculate(new int[]{1, 2, 5, 7});
		calculate(new int[]{1, 2, 5, 7, 9});
		calculate(new int[]{23, 75, 85, 12, 1});
		calculate(new int[]{23, 75, 85, 12, 1, 7});
		calculate(new int[]{1, 2, 3, 4, 5, 6});

		System.out.println("--------------------");
		for (int i = 2; i <= MAX_ARR_SIZE; i++) {
			int[] arr = new int[i];
			for (int j = 0; j < i; j++) {
				arr[j] = j + 1;
			}

			// Interesting: see https://oeis.org/A092186
			calculate(arr);
		}
	}

	static void solve_very_fast(int[] arr) {
		Arrays.sort(arr);

		int perimeter = 0;

		if ((arr.length % 2) == 1) {
			for (int i = 0; i < arr.length; i++) {
				if (i < (arr.length / 2)) {
					perimeter -= 2 * arr[i];
				} else {
					perimeter += 2 * arr[i];
				}
			}
		} else {
			for (int i = 0; i < arr.length; i++) {
				if (i < ((arr.length / 2) - 1)) {
					perimeter -= 2 * arr[i];
				} else {
					if (i != ((arr.length / 2) - 1)) {
						perimeter += 2 * arr[i];
					}
				}
			}
		}

		perimeter += 2 * arr.length;

		long[] permutationsCount = new long[]{
				-1, -1, 2, 2, 8, 12, 72, 144, 1152, 2880, 28800, 86400, 1036800, 3628800, 50803200, 203212800};

		System.out.println(perimeter + "\t" + permutationsCount[arr.length]);
	}

	static void solve_brute_force(int[] arr) {
		TreeMap<Integer, Long> perimeterCount = new TreeMap<>();
		solve_brute_force(arr, 0, new BitSet(arr.length), new int[arr.length], perimeterCount);
		// System.out.println(perimeterCount);
		System.out.println(perimeterCount.lastKey() + "\t" + perimeterCount.lastEntry().getValue());
	}

	static void solve_brute_force(int[] arr, int idx, BitSet used, int[] permutation, Map<Integer, Long> perimeterCount) {
		if (idx == arr.length) {
			int perimeter = permutation[0];
			for (int i = 1; i < permutation.length; i++) {
				perimeter += Math.abs(permutation[i] - permutation[i - 1]);
			}
			perimeter += permutation[arr.length - 1];
			perimeter += 2 * arr.length;
			perimeterCount.put(perimeter, 1 + perimeterCount.getOrDefault(perimeter, 0L));
			return;
		}

		for (int i = 0; i < arr.length; i++) {
			if (!used.get(i)) {
				used.set(i);
				permutation[idx] = arr[i];
				solve_brute_force(arr, idx + 1, used, permutation, perimeterCount);
				used.clear(i);
			}
		}
	}

	static void calculate(int[] arr) {
		cleanMemoized();

		int maxPerimeter = calculateMaxPerimeter(arr, 0, 0, 0).perimeter;

		int arrLength = arr.length;

		UsagePattern[] usage = new UsagePattern[arrLength];
		collectUsagePatterns(arr, 0, 0, 0, usage);

		UsagePattern[] validPermutation = generateValidPatternPermutation(arrLength, usage);
		long variants = countAllValidPermutations(validPermutation);

		System.out.println(Arrays.toString(arr));
		System.out.println(Arrays.toString(usage));
		System.out.println(Arrays.toString(validPermutation));
		System.out.println(maxPerimeter + "\t" + variants);
		// solve_brute_force(arr);
		solve_very_fast(arr);
		System.out.println();
	}

	static long countAllValidPermutations(UsagePattern[] validPermutation) {
		for (int i = 0; i < memoizedPermutationsCount.length; i++) {
			for (int j = 0; j < memoizedPermutationsCount[i].length; j++) {
				Arrays.fill(memoizedPermutationsCount[i][j], -1);
			}
		}

		long validPermutations = countAllValidPermutations(validPermutation, validPermutation[0], validPermutation[1], 2);

		Map<UsagePattern, Integer> usagePatternCount = countUsagePatterns(validPermutation);
		for (UsagePattern up : usagePatternCount.keySet()) {
			validPermutations *= factorial(usagePatternCount.get(up));
		}

		return validPermutations;
	}

	static long factorial(int x) {
		long f = 1;
		for (int i = 1; i <= x; i++) {
			f *= i;
		}
		return f;
	}

	static final int MAX_VALUE = 100;
	static final int MAX_ARR_SIZE = 15;
	static PerimeterSoFar[][][] memoizedPerimeter = new PerimeterSoFar[MAX_ARR_SIZE][MAX_ARR_SIZE * 4][MAX_ARR_SIZE * 4 * MAX_VALUE];
	static long[][][] memoizedPermutationsCount = new long[UsagePattern.values().length][UsagePattern.values().length][MAX_ARR_SIZE];

	static long countAllValidPermutations(UsagePattern[] validPermutation, UsagePattern first, UsagePattern second, int tailIdx) {
		if (tailIdx == validPermutation.length) {
			if ((first == UsagePattern._11) && (second == UsagePattern._01)) {
				return 2;
			}
			if ((first == UsagePattern._10) && (second == UsagePattern._11)) {
				return 2;
			}
			if ((first == UsagePattern._00) && (second == UsagePattern._10)) {
				return 2;
			}
			if ((first == UsagePattern._01) && (second == UsagePattern._00)) {
				return 2;
			}
			return 1;
		}

		if (memoizedPermutationsCount[first.index][second.index][tailIdx] != -1) {
			return memoizedPermutationsCount[first.index][second.index][tailIdx];
		}

		if ((first == UsagePattern._11) && (second == UsagePattern._01)) {
			memoizedPermutationsCount[first.index][second.index][tailIdx] =
					countAllValidPermutations(validPermutation, UsagePattern._01, validPermutation[tailIdx], tailIdx + 1) +
							countAllValidPermutations(validPermutation, UsagePattern._11, validPermutation[tailIdx], tailIdx + 1);

		} else if ((first == UsagePattern._10) && (second == UsagePattern._11)) {
			memoizedPermutationsCount[first.index][second.index][tailIdx] =
					countAllValidPermutations(validPermutation, UsagePattern._01, validPermutation[tailIdx], tailIdx + 1) +
							countAllValidPermutations(validPermutation, UsagePattern._11, validPermutation[tailIdx], tailIdx + 1);

		} else if ((first == UsagePattern._00) && (second == UsagePattern._10)) {

			memoizedPermutationsCount[first.index][second.index][tailIdx] =
					countAllValidPermutations(validPermutation, UsagePattern._10, validPermutation[tailIdx], tailIdx + 1) +
							countAllValidPermutations(validPermutation, UsagePattern._00, validPermutation[tailIdx], tailIdx + 1);

		} else if ((first == UsagePattern._01) && (second == UsagePattern._00)) {
			memoizedPermutationsCount[first.index][second.index][tailIdx] =
					countAllValidPermutations(validPermutation, UsagePattern._10, validPermutation[tailIdx], tailIdx + 1) +
							countAllValidPermutations(validPermutation, UsagePattern._00, validPermutation[tailIdx], tailIdx + 1);
		} else {
			memoizedPermutationsCount[first.index][second.index][tailIdx] = countAllValidPermutations(validPermutation, second, validPermutation[tailIdx],
					tailIdx + 1);
		}

		return memoizedPermutationsCount[first.index][second.index][tailIdx];
	}

	static UsagePattern[] generateValidPatternPermutation(int arrLength, UsagePattern[] usage) {
		Map<UsagePattern, Integer> usagePatternCount = countUsagePatterns(usage);

		UsagePattern[] validPermutation = new UsagePattern[arrLength];

		validPermutation[0] = UsagePattern._11;
		usagePatternCount.put(UsagePattern._11, usagePatternCount.get(UsagePattern._11) - 1);

		for (int i = 1; i < validPermutation.length; i++) {

			if (validPermutation[i - 1] == UsagePattern._00) {
				validPermutation[i] = UsagePattern._11;
				usagePatternCount.put(UsagePattern._11, usagePatternCount.get(UsagePattern._11) - 1);
				continue;
			}

			if ((validPermutation[i - 1] == UsagePattern._01)
					|| (validPermutation[i - 1] == UsagePattern._11)) {

				if (usagePatternCount.getOrDefault(UsagePattern._00, 0) > 0) {
					validPermutation[i] = UsagePattern._00;
					usagePatternCount.put(UsagePattern._00, usagePatternCount.get(UsagePattern._00) - 1);
					continue;
				}

				if (usagePatternCount.getOrDefault(UsagePattern._01, 0) > 0) {
					validPermutation[i] = UsagePattern._01;
					usagePatternCount.put(UsagePattern._01, usagePatternCount.get(UsagePattern._01) - 1);
					continue;
				}

				throw new RuntimeException();
			}
		}

		// Reverse valid permutation - so _11 will be in the end
		for (int i = 0; i < (validPermutation.length / 2); i++) {
			UsagePattern tmp = validPermutation[i];
			validPermutation[i] = validPermutation[validPermutation.length - 1 - i];
			validPermutation[validPermutation.length - 1 - i] = tmp;
		}

		// Reverse every _01 to _10
		for (int i = 0; i < validPermutation.length; i++) {
			if (validPermutation[i] == UsagePattern._01) {
				validPermutation[i] = UsagePattern._10;
			}
		}

		return validPermutation;
	}

	static Map<UsagePattern, Integer> countUsagePatterns(UsagePattern[] usage) {
		Map<UsagePattern, Integer> usagePatternCount = new EnumMap<>(UsagePattern.class);
		for (UsagePattern up : usage) {
			usagePatternCount.put(up, 1 + usagePatternCount.getOrDefault(up, 0));
		}
		return usagePatternCount;
	}

	static void cleanMemoized() {
		for (int i = 0; i < memoizedPerimeter.length; i++) {
			for (int j = 0; j < memoizedPerimeter[i].length; j++) {
				Arrays.fill(memoizedPerimeter[i][j], null);
			}
		}
	}

	static PerimeterSoFar calculateMaxPerimeter(int[] arr, int idx, int plusMinus, int perimeterSoFar) {
		if (idx == arr.length) {
			return new PerimeterSoFar((plusMinus == 2) ? perimeterSoFar + (2 * arr.length) : -1, null);
		}

		int pmIdx = plusMinus + (2 * MAX_ARR_SIZE);
		int perIdx = perimeterSoFar + (2 * MAX_VALUE * MAX_ARR_SIZE);

		if (memoizedPerimeter[idx][pmIdx][perIdx] == null) {

			PerimeterSoFar c_01 = calculateMaxPerimeter(arr, idx + 1, plusMinus, perimeterSoFar);
			PerimeterSoFar c_11 = calculateMaxPerimeter(arr, idx + 1, plusMinus + 2, perimeterSoFar + (2 * arr[idx]));
			PerimeterSoFar c_00 = calculateMaxPerimeter(arr, idx + 1, plusMinus - 2, perimeterSoFar - (2 * arr[idx]));

			int maxPerimeter = Math.max(c_01.perimeter, Math.max(c_11.perimeter, c_00.perimeter));

			if (maxPerimeter == c_01.perimeter) {
				memoizedPerimeter[idx][pmIdx][perIdx] = new PerimeterSoFar(maxPerimeter, UsagePattern._01);
			}

			if (maxPerimeter == c_00.perimeter) {
				memoizedPerimeter[idx][pmIdx][perIdx] = new PerimeterSoFar(maxPerimeter, UsagePattern._00);
			}

			if (maxPerimeter == c_11.perimeter) {
				memoizedPerimeter[idx][pmIdx][perIdx] = new PerimeterSoFar(maxPerimeter, UsagePattern._11);
			}
		}

		return memoizedPerimeter[idx][pmIdx][perIdx];
	}

	static void collectUsagePatterns(int[] arr, int idx, int plusMinus, int perimeterSoFar, UsagePattern[] usage) {
		if (idx == arr.length) {
			return;
		}

		int pmIdx = plusMinus + (MAX_ARR_SIZE * 2);
		int perIdx = perimeterSoFar + (2 * MAX_VALUE * MAX_ARR_SIZE);

		usage[idx] = memoizedPerimeter[idx][pmIdx][perIdx].usage;

		switch (memoizedPerimeter[idx][pmIdx][perIdx].usage) {
			case _00 :
				collectUsagePatterns(arr, idx + 1, plusMinus - 2, perimeterSoFar - (2 * arr[idx]), usage);
				break;

			case _01 :
				collectUsagePatterns(arr, idx + 1, plusMinus, perimeterSoFar, usage);
				break;

			case _11 :
				collectUsagePatterns(arr, idx + 1, plusMinus + 2, perimeterSoFar + (2 * arr[idx]), usage);
				break;
			default :
				throw new RuntimeException();
		}
	}

	static class PerimeterSoFar {
		int perimeter;
		UsagePattern usage;

		public PerimeterSoFar(int perimeter, UsagePattern usage) {
			this.perimeter = perimeter;
			this.usage = usage;
		}
	}

	enum UsagePattern {
		_00(0),
		_01(1),
		_10(2),
		_11(3);

		int index;

		UsagePattern(int index) {
			this.index = index;
		}
	}
}
