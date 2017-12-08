package spoj.dynamic_programming.fcandy;

// http://www.spoj.com/problems/FCANDY/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FCANDY_11_Dijkstra_Another_One {

	private static final int INFINITY = 1000000000;

	private static final int MAX_I = 100;
	private static final int MAX_COUNT = 50;
	private static final int MAX_CALORIES = 50;

	public static void main(String[] args) {
		new MinPriorityQueue();
		FCANDY_1_Top_Down.evaluateExample(FCANDY_11_Dijkstra_Another_One::solve1);
		FCANDY_1_Top_Down.evaluateRandomized(FCANDY_11_Dijkstra_Another_One::solve1, true, 6);
		FCANDY_1_Top_Down.evaluateRandomized(FCANDY_11_Dijkstra_Another_One::solve1, false, MAX_I, MAX_COUNT, MAX_CALORIES);

		for (int x : visitedStatesCountList) {
			System.out.printf("%8d", x);
		}
	}

	static List<Integer> visitedStatesCountList = new ArrayList<>();

	static final int solve1(Candies[] candies) {

		int totalCalories = calculateTotalalories(candies);

		Arrays.sort(candies, (c1, c2) -> Integer.compare(c2.calories * c2.count, c1.calories * c1.count));

		int[] maxSumExtra = new int[candies.length];
		maxSumExtra[candies.length - 1] = candies[candies.length - 1].calories * candies[candies.length - 1].count;
		for (int i = candies.length - 2; i >= 0; i--) {
			maxSumExtra[i] = maxSumExtra[i + 1] + (candies[i].calories * candies[i].count);
		}

		MinPriorityQueue.clear();
		MinPriorityQueue.addOrUpdate(-1, 0, INFINITY);

		int result = INFINITY;

		int visitedStatesCount = 0;

		while (!MinPriorityQueue.isEmpty()) {

			visitedStatesCount++;

			State currState = MinPriorityQueue.get();
			int currStateI = currState.i;
			int currStateCalories = currState.calories;

			if (currStateI == (candies.length - 1)) {
				int currResult = totalCalories - (2 * currStateCalories);
				result = Math.min(result, currResult);
				if (result == 0) {
					return result;
				}
				continue;
			}

			if ((totalCalories - (2 * (currStateCalories + maxSumExtra[currStateI + 1]))) >= result) {
				// If current state can't lead to the better solution
				continue;
			}

			for (int cnt = 0; cnt <= candies[currStateI + 1].count; cnt++) {

				int newCalories =
						currStateCalories + (candies[currStateI + 1].calories * cnt);

				if ((2 * newCalories) <= totalCalories) { // If new state is allowed

					if (((currStateI + 1) != (candies.length - 1))
							&& ((totalCalories - (2 * (newCalories + maxSumExtra[currStateI + 2]))) >= result)) {
						// If new state can't lead to the better solution
						continue;
					}

					int newPriority = (totalCalories - (2 * newCalories));

					if (newPriority < MinPriorityQueue.prio(currStateI + 1, newCalories)) {

						MinPriorityQueue.addOrUpdate(currStateI + 1, newCalories, newPriority);
					}
				}
			}
		}

		visitedStatesCountList.add(visitedStatesCount);
		return result;
	}

	static final int calculateTotalalories(Candies[] candies) {
		int totalCalories = 0;
		for (Candies ca : candies) {
			totalCalories += ca.calories * ca.count;
		}
		return totalCalories;
	}

	static final class MinPriorityQueue {

		static final int MAX_TOTAL_CALORIES = ((MAX_COUNT * MAX_CALORIES * MAX_I) / 2) + 1;

		static final State[] queue = new State[MAX_I * MAX_TOTAL_CALORIES];
		static int queueSize = 0;

		static final Map<Integer, Map<Integer, Integer>> stateToIndex = new HashMap<>();
		static final Map<Integer, Map<Integer, Integer>> stateToPriority = new HashMap<>();
		static {
			for (int i = -1; i <= (MAX_I + 1); i++) {
				stateToIndex.put(i, new HashMap<>(MAX_TOTAL_CALORIES));
				stateToPriority.put(i, new HashMap<>(MAX_TOTAL_CALORIES));
			}
		}

		static final void clear() {
			queueSize = 0;
			for (int i = -1; i <= MAX_I; i++) {
				stateToIndex.get(i).clear();
				stateToPriority.get(i).clear();
			}
		}

		static final boolean isEmpty() {
			return queueSize == 0;
		}

		static final State get() {
			State result = queue[0];

			swap(0, queueSize - 1);
			queueSize--;
			stateToIndex.get(result.i).remove(result.calories);
			pushDown(0);

			return result;
		}

		static final void addOrUpdate(int i, int calories, int prio) {

			if (!stateToIndex.get(i).containsKey(calories)) {

				if (queue[queueSize] == null) {
					queue[queueSize] = new State(i, calories);
				} else {
					queue[queueSize].i = i;
					queue[queueSize].calories = calories;
				}

				queueSize++;
				stateToIndex.get(i).put(calories, queueSize - 1);
				stateToPriority.get(i).put(calories, prio);
				pushUp(queueSize - 1);

			} else {

				int existingPrio = stateToPriority.get(i).get(calories);

				if (existingPrio > prio) {
					stateToPriority.get(i).put(calories, prio);
					int curr = stateToIndex.get(i).get(calories);
					pushUp(curr);
				} else if (existingPrio < prio) {
					stateToPriority.get(i).put(calories, prio);
					int curr = stateToIndex.get(i).get(calories);
					pushDown(curr);
					throw new RuntimeException("This branch should not be executed!");
				}
			}
		}

		static final int left(int curr) {
			return (curr * 2) + 1;
		}

		static final int right(int curr) {
			return (curr * 2) + 2;
		}

		static final int parent(int curr) {
			return (curr - 1) / 2;
		}

		static final void pushDown(int curr) {
			while (true) {
				int min = left(curr);
				if (min >= queueSize) {
					return;
				}

				int right = right(curr);
				if ((right < queueSize)
						&& (prio(right) < prio(min))) {

					min = right;
				}

				if (prio(min) < prio(curr)) {
					swap(curr, min);
					curr = min;
				} else {
					break;
				}
			}
		}

		static final void pushUp(int curr) {
			while (curr != 0) {
				int parent = parent(curr);

				if (prio(curr) < prio(parent)) {
					swap(curr, parent);
					curr = parent;
				} else {
					break;
				}
			}
		}

		static final int prio(int curr) {
			State s = queue[curr];
			return stateToPriority.get(s.i).get(s.calories);
		}

		static final int prio(int i, int calories) {
			Integer prio = stateToPriority.get(i).get(calories);
			return prio == null ? INFINITY : prio;
		}

		static final void swap(int i, int j) {
			State stI = queue[i];
			State stJ = queue[j];

			queue[i] = stJ;
			stateToIndex.get(stJ.i).put(stJ.calories, i);

			queue[j] = stI;
			stateToIndex.get(stI.i).put(stI.calories, j);
		}
	}

	static final class State {

		int i;
		int calories;

		public State(int i, int calories) {
			this.i = i;
			this.calories = calories;
		}

		@Override
		public String toString() {
			return "(" + this.i + ", " + this.calories + ")";
		}
	}
}
