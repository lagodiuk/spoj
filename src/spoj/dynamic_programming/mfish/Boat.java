package spoj.dynamic_programming.mfish;

public class Boat {

	int anchor;
	int length;
	int minPos;

	public Boat(int anchor, int length) {
		this.anchor = anchor;
		this.length = length;
		this.minPos = (anchor - length) + 1;
		if (this.minPos < 1) {
			this.minPos = 1;
		}
	}
}
