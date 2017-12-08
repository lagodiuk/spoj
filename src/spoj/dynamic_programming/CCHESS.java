package spoj.dynamic_programming;

// http://www.spoj.com/problems/CCHESS/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CCHESS {

	public static void main(String[] args) {
		System.out.println(solveUsingBellmanFordAlgorithm(2, 5, 5, 2));
		System.out.println(solveUsingBellmanFordAlgorithm(4, 7, 3, 2));
		System.out.println(solveUsingBellmanFordAlgorithm(1, 2, 3, 4));
		System.out.println(solveUsingBellmanFordAlgorithm(0, 0, 1, 0));
		System.out.println(solveUsingBellmanFordAlgorithm(4, 4, 4, 4));
		System.out.println(solveUsingBellmanFordAlgorithm(1, 2, 2, 1));

		for (int srcX = 0; srcX < DIM; srcX++) {
			for (int srcY = 0; srcY < DIM; srcY++) {
				for (int destX = 0; destX < DIM; destX++) {
					for (int destY = 0; destY < DIM; destY++) {
						int d1 = solveUsingBellmanFordAlgorithm(srcX, srcY, destX, destY);
						int d2 = getPrecalculatedDistance(srcX, srcY, destX, destY);
						// System.out.println(srcX + " " + srcY + " " + destX + " " + destY);
						// System.out.println(d1);
						if (d1 != d2) {
							System.out.println(srcX + " " + srcY + " " + destX + " " + destY);
						}
					}
				}
			}
		}
	}

	static final int DIM = 8;

	static final int INFINITY = 1000000;

	static final int[][] MOVES = {
			{1, 2},
			{1, -2},
			{-1, 2},
			{-1, -2},
			{2, 1},
			{2, -1},
			{-2, 1},
			{-2, -1},
	};

	static final List<Edge> EDGES = initEdges();

	static final int[][] DISTANCES = precalculateDistancesUsingFloydWarsallAlgorithm();

	static int getPrecalculatedDistance(int srcX, int srcY, int destX, int destY) {
		return DISTANCES[vertexIdx(srcX, srcY)][vertexIdx(destX, destY)];
	}

	static int[][] precalculateDistancesUsingFloydWarsallAlgorithm() {

		int[][] dist = new int[DIM * DIM][DIM * DIM];
		for (int i = 0; i < dist.length; i++) {
			Arrays.fill(dist[i], INFINITY);
		}

		for (int srcX = 0; srcX < DIM; srcX++) {
			for (int srcY = 0; srcY < DIM; srcY++) {

				int srcIdx = vertexIdx(srcX, srcY);
				dist[srcIdx][srcIdx] = 0;

				for (int[] mv : MOVES) {
					int destX = srcX + mv[0];
					int destY = srcY + mv[1];

					if (isAllowed(destX, destY)) {
						int destIdx = vertexIdx(destX, destY);
						int weight = weight(srcX, srcY, destX, destY);

						dist[srcIdx][destIdx] = weight;
					}
				}
			}
		}

		for (int k = 0; k < dist.length; k++) {
			for (int i = 0; i < dist.length; i++) {
				for (int j = 0; j < dist.length; j++) {
					if (dist[i][j] > (dist[i][k] + dist[k][j])) {
						dist[i][j] = dist[i][k] + dist[k][j];
					}
				}
			}
		}

		return dist;
	}

	static int solveUsingBellmanFordAlgorithm(int srcX, int srcY, int destX, int destY) {

		int[] dist = new int[DIM * DIM];
		Arrays.fill(dist, INFINITY);
		dist[vertexIdx(srcX, srcY)] = 0;

		for (int i = 0; i < (dist.length - 1); i++) {
			for (Edge e : EDGES) {
				relax(e, dist);
			}
		}

		return dist[vertexIdx(destX, destY)];
	}

	static void relax(Edge edge, int[] dist) {
		if (dist[edge.tgt] > (dist[edge.src] + edge.weight)) {
			dist[edge.tgt] = dist[edge.src] + edge.weight;
		}
	}

	static List<Edge> initEdges() {
		List<Edge> edges = new ArrayList<>();

		for (int srcX = 0; srcX < DIM; srcX++) {
			for (int srcY = 0; srcY < DIM; srcY++) {

				int srcIdx = vertexIdx(srcX, srcY);

				for (int[] mv : MOVES) {
					int destX = srcX + mv[0];
					int destY = srcY + mv[1];

					if (isAllowed(destX, destY)) {
						int destIdx = vertexIdx(destX, destY);
						int dist = weight(srcX, srcY, destX, destY);
						edges.add(new Edge(srcIdx, destIdx, dist));
					}
				}
			}
		}

		return edges;
	}

	static boolean isAllowed(int x, int y) {
		return (x >= 0) && (x < DIM) && (y >= 0) && (y < DIM);
	}

	static int vertexIdx(int x, int y) {
		return (y * DIM) + x;
	}

	static int weight(int x1, int y1, int x2, int y2) {
		return (x1 * x2) + (y1 * y2);
	}

	static class Edge {
		int src;
		int tgt;
		int weight;
		public Edge(int src, int dest, int weight) {
			this.src = src;
			this.tgt = dest;
			this.weight = weight;
		}
	}
}
