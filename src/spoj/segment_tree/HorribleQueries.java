package spoj.segment_tree;

import java.util.Scanner;

/**
 * http://www.spoj.com/problems/HORRIBLE/
 */
public class HorribleQueries {

	public static void main(String[] args) throws Exception {
		Scanner scan = new Scanner(System.in);
		int tests = scan.nextInt();
		for (int i = 0; i < tests; i++) {
			int size = scan.nextInt();
			int queriesCount = scan.nextInt();
			SegmentTreeNode st = new SegmentTreeNode(0, size);
			for (int j = 0; j < queriesCount; j++) {
				int queryType = scan.nextInt();
				int l = scan.nextInt();
				int r = scan.nextInt();
				if (queryType == 1) {
					System.out.println(st.query(l, r));
				} else {
					int val = scan.nextInt();
					st.update(l, r, val);
				}
			}
		}
	}

	static class SegmentTreeNode {
		int left;
		int right;

		SegmentTreeNode lChild;
		SegmentTreeNode rChild;

		long sum;
		long lazy;

		SegmentTreeNode(int left, int right) {

			this.left = left;
			this.right = right;

			if (left == right) {
				return;
			}

			int mid = (left + right) / 2;
			this.lChild = new SegmentTreeNode(left, mid);
			this.rChild = new SegmentTreeNode(mid + 1, right);
		}

		void update(int l, int r, int val) {
			this.lazyUpdate();

			if ((r < this.left) || (this.right < l)) {
				return;
			}

			if ((l <= this.left) && (this.right <= r)) {
				this.lazy = val;
				this.lazyUpdate();
				return;
			}

			this.lChild.update(l, r, val);
			this.rChild.update(l, r, val);
			this.sum = this.lChild.sum + this.rChild.sum;
		}

		long query(int l, int r) {
			this.lazyUpdate();

			if ((r < this.left) || (this.right < l)) {
				return 0;
			}

			if ((l <= this.left) && (this.right <= r)) {
				return this.sum;
			}

			return this.lChild.query(l, r) + this.rChild.query(l, r);
		}

		void lazyUpdate() {
			this.sum += this.lazy * ((this.right - this.left) + 1);
			if (this.lChild != null) {
				this.lChild.lazy += this.lazy;
			}
			if (this.rChild != null) {
				this.rChild.lazy += this.lazy;
			}
			this.lazy = 0;
		}
	}

}
