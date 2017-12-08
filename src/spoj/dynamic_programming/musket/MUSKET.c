// http://www.spoj.com/problems/MUSKET/

#include <memory.h>
#include <stdio.h>

#define MAX_DIM 102
#define NOT_INITIALIZED -1
#define TRUE 1
#define FALSE 0

int A[MAX_DIM][MAX_DIM];
int can_win[MAX_DIM];
int length;

int memoized[2][MAX_DIM][MAX_DIM];

int solve(int left, int right, int parent_left, int offset) {

	if(memoized[parent_left][left][right] != NOT_INITIALIZED) {
		return memoized[parent_left][left][right]; 
	}

	int parent = parent_left ? left - 1 : right + 1;
	int real_parent_idx = (parent + offset) % length;
	int real_left_idx = (left + offset) % length;
	int real_right_idx = (right + offset) % length;

	if(left == right) {
		memoized[parent_left][left][right] = A[real_parent_idx][real_left_idx];
		return memoized[parent_left][left][right];
	}

	if(A[real_parent_idx][real_left_idx] 
		&& solve(left + 1, right, TRUE, offset)) {
		
		memoized[parent_left][left][right] = TRUE;
		return memoized[parent_left][left][right];
	}

	if(A[real_parent_idx][real_right_idx] 
		&& solve(left, right - 1, FALSE, offset)) {
		
		memoized[parent_left][left][right] = TRUE;
		return memoized[parent_left][left][right];
	}

	int k;
	int real_k_idx;
	
	for(k = left + 1; k <= right - 1; ++k) {
		real_k_idx = (k + offset) % length;
		
		if(A[real_parent_idx][real_k_idx] 
			&& solve(left, k - 1, FALSE, offset) 
			&& solve(k + 1, right, TRUE, offset)) {

			memoized[parent_left][left][right] = TRUE;
			return memoized[parent_left][left][right];
		}
	}

	memoized[parent_left][left][right] = FALSE;
	return memoized[parent_left][left][right];
}

void calculate_winners() {
	memset(can_win, 0, sizeof can_win);

	int offset;
	int parent_idx;
	int shifted_left_idx;
	int shifted_right_idx;
	int shifted_curr_idx;

	for(offset = 0; offset < length; ++offset) {
		memset(memoized, NOT_INITIALIZED, sizeof memoized);

		shifted_left_idx = (0 + offset) % length;
		shifted_right_idx = (length - 1 + offset) % length;

		if((!can_win[shifted_left_idx]) 
			&& solve(1, length - 1, TRUE, offset)) {
		
			can_win[shifted_left_idx] = TRUE;
		}

		if((!can_win[shifted_right_idx]) 
			&& solve(0, length - 2, FALSE, offset)) {
		
			can_win[shifted_right_idx] = TRUE;
		}

		for(parent_idx = 1; parent_idx <= length - 2; ++parent_idx) {
			shifted_curr_idx = (parent_idx + offset) % length;

			if(!can_win[shifted_curr_idx]) {
				if(solve(0, parent_idx - 1, FALSE, offset) 
					&& solve(parent_idx + 1, length - 1, TRUE, offset)) {

					can_win[shifted_curr_idx] = TRUE;
				}
			}
		}
	}
}

int main() {
	int testsNum;
	scanf("%d", &testsNum);

	int i;
	int j;
	char str[MAX_DIM + 5];
	int num_winners;

	while(testsNum--) {
		scanf("%d", &length);
		for(i = 0; i < length; ++i) {
			scanf("%s", str);
			for(j = 0; j < length; ++j) {
				A[i][j] = (str[j] == '1') ? 1 : 0;
			}
		}
		calculate_winners();

		num_winners = 0;
		for(i = 0; i < length; ++i) {
			if(can_win[i]) {
				++num_winners;
			}
		}

		printf("%d\n", num_winners);
		for(i = 0; i < length; ++i) {
			if(can_win[i]) {
				printf("%d\n", (i + 1));
			}
		}
	}

	return 0;
}
