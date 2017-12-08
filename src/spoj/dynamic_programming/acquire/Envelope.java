package spoj.dynamic_programming.acquire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// http://wcipeg.com/wiki/Convex_hull_trick

public class Envelope {

	private List<Line> stack = new ArrayList<>();
	private List<Double> intersectionPoints = new ArrayList<>();

	public long query(long x) {
		if (this.stack.isEmpty()) {
			throw new RuntimeException();
		}

		if (this.intersectionPoints.isEmpty()) {
			return this.stack.get(0).valueAtX(x);
		}
		int idx = Collections.binarySearch(this.intersectionPoints, (double) x);
		if (idx >= 0) {
			return this.stack.get(idx).valueAtX(x);
		} else {
			return this.stack.get(-idx - 1).valueAtX(x);
		}
	}

	public final void add(Line curr) {

		if (this.stack.isEmpty()) {
			this.stack.add(curr);
			return;
		}

		if (this.stack.size() == 1) {

			Line top = this.stack.get(this.stack.size() - 1);
			double xTop = ((double) (curr.b - top.b)) / (top.k - curr.k);
			this.intersectionPoints.add(xTop);

			this.stack.add(curr);

		} else {
			Line beforeTop = this.stack.get(this.stack.size() - 2);
			Line top = this.stack.get(this.stack.size() - 1);

			// long xBefore = (curr.b - beforeTop.b) / (beforeTop.k - curr.k);
			// long xTop = (curr.b - top.b) / (top.k - curr.k);
			// if (xTop > xBefore) {
			if (((curr.b - top.b) * (beforeTop.k - curr.k)) > ((curr.b - beforeTop.b) * (top.k - curr.k))) {

				double xTop = ((double) (curr.b - top.b)) / (top.k - curr.k);
				this.intersectionPoints.add(xTop);

				this.stack.add(curr);

			} else {

				this.stack.remove(this.stack.size() - 1);
				this.intersectionPoints.remove(this.intersectionPoints.size() - 1);

				this.add(curr);
			}

			// k1x + b1 = k2x + b2
			// x(k1 - k2) = (b2 - b1)
			// x = (b2-b1)/(k1-k2)
		}
	}

	/**
	 * y = kx + b
	 */
	static class Line {
		final long b;
		final long k;
		public Line(long b, long k) {
			this.b = b;
			this.k = k;
		}

		long valueAtX(long x) {
			return (this.k * x) + this.b;
		}
	}
}
