package spoj.dynamic_programming.najkraci;

// http://www.spoj.com/problems/NAJKRACI/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NAJKRACI_3_Dijkstra_Dynamic_Programming {

	public static void main(String... args) {
		solve(4, new Edge_2[]{
				new Edge_2(1, 2, 5),
				new Edge_2(2, 3, 5),
				new Edge_2(3, 4, 5),
				new Edge_2(1, 4, 8),
		});

		solve(5, new Edge_2[]{
				new Edge_2(1, 2, 20),
				new Edge_2(1, 3, 2),
				new Edge_2(2, 3, 2),
				new Edge_2(4, 2, 3),
				new Edge_2(4, 2, 3),
				new Edge_2(3, 4, 5),
				new Edge_2(4, 3, 5),
				new Edge_2(5, 4, 20),
		});
	}

	static int[] solve(int verticesCnt, Edge[] edges) {
		Edge_2[] wrapper = new Edge_2[edges.length];
		for (int i = 0; i < edges.length; i++) {
			wrapper[i] = new Edge_2(edges[i].from + 1, edges[i].to + 1, edges[i].weight);
		}
		return solve(verticesCnt, wrapper);
	}

	static int[] solve(int verticesCnt, Edge_2[] edges) {
		for (int i = 0; i < verticesCnt; i++) {
			Map<Integer, Vertex> vtxs = initializeVertices(verticesCnt, edges);
			dijkstra_calc_dist(i, vtxs);
			dfs_calc_outgoing(i, vtxs);
			dijkstra_calc_ingoing(i, vtxs);
			dijkstra_calc_variants(i, vtxs);
		}

		for (Edge_2 e : edges) {
			System.out.println(e);
		}
		System.out.println();

		int[] edgeCnt = new int[edges.length];
		for (int i = 0; i < edges.length; i++) {
			edgeCnt[i] = edges[i].cnt;
		}
		return edgeCnt;
	}

	private static void dijkstra_calc_variants(int i, Map<Integer, Vertex> vtxs) {
		PQ pq = new PQ();
		pq.update(vtxs.get(i), 0);
		vtxs.get(i).ingoing_cnt = 1;

		while (pq.isNotEmpty()) {
			Vertex curr = pq.dequeue();

			for (Edge_2 e : curr.edges) {
				Vertex to = vtxs.get(e.to);
				if (to.dist == (curr.dist + e.weight)) {
					pq.update(to, curr.dist + e.weight);
					e.cnt += curr.ingoing_cnt * to.outgoing_cnt;
				}
			}
		}
	}

	private static void dijkstra_calc_ingoing(int i, Map<Integer, Vertex> vtxs) {
		PQ pq = new PQ();
		pq.update(vtxs.get(i), 0);
		vtxs.get(i).ingoing_cnt = 1;

		while (pq.isNotEmpty()) {
			Vertex curr = pq.dequeue();

			for (Edge_2 e : curr.edges) {
				Vertex to = vtxs.get(e.to);
				if (to.dist == (curr.dist + e.weight)) {
					to.ingoing_cnt += curr.ingoing_cnt;
					pq.update(to, curr.dist + e.weight);
				}
			}
		}
	}

	static int dfs_calc_outgoing(int rootIdx, Map<Integer, Vertex> vtxs) {
		Vertex root = vtxs.get(rootIdx);
		if (root.outgoing_cnt != -1) {
			return root.outgoing_cnt;
		}
		int result = 0;
		for (Edge_2 e : root.edges) {
			if (vtxs.get(e.to).dist == (root.dist + e.weight)) {
				int subCnt = dfs_calc_outgoing(e.to, vtxs);
				result += subCnt;
			}
		}
		result++;
		root.outgoing_cnt = result;
		return result;
	}

	private static final int INF = 10000000;

	static void dijkstra_calc_dist(int start, Map<Integer, Vertex> vtxs) {

		PQ pq = new PQ();
		pq.update(vtxs.get(start), 0);

		while (pq.isNotEmpty()) {
			Vertex curr = pq.dequeue();

			for (Edge_2 e : curr.edges) {
				Vertex to = vtxs.get(e.to);
				if (to.dist > (curr.dist + e.weight)) {
					pq.update(to, curr.dist + e.weight);
				}
			}
		}
	}

	private static Map<Integer, Vertex> initializeVertices(int verticesCnt, Edge_2[] edges) {
		Map<Integer, Vertex> vtxs = new HashMap<>();
		for (int i = 0; i < verticesCnt; i++) {
			Vertex v = new Vertex(i);
			v.dist = INF;
			v.pq_index = -1;
			vtxs.put(i, v);
		}
		for (Edge_2 e : edges) {
			vtxs.get(e.from).edges.add(e);
		}
		return vtxs;
	}

	// Priority Queue based on the Binary Heap
	static class PQ {

		private List<Vertex> q = new ArrayList<>();

		int left(int curr) {
			return (2 * curr) + 1;
		}

		int right(int curr) {
			return (2 * curr) + 2;
		}

		int parent(int curr) {
			return (curr - 1) / 2;
		}

		void swap(int a, int b) {
			Vertex av = this.q.get(a);
			Vertex bv = this.q.get(b);

			this.q.set(b, av);
			av.pq_index = b;

			this.q.set(a, bv);
			bv.pq_index = a;
		}

		void pushUp(int idx) {
			if (idx == 0) {
				return;
			}
			int p = this.parent(idx);
			if (this.q.get(p).dist > this.q.get(idx).dist) {
				this.swap(p, idx);
				this.pushUp(p);
			}
		}

		void pushDown(int idx) {
			int min = this.left(idx);
			if (min >= this.q.size()) {
				return;
			}
			int r = this.right(idx);
			if ((r < this.q.size()) && (this.q.get(r).dist < this.q.get(min).dist)) {
				min = r;
			}
			if (this.q.get(idx).dist > this.q.get(min).dist) {
				this.swap(idx, min);
				this.pushDown(min);
			}
		}

		void enqueue(Vertex v) {
			this.q.add(v);
			v.pq_index = this.q.size() - 1;
			this.pushUp(v.pq_index);
		}

		Vertex dequeue() {
			Vertex result = this.q.get(0);

			Vertex last = this.q.remove(this.q.size() - 1);
			if (!this.q.isEmpty()) {
				this.q.set(0, last);
				last.pq_index = 0;
				this.pushDown(0);
			}

			result.pq_index = -1;
			return result;
		}

		void update(Vertex v, int new_dist) {
			if (v.dist < new_dist) {
				// In general:
				// this.pushDown(v.pq_index);
				// However:
				// we always expect that the new distance is smaller
				throw new RuntimeException();
			}
			v.dist = new_dist;
			if (v.pq_index == -1) {
				this.enqueue(v);
			} else {
				this.pushUp(v.pq_index);
			}
		}

		boolean isNotEmpty() {
			return !this.q.isEmpty();
		}
	}

	static class Vertex {
		final int id; // unique id of the vertex
		int dist; // current shortest distance to the vertex
		int pq_index; // index inside the priority queue
		final List<Edge_2> edges = new ArrayList<>(); // outgoing edges
		int outgoing_cnt = -1;
		int ingoing_cnt = 0;
		Vertex(int id) {
			this.id = id;
		}
	}

	static class Edge_2 {
		final int from;
		final int to;
		final int weight;
		int cnt; // how many times given edge is present inside the shortest paths
		public Edge_2(int from, int to, int w) {
			this.from = from - 1;
			this.to = to - 1;
			this.weight = w;
		}

		@Override
		public String toString() {
			return (this.from + 1) + " -> " + (this.to + 1) + " : " + this.cnt;
		}
	}

}