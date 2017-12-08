// http://www.spoj.com/problems/MKBUDGET/

#include <stdio.h>
#include <stdint.h>

#define MAX_COUNT 25
#define INFINITY 2000000000L

// array, which represents minimal required amount of workers
// for each month
int64_t workers[MAX_COUNT];

// number of months
int32_t length;

// cost for previous and current months
int64_t cost[2][MAX_COUNT];
int32_t prev;
int32_t curr;

// cost to hire new worker
int64_t hire;
// monthly salary of worker
int64_t salary;
// cost which company pys to worker, when he leaving company
int64_t severance;

int64_t find_minimum_cost() {
	int32_t m; // month index
	int32_t c; // current count index
	int32_t k; // previous count index

	prev = 0;
	curr = 1;

	for(m = 0; m < length; ++m) {
		cost[prev][m] = (workers[0] > workers[m]) 
						? INFINITY 
						: workers[m] * (salary + hire);
	}

	int64_t curr_cost;

	for(m = 1; m < length; ++m) {
		for(c = 0; c < length; ++c) {
			cost[curr][c] = INFINITY;
			if(workers[c] < workers[m]) {
				continue;
			}
			for(k = 0; k < length; ++k) {
				curr_cost = cost[prev][k] + workers[c] * salary;
				if(workers[c] < workers[k]) {
					curr_cost += (workers[k] - workers[c]) * severance;
				}
				if(workers[c] > workers[k]) {
					curr_cost += (workers[c] - workers[k]) * hire;
				}
				cost[curr][c] = (curr_cost > cost[curr][c])
								? cost[curr][c]
								: curr_cost;
			}
		}
		// swap prev and curr
		prev ^= 1;
		curr ^= 1;
	}

	int64_t result = INFINITY;
	for(c = 0; c < length; ++c) {
		result = (result > cost[prev][c])
				 ? cost[prev][c]
				 : result;
	}

	return result;
}

int main() {
	int i;
	int64_t result;
	scanf("%d", &length);

	int case_num = 1;
	
	while(length) {
		scanf("%lld %lld %lld", &hire, &salary, &severance);
		for(i = 0; i < length; ++i) {
			scanf("%lld", &workers[i]);
		}
		result = find_minimum_cost();
		printf("Case %d, cost = $%lld\n", case_num, result);
		scanf("%d", &length);
		case_num++;
	}

	return 0;
}
