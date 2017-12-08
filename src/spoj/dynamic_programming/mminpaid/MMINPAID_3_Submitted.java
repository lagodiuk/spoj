package spoj.dynamic_programming.mminpaid;

// http://www.spoj.com/problems/MMINPAID/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MMINPAID_3_Submitted {

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		int targetCity = sc.nextInt();
		Road[] roads = new Road[sc.nextInt()];
		for (int i = 0; i < roads.length; i++) {
			roads[i] = new Road(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
		}
		int result = solve(roads, targetCity);
		if (result != -1) {
			System.out.println(result);
		} else {
			System.out.println("impossible");
		}
	}

	static int solve(Road[] roads, int targetCity) {
		List[] cityToRoads = groupBySourceCity(roads, targetCity);

		initializeQueue();
		updateState(1, 0, 0);

		int result = MAX_COST;
		while (!queueIsEmpty()) {
			State currState = dequeue();
			int currCity = currState.city;
			int currVisited = currState.visited;
			int currCost = cost(currState.city, currState.visited);

			if (currCity == targetCity) {
				result = Math.min(result, currCost);
			}

			List<Road> outRoads = cityToRoads[currCity];

			for (Road rd : outRoads) {
				int cost = rd.costInTgt;

				if ((currVisited & (1 << rd.paymentCityInAdvance)) != 0) {
					cost = Math.min(cost, rd.costInAdvance);
				}

				if ((currCost + cost) < cost(rd.tgt, currVisited | (1 << rd.tgt))) {
					updateState(rd.tgt, currVisited | (1 << rd.tgt), currCost + cost);
				}
			}
		}

		return result == MAX_COST ? -1 : result;
	}

	private static List[] groupBySourceCity(Road[] roads, int tartegCity) {
		List[] cityToRoads = new List[tartegCity + 1];
		for (int i = 1; i <= tartegCity; i++) {
			cityToRoads[i] = new ArrayList<>(tartegCity);
		}
		for (Road road : roads) {
			cityToRoads[road.src].add(road);
		}
		return cityToRoads;
	}

	static final int MAX_COST = 1000000;
	static final int MAX_CITIES = 10;

	static final List<State> queue = new ArrayList<>(MAX_CITIES * ((1 << MAX_CITIES) + 1));
	static final int[][] stateToQueueIndex = new int[MAX_CITIES][(1 << (MAX_CITIES + 1)) + 1];
	static final int[][] stateToCost = new int[MAX_CITIES][(1 << (MAX_CITIES + 1)) + 1];

	static final private void initializeQueue() {
		queue.clear();
		for (int i = 0; i < MAX_CITIES; i++) {
			Arrays.fill(stateToCost[i], MAX_COST);
			Arrays.fill(stateToQueueIndex[i], -1);
		}
	}

	static final boolean queueIsEmpty() {
		return queue.isEmpty();
	}

	static final State dequeue() {
		swap(0, queue.size() - 1);
		State result = queue.remove(queue.size() - 1);
		stateToQueueIndex[result.city - 1][result.visited] = -1;
		pushDown(0);
		return result;
	}

	static final void updateState(int city, int visited, int newCost) {
		if (stateToQueueIndex[city - 1][visited] == -1) {
			State state = new State(city, visited);
			queue.add(state);
			stateToQueueIndex[state.city - 1][state.visited] = queue.size() - 1;
			stateToCost[state.city - 1][state.visited] = newCost;
			pushUp(queue.size() - 1);
		} else {

			int existingCost = stateToCost[city - 1][visited];
			stateToCost[city - 1][visited] = newCost;

			if (newCost < existingCost) {
				pushUp(stateToQueueIndex[city - 1][visited]);
			} else {
				pushDown(stateToQueueIndex[city - 1][visited]);
				throw new RuntimeException("Should not be executed in Dijkstra algorithm!");
			}
		}
	}

	static final void pushUp(int curr) {
		while (true) {
			if (curr == 0) {
				break;
			}

			int parent = parent(curr);
			if (cost(curr) < cost(parent)) {
				swap(curr, parent);
				curr = parent;
			} else {
				break;
			}
		}
	}

	static final void pushDown(int curr) {
		while (true) {
			int min = left(curr);
			if (min >= queue.size()) {
				break;
			}

			int right = right(curr);
			if ((right < queue.size())
					&& (cost(right) < cost(min))) {
				min = right;
			}

			if (cost(min) < cost(curr)) {
				swap(min, curr);
				curr = min;
			} else {
				break;
			}
		}
	}

	static final int left(int curr) {
		return (curr << 1) + 1;
	}

	static final int right(int curr) {
		return (curr << 1) + 2;
	}

	static final int parent(int curr) {
		return (curr - 1) / 2;
	}

	static final void swap(int i, int j) {
		State stateI = queue.get(i);
		State stateJ = queue.get(j);

		queue.set(j, stateI);
		stateToQueueIndex[stateI.city - 1][stateI.visited] = j;

		queue.set(i, stateJ);
		stateToQueueIndex[stateJ.city - 1][stateJ.visited] = i;
	}

	static final int cost(int idx) {
		State state = queue.get(idx);
		return stateToCost[state.city - 1][state.visited];
	}

	static final int cost(int city, int visited) {
		return stateToCost[city - 1][visited];
	}

	static class State {

		final int city;
		final int visited;

		public State(int city, int payedCities) {
			this.city = city;
			this.visited = payedCities;
		}

		@Override
		public String toString() {
			return "(" + this.city + ", " + Integer.toBinaryString(this.visited) + ")";
		}
	}

	static class Road {

		final int src;
		final int tgt;

		final int paymentCityInAdvance;

		final int costInAdvance;
		final int costInTgt;

		public Road(int src, int tgt, int paymentCityInAdvance, int costInAdvance, int costInTgt) {
			this.src = src;
			this.tgt = tgt;
			this.paymentCityInAdvance = paymentCityInAdvance;
			this.costInAdvance = costInAdvance;
			this.costInTgt = costInTgt;
		}

		@Override
		public String toString() {
			return "Road [src=" + this.src + ", tgt=" + this.tgt + ", paymentCityInAdvance=" + this.paymentCityInAdvance + ", costInAdvance="
					+ this.costInAdvance
					+ ", costInTgt="
					+ this.costInTgt + "]";
		}
	}
}
