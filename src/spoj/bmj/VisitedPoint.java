package spoj.bmj;

public class VisitedPoint {

	public final int x;
	public final int y;

	public VisitedPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return String.format("(%d, %d)", this.x, this.y);
	}
}
