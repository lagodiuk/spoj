package spoj.segment_tree;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.TreeMap;

/**
 * http://www.spoj.com/problems/YODANESS/
 */
public class Yodaness_Level {

	public static void main(String[] args) throws Exception {
		// int[] arr = {1, 2, 3, 6, 4, 5};
		// int[] arr = {5, 6, 4, 1, 2, 3};
		// System.out.println(sort(arr, 0, 5));
		// System.out.println(Arrays.toString(arr));

		// System.out.println(getYodanessLevel("you are strong in the force",
		// "in the force strong you are"));
		// System.out.println(getYodanessLevel("or i will not help you",
		// "or i will help you not"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int testsNum = Integer.parseInt(br.readLine());
		String orig;
		String yoda;
		for (int i = 0; i < testsNum; i++) {
			br.readLine();
			yoda = br.readLine();
			orig = br.readLine();
			System.out.println(getYodanessLevel(orig, yoda));
		}
	}

	static final int getYodanessLevel(String orig, String yoda) {
		TreeMap<String, Integer> wordToIndex = new TreeMap<>();

		int i = 0;
		for (String s : orig.split(" ")) {
			wordToIndex.put(s, i);
			i++;
		}

		i = 0;
		for (String s : yoda.split(" ")) {
			arr[i] = wordToIndex.get(s);
			i++;
		}

		return sort(arr, 0, i - 1);
	}

	static final int[] arr = new int[30002];
	static final int[] tmp = new int[30002];

	static final int sort(int[] arr, int l, int r) {
		if (l >= r) {
			return 0;
		}

		int inversions = 0;

		int mid = (l + r) >> 1;
		inversions += sort(arr, l, mid);
		inversions += sort(arr, mid + 1, r);
		inversions += merge(arr, l, mid, r);
		return inversions;
	}

	static final int merge(int[] arr, int l, int m, int r) {
		int leftLen = (m - l) + 1;

		int idxL = 0;
		System.arraycopy(arr, l, tmp, idxL, leftLen);
		tmp[leftLen] = Integer.MAX_VALUE;

		int idxR = leftLen + 1;
		System.arraycopy(arr, m + 1, tmp, idxR, r - m);
		tmp[(r - l) + 2] = Integer.MAX_VALUE;

		int inversions = 0;

		for (int i = l; i <= r; i++) {
			if (tmp[idxL] <= tmp[idxR]) {
				arr[i] = tmp[idxL];
				idxL++;
			} else {
				arr[i] = tmp[idxR];
				idxR++;
				inversions += leftLen - idxL;
			}
		}

		return inversions;
	}

}
