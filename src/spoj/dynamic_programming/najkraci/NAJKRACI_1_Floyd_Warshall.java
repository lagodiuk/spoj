package spoj.dynamic_programming.najkraci;

// http://www.spoj.com/problems/NAJKRACI/

import java.util.Arrays;

public class NAJKRACI_1_Floyd_Warshall {

	public static void main(String[] args) {

		solve(4, new Edge[]{
				new Edge(1, 2, 5),
				new Edge(2, 3, 5),
				new Edge(3, 4, 5),
				new Edge(1, 4, 8),
		});

		solve(5, new Edge[]{
				new Edge(1, 2, 20),
				new Edge(1, 3, 2),
				new Edge(2, 3, 2),
				new Edge(4, 2, 3),
				new Edge(4, 2, 3),
				new Edge(3, 4, 5),
				new Edge(4, 3, 5),
				new Edge(5, 4, 20),
		});
	}

	static final int INF = 10000000;

	public static int[] solve(int citiesCount, Edge[] edges) {
		int[][] dist = new int[citiesCount][citiesCount];
		int[][] cnt = new int[citiesCount][citiesCount];

		for (int[] row : dist) {
			Arrays.fill(row, INF);
		}
		for (int i = 0; i < citiesCount; i++) {
			dist[i][i] = 0;
		}

		for (Edge e : edges) {
			if (dist[e.from][e.to] > e.weight) {
				dist[e.from][e.to] = e.weight;
				cnt[e.from][e.to] = 1;
			} else if (dist[e.from][e.to] == e.weight) {
				cnt[e.from][e.to]++;
			}
		}

		for (int k = 0; k < citiesCount; k++) {
			for (int i = 0; i < citiesCount; i++) {
				for (int j = 0; j < citiesCount; j++) {
					if (dist[i][j] > (dist[i][k] + dist[k][j])) {
						dist[i][j] = dist[i][k] + dist[k][j];
						cnt[i][j] = cnt[i][k] * cnt[k][j];
					} else if (dist[i][j] == (dist[i][k] + dist[k][j])) {
						cnt[i][j] += cnt[i][k] * cnt[k][j];
					}
				}
			}
		}

		for (int i = 0; i < citiesCount; i++) {
			cnt[i][i] = 1;
		}

		int[] edgeCnt = new int[edges.length];
		for (int i = 0; i < citiesCount; i++) {
			for (int j = 0; j < citiesCount; j++) {
				for (int edgeIdx = 0; edgeIdx < edges.length; edgeIdx++) {
					Edge e = edges[edgeIdx];
					int dist_i_j = dist[i][j];
					int dist_i_from = dist[i][e.from];
					int dist_to_j = dist[e.to][j];
					int e_weight = e.weight;
					if (dist_i_j == (e_weight + dist_i_from + dist_to_j)) {
						edgeCnt[edgeIdx] += cnt[i][e.from] * cnt[e.to][j];
					}
				}
			}
		}

		System.out.println(Arrays.toString(edgeCnt));

		return edgeCnt;
	}
}
