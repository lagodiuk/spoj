package spoj.dynamic_programming.trstage;

// http://www.spoj.com/problems/TRSTAGE/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TRSTAGE_1 {

	public static void main(String[] args) {
		solve(new int[]{3, 1, 2}, 1, 4, new Road[]{
				new Road(1, 2, 10),
				new Road(2, 3, 30),
				new Road(3, 4, 20),
		});

		solve(new int[]{3, 1}, 2, 1, new Road[]{
				new Road(2, 3, 3),
				new Road(1, 3, 3),
				new Road(4, 1, 2),
				new Road(4, 2, 5),
		});

		solve(new int[]{5, 5}, 4, 1, new Road[]{
				new Road(1, 2, 10),
				new Road(2, 3, 10),
				new Road(3, 4, 10),
		});

		solve(new int[]{1, 2}, 1, 2, new Road[]{});

		solve(new int[]{2, 7, 1, 8, 4, 5, 6, 3}, 1, 5, new Road[]{
				new Road(1, 2, 5),
				new Road(2, 3, 4),
				new Road(3, 4, 7),
				new Road(4, 5, 3),
				new Road(1, 3, 25),
				new Road(2, 4, 23),
				new Road(3, 5, 22),
				new Road(1, 4, 45),
				new Road(2, 5, 51),
				new Road(1, 5, 99),
		});
	}

	static void solve(int[] ticketSpeed, int startCity, int destinationCity, Road[] roads) {
		Map<Integer, Map<Integer, Integer>> cityDistanceIndex = buildCityDistancesIndex(roads);

		PriorityQueue pq = new PriorityQueue();
		pq.enqueue(new State(startCity, 0), Rational.ZERO);

		while (!pq.isEmpty()) {

			Rational currDist = pq.getMinDist();
			State currState = pq.getMinDistState();
			int currCity = currState.city;
			int usedTicketsMask = currState.usedTicketsMask;

			if (currCity == destinationCity) {
				System.out.println(currDist.value());
				return;
			}

			Map<Integer, Integer> accessibleCities = cityDistanceIndex.get(currCity);
			if (accessibleCities == null) {
				continue;
			}

			for (Integer city : accessibleCities.keySet()) {
				int cityDist = accessibleCities.get(city);

				for (int b = 0; b < ticketSpeed.length; b++) {
					if ((usedTicketsMask & (1 << b)) == 0) {

						State nextState = new State(city, usedTicketsMask | (1 << b));
						Rational nextStateDist = pq.distance(nextState);
						Rational newNextStateDist = currDist.add(new Rational(cityDist, ticketSpeed[b]));

						if (nextStateDist.greater(newNextStateDist)) {
							pq.enqueue(nextState, newNextStateDist);
						}
					}
				}
			}
		}

		System.out.println("Impossible");
	}

	private static Map<Integer, Map<Integer, Integer>> buildCityDistancesIndex(Road[] roads) {
		Map<Integer, Map<Integer, Integer>> adjacencyMatrix = new HashMap<>();
		for (Road road : roads) {
			if (!adjacencyMatrix.containsKey(road.cityA)) {
				adjacencyMatrix.put(road.cityA, new HashMap<>());
			}
			if (!adjacencyMatrix.containsKey(road.cityB)) {
				adjacencyMatrix.put(road.cityB, new HashMap<>());
			}
			adjacencyMatrix.get(road.cityA).put(road.cityB, road.dist);
			adjacencyMatrix.get(road.cityB).put(road.cityA, road.dist);
		}
		return adjacencyMatrix;
	}

	static class PriorityQueue {

		private Map<State, Rational> stateDist = new HashMap<>();
		private Map<State, Integer> stateIndex = new HashMap<>();
		private List<State> queue = new ArrayList<>();

		public Rational distance(State state) {
			return this.stateDist.getOrDefault(state, Rational.INFINITY);
		}

		private Rational distance(int stateIdx) {
			return this.stateDist.get(this.queue.get(stateIdx));
		}

		public boolean isEmpty() {
			return this.queue.isEmpty();
		}

		public Rational getMinDist() {
			return this.stateDist.get(this.queue.get(0));
		}

		public State getMinDistState() {
			State minDistState = this.queue.get(0);
			this.swap(0, this.queue.size() - 1);
			this.queue.remove(this.queue.size() - 1);
			this.pushDown(0);
			this.stateIndex.remove(minDistState);
			return minDistState;
		}

		public void enqueue(State state, Rational dist) {
			int stateIdx;
			if (this.enqueued(state)) {
				stateIdx = this.stateIndex.get(state);
			} else {
				this.queue.add(state);
				stateIdx = this.queue.size() - 1;
				this.stateIndex.put(state, stateIdx);
			}

			Rational existingDist = this.distance(state);
			this.stateDist.put(state, dist);
			if (dist.greater(existingDist)) {
				this.pushDown(stateIdx);
			} else {
				this.pushUp(stateIdx);
			}
		}

		private boolean enqueued(State state) {
			return this.stateIndex.containsKey(state);
		}

		private void pushDown(int curr) {
			int minIdx = this.leftChild(curr);
			if (minIdx >= this.queue.size()) {
				return;
			}

			int rightIdx = this.rightChild(curr);
			if ((rightIdx < this.queue.size())
					&& this.distance(minIdx).greater(this.distance(rightIdx))) {
				minIdx = rightIdx;
			}

			if (this.distance(curr).greater(this.distance(minIdx))) {
				this.swap(curr, minIdx);
				this.pushDown(minIdx);
			}
		}

		private void pushUp(int curr) {
			if (curr == 0) {
				return;
			}

			int parentIdx = this.parent(curr);
			if (this.distance(parentIdx).greater(this.distance(curr))) {
				this.swap(parentIdx, curr);
				this.pushUp(parentIdx);
			}
		}

		private int leftChild(int curr) {
			return (curr * 2) + 1;
		}

		private int rightChild(int curr) {
			return (curr * 2) + 2;
		}

		private int parent(int curr) {
			return (curr - 1) / 2;
		}

		private void swap(int a, int b) {
			State stateA = this.queue.get(a);
			State stateB = this.queue.get(b);

			this.queue.set(a, stateB);
			this.stateIndex.put(stateB, a);

			this.queue.set(b, stateA);
			this.stateIndex.put(stateA, b);
		}
	}

	static class State {

		int city;
		int usedTicketsMask;

		public State(int city, int usedTicketsMask) {
			this.city = city;
			this.usedTicketsMask = usedTicketsMask;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + this.city;
			result = (prime * result) + this.usedTicketsMask;
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
			if (this.usedTicketsMask != other.usedTicketsMask) {
				return false;
			}
			return true;
		}
	}

	static class Road {

		// No two roads connect the same pair of cities.
		// A road never connects a city with itself.
		// Each road can be traveled in both directions.

		int cityA;
		int cityB;
		int dist;

		public Road(int cityA, int cityB, int dist) {
			this.cityA = cityA;
			this.cityB = cityB;
			this.dist = dist;
		}
	}

	static class Rational {

		public static final Rational INFINITY = new Rational(1, 0);
		public static final Rational ZERO = new Rational(0, 1);

		private final int num; // numerator
		private final int denom; // denominator

		public Rational(int num, int denom) {
			int gcd = this.gcd(num, denom);
			this.num = num / gcd;
			this.denom = denom / gcd;
		}

		private int gcd(int a, int b) {
			while (b != 0) {
				int tmp = b;
				b = a % b;
				a = tmp;
			}
			return a;
		}

		public double value() {
			return (double) this.num / this.denom;
		}

		public Rational add(Rational other) {
			return new Rational(
					(this.num * other.denom) + (other.num * this.denom),
					this.denom * other.denom);
		}

		public boolean greater(Rational other) {
			return (this.num * other.denom) > (other.num * this.denom);
		}

		@Override
		public String toString() {
			return this.num + "/" + this.denom;
		}
	}
}
