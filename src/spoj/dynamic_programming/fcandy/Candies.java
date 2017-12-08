package spoj.dynamic_programming.fcandy;

// http://www.spoj.com/problems/FCANDY/

class Candies {
	int count;
	int calories;
	public Candies(int count, int calories) {
		this.count = count;
		this.calories = calories;
	}

	@Override
	public String toString() {
		return "(" + this.count + ", " + this.calories + ")";
	}
}