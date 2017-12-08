package spoj.dynamic_programming.bridge;

public class Bridge {
	public int from;
	public int to;
	public Bridge(int from, int to) {
		this.from = from;
		this.to = to;
	}
	@Override
	public String toString() {
		return "[from=" + this.from + ", to=" + this.to + "]";
	}
}
