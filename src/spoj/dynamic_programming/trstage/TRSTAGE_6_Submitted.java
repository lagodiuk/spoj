package spoj.dynamic_programming.trstage;

// http://www.spoj.com/problems/TRSTAGE/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TRSTAGE_6_Submitted {

	public static void main(String[] args) throws Exception {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s;

		while ((s = br.readLine()) != null) {
			String[] parts = s.split(" ");
			int ticketsNum = Integer.parseInt(parts[0]);
			int citiesNum = Integer.parseInt(parts[1]);
			int roadsNum = Integer.parseInt(parts[2]);
			int srcCity = Integer.parseInt(parts[3]);
			int destCity = Integer.parseInt(parts[4]);

			if ((ticketsNum == 0)
					&& (citiesNum == 0)
					&& (roadsNum == 0)
					&& (srcCity == 0)
					&& (destCity == 0)) {
				return;
			}

			int[] tickets = new int[ticketsNum];
			s = br.readLine();
			parts = s.split(" ");
			for (int i = 0; i < ticketsNum; i++) {
				tickets[i] = Integer.parseInt(parts[i]);
			}

			Road[] roads = new Road[roadsNum];
			for (int i = 0; i < roadsNum; i++) {
				s = br.readLine();
				parts = s.split(" ");
				roads[i] = new Road(
						Integer.parseInt(parts[0]),
						Integer.parseInt(parts[1]),
						Integer.parseInt(parts[2]));
			}

			solve(tickets, srcCity, destCity, roads);
		}
	}

	static final String IMPOSSIBLE = "Impossible";
	static final int MAX_CITIES = 32;
	static final int MAX_TICKETS = 8;
	static final long INFINITY = (((long) 1) << 32) | (0 & 0xffffffffL);
	static final List[] cityDistanceIndex = new List[MAX_CITIES];
	static {
		for (int i = 0; i < MAX_CITIES; i++) {
			cityDistanceIndex[i] = new ArrayList<>(MAX_CITIES);
		}
	}

	static final void solve(int[] ticketSpeed, int startCity, int destinationCity, Road[] roads) {
		buildCityDistancesIndex(roads);

		PriorityQueue.resetQueue();
		PriorityQueue.enqueue(startCity, 0, 0, 1);

		while (!PriorityQueue.isEmpty()) {

			State currState = PriorityQueue.getMinDistState();
			int currDistNum = (int) (PriorityQueue.stateDist[currState.usedTicketsMask][currState.city] >> 32);
			int currDistDenom = (int) (PriorityQueue.stateDist[currState.usedTicketsMask][currState.city]);
			int currCity = currState.city;
			int usedTicketsMask = currState.usedTicketsMask;

			if (currCity == destinationCity) {
				System.out.println((double) currDistNum / currDistDenom);
				return;
			}

			List accessibleCities = cityDistanceIndex[currCity];

			for (int b = 0; b < ticketSpeed.length; b++) {
				if ((usedTicketsMask & (1 << b)) == 0) {

					int nextUsedTicketsMask = usedTicketsMask | (1 << b);

					for (int i = 0; i < accessibleCities.size(); i++) {

						Road road = (Road) accessibleCities.get(i);
						int nextCity = road.cityB;
						int cityDist = road.dist;

						int nextStateDistNum = (int) (PriorityQueue.stateDist[nextUsedTicketsMask][nextCity] >> 32);
						int nextStateDistDenom = (int) (PriorityQueue.stateDist[nextUsedTicketsMask][nextCity]);

						// int gcd = gcd(cityDist, ticketSpeed[b]);
						int gcd = cityDist;
						int y = ticketSpeed[b];
						while (y != 0) {
							int tmp = y;
							y = gcd % y;
							gcd = tmp;
						}
						int addNum = cityDist / gcd;
						int addDenom = ticketSpeed[b] / gcd;

						int newDistNum = (currDistNum * addDenom) + (addNum * currDistDenom);
						int newDistDenom = addDenom * currDistDenom;
						// gcd = gcd(newDistNum, newDistDenom);
						gcd = newDistNum;
						y = newDistDenom;
						while (y != 0) {
							int tmp = y;
							y = gcd % y;
							gcd = tmp;
						}
						newDistNum /= gcd;
						newDistDenom /= gcd;

						if ((nextStateDistNum * newDistDenom) > (newDistNum * nextStateDistDenom)) {
							PriorityQueue.enqueue(nextCity, nextUsedTicketsMask, newDistNum, newDistDenom);
						}
					}
				}
			}
		}

		System.out.println(IMPOSSIBLE);
	}

	static final void buildCityDistancesIndex(Road[] roads) {
		for (int i = 0; i < MAX_CITIES; i++) {
			cityDistanceIndex[i].clear();
		}
		for (Road road : roads) {
			cityDistanceIndex[road.cityA].add(road);
			cityDistanceIndex[road.cityB].add(new Road(road.cityB, road.cityA, road.dist));
		}
	}

	// static final int gcd(int a, int b) {
	// while (b != 0) {
	// int tmp = b;
	// b = a % b;
	// a = tmp;
	// }
	// return a;
	// }

	static final class PriorityQueue {

		static final int[][] queueIndex;
		// 2 ints packed into long
		// left: numerator
		// right: denominator
		// long l = (((long)x) << 32) | (y & 0xffffffffL);
		// int x = (int)(l >> 32);
		// int y = (int)l;
		static final long[][] stateDist;
		static final State[] queue;
		static int queueSize;

		static {
			queueIndex = new int[1 << MAX_TICKETS][MAX_CITIES];
			stateDist = new long[1 << MAX_TICKETS][MAX_CITIES];
			queue = new State[(MAX_CITIES * (1 << MAX_TICKETS)) + 1];
			resetQueue();
		}

		public static final void resetQueue() {
			queueSize = 0;
			for (int i = 0; i < stateDist.length; i++) {
				Arrays.fill(stateDist[i], INFINITY);
			}
			for (int i = 0; i < queueIndex.length; i++) {
				Arrays.fill(queueIndex[i], -1);
			}
		}

		// public static final int distanceNum(int city, int usedTicketsMask) {
		// return (int) (stateDist[usedTicketsMask][city] >> 32);
		// }
		//
		// public static final int distanceDenom(int city, int usedTicketsMask) {
		// return (int) stateDist[usedTicketsMask][city];
		// }

		// private static final int distanceNum(int queueIdx) {
		// State state = queue[queueIdx];
		// return (int) (stateDist[state.usedTicketsMask][state.city] >> 32);
		// }
		//
		// private static final int distanceDenom(int queueIdx) {
		// State state = queue[queueIdx];
		// return (int) (stateDist[state.usedTicketsMask][state.city]);
		// }

		public static final boolean isEmpty() {
			return queueSize == 0;
		}

		public static final State getMinDistState() {
			State minDistState = queue[0];
			swap(0, queueSize - 1);
			--queueSize;
			pushDown(0);
			queueIndex[minDistState.usedTicketsMask][minDistState.city] = -1;
			return minDistState;
		}

		public static final void enqueue(int city, int usedTicketsMask, int distNum, int distDenom) {
			int queueIdx;
			if (enqueued(city, usedTicketsMask)) {
				queueIdx = queueIndex[usedTicketsMask][city];
			} else {
				if (queue[queueSize] == null) {
					queue[queueSize] = new State(city, usedTicketsMask);
				} else {
					queue[queueSize].city = city;
					queue[queueSize].usedTicketsMask = usedTicketsMask;
				}
				++queueSize;
				queueIdx = queueSize - 1;
				queueIndex[usedTicketsMask][city] = queueIdx;
			}

			// int existingDistNum = this.distanceNum(city, usedTicketsMask);
			// int existingDistDenom = this.distanceDenom(city, usedTicketsMask);

			stateDist[usedTicketsMask][city] = (((long) distNum) << 32) | (distDenom & 0xffffffffL);

			// if ((distNum * existingDistDenom) > (existingDistNum * distDenom)) {
			// pushDown(queueIdx);
			// } else {
			pushUp(queueIdx);
			// }
		}

		private static final boolean enqueued(int city, int usedTicketsMask) {
			return queueIndex[usedTicketsMask][city] != -1;
		}

		private static final void pushDown(int curr) {
			while (true) {
				int minIdx = (curr * 2) + 1;
				if (minIdx >= queueSize) {
					return;
				}

				int minIdxNum = (int) (stateDist[queue[minIdx].usedTicketsMask][queue[minIdx].city] >> 32);
				int minIdxDenom = (int) (stateDist[queue[minIdx].usedTicketsMask][queue[minIdx].city]);

				int rightIdx = (curr * 2) + 2;

				if (rightIdx < queueSize) {
					int rightIdxNum = (int) (stateDist[queue[rightIdx].usedTicketsMask][queue[rightIdx].city] >> 32);
					int rightIdxDenom = (int) (stateDist[queue[rightIdx].usedTicketsMask][queue[rightIdx].city]);

					if ((minIdxNum * rightIdxDenom) > (rightIdxNum * minIdxDenom)) {
						minIdx = rightIdx;
						minIdxNum = rightIdxNum;
						minIdxDenom = rightIdxDenom;
					}
				}

				int currNum = (int) (stateDist[queue[curr].usedTicketsMask][queue[curr].city] >> 32);
				int currDenom = (int) (stateDist[queue[curr].usedTicketsMask][queue[curr].city]);

				if ((currNum * minIdxDenom) > (minIdxNum * currDenom)) {
					swap(curr, minIdx);
					curr = minIdx;
				} else {
					return;
				}
			}
		}

		private static final void pushUp(int curr) {
			while (curr != 0) {
				int parentIdx = (curr - 1) / 2;

				int parentIdxNum = (int) (stateDist[queue[parentIdx].usedTicketsMask][queue[parentIdx].city] >> 32);
				int parentIdxDenom = (int) (stateDist[queue[parentIdx].usedTicketsMask][queue[parentIdx].city]);

				int currNum = (int) (stateDist[queue[curr].usedTicketsMask][queue[curr].city] >> 32);
				int currDenom = (int) (stateDist[queue[curr].usedTicketsMask][queue[curr].city]);

				if ((parentIdxNum * currDenom) > (currNum * parentIdxDenom)) {
					swap(parentIdx, curr);
					curr = parentIdx;
				} else {
					return;
				}
			}
		}

		private static final void swap(int a, int b) {
			State stateA = queue[a];
			State stateB = queue[b];

			queue[a] = stateB;
			queueIndex[stateB.usedTicketsMask][stateB.city] = a;

			queue[b] = stateA;
			queueIndex[stateA.usedTicketsMask][stateA.city] = b;
		}
	}

	static final class State {

		int city;
		int usedTicketsMask;

		public State(int city, int usedTicketsMask) {
			this.city = city;
			this.usedTicketsMask = usedTicketsMask;
		}
	}

	static final class Road {

		// No two roads connect the same pair of cities.
		// A road never connects a city with itself.
		// Each road can be traveled in both directions.

		final int cityA;
		final int cityB;
		final int dist;

		public Road(int cityA, int cityB, int dist) {
			this.cityA = cityA;
			this.cityB = cityB;
			this.dist = dist;
		}
	}
}
