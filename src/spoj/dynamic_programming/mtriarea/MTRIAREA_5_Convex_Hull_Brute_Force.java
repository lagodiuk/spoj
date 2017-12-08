package spoj.dynamic_programming.mtriarea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

// http://www.spoj.com/problems/MTRIAREA/

public class MTRIAREA_5_Convex_Hull_Brute_Force {

	public static void main(String[] args) {
		MTRIAREA_1_Brute_Force.test(MTRIAREA_5_Convex_Hull_Brute_Force::solve, true, 50, true);
		// MTRIAREA_1_Brute_Force.test(MTRIAREA_5_Convex_Hull_Brute_Force::solve, false, 10000,
		// false);
	}

	public static double solve(Point[] points) {
		points = build_convex_hull(points);
		System.out.println("Convex Hull: " + Arrays.toString(points));
		if (points.length < 3) {
			return 0;
		}
		double area = -1;
		for (int i = 0; i < (points.length - 2); i++) {
			for (int j = i + 1; j < (points.length - 1); j++) {
				int best_k = find_best_k_given_i_j(points, i, j);
				double curr_area = area(points[i], points[j], points[best_k]);
				area = Math.max(area, curr_area);
			}
		}
		return area;
	}

	// TODO: use ternary search here, in order to reduce complexity to O(log(N))
	private static int find_best_k_given_i_j(Point[] points, int i, int j) {
		double best_area_given_k = -1;
		int best_k = -1;
		for (int k = points.length - 1; k > j; k--) {
			double newArea = area(points[i], points[j], points[k]);
			if (newArea >= best_area_given_k) {
				best_area_given_k = newArea;
				best_k = k;
			} else {
				break;
			}
		}
		return best_k;
	}

	public static double area(Point pi, Point pj, Point pk) {
		return Math.abs(((pi.x * pj.y) - (pj.x * pi.y))
				+ ((pj.x * pk.y) - (pk.x * pj.y))
				+ ((pk.x * pi.y) - (pi.x * pk.y))) / 2.0;
	}

	static Point[] build_convex_hull(Point[] points) {
		Point leftmost_bottom = find_leftmost_bottom(points);
		Point[] new_points = filter_out_leftmost_bottom(points, leftmost_bottom);
		move_center_coord_to_leftmost_bottom(leftmost_bottom, new_points);
		sort_points(leftmost_bottom, new_points);
		List<Point> convex_hull = get_convex_hull(new_points);
		Point[] convex_hull_arr = convex_hull.toArray(new Point[0]);
		move_center_coord_to_leftmost_bottom(new Point(-leftmost_bottom.x, -leftmost_bottom.y), convex_hull_arr);
		return convex_hull_arr;
	}

	private static List<Point> get_convex_hull(Point[] new_points) {
		List<Point> convex_hull = new ArrayList<>();
		convex_hull.add(new Point(0, 0));
		for (int i = 0; i < new_points.length;) {
			if (convex_hull.size() <= 2) {
				convex_hull.add(new_points[i]);
				i++;
			} else {
				Point curr = new_points[i];
				Point top = convex_hull.get(convex_hull.size() - 1);
				if ((curr.x == top.x) && (curr.y == top.y)) {
					// ignore identical points
					i++;
					continue;
				}
				Point prev = convex_hull.get(convex_hull.size() - 2);
				Point v_top_prev = new Point(top.x - prev.x, top.y - prev.y);
				Point v_curr_top = new Point(curr.x - top.x, curr.y - top.y);
				int prod = pseudoscalar_product(v_top_prev, v_curr_top);
				if (prod >= 0) {
					convex_hull.add(curr);
					i++;
				} else {
					convex_hull.remove(convex_hull.size() - 1);
				}
			}
		}
		return convex_hull;
	}

	private static void sort_points(Point leftmost_bottom, Point[] new_points) {
		Arrays.sort(new_points, new Comparator<Point>() {
			@Override
			public int compare(Point a, Point b) {
				Point v1 = new Point(a.x, a.y);
				Point v2 = new Point(b.x, b.y);
				int prod = pseudoscalar_product(v1, v2);
				if (prod != 0) {
					return (prod > 0) ? -1 : 1;
				} else {
					int dist1 = ((leftmost_bottom.x - a.x) * (leftmost_bottom.x - a.x)) + ((leftmost_bottom.y - a.y) * (leftmost_bottom.y - a.y));
					int dist2 = ((leftmost_bottom.x - b.x) * (leftmost_bottom.x - b.x)) + ((leftmost_bottom.y - b.y) * (leftmost_bottom.y - b.y));
					return (dist1 <= dist2) ? -1 : 1;
				}
			}
		});
	}

	private static void move_center_coord_to_leftmost_bottom(Point leftmost_bottom, Point[] new_points) {
		for (int i = 0; i < new_points.length; i++) {
			new_points[i] = new Point(new_points[i].x - leftmost_bottom.x, new_points[i].y - leftmost_bottom.y);
		}
	}

	private static Point[] filter_out_leftmost_bottom(Point[] points, Point leftmost_bottom) {
		Point[] new_points = new Point[points.length - 1];
		int idx = 0;
		for (int i = 0; i < points.length; i++) {
			if (points[i] != leftmost_bottom) {
				new_points[idx] = points[i];
				idx++;
			}
		}
		return new_points;
	}

	static int pseudoscalar_product(Point a, Point b) {
		return (a.x * b.y) - (b.x * a.y);
	}

	static Point find_leftmost_bottom(Point[] points) {
		Point result = points[0];
		for (int i = 1; i < points.length; i++) {
			if (points[i].y < result.y) {
				result = points[i];
			} else if ((points[i].y == result.y) && (points[i].x < result.x)) {
				result = points[i];
			}
		}
		return result;
	}
}
