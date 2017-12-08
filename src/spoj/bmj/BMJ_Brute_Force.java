package spoj.bmj;

import java.util.ArrayList;
import java.util.List;

// http://www.spoj.com/problems/BMJ/

public class BMJ_Brute_Force {

	public static void main(String[] args) {
		List<VisitedPoint> visited = new ArrayList<>();
		visited.add(new VisitedPoint(0, 0));
		for (int level = 1; level < 100; level++) {
			generateLevel(level, visited);
		}
		for (int i = 0; i < visited.size(); i++) {
			int stepIndex = i + 1;
			VisitedPoint expected = visited.get(i);
			VisitedPoint actual = BMJ_Solution.getVisitedPoint(stepIndex);
			System.out.printf("%5d -> %8s -> %8s %n", stepIndex, expected.toString(), actual);
			if ((expected.x != actual.x) || (expected.y != actual.y)) {
				throw new RuntimeException("Different results!");
			}
		}
	}

	static void generateLevel(int level, List<VisitedPoint> visited) {

		int x = 0;
		int y = level;

		for (int i = 0; i < level; i++) {
			visited.add(new VisitedPoint(x, y));
			x--;
		}
		for (int i = 0; i < level; i++) {
			visited.add(new VisitedPoint(x, y));
			y--;
		}
		for (int i = 0; i < level; i++) {
			visited.add(new VisitedPoint(x, y));
			x++;
			y--;
		}
		for (int i = 0; i < level; i++) {
			visited.add(new VisitedPoint(x, y));
			x++;
		}
		for (int i = 0; i < (level + 1); i++) {
			visited.add(new VisitedPoint(x, y));
			y++;
		}
		for (int i = 0; i < level; i++) {
			visited.add(new VisitedPoint(x, y));
			y++;
			x--;
		}
	}
}
