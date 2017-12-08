package spoj.dynamic_programming.dp;

// http://www.spoj.com/problems/DP/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DP_3_Submitted_To_SPOJ {

	public static void main(String[] args) throws Exception {
		solve(Arrays.asList(
				"3442211",
				"34$221X",
				"3442211"
				), 3, 7);

		solve(Arrays.asList(
				"001000$",
				"$010X0$",
				"0010000"
				), 3, 7);

		solve(Arrays.asList(
				"$X$",
				"$$$",
				"$$$"
				), 3, 3);

		solve(Arrays.asList(
				"$X$123456776543",
				"$$$8765434$6876",
				"$$$09876345678$",
				"234568765679875",
				"234545677$5432$"
				), 5, 15);

		solve(Arrays.asList(
				"$X$123456776543",
				"$$$8765434$6876",
				"$$$09876345678$",
				"234568765679875",
				"234545677$5432$",
				"$$$09876345678$",
				"234568765679875",
				"234545677$5432$"
				), 8, 15);

		// BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		//
		// String s = br.readLine().trim();
		// while (s.isEmpty()) {
		// s = br.readLine().trim();
		// }
		// int testsNum = Integer.parseInt(s);
		//
		// List<String> mapStr = new ArrayList<>(MAX_SIZE);
		//
		// Pattern space = Pattern.compile("\\s+");
		//
		// for (int i = 0; i < testsNum; i++) {
		//
		// String rows_cols = br.readLine().trim();
		// while (rows_cols.isEmpty()) {
		// rows_cols = br.readLine().trim();
		// }
		//
		// String[] parts = space.split(rows_cols, -1);
		// int rows = Integer.parseInt(parts[0]);
		// int cols = Integer.parseInt(parts[1]);
		//
		// for (int r = 0; r < rows; r++) {
		// s = br.readLine().trim();
		// while (s.isEmpty()) {
		// s = br.readLine().trim();
		// }
		// mapStr.add(s);
		// }
		//
		// solve(mapStr, rows, cols);
		// mapStr.clear();
		// }

		// testRandomlyGeneratedMaps();
	}

	private static void testRandomlyGeneratedMaps() {
		Random rnd = new Random(1);
		for (int i = 0; i < 200; i++) {

			int rows = rnd.nextInt(55) + 1;
			int cols = rnd.nextInt(55) + 1;

			List<String> mapStr = new ArrayList<>(MAX_SIZE);
			for (int r = 0; r < rows; r++) {
				StringBuilder sb = new StringBuilder();
				for (int c = 0; c < cols; c++) {
					sb.append((char) (rnd.nextInt(4) + '0'));
				}
				mapStr.add(sb.toString());
			}

			for (int j = 0; j < rnd.nextInt(30); j++) {
				int rw = rnd.nextInt(rows);
				StringBuilder sb = new StringBuilder(mapStr.get(rw));
				sb.setCharAt(rnd.nextInt(cols), '$');
				mapStr.set(rw, sb.toString());
			}

			int rw = rnd.nextInt(rows);
			StringBuilder sb = new StringBuilder(mapStr.get(rw));
			sb.setCharAt(rnd.nextInt(cols), 'X');
			mapStr.set(rw, sb.toString());

			for (String s : mapStr) {
				System.out.println(s);
			}

			solve(mapStr, rows, cols);
		}
	}

	static final int[][] ADJACENT_DIRECTIONS = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
	static final char PIZZA_HOUSE = 'X';
	static final char TARGET_HOUSE = '$';
	static final int INFINITY = 10000000;
	static final int CAN_NOT_SOLVE = -1;
	static final int TIME_ADJACENT_BUILDING = 2;
	static final int TIME_ADJACENT_DIFFERENT_HEIGHT = 3;
	static final int TIME_ADJACENT_SAME_HEIGHT = 1;
	static final int NOT_INITIALIZED = -1;

	static final int MAX_SIZE = 55;
	static final int MAX_TARGETS = 25;
	static final char[][] mapChr = new char[MAX_SIZE][MAX_SIZE];
	static final Node[][] nodes = new Node[MAX_SIZE][MAX_SIZE];
	static {
		for (int r = 0; r < MAX_SIZE; r++) {
			for (int c = 0; c < MAX_SIZE; c++) {
				nodes[r][c] = new Node(r, c);
			}
		}
	}
	static final MinPriorityQueue pq = new MinPriorityQueue();
	static final List<Integer> distances = new ArrayList<>(MAX_TARGETS);

	static int solve(List<String> mapStr, int rows, int cols) {

		for (int i = 0; i < rows; i++) {
			String row = mapStr.get(i);
			for (int j = 0; j < cols; j++) {
				mapChr[i][j] = row.charAt(j);
			}
		}

		int result = solve(mapChr, rows, cols);
		System.out.println(result);
		// System.out.println();

		return result;
	}

	static int solve(char[][] map, int rows, int cols) {

		// Initialize nodes
		Node start = null;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				nodes[r][c].dist = INFINITY;
				nodes[r][c].indexPQ = -1;
				if (map[r][c] == PIZZA_HOUSE) {
					start = nodes[r][c];
				}
			}
		}
		// End initialization of nodes

		if (start == null) {
			return 0;
		}

		// Dijkstra algorithm for finding of distances to targets
		start.dist = 0;
		pq.enqueue(start);

		while (!pq.isEmpty()) {
			Node curr = pq.dequeue();

			int r = curr.row;
			int c = curr.col;

			for (int[] diff : ADJACENT_DIRECTIONS) {

				int newR = r + diff[0];
				int newC = c + diff[1];

				if ((newR >= 0) && (newR < rows) && (newC >= 0) && (newC < cols)) {
					char upMap = map[newR][newC];
					char currMap = map[r][c];
					Node upNode = nodes[newR][newC];

					if ((upMap == PIZZA_HOUSE)
							|| (upMap == TARGET_HOUSE)
							|| (currMap == PIZZA_HOUSE)
							|| (currMap == TARGET_HOUSE)
							|| (Math.abs(upMap - currMap) <= 1)) {

						int dist;
						if ((upMap == PIZZA_HOUSE)
								|| (upMap == TARGET_HOUSE)
								|| (currMap == PIZZA_HOUSE)
								|| (currMap == TARGET_HOUSE)) {

							dist = TIME_ADJACENT_BUILDING;
						} else {
							dist = (upMap == currMap)
									? TIME_ADJACENT_SAME_HEIGHT
									: TIME_ADJACENT_DIFFERENT_HEIGHT;
						}

						int newDist = curr.dist + dist;
						if (upNode.dist > newDist) {
							if (upNode.inQueue()) {
								pq.updateDistance(upNode, newDist);
							} else {
								upNode.dist = newDist;
								pq.enqueue(upNode);
							}
						}
					}
				}
			}
		}
		// End Dijkstra algorithm for finding of distances to targets

		// Collect all distances to target nodes
		distances.clear();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (map[r][c] == TARGET_HOUSE) {
					int dist = nodes[r][c].dist;
					distances.add(dist);

					if (dist == INFINITY) {
						return CAN_NOT_SOLVE;
					}
				}
			}
		}
		// End collecting of distances

		if (distances.isEmpty()) {
			return 0;
		}

		// displayDistances(nodes, rows, cols);
		// System.out.println(distances);

		int minTime = minTime(distances);
		return minTime;
	}

	private static void displayDistances(Node[][] nodes, int rows, int cols) {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				System.out.printf("%3d ", nodes[r][c].dist);
			}
			System.out.println();
		}
	}

	static int minTime(List<Integer> times) {
		Collections.sort(times);

		int times_x2_sum = 0;
		for (int x : times) {
			times_x2_sum += 2 * x;
		}

		int[][][] memoized = new int[times.size() + 2][times.size() + 2][times_x2_sum + 5];
		for (int i = 0; i < memoized.length; i++) {
			for (int j = 0; j < memoized[0].length; j++) {
				Arrays.fill(memoized[i][j], NOT_INITIALIZED);
			}
		}

		int result = minTime(times, times_x2_sum, 0, -1, -1, memoized);
		return result;
	}

	static int minTime(List<Integer> times,
			int times_x2_sum,
			int t1,
			int last1,
			int last2,
			int[][][] memoized) {

		int pos = Math.max(last1, last2) + 1;

		if (pos == times.size()) {
			int t2 = times_x2_sum - t1;

			int delivery1 = t1 - (last1 >= 0 ? times.get(last1) : 0);
			int delivery2 = t2 - (last2 >= 0 ? times.get(last2) : 0);

			return Math.max(delivery1, delivery2);
		}

		if (memoized[last1 + 1][last2 + 1][t1] != NOT_INITIALIZED) {
			return memoized[last1 + 1][last2 + 1][t1];
		}

		int result1 = minTime(times, times_x2_sum, t1 + (2 * times.get(pos)), pos, last2, memoized);
		int result2 = minTime(times, times_x2_sum, t1, last1, pos, memoized);
		int result = Math.min(result1, result2);

		memoized[last1 + 1][last2 + 1][t1] = result;
		return result;
	}

	static class Node {
		int row;
		int col;
		int dist = INFINITY;
		int indexPQ = -1; // index in priority queue

		public Node(int row, int col) {
			this.row = row;
			this.col = col;
		}

		boolean inQueue() {
			return this.indexPQ != -1;
		}
	}

	static class MinPriorityQueue {

		List<Node> nodes = new ArrayList<>((MAX_SIZE * MAX_SIZE) + 5);

		boolean isEmpty() {
			return this.nodes.isEmpty();
		}

		void enqueue(Node n) {
			this.nodes.add(n);
			n.indexPQ = this.nodes.size() - 1;
			this.pushUp(n.indexPQ);
		}

		Node dequeue() {
			Node firstNode = this.nodes.get(0);

			Node lastNode = this.nodes.get(this.nodes.size() - 1);
			lastNode.indexPQ = 0;
			this.nodes.set(0, lastNode);
			this.nodes.remove(this.nodes.size() - 1);
			if (!this.nodes.isEmpty()) {
				this.pushDown(0);
			}

			return firstNode;
		}

		void updateDistance(Node n, int newDist) {
			if (newDist < n.dist) {
				n.dist = newDist;
				this.pushUp(n.indexPQ);
			} else {
				throw new RuntimeException();
				// n.dist = newDist;
				// this.pushDown(n.indexPQ);
			}
		}

		void pushUp(int idx) {
			if (idx == 0) {
				return;
			}

			int parent = this.parent(idx);
			if (this.nodes.get(idx).dist < this.nodes.get(parent).dist) {
				this.swap(idx, parent);
				this.pushUp(parent);
			}
		}

		void pushDown(int idx) {
			int minIdx = this.left(idx);
			if (minIdx >= this.nodes.size()) {
				return;
			}

			int rightIdx = this.right(idx);
			if ((rightIdx < this.nodes.size())
					&& (this.nodes.get(rightIdx).dist < this.nodes.get(minIdx).dist)) {

				minIdx = rightIdx;
			}

			if (this.nodes.get(idx).dist > this.nodes.get(minIdx).dist) {
				this.swap(idx, minIdx);
				this.pushDown(minIdx);
			}
		}

		void swap(int i, int j) {
			Node tmp = this.nodes.get(i);
			this.nodes.set(i, this.nodes.get(j));
			this.nodes.set(j, tmp);
			this.nodes.get(i).indexPQ = i;
			this.nodes.get(j).indexPQ = j;
		}

		int left(int idx) {
			return (idx * 2) + 1;
		}

		int right(int idx) {
			return (idx * 2) + 2;
		}

		int parent(int idx) {
			return (idx - 1) / 2;
		}
	}
}
