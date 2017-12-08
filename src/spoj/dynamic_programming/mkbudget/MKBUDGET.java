package spoj.dynamic_programming.mkbudget;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MKBUDGET {

	public static void main(String[] args) {
		System.out.println(MKBUDGET_Brute_Force.solve(new int[]{10, 9, 11}, 400, 500, 600));
		System.out.println(MKBUDGET_Brute_Force.solve(new int[]{11, 9, 10, 14, 9, 9, 13, 15}, 400, 600, 600));
		System.out.println();
		System.out.println(solve(new int[]{10, 9, 11}, 400, 500, 600));
		System.out.println(solve(new int[]{11, 9, 10, 14, 9, 9, 13, 15}, 400, 600, 600));
		System.out.println();
		System.out.println(solve2(new int[]{10, 9, 11}, 400, 500, 600));
		System.out.println(solve2(new int[]{11, 9, 10, 14, 9, 9, 13, 15}, 400, 600, 600));
		System.out.println();
		System.out.println(solve3(new int[]{10, 9, 11}, 400, 500, 600));
		System.out.println(solve3(new int[]{11, 9, 10, 14, 9, 9, 13, 15}, 400, 600, 600));
		System.out.println();
		System.out.println(MKBUDGET_Brute_Force.solve(new int[]{13, 14, 11}, 615, 58, 522));
		System.out.println(solve(new int[]{13, 14, 11}, 615, 58, 522));
		System.out.println(solve2(new int[]{13, 14, 11}, 615, 58, 522));
		System.out.println(solve3(new int[]{13, 14, 11}, 615, 58, 522));
		System.out.println();

		Random rnd = new Random(1);
		for (int i = 0; i < 40; i++) {
			int[] count = MKBUDGET_Brute_Force.generateRandomCountArray(rnd);
			int hire = rnd.nextInt(1000);
			int salary = rnd.nextInt(1000);
			int severance = rnd.nextInt(1000);

			int bf = MKBUDGET_Brute_Force.solve(count, hire, salary, severance);
			int s1 = solve(count, hire, salary, severance);
			int s2 = solve2(count, hire, salary, severance);
			int s3 = solve3(count, hire, salary, severance);

			System.out.println(Arrays.toString(count) + "\t" + hire + "\t" + salary + "\t" + severance);
			System.out.println(count.length + " " + hire + " " + salary + " " + severance + " " + Arrays.toString(count).replaceAll("[\\[\\],]", " "));
			System.out.println(bf);
			System.out.println(s1);
			System.out.println(s2);
			System.out.println(s3);
			System.out.println();

			Set<Integer> allResults = new HashSet<>();
			allResults.add(bf);
			allResults.add(s1);
			allResults.add(s2);
			allResults.add(s3);
			if (allResults.size() != 1) {
				throw new RuntimeException();
			}
		}
	}
	static int solve(int[] count, int hire, int salary, int severance) {
		return solve(count, hire, salary, severance, 0, 0);
	}

	static int solve(int[] count, int hire, int salary, int severance, int i, int currCnt) {
		if (i == count.length) {
			return 0;
		}

		if (currCnt == count[i]) {
			return solve(count, hire, salary, severance, i + 1, currCnt)
					+ (currCnt * salary);
		}

		if (currCnt < count[i]) {
			return solve(count, hire, salary, severance, i + 1, count[i])
					+ (count[i] * salary)
					+ ((count[i] - currCnt) * hire);
		}

		int min = Integer.MAX_VALUE;
		int sev = 0;
		while (currCnt >= count[i]) {
			min = Math.min(min,
					solve(count, hire, salary, severance, i + 1, currCnt)
							+ (currCnt * salary)
							+ sev);
			sev += severance;
			currCnt--;
		}
		return min;
	}

	static int solve2(int[] count, int hire, int salary, int severance) {
		int[] sortedCount = new int[count.length];
		System.arraycopy(count, 0, sortedCount, 0, count.length);
		Arrays.sort(sortedCount);
		return solve2(count, sortedCount, hire, salary, severance, 0, 0);
	}

	static int solve2(int[] count, int[] sortedCount, int hire, int salary, int severance, int i, int currCnt) {
		if (i == count.length) {
			return 0;
		}

		if (currCnt == count[i]) {
			return solve2(count, sortedCount, hire, salary, severance, i + 1, currCnt)
					+ (currCnt * salary);
		}

		if (currCnt < count[i]) {
			return solve2(count, sortedCount, hire, salary, severance, i + 1, count[i])
					+ (count[i] * salary)
					+ ((count[i] - currCnt) * hire);
		}

		int min = Integer.MAX_VALUE;
		int currIdx = Arrays.binarySearch(sortedCount, currCnt);
		while ((currIdx >= 0) && (sortedCount[currIdx] >= count[i])) {
			min = Math.min(min,
					solve2(count, sortedCount, hire, salary, severance, i + 1, sortedCount[currIdx])
							+ (sortedCount[currIdx] * salary)
							+ ((currCnt - sortedCount[currIdx]) * severance));
			currIdx--;
		}
		return min;
	}

	static int solve3(int[] count, int hire, int salary, int severance) {
		int INFINITY = 1000000;

		int[][] res = new int[count.length][count.length];

		for (int j = 0; j < count.length; j++) {
			if (count[j] < count[0]) {
				res[j][0] = INFINITY;
				continue;
			}
			res[j][0] = (hire * count[j]) + (salary * count[j]);
		}

		for (int i = 1; i < count.length; i++) {
			for (int j = 0; j < count.length; j++) {
				res[j][i] = INFINITY;
				if (count[j] < count[i]) {
					continue;
				}
				for (int k = 0; k < count.length; k++) {
					int curr = res[k][i - 1] + (count[j] * salary);

					if (count[k] < count[j]) {
						curr += (count[j] - count[k]) * hire;

					} else if (count[k] > count[j]) {
						curr += (count[k] - count[j]) * severance;
					}
					res[j][i] = Math.min(res[j][i], curr);
				}
			}
		}

		int result = Integer.MAX_VALUE;
		for (int j = 0; j < count.length; j++) {
			result = Math.min(result, res[j][count.length - 1]);
		}

		return result;
	}

}
