package spoj.dynamic_programming.mkuhar;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.BiFunction;

// http://www.spoj.com/problems/MKUHAR/

public class MKUHAR_Brute_Force_2 {

	public static void main(String[] args) {
		test(null);
	}

	static void test(BiFunction<Integer, Ingredient[], Integer> f) {
		test(f, true, 8, 15, 70, 40);
	}

	static void test(BiFunction<Integer, Ingredient[], Integer> f, boolean testBruteForce, int max_count, int max, int max_money, int testsNum) {

		testSolution(f, new Ingredient[]{
				new Ingredient(10, 8, 10, 10, 13, 11),
				new Ingredient(12, 20, 6, 10, 17, 24),
		}, 100, testBruteForce);

		testSolution(f, new Ingredient[]{
				new Ingredient(10, 5, 7, 10, 13, 14),
				new Ingredient(10, 5, 8, 11, 14, 15),
				new Ingredient(10, 5, 9, 12, 15, 16),
		}, 65, testBruteForce);

		Random rnd = new Random(0);

		for (int t = 0; t < testsNum; t++) {
			Ingredient[] ingredients = new Ingredient[rnd.nextInt(max_count) + 1];
			for (int i = 0; i < ingredients.length; i++) {
				int needed = rnd.nextInt(max) + 1;
				int actual = rnd.nextInt(max) + 1;
				int size_small = rnd.nextInt(max) + 1;
				int cost_small = rnd.nextInt(max) + 1;
				int size_big = size_small + rnd.nextInt((max - size_small) + 1) + 1;
				int cost_big = cost_small + rnd.nextInt((max - cost_small) + 1) + 1;
				ingredients[i] = new Ingredient(
						needed,
						actual,
						size_small,
						cost_small,
						size_big,
						cost_big);
			}
			int money = rnd.nextInt(max_money);
			testSolution(f, ingredients, money, testBruteForce);
		}
	}

	private static void testSolution(BiFunction<Integer, Ingredient[], Integer> f, Ingredient[] ingredients, int money, boolean testBruteForce) {
		System.out.println(ingredients.length + " " + money);
		for (Ingredient ing : ingredients) {
			System.out.printf("%d %d %d %d %d %d %n", ing.needed, ing.available, ing.size_small, ing.cost_small, ing.size_big, ing.cost_big);
		}

		if (testBruteForce) {
			int expected = MKUHAR_Brute_Force_2.solve(money, ingredients);
			System.out.println("Expected result: " + expected);

			if (f != null) {
				int actual = f.apply(money, ingredients);
				System.out.println("Actual result: " + actual);
				if (actual != expected) {
					throw new RuntimeException("actual != expected");
				}
			}
		} else {
			int actual = f.apply(money, ingredients);
			System.out.println("Actual result: " + actual);
		}

		System.out.println();
	}

	static int solve(int money, Ingredient[] ingredients) {
		memoized.clear();
		return solve(money, 0, ingredients, Integer.MAX_VALUE, ingredients[0].available);
	}

	static class Key {
		final int money;
		final int idx;
		final int amount;
		final int curr_cnt;
		public Key(int money, int idx, int amount, int curr_cnt) {
			this.money = money;
			this.idx = idx;
			this.amount = amount;
			this.curr_cnt = curr_cnt;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + this.amount;
			result = (prime * result) + this.curr_cnt;
			result = (prime * result) + this.idx;
			result = (prime * result) + this.money;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (this.getClass() != obj.getClass()) {
				return false;
			}
			Key other = (Key) obj;
			if (this.amount != other.amount) {
				return false;
			}
			if (this.curr_cnt != other.curr_cnt) {
				return false;
			}
			if (this.idx != other.idx) {
				return false;
			}
			if (this.money != other.money) {
				return false;
			}
			return true;
		}
	}
	static Map<Key, Integer> memoized = new HashMap<>();

	static int solve(int money, int idx, Ingredient[] ingredients, int amount, int curr_cnt) {
		if (idx == ingredients.length) {
			return amount;
		}

		Key key = new Key(money, idx, amount, curr_cnt);
		if (memoized.containsKey(key)) {
			return memoized.get(key);
		}

		int result = Integer.MIN_VALUE;

		if (money >= ingredients[idx].cost_small) {
			result = Math.max(result, solve(money - ingredients[idx].cost_small, idx, ingredients, amount, curr_cnt + ingredients[idx].size_small));
		}

		if (money >= ingredients[idx].cost_big) {
			result = Math.max(result, solve(money - ingredients[idx].cost_big, idx, ingredients, amount, curr_cnt + ingredients[idx].size_big));
		}

		int next_cnt = ((idx + 1) < ingredients.length) ? ingredients[idx + 1].available : -100;
		result = Math.max(result, solve(money, idx + 1, ingredients, Math.min(amount, curr_cnt / ingredients[idx].needed), next_cnt));

		memoized.put(key, result);
		return result;
	}
}
