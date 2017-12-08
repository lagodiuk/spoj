package spoj.segment_tree;

// http://www.spoj.com/problems/FREQUENT/

public class FREQUENT {

	public static void main(String[] args) {
		SegmentTreeNode segmentTree = new SegmentTreeNode(new int[]{-1, -1, 1, 1, 1, 1, 3, 10, 10, 10});
		System.out.println(segmentTree.q(2, 3).maxFreq);
		System.out.println(segmentTree.q(1, 10).maxFreq);
		System.out.println(segmentTree.q(5, 10).maxFreq);

		System.out.println();
		System.out.println(segmentTree.q(1, 1).maxFreq);
		System.out.println(segmentTree.q(2, 2).maxFreq);
		System.out.println(segmentTree.q(10, 10).maxFreq);

		System.out.println();
		System.out.println(segmentTree.q(1, 3).maxFreq);
		System.out.println(segmentTree.q(1, 4).maxFreq);
		System.out.println(segmentTree.q(1, 5).maxFreq);
		System.out.println(segmentTree.q(7, 10).maxFreq);
		System.out.println(segmentTree.q(3, 10).maxFreq);
	}

	static class SegmentTreeNode {
		int maxFreq;

		int leftVal;
		int rightVal;
		int prefixLength;
		int suffixLength;

		int leftBound;
		int rightBound;
		SegmentTreeNode leftChild;
		SegmentTreeNode rightChild;

		public SegmentTreeNode() {
			// Empty constructor
		}

		public SegmentTreeNode(int[] arr) {
			this(arr, 0, arr.length - 1);
		}

		public SegmentTreeNode(int[] arr, int l, int r) {
			if (l == r) {
				this.maxFreq = 1;
				this.leftVal = arr[l];
				this.rightVal = arr[l];
				this.prefixLength = 1;
				this.suffixLength = 1;
				this.leftBound = l;
				this.rightBound = r;
				return;
			}

			int mid = (l + r) / 2;
			this.leftChild = new SegmentTreeNode(arr, l, mid);
			this.rightChild = new SegmentTreeNode(arr, mid + 1, r);

			this.maxFreq = Math.max(
					Math.max(this.leftChild.maxFreq, this.rightChild.maxFreq),
					(this.leftChild.rightVal == this.rightChild.leftVal)
							? this.leftChild.suffixLength + this.rightChild.prefixLength
							: Integer.MIN_VALUE);

			this.leftVal = this.leftChild.leftVal;
			this.rightVal = this.rightChild.rightVal;

			this.prefixLength = Math.max(
					this.leftChild.prefixLength,
					((this.leftChild.leftVal == this.leftChild.rightVal)
					&& (this.leftChild.rightVal == this.rightChild.leftVal))
							? ((mid - l) + 1) + this.rightChild.prefixLength
							: Integer.MIN_VALUE);

			this.suffixLength = Math.max(
					this.rightChild.suffixLength,
					((this.rightChild.rightVal == this.rightChild.leftVal)
					&& (this.rightChild.leftVal == this.leftChild.rightVal))
							? (r - mid) + this.leftChild.suffixLength
							: Integer.MIN_VALUE);

			this.leftBound = l;
			this.rightBound = r;
		}

		SegmentTreeNode q(int l, int r) {
			return this.query(l - 1, r - 1);
		}

		SegmentTreeNode query(int l, int r) {
			if ((l <= this.leftBound) && (this.rightBound <= r)) {
				return this;
			}

			if (r <= this.leftChild.rightBound) {
				return this.leftChild.query(l, r);
			}

			if (l >= this.rightChild.leftBound) {
				return this.rightChild.query(l, r);
			}

			SegmentTreeNode leftResult = this.leftChild.query(l, r);
			SegmentTreeNode rightResult = this.rightChild.query(l, r);
			SegmentTreeNode result = new SegmentTreeNode();

			result.maxFreq = Math.max(
					Math.max(leftResult.maxFreq, rightResult.maxFreq),
					(leftResult.rightVal == rightResult.leftVal)
							? leftResult.suffixLength + rightResult.prefixLength
							: Integer.MIN_VALUE);

			result.leftVal = leftResult.leftVal;
			result.rightVal = rightResult.rightVal;

			result.prefixLength = Math.max(
					leftResult.prefixLength,
					((leftResult.leftVal == leftResult.rightVal)
					&& (leftResult.rightVal == rightResult.leftVal))
							? ((leftResult.rightBound - leftResult.leftBound) + 1) + rightResult.prefixLength
							: Integer.MIN_VALUE);

			result.suffixLength = Math.max(
					rightResult.suffixLength,
					((rightResult.rightVal == rightResult.leftVal)
					&& (rightResult.leftVal == leftResult.rightVal))
							? ((rightResult.rightBound - rightResult.leftBound) + 1) + leftResult.suffixLength
							: Integer.MIN_VALUE);

			result.leftBound = leftResult.leftBound;
			result.rightBound = rightResult.rightBound;

			return result;
		}
	}

}
