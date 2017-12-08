package spoj.dynamic_programming.acquire;

// http://www.spoj.com/problems/ACQUIRE/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class ACQUIRE {

	public static void main(String... args) {
		testSmallInstancesOfTheProblem();
		testLargeInstancesOfTheProblem();
	}

	private static void testLargeInstancesOfTheProblem() {
		Random rnd = new Random(1);
		for (int i = 0; i < 50; i++) {
			Field[] p = new Field[rnd.nextInt(MAX_FIELDS_COUNT) + 1];
			for (int j = 0; j < p.length; j++) {
				p[j] = new Field(rnd.nextInt(MAX_DIMENSION) + 1, rnd.nextInt(MAX_DIMENSION) + 1);
			}
			// System.out.println(Arrays.toString(p));
			for (int k = 0; k < p.length; k++) {
				ACQUIRE_Submitted.inputItemsHeight[k] = p[k].height;
				ACQUIRE_Submitted.inputItemsWidth[k] = p[k].width;
			}
			System.out.println(ACQUIRE_Submitted.solve(p.length));
			System.out.println();
		}
	}

	private static void testSmallInstancesOfTheProblem() {
		test(new Field[]{
				new Field(100, 1),
				new Field(15, 15),
				new Field(20, 5),
				new Field(1, 100)
		});

		test(new Field[]{
				new Field(100, 1),
				new Field(90, 2),
				new Field(50, 50),
				new Field(40, 60),
				new Field(10, 100),
				new Field(5, 200)
		});

		Random rnd = new Random(1);
		for (int i = 0; i < 50; i++) {
			Field[] p = new Field[rnd.nextInt(11) + 1];
			for (int j = 0; j < p.length; j++) {
				p[j] = new Field(rnd.nextInt(100) + 1, rnd.nextInt(100) + 1);
			}
			System.out.println(Arrays.toString(p));
			test(p);
		}
	}

	public static final Comparator<Field> COMPARATOR = new Comparator<Field>() {
		@Override
		public int compare(Field o1, Field o2) {
			int comparisonOfX = Integer.compare(o1.width, o2.width);
			return (comparisonOfX != 0) ? comparisonOfX : Integer.compare(o2.height, o1.height);
		}
	};

	public static void test(Field[] p) {
		long result0 = f0(Arrays.asList(p.clone()));
		long result1 = f1(p.clone());
		long result2 = f2(p.clone());
		long result3 = f3(p.clone());
		long result4 = f4(p.clone());
		long result5 = f5(p.clone());

		System.out.println(result0);
		System.out.println(result1);
		System.out.println(result2);
		System.out.println(result3);
		System.out.println(result4);
		System.out.println(result5);

		System.out.println();

		if ((result0 != result1) || (result0 != result2) || (result0 != result3) || (result0 != result4) || (result0 != result5)) {
			throw new RuntimeException();
		}
	}

	public static long f5(Field[] p) {
		for (int k = 0; k < p.length; k++) {
			ACQUIRE_Submitted.inputItemsHeight[k] = p[k].height;
			ACQUIRE_Submitted.inputItemsWidth[k] = p[k].width;
		}
		return ACQUIRE_Submitted.solve(p.length);
	}

	/**
	 * Based on idea from: http://wcipeg.com/wiki/Convex_hull_trick
	 * Using "Envelope" data structure
	 * Complexity O(N*log(N))
	 */
	public static long f4(Field[] p) {
		// Complexity of the counting sort is O(MAX_DIMENSION + N)
		counting_sort_By_WidthIncreasing_HeightIncreasing(p);

		// Now, we have to remove all fields, which fully covered by other fields
		// In other words: we have to keep only that fields,
		// which are not fully covered by other fields ("external frontier")
		// Complexity: O(N)
		// Approach: iterate from the widest field to the field with smallest width
		// (iterate from the last to first), and track the "largest height so far"

		int externalItemsCount = 0;
		int height = 0;
		for (int i = p.length - 1; i >= 0; i--) {
			if (p[i].height > height) {
				height = p[i].height;
				externalItems[externalItemsCount] = p[i];
				externalItemsCount++;
			}
		}

		// Reverse externalItems
		for (int i = 0; i < (externalItemsCount / 2); i++) {
			Field tmpField = externalItems[i];
			externalItems[i] = externalItems[externalItemsCount - 1 - i];
			externalItems[externalItemsCount - 1 - i] = tmpField;
		}

		// O(N^2)
		// long[] result = new long[externalItemsCount];
		// result[0] = externalItems[0].width * externalItems[0].height;
		// for (int i = 1; i < externalItemsCount; i++) {
		// result[i] = INF;
		// for (int j = i - 1; j >= -1; j--) {
		// result[i] = Math.min(result[i], ((j == -1) ? 0 : result[j]) + (externalItems[i].width *
		// externalItems[j + 1].height));
		// }
		// }
		// return result[externalItemsCount - 1];

		Envelope env = new Envelope();
		env.add(new Envelope.Line(0, externalItems[0].height));
		long cost = INF;
		for (int i = 0; i < externalItemsCount; i++) {
			cost = env.query(externalItems[i].width);
			if (i != (externalItemsCount - 1)) {
				env.add(new Envelope.Line(cost, externalItems[i + 1].height));
			}
		}
		return cost;
	}

	/**
	 * Bottom-up Dynamic Programming solution
	 * O(N^2)
	 */
	public static long f3(Field[] p) {
		// Complexity of the counting sort is O(MAX_DIMENSION + N)
		counting_sort_By_WidthIncreasing_HeightIncreasing(p);

		// Now, we have to remove all fields, which fully covered by other fields
		// In other words: we have to keep only that fields,
		// which are not fully covered by other fields ("external frontier")
		// Complexity: O(N)
		// Approach: iterate from the widest field to the field with smallest width
		// (iterate from the last to first), and track the "largest height so far"

		int externalItemsCount = 0;
		int height = 0;
		for (int i = p.length - 1; i >= 0; i--) {
			if (p[i].height > height) {
				height = p[i].height;
				externalItems[externalItemsCount] = p[i];
				externalItemsCount++;
			}
		}

		// Reverse externalItems
		for (int i = 0; i < (externalItemsCount / 2); i++) {
			Field tmpField = externalItems[i];
			externalItems[i] = externalItems[externalItemsCount - 1 - i];
			externalItems[externalItemsCount - 1 - i] = tmpField;
		}

		// O(N^2)
		long[] result = new long[externalItemsCount];
		result[0] = externalItems[0].width * externalItems[0].height;
		for (int i = 1; i < externalItemsCount; i++) {
			result[i] = INF;
			for (int j = i - 1; j >= -1; j--) {
				result[i] = Math.min(result[i], ((j == -1) ? 0 : result[j]) + (externalItems[i].width * externalItems[j + 1].height));
			}
		}
		return result[externalItemsCount - 1];
	}

	/**
	 * Bottom-up Dynamic Programming solution
	 * O(N^3)
	 */
	public static int f2(Field[] p) {
		// O(N*log(N))
		Arrays.sort(p, COMPARATOR);
		// O(N^3)
		int[] resultCurr = new int[p.length + 1];
		int[] resultPrev = new int[p.length + 1];
		Arrays.fill(resultPrev, INF);
		resultPrev[p.length] = 0;
		for (int i = p.length - 1; i >= 0; i--) {
			for (int j = i; j >= 0; j--) {
				resultCurr[j] = resultPrev[(p[i].height < p[j].height) ? j : i];
				for (int k = 0; k <= i; k++) {
					if ((p[k].height >= p[i].height) && (p[k].height >= p[j].height)) {
						resultCurr[j] = Math.min(resultCurr[j], (p[i].width * p[k].height) + resultPrev[i + 1]);
					}
				}
			}
			int[] tmp = resultPrev;
			resultPrev = resultCurr;
			resultCurr = tmp;
		}
		return resultPrev[0];
	}

	/**
	 * Top-down Dynamic Programming solution
	 * (but without memoization)
	 * With memoization can be O(N^3)
	 */
	public static int f1(Field[] p) {
		// O(N*log(N))
		Arrays.sort(p, COMPARATOR);
		return f1(p, 0, 0);
	}
	public static int f1(Field[] p, int i, int j) {
		if (i == p.length) {
			return (j == p.length) ? 0 : INF;
		}
		int result = f1(p, i + 1, (p[i].height < p[j].height) ? j : i);
		for (int k = 0; k <= i; k++) {
			if ((p[k].height >= p[i].height) && (p[k].height >= p[j].height)) {
				result = Math.min(result, (p[i].width * p[k].height) + f1(p, i + 1, i + 1));
			}
		}
		return result;
	}

	static final int INF = 1000000;

	/**
	 * Solution based on filtering of lists
	 * Memoization can't be used here
	 */
	static int f0(List<Field> P) {
		if (P.isEmpty()) {
			return 0;
		}
		int result = INF;
		for (Field pi : P) {
			for (Field pj : P) {
				if ((pj.width <= pi.width) && (pj.height >= pi.height)) {
					List<Field> Pn = new ArrayList<>();
					for (Field p : P) {
						if ((p.width > pi.width) || (p.height > pj.height)) {
							Pn.add(p);
						}
					}
					result = Math.min(result, (pi.width * pj.height) + f0(Pn));
				}
			}
		}
		return result;
	}

	static final int MAX_DIMENSION = 1000001;
	static final int MAX_FIELDS_COUNT = 50001;

	static final int[] positionsCount = new int[MAX_DIMENSION + 1];
	static final Field[] tmp = new Field[MAX_FIELDS_COUNT + 1];
	static final Field[] externalItems = new Field[MAX_FIELDS_COUNT + 1];

	/**
	 * Using counting sort with O(MAX_DIMENSION + N) complexity
	 */
	static void counting_sort_By_WidthIncreasing_HeightIncreasing(Field[] fields) {
		// Sort by height decreasing
		Arrays.fill(positionsCount, 0);
		for (int i = 0; i < fields.length; i++) {
			positionsCount[fields[i].height]++;
		}
		for (int i = 1; i < positionsCount.length; i++) {
			positionsCount[i] += positionsCount[i - 1];
		}
		for (int i = fields.length - 1; i >= 0; i--) {
			tmp[positionsCount[fields[i].height] - 1] = fields[i];
			positionsCount[fields[i].height]--;
		}

		// Sort by width increasing
		Arrays.fill(positionsCount, 0);
		for (int i = 0; i < fields.length; i++) {
			positionsCount[tmp[i].width]++;
		}
		for (int i = 1; i < positionsCount.length; i++) {
			positionsCount[i] += positionsCount[i - 1];
		}

		for (int i = fields.length - 1; i >= 0; i--) {
			fields[positionsCount[tmp[i].width] - 1] = tmp[i];
			positionsCount[tmp[i].width]--;
		}
	}

	static class Field {
		int width;
		int height;

		public Field(int x, int y) {
			this.width = x;
			this.height = y;
		}

		@Override
		public String toString() {
			return "(" + this.width + ", " + this.height + ')';
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + this.width;
			result = (prime * result) + this.height;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (this.getClass() != obj.getClass()) {
				return false;
			}
			Field other = (Field) obj;
			if (this.width != other.width) {
				return false;
			}
			if (this.height != other.height) {
				return false;
			}
			return true;
		}
	}
}