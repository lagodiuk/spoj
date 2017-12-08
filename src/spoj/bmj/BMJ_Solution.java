package spoj.bmj;

// http://www.spoj.com/problems/BMJ/

public class BMJ_Solution {

	public static void main(String[] args) {
		System.out.println(getVisitedPoint(1));
		System.out.println(getVisitedPoint(2));
		System.out.println(getVisitedPoint(3));
		System.out.println(getVisitedPoint(4));
		System.out.println(getVisitedPoint(5));
		System.out.println(getVisitedPoint(6));
		System.out.println(getVisitedPoint(7));
		System.out.println(getVisitedPoint(8));
		System.out.println(getVisitedPoint(9));
		System.out.println(getVisitedPoint(19));
		System.out.println(getVisitedPoint(20));
		System.out.println(getVisitedPoint(21));
		System.out.println(getVisitedPoint(22));
	}

	/**
	 * Let's introduce the levels of moving:
	 * Level 0) Starting from the point (0, 0) - 1 move up (move number 1)
	 * Level 1) Then move: 1L 1D 1Rd 1R 2U 1Lu - 7 moves number: 2 to 8
	 * Level 2) Then move: 2L 2D 2Rd 2R 3U 2Lu - 13 moves number: 9 to 21
	 * Level 3) Then move: 3L 3D 3Rd 3R 4U 3Lu - 19 moves number: 22 to 40
	 * ...
	 * Level k) Then move: kL kD kRd kR (k+1)U kLu - (6k + 1) moves
	 *
	 * L - move one step left
	 * D - move one step down
	 * Rd - move one step right and down (diagonal)
	 * R - move one step right
	 * U - move one step up
	 * Lu - move one step left and up (diagonal)
	 *
	 * Sum of all numbers (6k+1) for k from 0 to n is: (3n^2 + 4n + 1)
	 *
	 * So, we can calculate the greatest level n, which is not greater then given step index,
	 * and the current step belongs to the level (n+1). And, finally, we can infer the move, to
	 * which
	 * the step index belongs. And, having the move, we can get the proper coordinate.
	 */
	static VisitedPoint getVisitedPoint(int stepIndex) {
		stepIndex--;
		int n = (int) Math.floor((-4.0 + Math.sqrt(4 + (12 * stepIndex))) / 6);
		int delt = stepIndex - ((3 * n * n) + (4 * n) + 1);
		if (delt == 0) {
			return new VisitedPoint(0, n + 1);
		}
		if (delt <= (n + 1)) { // Left
			return new VisitedPoint(-delt, n + 1);
		}
		if (delt <= (2 * (n + 1))) { // Down
			return new VisitedPoint(-(n + 1), (n + 1) - (delt - (n + 1)));
		}
		if (delt <= (3 * (n + 1))) { // Right down
			return new VisitedPoint(-(n + 1) + (delt - (2 * (n + 1))), 0 - (delt - (2 * (n + 1))));
		}
		if (delt <= (4 * (n + 1))) { // Right
			return new VisitedPoint(0 + (delt - (3 * (n + 1))), -(n + 1));
		}
		if (delt <= ((5 * (n + 1)) + 1)) { // Up
			return new VisitedPoint((n + 1), -(n + 1) + (delt - (4 * (n + 1))));
		}
		// Up left
		return new VisitedPoint((n + 1) - (delt - ((5 * (n + 1)) + 1)), 1 + (delt - ((5 * (n + 1)) + 1)));
	}
}
