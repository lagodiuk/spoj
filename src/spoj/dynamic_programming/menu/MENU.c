// http://www.spoj.com/problems/MENU/

#include <stdio.h>
#include <stdint.h>
#include <memory.h>

#define MAX_DISHES_NUM 51
#define MAX_DAYS_NUM 21
#define MAX_BUDGET 100
#define MAX_COST 50
#define MAX_BENEFIT 10000
#define NEGATIVE_INFINITY -500000
#define Boolean int32_t
#define TRUE 1
#define FALSE 0

typedef
struct {
	int32_t cost;
	int32_t benefit;
} Dish;

Dish dishes[MAX_DISHES_NUM];
int32_t dishes_num;

int32_t memoized[MAX_BUDGET + 1][MAX_DAYS_NUM + 1][MAX_DISHES_NUM][2][2];

static inline int32_t max(int32_t a, int32_t b) {
	return (a > b) ? a : b;
}

int32_t benefit(int32_t budget, int32_t days, int32_t curr, Boolean prevEqualsCurr, Boolean prevEqualsPrevPrev) {
	if(budget < 0) {
		return NEGATIVE_INFINITY;
	}

	if(days == 0) {
		return 0;
	}

	if(memoized[budget][days][curr][prevEqualsCurr][prevEqualsPrevPrev] != -1) {
		return memoized[budget][days][curr][prevEqualsCurr][prevEqualsPrevPrev];
	}

	int32_t currBenefit = dishes[curr].benefit;
	if(prevEqualsCurr) {
		currBenefit = prevEqualsPrevPrev ? 0 : (currBenefit >> 1);
	}

	memoized[budget][days][curr][prevEqualsCurr][prevEqualsPrevPrev] = NEGATIVE_INFINITY;
	int32_t i;
	for(i = 0; i < dishes_num; ++i) {
		memoized[budget][days][curr][prevEqualsCurr][prevEqualsPrevPrev] = max(
			memoized[budget][days][curr][prevEqualsCurr][prevEqualsPrevPrev],
			benefit(budget - dishes[curr].cost, days - 1, i, (curr == i) ? TRUE : FALSE, prevEqualsCurr ? TRUE : FALSE) + currBenefit);
	}

	if(memoized[budget][days][curr][prevEqualsCurr][prevEqualsPrevPrev] < 0) {
		memoized[budget][days][curr][prevEqualsCurr][prevEqualsPrevPrev] = NEGATIVE_INFINITY;
	}

	return memoized[budget][days][curr][prevEqualsCurr][prevEqualsPrevPrev];
}

void backtrack(int32_t budget, int32_t days, int32_t curr) {
	
	Boolean prevEqualsCurr = FALSE;
	Boolean prevEqualsPrevPrev = FALSE;
	int32_t currBenefit;
	int32_t i;
	int32_t newBudget;
	int32_t candidate;

	while(days > 0) {
	
		printf("%d ", (curr + 1));

		currBenefit = dishes[curr].benefit;
		if(prevEqualsCurr) {
			currBenefit = prevEqualsPrevPrev ? 0 : (currBenefit >> 1);
		}

		newBudget = budget - dishes[curr].cost;

		for(i = 0; i < dishes_num; ++i) {

			if(days == 1) {
				candidate = currBenefit;
			} else {
				candidate = currBenefit 
					+ memoized[newBudget][days - 1][i][(curr == i) ? TRUE : FALSE][prevEqualsCurr];
			}


			if(memoized[budget][days][curr][prevEqualsCurr][prevEqualsPrevPrev] == candidate) {
				
				days -= 1;
				budget = newBudget;
				prevEqualsPrevPrev = prevEqualsCurr;
				prevEqualsCurr = (curr == i) ? TRUE : FALSE;
				curr = i;
				break;
			}
		}
	}

	printf("\n\n");
}

void solve(int32_t budget, int32_t days) {
	memset(memoized, -1, sizeof memoized);
	
	int32_t currDish;
	int32_t currBudget;
	int32_t currBenefit;

	int32_t maxBenefit = NEGATIVE_INFINITY;
	int32_t minBudget = -1;
	int32_t startDish = -1;

	for(currDish = 0; currDish < dishes_num; ++currDish) {
		for(currBudget = 0; currBudget <= budget; ++currBudget) {
			
			currBenefit = benefit(currBudget, days, currDish, FALSE, FALSE);
			
			if((currBenefit > maxBenefit) 
				|| ((currBenefit == maxBenefit) && (minBudget > currBudget))) {
				
				maxBenefit = currBenefit;
				minBudget = currBudget;
				startDish = currDish;
			}
		}
	}

	if(maxBenefit == NEGATIVE_INFINITY) {
		printf("0.0\n\n");
		return;
	}

	printf("%d.%d\n", maxBenefit / 10, maxBenefit % 10);
	backtrack(minBudget, days, startDish);
}

int main() {
	int32_t i;
	int32_t budget;
	int32_t days;
	
	scanf("%d %d %d", &days, &dishes_num, &budget);
	while(budget != 0 || days != 0 || dishes_num != 0) {
		for(i = 0; i < dishes_num; ++i) {
			scanf("%d %d", &dishes[i].cost, &dishes[i].benefit);
			dishes[i].benefit *= 10;
		}
		solve(budget, days);
		scanf("%d %d %d", &days, &dishes_num, &budget);
	}

	return 0;
}
