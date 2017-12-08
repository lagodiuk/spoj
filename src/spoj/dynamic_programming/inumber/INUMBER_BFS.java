package spoj.dynamic_programming.inumber;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// http://www.spoj.com/problems/INUMBER/

public class INUMBER_BFS {

	public static void main(String[] args) {
		for (int i = 1; i <= 1000; i++) {
			System.out.println(i);
			solve(i);
			System.out.println();
		}
	}

	static final int MAX_NUM = 1001;
	static final int INF = 1000000;

	static int[][] digitsNum = new int[MAX_NUM][MAX_NUM];

	static void solve(int num) {
		// Initialization
		for (int[] x : digitsNum) {
			Arrays.fill(x, INF);
		}

		// Generating initial state
		State start = new State(num, 0);
		digitsNum[start.sum][start.mod] = 0;

		// BFS way of visiting the subproblems
		List<State> queue = new LinkedList<>();
		queue.add(start);

		State curr = null;
		while (!queue.isEmpty()) {
			// Next item from FIFO queue
			curr = queue.remove(0);

			// Do we reach the final state?
			// (sum of digits == num) AND (current number % num == 0)
			if ((curr.sum == 0) && (curr.mod == 0)) {
				break;
			}

			// Enumerating all states, which can be produced from the current state
			for (int i = 0; i <= 9; i++) {
				State s = new State(curr.sum - i, ((curr.mod * 10) + i) % num);

				// Is this state acceptable?
				if (s.sum >= 0) {
					// Have we already visited this state?
					if (digitsNum[s.sum][s.mod] > (digitsNum[curr.sum][curr.mod] + 1)) {

						// Update amount of digits
						digitsNum[s.sum][s.mod] = digitsNum[curr.sum][curr.mod] + 1;

						// Store reference to the parent state
						s.prev = curr;

						// Enqueue
						queue.add(s);
					}
				}
			}
		}

		// Backtracking
		StringBuilder sb = new StringBuilder();
		while (curr.prev != null) {
			sb.append(curr.prev.sum - curr.sum);
			curr = curr.prev;
		}
		System.out.println(sb.reverse().toString());
	}

	static class State {
		int sum;
		int mod;
		State prev;
		public State(int sum, int mod) {
			this.sum = sum;
			this.mod = mod;
			this.prev = null;
		}
	}
}
