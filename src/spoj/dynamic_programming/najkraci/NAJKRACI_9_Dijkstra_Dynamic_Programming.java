package spoj.dynamic_programming.najkraci;

// http://www.spoj.com/problems/NAJKRACI/

import java.util.ArrayList;
import java.util.List;

public class NAJKRACI_9_Dijkstra_Dynamic_Programming {

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

		// all vertices
		Vertex[] vtxs = initializeVertices(verticesCnt, edges);
		// priority queue
		PQ pq = new PQ(verticesCnt);
		// Stack
		Stack stack = new Stack(Math.max(edges.length, vtxs.length) + 1);

		for (int i = 0; i < verticesCnt; i++) {
			reinitializeVertices(vtxs);
			dijkstra_calc_dist(i, vtxs, pq);
			dijkstra_calc_ingoing(i, vtxs, pq);
			dfs_calc_outgoing_non_recursive(i, vtxs, stack);
			dfs_calc_variants_non_recursive(i, vtxs, stack);
		}

		// for (Edge_2 e : edges) {
		// System.out.println(e);
		// }
		// System.out.println();

		int[] edgeCnt = new int[edges.length];
		for (int i = 0; i < edges.length; i++) {
			edgeCnt[i] = (int) edges[i].cnt;
		}
		return edgeCnt;
	}

	public static final long MOD = 1_000_000_007;

	static void dfs_calc_variants_non_recursive(int rootIdx, Vertex[] vtxs, Stack stack) {

		stack.push(rootIdx, -1);
		vtxs[rootIdx].processed = true;

		while (!stack.isEmpty()) {

			int currIdx = stack.topVertexIdx();
			stack.pop();

			Vertex curr = vtxs[currIdx];

			for (Edge_2 e : curr.shortestPathsEdges) {
				e.cnt = (e.cnt + ((curr.ingoing_cnt * vtxs[e.to].outgoing_cnt) % MOD)) % MOD;
				if (!vtxs[e.to].processed) {
					stack.push(e.to, -1);
					vtxs[e.to].processed = true;
				}
			}
		}
	}

	private static void dijkstra_calc_ingoing(int i, Vertex[] vtxs, PQ pq) {

		pq.update(vtxs[i], 0);
		vtxs[i].ingoing_cnt = 1;

		while (pq.isNotEmpty()) {
			Vertex curr = pq.dequeue();

			for (Edge_2 e : curr.edges) {
				Vertex to = vtxs[e.to];
				if (to.dist == (curr.dist + e.weight)) {
					to.ingoing_cnt = (to.ingoing_cnt + curr.ingoing_cnt) % MOD;
					pq.update(to, curr.dist + e.weight);
					curr.shortestPathsEdges.add(e);
				}
			}
		}
	}

	static void dfs_calc_outgoing_non_recursive(int rootIdx, Vertex[] vtxs, Stack stack) {

		stack.push(rootIdx, -1);

		while (!stack.isEmpty()) {

			int currIdx = stack.topVertexIdx();
			int parentIdx = stack.topParentVertexIdx();

			Vertex curr = vtxs[currIdx];

			if (curr.processed == true) {
				if (currIdx != rootIdx) {
					vtxs[parentIdx].outgoing_cnt = (vtxs[parentIdx].outgoing_cnt + curr.outgoing_cnt) % MOD;
				}
				stack.pop();
				continue;
			}

			for (Edge_2 e : curr.shortestPathsEdges) {
				if (!vtxs[e.to].processed) {
					stack.push(e.to, currIdx);
				} else {
					curr.outgoing_cnt = (curr.outgoing_cnt + vtxs[e.to].outgoing_cnt) % MOD;
				}
			}

			curr.processed = true;
		}

		for (Vertex v : vtxs) {
			v.processed = false;
		}
	}

	private static final int INF = 10000000;

	static void dijkstra_calc_dist(int start, Vertex[] vtxs, PQ pq) {

		pq.update(vtxs[start], 0);

		while (pq.isNotEmpty()) {
			Vertex curr = pq.dequeue();

			for (Edge_2 e : curr.edges) {
				Vertex to = vtxs[e.to];
				if (to.dist > (curr.dist + e.weight)) {
					pq.update(to, curr.dist + e.weight);
				}
			}
		}
	}

	private static void reinitializeVertices(Vertex[] vtxs) {
		for (int i = 0; i < vtxs.length; i++) {
			vtxs[i].dist = INF;
			vtxs[i].pq_index = -1;
			vtxs[i].outgoing_cnt = 1;
			vtxs[i].ingoing_cnt = 0;
			vtxs[i].processed = false;
			vtxs[i].shortestPathsEdges.clear();
		}
	}

	private static Vertex[] initializeVertices(int verticesCnt, Edge_2[] edges) {
		Vertex[] vtxs = new Vertex[verticesCnt];
		for (int i = 0; i < verticesCnt; i++) {
			vtxs[i] = new Vertex(i);
		}
		for (Edge_2 e : edges) {
			vtxs[e.from].edges.add(e);
		}
		for (int i = 0; i < verticesCnt; i++) {
			vtxs[i].shortestPathsEdges = new ArrayList<>(vtxs[i].edges.size());
		}
		return vtxs;
	}

	static class Stack {

		int[] vertexIdxs;
		int[] parentVertexIdxs;
		int size;

		public Stack(int capacity) {
			this.parentVertexIdxs = new int[capacity];
			this.vertexIdxs = new int[capacity];
			this.size = 0;
		}

		boolean isEmpty() {
			return this.size == 0;
		}

		void push(int vertexIdx, int parentVertexIdx) {
			this.vertexIdxs[this.size] = vertexIdx;
			this.parentVertexIdxs[this.size] = parentVertexIdx;
			this.size++;
		}

		int topVertexIdx() {
			return this.vertexIdxs[this.size - 1];
		}

		int topParentVertexIdx() {
			return this.parentVertexIdxs[this.size - 1];
		}

		void pop() {
			this.size--;
		}
	}

	// Priority Queue based on the Binary Heap
	static class PQ {

		private Vertex[] q;
		int size;

		public PQ(int capacity) {
			this.q = new Vertex[capacity + 1];
			this.size = 0;
		}

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
			Vertex av = this.q[a];
			Vertex bv = this.q[b];

			this.q[b] = av;
			av.pq_index = b;

			this.q[a] = bv;
			bv.pq_index = a;
		}

		void pushUp(int idx) {
			while (true) {
				if (idx == 0) {
					break;
				}
				int p = this.parent(idx);
				if (this.q[p].dist > this.q[idx].dist) {
					this.swap(p, idx);
					idx = p;
				} else {
					break;
				}
			}
		}

		void pushDown(int idx) {
			while (true) {
				int min = this.left(idx);
				if (min >= this.size) {
					break;
				}
				int r = this.right(idx);
				if ((r < this.size) && (this.q[r].dist < this.q[min].dist)) {
					min = r;
				}
				if (this.q[idx].dist > this.q[min].dist) {
					this.swap(idx, min);
					idx = min;
				} else {
					break;
				}
			}
		}

		void enqueue(Vertex v) {
			this.q[this.size] = v;
			this.size++;
			v.pq_index = this.size - 1;
			this.pushUp(v.pq_index);
		}

		Vertex dequeue() {
			Vertex result = this.q[0];

			Vertex last = this.q[this.size - 1];
			this.size--;

			if (this.size != 0) {
				this.q[0] = last;
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
			return this.size != 0;
		}
	}

	static class Vertex {
		// final int id; // unique id of the vertex
		final List<Edge_2> edges = new ArrayList<>(); // outgoing edges
		List<Edge_2> shortestPathsEdges; // outgoing edges, which belong to the shortest paths tree
		int dist; // current shortest distance to the vertex
		int pq_index; // index inside the priority queue (needed by Dijkstra algorithm)
		// amount of vertices, reachable from given vertex through the outgoing edges
		// (including the current vertex)
		long outgoing_cnt = 1;
		// amount of ingoing paths into the current vertex
		long ingoing_cnt = 0;
		// if this vertex has been already processed by DFS
		boolean processed = false;
		Vertex(int id) {
			// this.id = id;
		}
	}

	static class Edge_2 {
		final int from;
		final int to;
		final int weight;
		long cnt; // how many times given edge is present inside the shortest paths
		public Edge_2(int from, int to, int w) {
			this.from = from - 1;
			this.to = to - 1;
			this.weight = w;
		}

		@Override
		public String toString() {
			return (this.weight + ": " + this.from + 1) + " -> " + (this.to + 1) + " : " + this.cnt;
		}
	}
}