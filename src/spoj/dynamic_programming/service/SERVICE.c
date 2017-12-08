// http://www.spoj.com/problems/SERVICE/

#include <stdint.h>
#include <memory.h>
#include <stdio.h>

#define MAX_CITIES 201
#define MAX_ORDERS 1002
#define POSITIVE_INFINITY 2000002

int32_t cost[MAX_CITIES][MAX_CITIES];
int32_t cities_count;

int32_t orders[MAX_ORDERS];
int32_t orders_count;

int32_t memoized[MAX_CITIES][MAX_CITIES][MAX_ORDERS];

static inline int32_t min(int32_t a, int32_t b) {
	return (a < b) ? a : b;
}

static inline int32_t max(int32_t a, int32_t b) {
	return (a > b) ? a : b;
}

static int32_t min_cost(int32_t firstPos, int32_t secondPos, int32_t curr_order) {
	if(curr_order == orders_count - 1) {
		return 0;
	}

	if(memoized[firstPos][secondPos][curr_order + 1] != -1) {
		return memoized[firstPos][secondPos][curr_order + 1];
	}

	int32_t thirdPos = (curr_order == -1) ? 2 : orders[curr_order] - 1;

	if((thirdPos == firstPos) || (thirdPos == secondPos) || (firstPos == secondPos)) {

		// It is not allowed that two service workers are present at the same location
		memoized[firstPos][secondPos][curr_order + 1] = POSITIVE_INFINITY;
	
	} else {

		int32_t nextPos = orders[curr_order + 1] - 1;

		int32_t c1 = min_cost(firstPos, secondPos, curr_order + 1) + cost[thirdPos][nextPos];
		int32_t c2 = min_cost(min(secondPos, thirdPos), max(secondPos, thirdPos), curr_order + 1) + cost[firstPos][nextPos];
		int32_t c3 = min_cost(min(firstPos, thirdPos), max(firstPos, thirdPos), curr_order + 1) + cost[secondPos][nextPos];

		memoized[firstPos][secondPos][curr_order + 1] = min(c1, min(c2, c3));
	}

	return memoized[firstPos][secondPos][curr_order + 1];
}

int main() {
	int32_t testsNum;
	scanf("%d", &testsNum);

	int32_t i;
	int32_t j;
	int32_t result;

	while(testsNum--) {

		scanf("%d %d", &cities_count, &orders_count);
		
		for(i = 0; i < cities_count; ++i) {
			for(j = 0; j < cities_count; ++j) {
				scanf("%d", &cost[i][j]);
			}
		}

		for(i = 0; i < orders_count; ++i) {
			scanf("%d", &orders[i]);
		}

		memset(memoized, -1, sizeof memoized);
		result = min_cost(0, 1, -1);
		
		printf("%d\n", result);
	}

	return 0;
}
