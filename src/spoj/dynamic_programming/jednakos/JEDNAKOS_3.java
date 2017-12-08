package spoj.dynamic_programming.jednakos;

import java.util.LinkedList;
import java.util.Queue;

// http://www.spoj.com/problems/JEDNAKOS/en/

public class JEDNAKOS_3 {

	public static void main(String[] args) {
		JEDNAKOS_Brute_Force.testManualCases(JEDNAKOS_3::solve);
		JEDNAKOS_Brute_Force.testRandomizedCases(JEDNAKOS_3::solve);
		JEDNAKOS_Brute_Force.testRandomizedCases(JEDNAKOS_3::solve, false, 1000, 100);
	}

	static final int INFINITY = 10000;

	static int solve(String digitsStr, int target) {
		int[] digits = toDigitsArray(digitsStr);

		boolean[][] visited = new boolean[digitsStr.length() + 1][target + 1];
		Queue<State> queue = new LinkedList<>();

		queue.add(new State(0, target, 0));

		int result = INFINITY;
		while (!queue.isEmpty()) {
			State curr = queue.remove();

			if (curr.currDigitPos == digits.length) {
				if (curr.targetSum == 0) {
					result = curr.dist;
					break;
				} else {
					continue;
				}
			}

			visited[curr.currDigitPos][curr.targetSum] = true;

			int currNum = digits[curr.currDigitPos];
			int len = 1;
			while ((currNum <= curr.targetSum)
					&& ((curr.currDigitPos + len) <= digits.length)) {

				if (((curr.targetSum - currNum) >= 0)
						&& !visited[curr.currDigitPos + len][curr.targetSum - currNum]) {

					queue.add(new State(curr.currDigitPos + len, curr.targetSum - currNum, curr.dist + 1));
					visited[curr.currDigitPos + len][curr.targetSum - currNum] = true;
				}

				if ((curr.currDigitPos + len) == digits.length) {
					break;
				}

				currNum = (currNum * 10) + digits[curr.currDigitPos + len];
				len++;
			}
		}

		return result - 1;
	}

	static class State {
		int currDigitPos;
		int targetSum;
		int dist;

		public State(int currDigitPos, int targetSum, int dist) {
			this.currDigitPos = currDigitPos;
			this.targetSum = targetSum;
			this.dist = dist;
		}

		@Override
		public String toString() {
			return "State [currDigitPos=" + this.currDigitPos + ", targetSum=" + this.targetSum + ", dist=" + this.dist + "]";
		}
	}

	private static int[] toDigitsArray(String digitsStr) {
		int[] digits = new int[digitsStr.length()];
		for (int i = 0; i < digitsStr.length(); i++) {
			digits[i] = digitsStr.charAt(i) - '0';
		}
		return digits;
	}
}
