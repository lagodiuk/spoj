package spoj.dynamic_programming.acquire;

// http://www.spoj.com/problems/ACQUIRE/

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ACQUIRE_Submitted {

	public static void main(String... args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(
				"/Users/yura/sandbox/code_interview_training/src/spoj/dynamic_programming/acquire/acquire_tests/acquire.10.in")));
		int inputItemsCount = Integer.parseInt(br.readLine().trim());
		String[] parts;
		String s;
		for (int i = 0; i < inputItemsCount; i++) {
			s = br.readLine();
			parts = s.split(" ");
			inputItemsWidth[i] = Integer.parseInt(parts[0]);
			inputItemsHeight[i] = Integer.parseInt(parts[1]);
		}
		long result = solve(inputItemsCount);
		System.out.println(result);
	}

	static final int MAX_DIMENSION = 1000001;
	static final int MAX_FIELDS_COUNT = 50001;

	// Input data
	static final int[] inputItemsWidth = new int[MAX_FIELDS_COUNT + 1];
	static final int[] inputItemsHeight = new int[MAX_FIELDS_COUNT + 1];

	// Needed for the Counting Sort
	static final int[] positionsCount = new int[MAX_DIMENSION + 1];
	static final int[] tmpForSortingWidth = new int[MAX_FIELDS_COUNT + 1];
	static final int[] tmpForSortingHeight = new int[MAX_FIELDS_COUNT + 1];

	// "Frontier" - items, which are not covered by other items
	static final int[] externalItemsWidth = new int[MAX_FIELDS_COUNT + 1];
	static final int[] externalItemsHeight = new int[MAX_FIELDS_COUNT + 1];

	// "Envelope" - data structure for handling of convex intersection kernel of multiple lines
	// y = kx + b
	static final long[] envelopeK = new long[MAX_FIELDS_COUNT + 1];
	static final long[] envelopeB = new long[MAX_FIELDS_COUNT + 1];
	static int envelopeStackSize = 0;
	static int queryPointer = 0;

	public static final long solve(final int length) {
		// Complexity of the counting sort is O(MAX_DIMENSION + N)
		counting_sort_By_WidthIncreasing_HeightIncreasing(length);

		// Now, we have to remove all fields, which fully covered by other fields
		// In other words: we have to keep only that fields,
		// which are not fully covered by other fields ("external frontier")
		// Complexity: O(N)
		// Approach: iterate from the widest field to the field with smallest width
		// (iterate from the last to first), and track the "largest height so far"

		int externalItemsCount = 0;
		int height = 0;
		for (int i = length - 1; i >= 0; i--) {
			if (inputItemsHeight[i] > height) {
				height = inputItemsHeight[i];
				externalItemsHeight[externalItemsCount] = inputItemsHeight[i];
				externalItemsWidth[externalItemsCount] = inputItemsWidth[i];
				externalItemsCount++;
			}
		}

		// Reverse externalItems
		int tmp;
		for (int i = 0; i < (externalItemsCount / 2); i++) {
			tmp = externalItemsHeight[i];
			externalItemsHeight[i] = externalItemsHeight[externalItemsCount - 1 - i];
			externalItemsHeight[externalItemsCount - 1 - i] = tmp;

			tmp = externalItemsWidth[i];
			externalItemsWidth[i] = externalItemsWidth[externalItemsCount - 1 - i];
			externalItemsWidth[externalItemsCount - 1 - i] = tmp;
		}

		// Amortized complexity O(N)
		envelopeStackSize = 0;
		queryPointer = 0;
		add(0, externalItemsHeight[0]);
		long cost = Long.MAX_VALUE;
		for (int i = 0; i < externalItemsCount; i++) {
			cost = query(externalItemsWidth[i]);
			if (i != (externalItemsCount - 1)) {
				add(cost, externalItemsHeight[i + 1]);
			}
		}
		return cost;
	}

	static int top;
	static int beforeTop;
	static final void add(final long b, final long k) {
		while (true) {
			if ((envelopeStackSize == 0) || (envelopeStackSize == 1)) {
				envelopeK[envelopeStackSize] = k;
				envelopeB[envelopeStackSize] = b;
				envelopeStackSize++;
				break;
			} else {
				top = envelopeStackSize - 1;
				beforeTop = envelopeStackSize - 2;
				if (((b - envelopeB[top]) * (envelopeK[beforeTop] - k)) > ((b - envelopeB[beforeTop]) * (envelopeK[top] - k))) {
					envelopeK[envelopeStackSize] = k;
					envelopeB[envelopeStackSize] = b;
					envelopeStackSize++;
					break;
				} else {
					envelopeStackSize--;
				}
			}
		}
	}

	static final long query(final long x) {
		if (queryPointer >= envelopeStackSize) {
			queryPointer = envelopeStackSize;
		}

		while ((queryPointer < (envelopeStackSize - 1))
				&& (((envelopeK[queryPointer + 1] * x) + envelopeB[queryPointer + 1]) < ((envelopeK[queryPointer] * x) + envelopeB[queryPointer]))) {
			queryPointer++;
		}

		return (envelopeK[queryPointer] * x) + envelopeB[queryPointer];
	}

	/**
	 * Using counting sort with O(MAX_DIMENSION + N) complexity
	 */
	static final void counting_sort_By_WidthIncreasing_HeightIncreasing(final int length) {
		// Sort by height decreasing
		Arrays.fill(positionsCount, 0);
		for (int i = 0; i < length; i++) {
			positionsCount[inputItemsHeight[i]]++;
		}
		for (int i = 1; i < positionsCount.length; i++) {
			positionsCount[i] += positionsCount[i - 1];
		}
		for (int i = length - 1; i >= 0; i--) {
			tmpForSortingHeight[positionsCount[inputItemsHeight[i]] - 1] = inputItemsHeight[i];
			tmpForSortingWidth[positionsCount[inputItemsHeight[i]] - 1] = inputItemsWidth[i];
			positionsCount[inputItemsHeight[i]]--;
		}

		// Sort by width increasing
		Arrays.fill(positionsCount, 0);
		for (int i = 0; i < length; i++) {
			positionsCount[tmpForSortingWidth[i]]++;
		}
		for (int i = 1; i < positionsCount.length; i++) {
			positionsCount[i] += positionsCount[i - 1];
		}

		for (int i = length - 1; i >= 0; i--) {
			inputItemsWidth[positionsCount[tmpForSortingWidth[i]] - 1] = tmpForSortingWidth[i];
			inputItemsHeight[positionsCount[tmpForSortingWidth[i]] - 1] = tmpForSortingHeight[i];
			positionsCount[tmpForSortingWidth[i]]--;
		}
	}
}