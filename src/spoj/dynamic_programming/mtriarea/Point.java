package spoj.dynamic_programming.mtriarea;

public class Point {

	public final int x;

	public final int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
}
