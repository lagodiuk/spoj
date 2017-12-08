package spoj.dynamic_programming.mtriarea;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

// http://www.spoj.com/problems/MTRIAREA/

public class MTRIAREA_1_Brute_Force {

	public static void main(String[] args) {
		test(null);
	}

	public static void test(Function<Point[], Double> tested) {
		test(tested, true, 40, true);
	}

	public static void test(Function<Point[], Double> tested, boolean compare_with_brute_force, int max_points_amount, boolean print_problem) {
		testSolution(tested, new Point[]{
				new Point(3, 4),
				new Point(2, 6),
				new Point(2, 7),
		}, compare_with_brute_force, print_problem);

		testSolution(tested, new Point[]{
				new Point(2, 6),
				new Point(3, 9),
				new Point(2, 0),
				new Point(8, 0),
				new Point(6, 5),
		}, compare_with_brute_force, print_problem);

		Random rnd = new Random(1);
		for (int test = 0; test < 150; test++) {
			Point[] points = new Point[rnd.nextInt(max_points_amount) + 1];
			for (int i = 0; i < points.length; i++) {
				points[i] = new Point(rnd.nextInt(100) - rnd.nextInt(100), rnd.nextInt(100) - rnd.nextInt(100));
			}
			testSolution(tested, points, compare_with_brute_force, print_problem);
		}
	}

	private static void testSolution(Function<Point[], Double> tested, Point[] points, boolean compare_with_brute_force, boolean print_problem) {
		if (print_problem) {
			System.out.println(points.length);
			for (Point pt : points) {
				System.out.println(pt.x + " " + pt.y);
			}
		}
		if (tested == null) {
			System.out.println("Result: " + solve(points));
		} else {
			if (compare_with_brute_force) {
				double expected = solve(points);
				double actual = tested.apply(points);
				if (Math.abs(expected - actual) >= 0.01) {
					System.out.println("Expected result: " + expected);
					System.out.println("Actual result: " + actual);
					tested.apply(points);
					throw new RuntimeException("Different results!");
				} else {
					System.out.println("Result: " + actual);
				}
			} else {
				double actual = tested.apply(points);
				System.out.println("Actual result: " + actual);
			}
		}
		System.out.println();
	}

	public static double solve(Point[] points) {
		double area = -1;
		Point[] bestArea = null;
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points.length; j++) {
				for (int k = 0; k < points.length; k++) {
					double newArea = area(points[i], points[j], points[k]);
					if (newArea > area) {
						area = newArea;
						bestArea = new Point[]{points[i], points[j], points[k]};
					}
				}
			}
		}
		System.out.println(Arrays.toString(bestArea));
		return area;
	}

	public static double area(Point pi, Point pj, Point pk) {
		return Math.abs(((pi.x * pj.y) - (pj.x * pi.y))
				+ ((pj.x * pk.y) - (pk.x * pj.y))
				+ ((pk.x * pi.y) - (pi.x * pk.y))) / 2.0;
	}
}
