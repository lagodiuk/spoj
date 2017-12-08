package spoj.dynamic_programming.activ;

// http://www.spoj.com/problems/ACTIV/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class ACTIV_Brute_Force {

	public static void main(String[] args) {
		testFewManualCases(null);
		testRandomizedCases(null);
	}

	static void testRandomizedCases(Function<Activity[], Integer> tested) {
		testRandomizedCases(tested, 10, true);
	}

	static void testRandomizedCases(Function<Activity[], Integer> tested, int maxActivitiesCount, boolean compareWithBruteForce) {
		Random rnd = new Random(0);
		int maxCoordinate = 100;

		for (int test = 0; test < 100; test++) {
			Activity[] activities = new Activity[rnd.nextInt(maxActivitiesCount) + 1];
			for (int i = 0; i < activities.length; i++) {
				int from = rnd.nextInt(maxCoordinate - 1) + 1;
				int to = from + rnd.nextInt((maxCoordinate - from) + 1) + 1;
				activities[i] = new Activity(from, to);
			}
			verify(activities, tested, compareWithBruteForce);
		}
	}

	static void testFewManualCases(Function<Activity[], Integer> tested) {
		verify(new Activity[]{
				new Activity(1, 3),
				new Activity(3, 5),
				new Activity(5, 7),
				new Activity(2, 4),
				new Activity(4, 6)
		}, tested, true);

		verify(new Activity[]{
				new Activity(500000000, 1000000000),
				new Activity(1, 500000000),
				new Activity(1, 500000000),
		}, tested, true);

		verify(new Activity[]{
				new Activity(999999999, 1000000000)
		}, tested, true);

		verify(new Activity[]{
				new Activity(1, 2),
				new Activity(2, 3),
				new Activity(3, 4),
				new Activity(1, 8),
				new Activity(4, 5),
				new Activity(5, 6)
		}, tested, true);

		verify(new Activity[]{
				new Activity(1, 1),
				new Activity(2, 2),
				new Activity(1, 1),
				new Activity(2, 2),
				new Activity(3, 3)
		}, tested, true);
	}

	static void verify(Activity[] activities, Function<Activity[], Integer> tested, boolean compareWithBruteForce) {
		System.out.println(activities.length);
		for (Activity a : activities) {
			System.out.println(a.start + " " + a.end);
		}

		if (tested == null) {
			int result = solve(activities);
			System.out.println("Result: " + result);
		} else {
			if (compareWithBruteForce) {
				int bruteForceResult = solve(activities.clone());
				int testedResult = tested.apply(activities.clone());
				if (bruteForceResult != testedResult) {
					System.out.println("Expected result: " + bruteForceResult);
					System.out.println("Actual result: " + testedResult);
					throw new RuntimeException("Results are different!");
				} else {
					System.out.println("Result: " + bruteForceResult);
				}
			} else {
				int testedResult = tested.apply(activities);
				System.out.println("Result: " + testedResult);
			}
		}

		System.out.println();
	}

	static int solve(Activity[] activities) {
		int result = solve(activities, 0, new ArrayList<>());
		return result;
	}

	static int solve(Activity[] activities, int curr, List<Activity> selected) {
		if (curr == activities.length) {
			if (selected.isEmpty()) {
				return 0;
			}

			Activity[] tmp = selected.toArray(new Activity[]{});
			Arrays.sort(tmp, (a1, a2) -> Integer.compare(a1.start, a2.start));
			for (int i = 1; i < tmp.length; i++) {
				if (tmp[i - 1].end > tmp[i].start) {
					return 0;
				}
			}
			return 1;
		}

		// with current
		selected.add(activities[curr]);
		int result = solve(activities, curr + 1, selected);
		selected.remove(selected.size() - 1);

		// without current
		result += solve(activities, curr + 1, selected);

		return result;
	}
}
