// http://www.spoj.com/problems/BRIDGE/

#include <stdio.h>
#include <stdlib.h>

#define MAX_SIZE 1005

typedef
struct {
	int from;
	int to;
} Bridge;

Bridge bridges[MAX_SIZE];
int count; // amount of bridges

int memoized[MAX_SIZE][2];
int curr_memoized;
int prev_memoized;
int curr;
int prev;
int excluding_subproblem;
int including_subproblem;

int bridge_compare_func(const void * v1, const void * v2) {
	Bridge * b1 = (Bridge *) v1;
	Bridge * b2 = (Bridge *) v2;
	return (b1->from == b2->from)
		 ? (b1->to - b2->to)
		 : (b1->from - b2->from);
}

int solve() {
	qsort(bridges, count, sizeof(Bridge), bridge_compare_func);

	curr_memoized = 0;
	prev_memoized = 1;

	for(curr = count - 1; curr >= 0; --curr) {
		for(prev = -1; prev < count; ++prev) {
			if((prev != -1) && (bridges[curr].to < bridges[prev].to)) {
				memoized[prev + 1][curr_memoized] = (curr + 1 == count)
													? 0
													: memoized[prev + 1][prev_memoized];
			} else {
				excluding_subproblem = (curr + 1 == count)
									   ? 0
									   : memoized[prev + 1][prev_memoized];

				including_subproblem = ((curr + 1 == count)
									    ? 0
										: memoized[curr + 1][prev_memoized]) + 1;

				memoized[prev + 1][curr_memoized] = (including_subproblem > excluding_subproblem)
													? including_subproblem
													: excluding_subproblem;
			}
		}
		curr_memoized ^= 1;
		prev_memoized ^= 1;
	}

	return memoized[0][prev_memoized];
}

int main() {
	int testsNum;
	scanf("%d", &testsNum);

	int i;
	int result;

	while(testsNum--) {
		scanf("%d", &count);
		for(i = 0; i < count; ++i) {
			scanf("%d", &(bridges[i].from));
		}
		for(i = 0; i < count; ++i) {
			scanf("%d", &(bridges[i].to));
		}
		result = solve();
		printf("%d\n", result);
	}

	return 0;
}
