package spoj.dynamic_programming.fcandy;

// http://www.spoj.com/problems/FCANDY/

import java.util.ArrayList;
import java.util.List;

public class FCANDY_12_Pisinger_Algorithm {

	public static void main(String[] args) {
		FCANDY_1_Top_Down.evaluateExample(FCANDY_12_Pisinger_Algorithm::solve1);
		FCANDY_1_Top_Down.evaluateRandomized(FCANDY_12_Pisinger_Algorithm::solve1, true, 6);
		FCANDY_1_Top_Down.evaluateRandomized(FCANDY_12_Pisinger_Algorithm::solve1, false, 100, 50, 50);
		FCANDY_1_Top_Down.evaluateRandomized(FCANDY_12_Pisinger_Algorithm::solve1, false, 100, 500, 200);
	}

	static final int solve1(Candies[] candies) {

		int totalCalories = calculateTotalalories(candies);

		List<Integer> allCandies = new ArrayList<>();
		for (Candies ca : candies) {
			for (int i = 0; i < ca.count; i++) {
				allCandies.add(ca.calories);
			}
		}

		int[] arr = new int[allCandies.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = allCandies.get(i);
		}

		int maxSubsetSum = Pisinger_Algorithm.subsetSumPisinger(arr, totalCalories / 2);

		return totalCalories - (2 * maxSubsetSum);
	}

	static final int calculateTotalalories(Candies[] candies) {
		int totalCalories = 0;
		for (Candies ca : candies) {
			totalCalories += ca.calories * ca.count;
		}
		return totalCalories;
	}
}
