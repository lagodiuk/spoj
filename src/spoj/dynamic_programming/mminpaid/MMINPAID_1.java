package spoj.dynamic_programming.mminpaid;

// http://www.spoj.com/problems/MMINPAID/

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiFunction;

public class MMINPAID_1 {

	public static void main(String[] args) {
		exampleFromDescriptionOfProblem(MMINPAID_1::solve);
		testRandomized1(MMINPAID_1::solve);
		testRandomized2(MMINPAID_1::solve);
	}

	static void testRandomized2(BiFunction<Road[], Integer, Integer> tested) {
		Random rnd = new Random(1);

		for (int i = 0; i < 100; i++) {
			int citiesCount = rnd.nextInt(10) + 1;
			int roadsCnt = Math.min(10, citiesCount + rnd.nextInt(11 - citiesCount));

			List<Road> roadsList = new ArrayList<>();
			for (int j = 2; j <= citiesCount; j++) {
				roadsList.add(new Road(j - 1, j, rnd.nextInt(citiesCount) + 1, rnd.nextInt(101), rnd.nextInt(101)));
			}

			while (roadsList.size() < roadsCnt) {
				roadsList.add(new Road(rnd.nextInt(citiesCount) + 1, rnd.nextInt(citiesCount) + 1, rnd.nextInt(citiesCount) + 1, rnd.nextInt(101), rnd
						.nextInt(101)));

			}
			Collections.shuffle(roadsList);

			Road[] roads = roadsList.toArray(new Road[0]);

			compareResults(roads, citiesCount, tested);
		}
	}

	static void testRandomized1(BiFunction<Road[], Integer, Integer> tested) {
		Random rnd = new Random(1);

		for (int i = 0; i < 100; i++) {
			int citiesCount = rnd.nextInt(10) + 1;
			Road[] roads = new Road[Math.min(10, citiesCount + rnd.nextInt(11 - citiesCount))];
			for (int j = 0; j < roads.length; j++) {
				roads[j] = new Road(rnd.nextInt(citiesCount) + 1, rnd.nextInt(citiesCount) + 1, rnd.nextInt(citiesCount) + 1, rnd.nextInt(101), rnd
						.nextInt(101));
			}

			compareResults(roads, citiesCount, tested);
		}
	}

	static void exampleFromDescriptionOfProblem(BiFunction<Road[], Integer, Integer> tested) {
		/**
		 * 4 5
		 * 1 2 1 10 10
		 * 2 3 1 30 50
		 * 3 4 3 80 80
		 * 2 1 2 10 10
		 * 1 3 2 10 50
		 *
		 * Expected output is: 110
		 */
		compareResults(new Road[]{
				new Road(1, 2, 1, 10, 10),
				new Road(2, 3, 1, 30, 50),
				new Road(3, 4, 3, 80, 80),
				new Road(2, 1, 2, 10, 10),
				new Road(1, 3, 2, 10, 50)
		}, 4, tested);
	}

	static void compareResults(Road[] roads, int tartegCity, BiFunction<Road[], Integer, Integer> tested) {
		System.out.println(tartegCity + " " + roads.length);
		for (Road rd : roads) {
			System.out.println(rd.src + " " + rd.tgt + " " + rd.paymentCityInAdvance + " " + rd.costInAdvance + " " + rd.costInTgt);
		}
		int expectedResult = solve(roads, tartegCity);
		int testedResult = tested.apply(roads, tartegCity);
		if (testedResult != expectedResult) {
			System.out.println("Expected result = " + expectedResult);
			System.out.println("Tested result = " + testedResult);
			throw new RuntimeException("Different results!");
		}
		System.out.println("Result = " + testedResult);
		System.out.println();
	}

	static int solve(Road[] roads, int tartegCity) {
		Map<Integer, List<Road>> cityToRoads = groupBySourceCity(roads);
		Map<Integer, List<Road>> payInAdvanceCityToRoads = groupByPayInAdvanceCity(roads);

		MinPriorityQueue pq = new MinPriorityQueue();
		pq.updateState(new State(1, new BitSet(tartegCity + 1)), 0);

		int result = Integer.MAX_VALUE;

		while (!pq.isEmpty()) {
			State currState = pq.dequeue();
			int currCity = currState.city;
			BitSet currVisitedCities = currState.visitedCities;
			int currCost = pq.cost(currState);

			if (currCity == tartegCity) {
				result = Math.min(result, currCost);
				continue;
			}

			List<Road> payInAdvance = payInAdvanceCityToRoads.get(currCity);
			if (payInAdvance == null) {
				payInAdvance = Collections.emptyList();
			}

			List<Road> outRoads = cityToRoads.get(currCity);
			if (outRoads == null) {
				outRoads = Collections.emptyList();
			}

			for (Road rd : outRoads) {

				int tgtCity = rd.tgt;

				BitSet newVisitedCities = (BitSet) currVisitedCities.clone();
				newVisitedCities.set(tgtCity);
				int cost = rd.costInTgt;

				if (currVisitedCities.get(rd.paymentCityInAdvance)) {
					cost = Math.min(cost, rd.costInAdvance);
				}

				int newCost = currCost + cost;

				State newState = new State(tgtCity, newVisitedCities);
				if (pq.cost(newState) > newCost) {
					pq.updateState(newState, newCost);
				}
			}
		}

		return (result == Integer.MAX_VALUE) ? -1 : result;
	}

	private static Map<Integer, List<Road>> groupBySourceCity(Road[] roads) {
		Map<Integer, List<Road>> cityToRoads = new HashMap<>();
		for (Road road : roads) {
			List<Road> bucket = cityToRoads.get(road.src);
			if (bucket == null) {
				bucket = new ArrayList<>();
				cityToRoads.put(road.src, bucket);
			}
			bucket.add(road);
		}
		return cityToRoads;
	}

	private static Map<Integer, List<Road>> groupByPayInAdvanceCity(Road[] roads) {
		Map<Integer, List<Road>> cityToRoads = new HashMap<>();
		for (Road road : roads) {
			List<Road> bucket = cityToRoads.get(road.paymentCityInAdvance);
			if (bucket == null) {
				bucket = new ArrayList<>();
				cityToRoads.put(road.paymentCityInAdvance, bucket);
			}
			bucket.add(road);
		}
		return cityToRoads;
	}

	static class State {

		final int city;
		final BitSet visitedCities;

		public State(int city, BitSet payedCities) {
			this.city = city;
			this.visitedCities = payedCities;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + this.city;
			result = (prime * result) + ((this.visitedCities == null) ? 0 : this.visitedCities.hashCode());
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
			if (this.city != other.city) {
				return false;
			}
			if (this.visitedCities == null) {
				if (other.visitedCities != null) {
					return false;
				}
			} else if (!this.visitedCities.equals(other.visitedCities)) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return "(" + this.city + ", " + this.visitedCities + ")";
		}
	}

	static class MinPriorityQueue {

		static final int MAX_COST = 10000000;

		List<State> queue = new ArrayList<>();
		Map<State, Integer> stateToQueueIndex = new HashMap<>();
		Map<State, Integer> stateToCost = new HashMap<>();

		boolean isEmpty() {
			return this.queue.isEmpty();
		}

		State dequeue() {
			this.swap(0, this.queue.size() - 1);
			State result = this.queue.remove(this.queue.size() - 1);
			this.stateToQueueIndex.remove(result);
			this.pushDown(0);
			return result;
		}

		void updateState(State state, int newCost) {
			if (!this.stateToQueueIndex.containsKey(state)) {
				this.queue.add(state);
				this.stateToQueueIndex.put(state, this.queue.size() - 1);
				this.stateToCost.put(state, newCost);
				this.pushUp(this.queue.size() - 1);
			} else {

				int existingCost = this.stateToCost.get(state);
				this.stateToCost.put(state, newCost);

				if (newCost < existingCost) {
					this.pushUp(this.stateToQueueIndex.get(state));
				} else {
					this.pushDown(this.stateToQueueIndex.get(state));
					throw new RuntimeException("Should not be executed in Dijkstra algorithm!");
				}
			}
		}

		void pushUp(int curr) {
			if (curr == 0) {
				return;
			}

			int parent = this.parent(curr);
			if (this.cost(curr) < this.cost(parent)) {
				this.swap(curr, parent);
				this.pushUp(parent);
			}
		}

		void pushDown(int curr) {
			int min = this.left(curr);
			if (min >= this.queue.size()) {
				return;
			}

			int right = this.right(curr);
			if ((right < this.queue.size())
					&& (this.cost(right) < this.cost(min))) {
				min = right;
			}

			if (this.cost(min) < this.cost(curr)) {
				this.swap(min, curr);
				this.pushDown(min);
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

		void swap(int i, int j) {
			State stateI = this.queue.get(i);
			State stateJ = this.queue.get(j);

			this.queue.set(j, stateI);
			this.stateToQueueIndex.put(stateI, j);

			this.queue.set(i, stateJ);
			this.stateToQueueIndex.put(stateJ, i);
		}

		int cost(int idx) {
			return this.stateToCost.get(this.queue.get(idx));
		}

		int cost(State state) {
			if (this.stateToCost.containsKey(state)) {
				return this.stateToCost.get(state);
			} else {
				return MAX_COST;
			}
		}

		@Override
		public String toString() {
			return this.queue.toString();
		}
	}
}
