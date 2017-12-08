package spoj.segment_tree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * http://www.spoj.com/problems/HORRIBLE/
 */
public class HorribleQueries_Using_Array {

	public static void main(String[] args) throws Exception {
		FastScanner scan = new FastScanner(new InputStreamReader(System.in));
		// System.out.println(SegmentTree.SIZE);
		int tests = scan.nextInt();
		for (int i = 0; i < tests; i++) {
			int right = scan.nextInt();
			SegmentTree.reset(right);
			int queriesCount = scan.nextInt();
			for (int j = 0; j < queriesCount; j++) {
				int queryType = scan.nextInt();
				int l = scan.nextInt();
				int r = scan.nextInt();
				if (queryType == 1) {
					System.out.println(SegmentTree.query(l, r, 0, 0, right));
				} else {
					int val = scan.nextInt();
					SegmentTree.update(l, r, val, 0, 0, right);
				}
			}
		}
	}

	static final class SegmentTree {
		static final int MAX_RIGHT = 100002;
		static final int SIZE = 524288; // getSegmentTreeArraySize(MAX_RIGHT);
		static final long[] sum = new long[SIZE];
		static final long[] lazy = new long[SIZE];

		static final int getSegmentTreeArraySize(int x) {
			int size = 1;
			while (size < x) {
				size <<= 1;
			}
			return size << 2;
		}

		static final void reset(int r) {
			Arrays.fill(sum, 0);
			Arrays.fill(lazy, 0);
		}

		static final void update(int l, int r, int val, int index, int indexL, int indexR) {
			lazyUpdate(index, indexL, indexR);

			if ((r < indexL) || (indexR < l)) {
				return;
			}

			if ((l <= indexL) && (indexR <= r)) {
				lazy[index] = val;
				lazyUpdate(index, indexL, indexR);
				return;
			}

			int mid = (indexL + indexR) >> 1;

			int idxL = (index << 1) + 1;
			int idxR = idxL + 1;

			update(l, r, val, idxL, indexL, mid);
			update(l, r, val, idxR, mid + 1, indexR);

			sum[index] = sum[idxL] + sum[idxR];
		}

		static final long query(int l, int r, int index, int indexL, int indexR) {
			lazyUpdate(index, indexL, indexR);

			if ((r < indexL) || (indexR < l)) {
				return 0;
			}

			if ((l <= indexL) && (indexR <= r)) {
				return sum[index];
			}

			int mid = (indexL + indexR) >> 1;

			int idxL = (index << 1) + 1;
			int idxR = idxL + 1;

			return query(l, r, idxL, indexL, mid)
					+ query(l, r, idxR, mid + 1, indexR);
		}

		static final void lazyUpdate(int index, int left, int right) {
			sum[index] += lazy[index] * ((right - left) + 1);

			int idxL = (index << 1) + 1;
			int idxR = idxL + 1;

			lazy[idxL] += lazy[index];
			lazy[idxR] += lazy[index];

			lazy[index] = 0;
		}
	}

	// http://neerc.ifmo.ru/trains/information/Template.java
	static final class FastScanner {
		BufferedReader br;
		StringTokenizer st;

		FastScanner(InputStreamReader in) throws Exception {
			this.br = new BufferedReader(in);
		}

		String next() {
			while ((this.st == null) || !this.st.hasMoreTokens()) {
				try {
					this.st = new StringTokenizer(this.br.readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return this.st.nextToken();
		}

		int nextInt() {
			return Integer.parseInt(this.next());
		}
	}
}
