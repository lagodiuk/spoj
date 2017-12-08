package spoj.dynamic_programming.trstage;

// http://www.spoj.com/problems/TRSTAGE/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TRSTAGE_4_Aggressive_Optimization {

	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int i = 0; i < 100; i++) {
			testRandomCase(rnd);
		}
		System.out.println("Total time: " + (executionTime / 1000.0) + " sec");
	}

	static long executionTime = 0;

	static void testRandomCase(Random rnd) {
		double sparsityCoefficient = 0.5;

		int ticketsNum = rnd.nextInt(8) + 1;
		int tickets[] = new int[ticketsNum];
		for (int i = 0; i < ticketsNum; i++) {
			tickets[i] = rnd.nextInt(10) + 1;
		}

		int citiesNum = rnd.nextInt(30) + 2;

		List<Road> roadsList = new ArrayList<>();
		for (int i = 0; i < citiesNum; i++) {
			for (int j = i + 1; j < citiesNum; j++) {
				if (rnd.nextDouble() > sparsityCoefficient) {
					if (rnd.nextDouble() > 0.5) {
						roadsList.add(new Road(i, j, rnd.nextInt(100) + 1));
					} else {
						roadsList.add(new Road(j, i, rnd.nextInt(100) + 1));
					}
				}
			}
		}

		Collections.shuffle(roadsList);

		int startCity = rnd.nextInt(citiesNum);
		int endCity = startCity;
		while (endCity == startCity) {
			endCity = rnd.nextInt(citiesNum);
		}
		Road[] roads = roadsList.toArray(new Road[0]);

		long startTime = System.currentTimeMillis();
		solve(tickets, startCity, endCity, roads);
		executionTime += System.currentTimeMillis() - startTime;
	}

	static final String IMPOSSIBLE = "Impossible";
	static final int MAX_CITIES = 32;
	static final int MAX_TICKETS = 8;
	static final PriorityQueue pq = new PriorityQueue(MAX_CITIES, MAX_TICKETS);
	static final List[] cityDistanceIndex = new List[MAX_CITIES];
	static {
		for (int i = 0; i < MAX_CITIES; i++) {
			cityDistanceIndex[i] = new ArrayList<>(MAX_CITIES);
		}
	}

	static void solve(int[] ticketSpeed, int startCity, int destinationCity, Road[] roads) {
		buildCityDistancesIndex(roads);

		pq.resetQueue();
		pq.enqueue(startCity, 0, Rational.ZERO);

		while (!pq.isEmpty()) {

			State currState = pq.getMinDistState();
			Rational currDist = pq.distance(currState.city, currState.usedTicketsMask);
			int currCity = currState.city;
			int usedTicketsMask = currState.usedTicketsMask;

			if (currCity == destinationCity) {
				System.out.println(currDist.value());
				return;
			}

			List accessibleCities = cityDistanceIndex[currCity];
			for (int i = 0; i < accessibleCities.size(); i++) {

				Road road = (Road) accessibleCities.get(i);
				int nextCity = road.cityB;
				int cityDist = road.dist;

				for (int b = 0; b < ticketSpeed.length; b++) {
					if ((usedTicketsMask & (1 << b)) == 0) {

						int nextUsedTicketsMask = usedTicketsMask | (1 << b);
						Rational nextStateDist = pq.distance(nextCity, nextUsedTicketsMask);
						Rational newNextStateDist = currDist.add(new Rational(cityDist, ticketSpeed[b]));

						if (nextStateDist.greater(newNextStateDist)) {
							pq.enqueue(nextCity, nextUsedTicketsMask, newNextStateDist);
						}
					}
				}
			}
		}

		System.out.println(IMPOSSIBLE);
	}

	private static void buildCityDistancesIndex(Road[] roads) {
		for (int i = 0; i < MAX_CITIES; i++) {
			cityDistanceIndex[i].clear();
		}
		for (Road road : roads) {
			cityDistanceIndex[road.cityA].add(road);
			cityDistanceIndex[road.cityB].add(new Road(road.cityB, road.cityA, road.dist));
		}
	}

	static class PriorityQueue {

		int[][] queueIndex;
		Rational[][] stateDist;
		private List<State> queue;

		public PriorityQueue(int citiesNum, int ticketsNum) {
			this.queueIndex = new int[1 << ticketsNum][citiesNum + 1];
			this.stateDist = new Rational[1 << ticketsNum][citiesNum + 1];
			this.queue = new ArrayList<>(citiesNum * (1 << ticketsNum));
			this.resetQueue();
		}

		public void resetQueue() {
			this.queue.clear();
			for (Rational[] row : this.stateDist) {
				Arrays.fill(row, null);
			}
			for (int[] row : this.queueIndex) {
				Arrays.fill(row, -1);
			}
		}

		public Rational distance(int city, int usedTicketsMask) {
			return this.stateDist[usedTicketsMask][city] != null
					? this.stateDist[usedTicketsMask][city]
					: Rational.INFINITY;
		}

		private Rational distance(int queueIdx) {
			State state = this.queue.get(queueIdx);
			return this.stateDist[state.usedTicketsMask][state.city] != null
					? this.stateDist[state.usedTicketsMask][state.city]
					: Rational.INFINITY;
		}

		public boolean isEmpty() {
			return this.queue.isEmpty();
		}

		public State getMinDistState() {
			State minDistState = this.queue.get(0);
			this.swap(0, this.queue.size() - 1);
			this.queue.remove(this.queue.size() - 1);
			this.pushDown(0);
			this.queueIndex[minDistState.usedTicketsMask][minDistState.city] = -1;
			return minDistState;
		}

		public void enqueue(int city, int usedTicketsMask, Rational dist) {
			int queueIdx;
			if (this.enqueued(city, usedTicketsMask)) {
				queueIdx = this.queueIndex[usedTicketsMask][city];
			} else {
				this.queue.add(new State(city, usedTicketsMask));
				queueIdx = this.queue.size() - 1;
				this.queueIndex[usedTicketsMask][city] = queueIdx;
			}

			// Rational existingDist = this.distance(city, usedTicketsMask);
			this.stateDist[usedTicketsMask][city] = dist;
			// if (dist.greater(existingDist)) {
			// this.pushDown(queueIdx);
			// } else {
			this.pushUp(queueIdx);
			// }
		}

		private boolean enqueued(int city, int usedTicketsMask) {
			return this.queueIndex[usedTicketsMask][city] != -1;
		}

		private void pushDown(int curr) {
			while (true) {
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
					curr = minIdx;
				} else {
					return;
				}
			}
		}

		private void pushUp(int curr) {
			while (curr != 0) {
				int parentIdx = this.parent(curr);
				if (this.distance(parentIdx).greater(this.distance(curr))) {
					this.swap(parentIdx, curr);
					curr = parentIdx;
				} else {
					return;
				}
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
			this.queueIndex[stateB.usedTicketsMask][stateB.city] = a;

			this.queue.set(b, stateA);
			this.queueIndex[stateA.usedTicketsMask][stateA.city] = b;
		}
	}

	static class State {

		int city;
		int usedTicketsMask;

		public State(int city, int usedTicketsMask) {
			this.city = city;
			this.usedTicketsMask = usedTicketsMask;
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
