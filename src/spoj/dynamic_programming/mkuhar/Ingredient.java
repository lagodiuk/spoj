package spoj.dynamic_programming.mkuhar;

class Ingredient {
	final int needed;
	final int available;
	final int size_small;
	final int cost_small;
	final int size_big;
	final int cost_big;
	public Ingredient(int needed, int available, int size_small, int cost_small, int size_big, int cost_big) {
		this.needed = needed;
		this.available = available;
		this.size_small = size_small;
		this.cost_small = cost_small;
		this.size_big = size_big;
		this.cost_big = cost_big;
	}
}