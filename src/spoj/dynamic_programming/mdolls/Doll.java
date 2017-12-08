package spoj.dynamic_programming.mdolls;

// http://www.spoj.com/problems/MDOLLS/en/

public class Doll {

	int width;
	int height;

	public Doll(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public String toString() {
		return "(w=" + this.width + ", h=" + this.height + ")";
	}
}
