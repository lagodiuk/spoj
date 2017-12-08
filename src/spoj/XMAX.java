package spoj;

// http://www.spoj.com/problems/XMAX/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class XMAX {

	public static void main(String[] args) {
		Random r = new Random(1);
		for (int i = 0; i < 50; i++) {
			int[] arr = new int[r.nextInt(20) + 1];
			for (int j = 0; j < arr.length; j++) {
				arr[j] = Math.abs(r.nextInt());
			}
			System.out.println(arr.length);
			System.out.println(Arrays.toString(arr).replaceAll("[\\[\\],]", " "));
			test(arr);
		}
	}

	static void test(int[] arr) {
		System.out.println(maxXOR_BruteForce(arr));
		System.out.println(maxXOR2_ExponentialComplexity(arr));
		System.out.println(maxXOR3_GaussianElimination(arr));
		System.out.println();
	}

	static int maxXOR3_GaussianElimination(int[] arr) {
		int maxRadix = 64;
		Map<Integer, List<Integer>> msbBuckets = new HashMap<>();
		for (int i = 0; i <= maxRadix; i++) {
			msbBuckets.put(i, new ArrayList<>());
		}

		for (int x : arr) {
			int bucket = getMostSignificantBitPosition(x);
			msbBuckets.get(bucket).add(x);
		}
		for (int i = maxRadix; i > 0; i--) {
			List<Integer> bucket = msbBuckets.get(i);
			if (bucket.size() == 0) {
				continue;
			}

			int first = bucket.get(0);
			for (int j = 1; j < bucket.size(); j++) {
				int curr = bucket.get(j);
				curr ^= first;
				int msb = getMostSignificantBitPosition(curr);
				msbBuckets.get(msb).add(curr);
			}
			bucket.clear();
			bucket.add(first);
		}

		int result = 0;
		for (int i = maxRadix; i > 0; i--) {
			List<Integer> bucket = msbBuckets.get(i);
			if (bucket.size() == 0) {
				continue;
			}

			int first = bucket.get(0);
			if ((result ^ first) > result) {
				result ^= first;
			}
		}

		return result;
	}

	static int getMostSignificantBitPosition(int x) {
		int n = 0;
		while (x != 0) {
			x >>= 1;
			n++;
		}
		return n;
	}

	static int maxXOR2_ExponentialComplexity(int[] arr) {
		int[] xrd = new int[arr.length];
		xrd[arr.length - 1] = arr[arr.length - 1];
		for (int i = arr.length - 2; i >= 0; i--) {
			xrd[i] = xrd[i + 1] ^ arr[i];
		}

		List<Integer> lst = new ArrayList<Integer>();
		lst.add(0);
		lst.add(0 ^ xrd[0]);
		for (int i = 1; i < arr.length; i++) {
			List<Integer> tmp = new ArrayList<Integer>();
			for (int x : lst) {
				tmp.add(x ^ xrd[i]);
			}
			lst.addAll(tmp);
		}

		// System.out.println(lst);
		return Collections.max(lst);
	}

	static long maxXOR_BruteForce(int[] arr) {
		return maxXOR(arr, 0, 0);
	}

	static long maxXOR(int[] arr, int pos, int xorSoFar) {
		if (pos == arr.length) {
			return xorSoFar;
		}
		return Math.max(maxXOR(arr, pos + 1, xorSoFar), maxXOR(arr, pos + 1, xorSoFar ^ arr[pos]));
	}
}
