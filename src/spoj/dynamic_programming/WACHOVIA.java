package spoj.dynamic_programming;

// http://www.spoj.com/problems/WACHOVIA/

public class WACHOVIA {

	public static void main(String[] args) {
		System.out.println(calculateMaxCost(
				new Bag[]{
						new Bag(178, 12),
						new Bag(30, 1),
						new Bag(13, 7),
						new Bag(34, 8),
						new Bag(87, 6),
				}, 34));

		System.out.println(calculateMaxCost(
				new Bag[]{new Bag(900, 25)}, 900));

		System.out.println(calculateMaxCost(
				new Bag[]{
						new Bag(27, 16),
						new Bag(131, 9),
						new Bag(132, 17),
						new Bag(6, 5),
						new Bag(6, 23),
						new Bag(56, 21),
						new Bag(100, 25),
						new Bag(1, 25),
						new Bag(25, 25),
						new Bag(100, 2),
				}, 100));
	}
	static long calculateMaxCost(Bag[] bags, long maxWeight) {
		return calculateMaxCost(bags, bags.length - 1, maxWeight);
	}

	static final int NEGATIVE_INFINITY = -100000000;

	// Solution with exponential complexity
	// TODO: add memoization
	static long calculateMaxCost(Bag[] bags, int currBagIndex, long maxWeight) {

		if (maxWeight < 0) {
			return NEGATIVE_INFINITY;
		}

		if (currBagIndex < 0) {
			return 0;
		}

		Bag currBag = bags[currBagIndex];

		// memoize cost[bagIndex][maxWeight]
		return Math.max(
				calculateMaxCost(bags, currBagIndex - 1, maxWeight),
				calculateMaxCost(bags, currBagIndex - 1, maxWeight - currBag.weight) + currBag.cost);
	}

	static class Bag {
		long weight;
		long cost;
		Bag(long weight, long cost) {
			this.weight = weight;
			this.cost = cost;
		}
	}
}
