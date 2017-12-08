package spoj.dynamic_programming.fcandy;

// http://www.spoj.com/problems/FCANDY/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FCANDY_9_Dijkstra_Optimized {

	private static final int INFINITY = 1000000000;

	private static final int MAX_I = 100;
	private static final int MAX_COUNT = 50;
	private static final int MAX_CALORIES = 50;

	public static void main(String[] args) {
		new MinPriorityQueue();
		FCANDY_1_Top_Down.evaluateExample(FCANDY_9_Dijkstra_Optimized::solve1);
		FCANDY_1_Top_Down.evaluateRandomized(FCANDY_9_Dijkstra_Optimized::solve1, true, 6);
		FCANDY_1_Top_Down.evaluateRandomized(FCANDY_9_Dijkstra_Optimized::solve1, false, MAX_I, MAX_COUNT, MAX_CALORIES);

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

		static final int[][] stateToIndex = new int[MAX_I + 1][MAX_TOTAL_CALORIES];
		static final int[][] stateToPriority = new int[MAX_I + 1][MAX_TOTAL_CALORIES];

		static final void clear() {
			queueSize = 0;
			for (int i = 0; i < MAX_I; i++) {
				Arrays.fill(stateToIndex[i], -1);
				Arrays.fill(stateToPriority[i], -1);
			}
		}

		static final boolean isEmpty() {
			return queueSize == 0;
		}

		static final State get() {
			State result = queue[0];

			swap(0, queueSize - 1);
			queueSize--;
			stateToIndex[result.i + 1][result.calories] = -1;
			pushDown(0);

			return result;
		}

		static final void addOrUpdate(int i, int calories, int prio) {

			if (stateToIndex[i + 1][calories] == -1) {

				if (queue[queueSize] == null) {
					queue[queueSize] = new State(i, calories);
				} else {
					queue[queueSize].i = i;
					queue[queueSize].calories = calories;
				}

				queueSize++;
				stateToIndex[i + 1][calories] = queueSize - 1;
				stateToPriority[i + 1][calories] = prio;
				pushUp(queueSize - 1);

			} else {

				int existingPrio = stateToPriority[i + 1][calories];
				int curr = stateToIndex[i + 1][calories];

				stateToPriority[i + 1][calories] = prio;

				if (existingPrio > prio) {
					pushUp(curr);
				} else if (existingPrio < prio) {
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
			return stateToPriority[s.i + 1][s.calories];
		}

		static final int prio(int i, int calories) {
			int prio = stateToPriority[i + 1][calories];
			return prio == -1 ? INFINITY : prio;
		}

		static final void swap(int i, int j) {
			State stI = queue[i];
			State stJ = queue[j];

			queue[i] = stJ;
			stateToIndex[stJ.i + 1][stJ.calories] = i;

			queue[j] = stI;
			stateToIndex[stI.i + 1][stI.calories] = j;
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
