// http://www.spoj.com/problems/NAJKRACI/

#include <stdint.h>
#include <stdio.h>

#define DEBUG 0

#define boolean int
#define True 1
#define False 0

#define MAX_VERTICES 1502
#define MAX_EDGES 5002
#define MOD 1000000007
#define INF 100000000

typedef
struct {
	int32_t from;
	int32_t to;
	int32_t weight;
	int32_t original_idx;
	int64_t cnt; // count
} Edge;

typedef
struct {
	int32_t edges_idx; // index of the first outgoing edge
	int32_t sp_edges_cnt; // amount of edges, which belong to the shortest path
	int32_t dist; // current shortest distance to the vertex
	int32_t pq_idx; // index inside the priority queue
	// amount of vertices, reachable from given vertex through the outgoing edges
	// (including the current vertex)
	int64_t outgoing_cnt; 
	// amount of ingoing paths into the current vertex
	int64_t ingoing_cnt;
	boolean processed;
} Vertex;

int edges_cnt;
int vertices_cnt;

Edge edges[MAX_EDGES];
Edge edges_sorted[MAX_EDGES];
int idxs_edges_shortest[MAX_EDGES];

Vertex vertices[MAX_VERTICES];

int count_from[MAX_EDGES];
void sort_edges() {
	int i;
	for(i = 0; i < edges_cnt; ++i) {
		count_from[edges[i].from]++;
	}
	count_from[0]--;
	for(i = 1; i < MAX_VERTICES; ++i) {
		count_from[i] += count_from[i - 1];
	}
	for(i = edges_cnt - 1; i >= 0; --i) {
		edges_sorted[count_from[edges[i].from]] = edges[i];
		count_from[edges[i].from]--;
	}
}

void initialize_vertices() {
	int i;
	for(i = 0; i < vertices_cnt; ++i) {
		vertices[i].edges_idx = -1;
	}

	vertices[edges_sorted[0].from].edges_idx = 0;
	for(i = 1; i < edges_cnt; ++i) {
		if(edges_sorted[i].from != edges_sorted[i - 1].from) {
			vertices[edges_sorted[i].from].edges_idx = i;
		}
	}
}

void reinitialize_vertices() {
	int i;
	for(i = 0; i < vertices_cnt; ++i) {
		vertices[i].dist = INF;
		vertices[i].pq_idx = -1;
		vertices[i].outgoing_cnt = 1;
		vertices[i].ingoing_cnt = 0;
		vertices[i].processed = False;
		vertices[i].sp_edges_cnt = 0;
	}
}

// Priority Queue
	int q[MAX_VERTICES]; // queue
	int qs; // queue size

	int left(int curr) {
		return (curr << 1) + 1;
	}

	int right(int curr) {
		return (curr << 1) + 2;
	}

	int parent(int curr) {
		return (curr - 1) >> 1;
	}

	void swap(int a, int b) {
		int tmp = q[a];
		q[a] = q[b];
		q[b] = tmp;
		vertices[q[a]].pq_idx = a;
		vertices[q[b]].pq_idx = b;
	}

	void pushUp(int idx) {
		int p;
		while(True) {
			if(!idx) {
				return;
			}
			p = parent(idx);
			if(vertices[q[p]].dist > vertices[q[idx]].dist) {
				swap(idx, p);
				idx = p;
			} else {
				return;
			}
		}
	}

	void pushDown(int idx) {
		int min;
		int r;
		while(True) {
			min = left(idx);
			if(min >= qs) {
				return;
			}
			r = right(idx);
			if((r < qs) && (vertices[q[r]].dist < vertices[q[min]].dist)) {
				min = r;
			}
			if(vertices[q[idx]].dist > vertices[q[min]].dist) {
				swap(idx, min);
				idx = min;
			} else {
				return;
			}
		}
	}
	
	void enqueue(int idx) {
		q[qs] = idx;
		vertices[idx].pq_idx = qs;
		qs++;
		pushUp(qs - 1);
	}

	int dequeue() {
		int result = q[0];
		
		int last = q[qs - 1];
		qs--;
		if(qs) {
			q[0] = last;
			vertices[last].pq_idx = 0;
			pushDown(0);
		}

		vertices[result].pq_idx = -1;
		return result;
	}

	void update(int v, int new_dist) {
		vertices[v].dist = new_dist;
		if(vertices[v].pq_idx == -1) {
			enqueue(v);
		} else {
			pushUp(vertices[v].pq_idx);
		}
	}
// Priority Queue

void dijkstra_calc_dist(int start) {
	
	update(start, 0);
	int curr;
	int e;
	int to;
	int tmp_dist;

	while(qs) {
		curr = dequeue();
		
		if(vertices[curr].edges_idx == -1) {
			continue;
		}
		e = vertices[curr].edges_idx;
		while(edges_sorted[e].from == curr) {
			to = edges_sorted[e].to;
			tmp_dist = vertices[curr].dist + edges_sorted[e].weight;
			if(vertices[to].dist > tmp_dist) {
				update(to, tmp_dist);
			}
			e++;
		}
	}
}

void dijkstra_calc_ingoing(int start) {
	
	vertices[start].ingoing_cnt = 1;
	update(start, 0);

	int curr;
	int e;
	int to;
	int tmp_dist;
	int to_idx;

	while(qs) {
		curr = dequeue();

		if(vertices[curr].edges_idx == -1) {
			continue;
		}
		e = vertices[curr].edges_idx;
		while(edges_sorted[e].from == curr) {
			
			tmp_dist = vertices[curr].dist + edges_sorted[e].weight;
			to_idx = edges_sorted[e].to;

			if(vertices[to_idx].dist == tmp_dist) {
				
				vertices[to_idx].ingoing_cnt = 
					(vertices[to_idx].ingoing_cnt + vertices[curr].ingoing_cnt) % MOD;
				
				update(to_idx, tmp_dist);
				
				idxs_edges_shortest[vertices[curr].edges_idx + vertices[curr].sp_edges_cnt] = e;
				vertices[curr].sp_edges_cnt++;
			}
			e++;
		}
	}
}

// Stack
	int vertex_idxs[MAX_EDGES + 2];
	int parent_vertex_idxs[MAX_EDGES + 2];
	int ss; // stack size

	void push(int vertex_idx, int parent_vertex_idx) {
		vertex_idxs[ss] = vertex_idx;
		parent_vertex_idxs[ss] = parent_vertex_idx;
		ss++;
	}

	int top_vertex_idx() {
		return vertex_idxs[ss - 1];
	}

	int top_parent_vertex_idx() {
		return parent_vertex_idxs[ss - 1];
	}

	void pop() {
		ss--;
	}
// Stack

void dfs_calc_outgoing_non_recursive(int root_idx) {
	push(root_idx, -1);

	int curr_idx;
	int parent_idx;
	int e;
	int i;
	int to_idx;
	
	while(ss) {
		curr_idx = top_vertex_idx();
		parent_idx = top_parent_vertex_idx();

		if(vertices[curr_idx].processed) {
			if(curr_idx != root_idx) {
				vertices[parent_idx].outgoing_cnt = 
					(vertices[parent_idx].outgoing_cnt + vertices[curr_idx].outgoing_cnt) % MOD;
			}
			pop();
			continue;
		}

		if(vertices[curr_idx].sp_edges_cnt) {
			for(i = 0; i < vertices[curr_idx].sp_edges_cnt; ++i) {
				e = idxs_edges_shortest[vertices[curr_idx].edges_idx + i];
				to_idx = edges_sorted[e].to;
				if(vertices[to_idx].processed) {
					vertices[curr_idx].outgoing_cnt = 
						(vertices[curr_idx].outgoing_cnt + vertices[to_idx].outgoing_cnt) % MOD;
				} else {
					push(to_idx, curr_idx);
				}
			}
		}

		vertices[curr_idx].processed = True;
	}

	for(i = 0; i < vertices_cnt; ++i) {
		vertices[i].processed = False;
	}
}

void dfs_calc_variants_non_recursive(int root_idx) {

	push(root_idx, -1);
	vertices[root_idx].processed = True;

	int curr_idx;
	int e;
	int to_idx;
	int i;

	while(ss) {
		curr_idx = top_vertex_idx();
		pop();

		if(vertices[curr_idx].sp_edges_cnt) {
			for(i = 0; i < vertices[curr_idx].sp_edges_cnt; ++i) {
				
				e = idxs_edges_shortest[vertices[curr_idx].edges_idx + i];
				to_idx = edges_sorted[e].to;
				
				edges_sorted[e].cnt = 
					(edges_sorted[e].cnt + ((vertices[curr_idx].ingoing_cnt * vertices[to_idx].outgoing_cnt) % MOD)) % MOD;

				if(!vertices[to_idx].processed) {
					push(to_idx, -1);
					vertices[to_idx].processed = True;
				}
			}
		}
	}
}

int main() {
	int i;
	scanf("%d %d", &vertices_cnt, &edges_cnt);
	for(i = 0; i < edges_cnt; ++i) {
		scanf("%d %d %d", &edges[i].from, &edges[i].to, &edges[i].weight);
		edges[i].from--;
		edges[i].to--;
		edges[i].original_idx = i;
		edges[i].cnt = 0;
	}
	sort_edges();
	
	if(DEBUG) { // debug
		printf("\n");
		for(i = 0; i < edges_cnt; ++i) {
			printf("%d %d (%d) [%d] \n", edges_sorted[i].from, edges_sorted[i].to, edges_sorted[i].weight,
			edges_sorted[i].original_idx);
		}
	}

	initialize_vertices();

	int e;

	for(i = 0; i < vertices_cnt; ++i) {
		reinitialize_vertices();
		
		dijkstra_calc_dist(i);

		if(DEBUG) { // debug
			int j;
			printf("\n");
			for(j = 0; j < vertices_cnt; ++j) {
				printf("dist from %d to %d is %d\n", i, j, vertices[j].dist);
			}
		}

		dijkstra_calc_ingoing(i);

		if(DEBUG) { // debug
			int j;
			printf("\n");
			for(j = 0; j < vertices_cnt; ++j) {
				printf("ingoing from %d to %d is %lld\n", i, j, vertices[j].ingoing_cnt);
			}
		}

		dfs_calc_outgoing_non_recursive(i);

		if(DEBUG) { // debug
			int j;
			printf("\n");
			for(j = 0; j < vertices_cnt; ++j) {
				printf("outgoing from %d to %d is %lld\n", i, j, vertices[j].outgoing_cnt);
			}
		}

		dfs_calc_variants_non_recursive(i);		
	}

	for(e = 0; e < edges_cnt; ++e) {
		edges[edges_sorted[e].original_idx].cnt = edges_sorted[e].cnt;
	}

	for(e = 0; e < edges_cnt; ++e) { 
		printf("%lld\n", edges[e].cnt);
	}

	return 0;
}
