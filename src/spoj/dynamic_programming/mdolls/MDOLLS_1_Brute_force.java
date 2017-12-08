package spoj.dynamic_programming.mdolls;

// http://www.spoj.com/problems/MDOLLS/en/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class MDOLLS_1_Brute_force {

	public static void main(String[] args) {
		testFewManualCases(null);
		testRandomlyGeneratedCases(null);
	}

	static void testRandomlyGeneratedCases(Function<Doll[], Integer> f) {
		testRandomlyGeneratedCases(f, true, 10, 10);
	}

	static void testRandomlyGeneratedCases(Function<Doll[], Integer> f, boolean testBruteForce, int maxDollsCount, int maxSize) {
		Random rnd = new Random(1);

		for (int testCase = 0; testCase < 50; testCase++) {
			Doll[] dolls = new Doll[rnd.nextInt(maxDollsCount) + 1];
			for (int d = 0; d < dolls.length; d++) {
				dolls[d] = new Doll(rnd.nextInt(maxSize) + 1, rnd.nextInt(maxSize) + 1);
			}
			compareWithBruteForce(dolls, f, testBruteForce);
		}
	}

	static void testFewManualCases(Function<Doll[], Integer> f) {
		testFewManualCases(f, true);
	}

	static void testFewManualCases(Function<Doll[], Integer> f, boolean testBruteForce) {
		compareWithBruteForce(new Doll[]{
				new Doll(20, 30),
				new Doll(40, 50),
				new Doll(30, 40),
		}, f, testBruteForce);

		compareWithBruteForce(new Doll[]{
				new Doll(20, 30),
				new Doll(10, 10),
				new Doll(30, 20),
				new Doll(40, 50),
		}, f, testBruteForce);

		compareWithBruteForce(new Doll[]{
				new Doll(10, 30),
				new Doll(20, 20),
				new Doll(30, 10),
		}, f, testBruteForce);

		compareWithBruteForce(new Doll[]{
				new Doll(10, 10),
				new Doll(20, 30),
				new Doll(40, 50),
				new Doll(39, 51),
		}, f, testBruteForce);

		compareWithBruteForce(new Doll[]{
				new Doll(1, 2),
				new Doll(2, 1),
				new Doll(3, 4),
				new Doll(4, 3),
				new Doll(5, 6),
				new Doll(6, 8),
				new Doll(7, 5),
				new Doll(8, 7),
		}, f, testBruteForce);

		compareWithBruteForce(new Doll[]{
				new Doll(5, 3),
				new Doll(3, 3),
				new Doll(10, 7),
				new Doll(4, 8),
				new Doll(4, 10),
		}, f, testBruteForce);
	}

	static void compareWithBruteForce(Doll[] dolls, Function<Doll[], Integer> f, boolean testBruteForce) {
		System.out.println(dolls.length);
		for (Doll d : dolls) {
			System.out.print(d.height + " " + d.width + " ");
		}
		System.out.println();

		if (testBruteForce) {
			int bruteForceResult = solve(dolls.clone());
			System.out.println("Result: " + bruteForceResult);

			if (f != null) {
				int testedResult = f.apply(dolls.clone());
				if (testedResult != bruteForceResult) {
					System.out.println("===> Tested Result: " + testedResult);
					f.apply(dolls);
					throw new RuntimeException("Different results!");
				}
			}
		} else {
			int testedResult = f.apply(dolls);
			System.out.println("Result: " + testedResult);
		}

		System.out.println();
	}

	static int solve(Doll[] dolls) {
		Arrays.sort(dolls, (d1, d2) -> {
			return Integer.compare(d1.height, d2.height);
		});

		int result = solve(dolls, 0, new ArrayList<>());
		return result;
	}

	static int solve(Doll[] dollsSortedByHeight, int curr, List<Doll> topLevel) {

		if (curr == dollsSortedByHeight.length) {
			return topLevel.size();
		}

		Doll currDoll = dollsSortedByHeight[curr];
		int result = Integer.MAX_VALUE;

		if (!topLevel.isEmpty()) {

			for (int i = 0; i < topLevel.size(); i++) {
				Doll existingTopLevelDoll = topLevel.get(i);

				if ((currDoll.width > existingTopLevelDoll.width)
						&& (currDoll.height > existingTopLevelDoll.height)) {

					// nest existing top level doll into current doll
					topLevel.set(i, currDoll);
					result = Math.min(result, solve(dollsSortedByHeight, curr + 1, topLevel));
					topLevel.set(i, existingTopLevelDoll);
				}
			}
		}

		// add current doll as a new top level doll
		topLevel.add(currDoll);
		result = Math.min(result, solve(dollsSortedByHeight, curr + 1, topLevel));
		topLevel.remove(topLevel.size() - 1);

		return result;
	}
}
