// http://www.spoj.com/problems/VOCV/

#include <stdio.h>
#include <stdint.h>
#include <memory.h>

#define MAX_NODES 100020
#define MAX_EDGES 200040
#define MOD 10007
#define boolean int
#define TRUE 1
#define FALSE 0

typedef
struct {
	int32_t node1;
	int32_t node2;
} Edge;

typedef
struct {
	boolean visited;

	int32_t first_edge_idx;
	int32_t next_edge_idx;

	int32_t parent_node_idx;

	int32_t min_lights_incl_curr;
	int32_t variants_cnt_incl_curr;

	int32_t min_lights_excl_curr;
	int32_t variants_cnt_excl_curr;

	int32_t min_lights;
	int32_t variants_cnt;
} Node;

Node nodes[MAX_NODES];
Edge edges[MAX_EDGES];

Edge input[MAX_EDGES];
int32_t node_cnt[MAX_NODES];

int32_t edges_cnt;
int32_t total_edges_cnt;

void sort_edges_by_node1() {
	int32_t i;

	// count occurrence of key: "node1"
	for(i = 0; i < total_edges_cnt; ++i) {
		node_cnt[input[i].node1]++;
	}

	// transform to cumulative sum
	for(i = 1; i < edges_cnt + 1; ++i) {
		node_cnt[i] += node_cnt[i - 1];
	}

	for(i = total_edges_cnt - 1; i >= 0; --i) {
		edges[node_cnt[input[i].node1] - 1] = input[i];
		node_cnt[input[i].node1]--;
	}
}

void initialize_nodes() {
	nodes[0].visited = FALSE;

	nodes[0].first_edge_idx = 0;
	nodes[0].next_edge_idx = 0;
	
	nodes[0].parent_node_idx = -1;
	
	nodes[0].min_lights_incl_curr = 1;
	nodes[0].variants_cnt_incl_curr = 1;
	
	nodes[0].min_lights_excl_curr = 0;
	nodes[0].variants_cnt_excl_curr = 1;
	
	int32_t i;
	for(i = 1; i < total_edges_cnt; ++i) {
		if(edges[i].node1 != edges[i - 1].node1) {
			nodes[edges[i].node1].visited = FALSE;

			nodes[edges[i].node1].first_edge_idx = i;
			nodes[edges[i].node1].next_edge_idx = i;
			
			nodes[edges[i].node1].parent_node_idx = -1;
			
			nodes[edges[i].node1].min_lights_incl_curr = 1;
			nodes[edges[i].node1].variants_cnt_incl_curr = 1;
			
			nodes[edges[i].node1].min_lights_excl_curr = 0;
			nodes[edges[i].node1].variants_cnt_excl_curr = 1;
		}
	}
}

int32_t prev_child_idx;
void update_node(int32_t idx) {

	if((nodes[idx].next_edge_idx - 1 >= nodes[idx].first_edge_idx) 
		&& (edges[nodes[idx].next_edge_idx - 1].node2 != nodes[idx].parent_node_idx)) {

		prev_child_idx = edges[nodes[idx].next_edge_idx - 1].node2;

		nodes[idx].min_lights_incl_curr += nodes[prev_child_idx].min_lights;
		nodes[idx].variants_cnt_incl_curr = 
					(nodes[idx].variants_cnt_incl_curr * nodes[prev_child_idx].variants_cnt) % MOD;

		nodes[idx].min_lights_excl_curr += nodes[prev_child_idx].min_lights_incl_curr;
		nodes[idx].variants_cnt_excl_curr =
					(nodes[idx].variants_cnt_excl_curr * nodes[prev_child_idx].variants_cnt_incl_curr) % MOD;
	}
}

int32_t curr;
void solve() {
	sort_edges_by_node1();
	initialize_nodes();

/*
	int i;
	for(i = 0; i < total_edges_cnt; i++) {
		printf("%d -> %d\n", edges[i].node1, edges[i].node2);
	}
*/

	curr = 0;

	while(TRUE) {
		nodes[curr].visited = TRUE;
		update_node(curr);

//		printf("%d has %d next edge idx\n", curr, nodes[curr].next_edge_idx);

		if((nodes[curr].next_edge_idx < total_edges_cnt) 
			&& (edges[nodes[curr].next_edge_idx].node1 == edges[nodes[curr].first_edge_idx].node1)) {
			
			if(nodes[edges[nodes[curr].next_edge_idx].node2].visited) {
				nodes[curr].parent_node_idx = edges[nodes[curr].next_edge_idx].node2;
				nodes[curr].next_edge_idx += 1;
			} else {
				nodes[curr].next_edge_idx += 1;
				curr = edges[nodes[curr].next_edge_idx - 1].node2;
			}

		} else {

			if(nodes[curr].min_lights_incl_curr == nodes[curr].min_lights_excl_curr) {
				nodes[curr].min_lights = nodes[curr].min_lights_incl_curr;
				nodes[curr].variants_cnt = 
						(nodes[curr].variants_cnt_incl_curr + nodes[curr].variants_cnt_excl_curr) % MOD;

			} else if(nodes[curr].min_lights_incl_curr < nodes[curr].min_lights_excl_curr) {
				nodes[curr].min_lights = nodes[curr].min_lights_incl_curr;
				nodes[curr].variants_cnt = nodes[curr].variants_cnt_incl_curr;
				
			} else {
				nodes[curr].min_lights = nodes[curr].min_lights_excl_curr;
				nodes[curr].variants_cnt = nodes[curr].variants_cnt_excl_curr;
			}

//			printf("Parent of %d is %d\n", curr, nodes[curr].parent_node_idx);

			if(nodes[curr].parent_node_idx == -1) {
				break;
			} else {
				curr = nodes[curr].parent_node_idx;
			}
		}
	}
}

int main() {
	int testsNum;
	scanf("%d", &testsNum);

	int32_t i;
	int32_t node1;
	int32_t node2;
	int32_t pos;

	while(testsNum--) {
		scanf("%d", &edges_cnt);
		edges_cnt -= 1;
		total_edges_cnt = edges_cnt * 2;
		memset(node_cnt, 0, sizeof node_cnt);
		
		pos = 0;
		for(i = 0; i < edges_cnt; ++i) {
			scanf("%d %d", &node1, &node2);
			--node1; // because node indices start from 1
			--node2;

			input[pos].node1 = node1;
			input[pos].node2 = node2;

			input[pos + 1].node1 = node2;
			input[pos + 1].node2 = node1;

			pos += 2;
		}

		solve();
		printf("%d %d\n", nodes[0].min_lights, nodes[0].variants_cnt);
	}

	return 0;
}
