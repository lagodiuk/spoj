package spoj.dynamic_programming.dp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Best_Times_Assignemnts_Brute_Force {

	public static void main(String[] args) {
		System.out.println(assign(Arrays.asList(2, 2, 4, 2, 4, 25, 6, 4, 6, 36, 27, 40, 11, 9, 11, 41, 32, 45)));
		compare(DP_2::minTime, true, 20);
		compare(DP_2::minTime, false, 50);
	}

	static void compare(Function<List<Integer>, Integer> tested, boolean compareWithBruteForce, int maxSize) {
		Random rnd = new Random(1);

		for (int i = 0; i < 50; i++) {
			int size = rnd.nextInt(maxSize - 1) + 1;
			List<Integer> times = new ArrayList<>();
			for (int j = 0; j < size; j++) {
				times.add(rnd.nextInt(10) + 1);
			}

			// System.out.println(times);
			int actual = tested.apply(new ArrayList<>(times));

			if (compareWithBruteForce) {
				int expected = assign(new ArrayList<>(times));

				if (expected != actual) {
					System.out.println("Expected result: " + expected);
					System.out.println("Actual result: " + actual);
					throw new RuntimeException("Results are different!");
				}
			}

			System.out.println("Result: " + actual);
			System.out.println();
		}
	}

	static int assign(List<Integer> times) {
		int result = assign(times, new ArrayList<>(), new ArrayList<>(), 0);
		return result;
	}

	static int assign(List<Integer> times, List<Integer> worker1, List<Integer> worker2, int pos) {
		if (pos == times.size()) {
			int t1 = calculateTime(worker1);
			int t2 = calculateTime(worker2);
			return Math.max(t1, t2);
		}

		worker1.add(times.get(pos));
		int result1 = assign(times, worker1, worker2, pos + 1);
		worker1.remove(worker1.size() - 1);

		worker2.add(times.get(pos));
		int result2 = assign(times, worker1, worker2, pos + 1);
		worker2.remove(worker2.size() - 1);

		return Math.min(result1, result2);
	}

	static int calculateTime(List<Integer> worker) {
		int maxTime = 0;
		int total = 0;
		for (int x : worker) {
			total += x * 2;
			maxTime = Math.max(maxTime, x);
		}
		total -= maxTime;
		return total;
	}
}
