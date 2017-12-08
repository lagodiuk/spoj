package spoj.dynamic_programming.fcandy;

// http://www.spoj.com/problems/FCANDY/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FCANDY_7_Dijkstra {

	private static final int INFINITY = 1000000000;

	public static void main(String[] args) {
		FCANDY_1_Top_Down.evaluateExample(FCANDY_7_Dijkstra::solve1);
		FCANDY_1_Top_Down.evaluateRandomized(FCANDY_7_Dijkstra::solve1, true, 6);
		FCANDY_1_Top_Down.evaluateRandomized(FCANDY_7_Dijkstra::solve1, false, 50);
	}

	static int solve1(Candies[] candies) {
		int totalCalories = calculateTotalalories(candies);

		MinPriorityQueue pq = new MinPriorityQueue();
		pq.addOrUpdate(new State(-1, 0), INFINITY);

		int result = INFINITY;

		while (!pq.isEmpty()) {

			State currState = pq.get();

			if (currState.i == (candies.length - 1)) {
				result = Math.min(result, totalCalories - (2 * currState.calories));
				if (result == 0) {
					return result;
				}
				continue;
			}

			for (int cnt = 0; cnt <= candies[currState.i + 1].count; cnt++) {

				int newCalories =
						currState.calories + (candies[currState.i + 1].calories * cnt);

				State newState = new State(currState.i + 1, newCalories);

				int newPriority = totalCalories - (2 * newCalories);

				if (((2 * newCalories) <= totalCalories) && (newPriority < pq.prio(newState))) {
					pq.addOrUpdate(newState, newPriority);
				}
			}
		}

		return result;
	}

	private static int calculateTotalalories(Candies[] candies) {
		int totalCalories = 0;
		for (Candies ca : candies) {
			totalCalories += ca.calories * ca.count;
		}
		return totalCalories;
	}

	static class MinPriorityQueue {

		List<State> queue = new ArrayList<>();
		Map<State, Integer> stateToIndex = new HashMap<>();
		Map<State, Integer> stateToPriority = new HashMap<>();

		boolean isEmpty() {
			return this.queue.isEmpty();
		}

		State get() {
			State result = this.queue.get(0);

			this.swap(0, this.queue.size() - 1);
			this.queue.remove(this.queue.size() - 1);
			this.stateToIndex.remove(result);
			this.pushDown(0);

			return result;
		}

		void addOrUpdate(State s, int prio) {

			if (!this.stateToIndex.containsKey(s)) {

				this.queue.add(s);
				this.stateToIndex.put(s, this.queue.size() - 1);
				this.stateToPriority.put(s, prio);
				this.pushUp(this.queue.size() - 1);

			} else {

				int existingPrio = this.stateToPriority.get(s);
				int curr = this.stateToIndex.get(s);

				this.stateToPriority.put(s, prio);

				if (existingPrio > prio) {
					this.pushUp(curr);
				} else if (existingPrio < prio) {
					this.pushDown(curr);
					throw new RuntimeException("This branch should not be executed!");
				}
			}
		}

		int left(int curr) {
			return (curr * 2) + 1;
		}

		int right(int curr) {
			return (curr * 2) + 2;
		}

		int parent(int curr) {
			return (curr - 1) / 2;
		}

		void pushDown(int curr) {

			int min = this.left(curr);
			if (min >= this.queue.size()) {
				return;
			}

			int right = this.right(curr);
			if ((right < this.queue.size())
					&& (this.prio(right) < this.prio(min))) {

				min = right;
			}

			if (this.prio(min) < this.prio(curr)) {
				this.swap(curr, min);
				this.pushDown(min);
			}
		}

		void pushUp(int curr) {
			if (curr == 0) {
				return;
			}

			int parent = this.parent(curr);

			if (this.prio(curr) < this.prio(parent)) {
				this.swap(curr, parent);
				this.pushUp(parent);
			}
		}

		int prio(int curr) {
			return this.stateToPriority.get(this.queue.get(curr));
		}

		int prio(State s) {
			Integer prio = this.stateToPriority.get(s);
			return prio == null ? INFINITY : prio;
		}

		void swap(int i, int j) {
			State stI = this.queue.get(i);
			State stJ = this.queue.get(j);

			this.queue.set(i, stJ);
			this.stateToIndex.put(stJ, i);

			this.queue.set(j, stI);
			this.stateToIndex.put(stI, j);
		}
	}

	static class State {
		int i;
		int calories;

		public State(int i, int x) {
			this.i = i;
			this.calories = x;
		}

		@Override
		public String toString() {
			return "(" + this.i + ", " + this.calories + ")";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + this.i;
			result = (prime * result) + this.calories;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (this.getClass() != obj.getClass()) {
				return false;
			}
			State other = (State) obj;
			if (this.i != other.i) {
				return false;
			}
			if (this.calories != other.calories) {
				return false;
			}
			return true;
		}
	}
}
