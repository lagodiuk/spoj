
#include <stdio.h>
#include <stdint.h>

#define MAX_ACTIVITIES 100005
#define MOD 100000000
#define TRUE 1
#define FALSE 0

typedef
struct {
	int32_t from;
	int32_t to;
} Activity;

Activity activities[MAX_ACTIVITIES];
int32_t length;

int32_t memoized[MAX_ACTIVITIES];

// START: Heap Sort O(N * log(N))

Activity tmp;
int32_t max;
int32_t r;
void push_down(int32_t curr, int max_idx) {
	while(curr <= max_idx) {
		max = (curr << 1) + 1;
		if(max > max_idx) {
			return;
		}

		r = (curr << 1) + 2;
		if((r <= max_idx) && (activities[r].from > activities[max].from)) {
			max = r;
		}

		if(activities[max].from > activities[curr].from) {
			tmp = activities[curr];
			activities[curr] = activities[max];
			activities[max] = tmp;
			curr = max;
		} else {
			return;
		}
	}
}

void sort_activities() {
	int32_t i;
	int32_t max_idx = length - 1;

	// Build max-heap
	for(i = length / 2 + 1; i >= 0; --i) {
		push_down(i, max_idx);
	}

	// Sort
	while(max_idx >= 0) {
		tmp = activities[0];
		activities[0] = activities[max_idx];
		activities[max_idx] = tmp;
		push_down(0, max_idx - 1);
		--max_idx;
	}
}

// END: Heap Sort O(N * log(N))

int read_test_case() {
	scanf("%d", &length);
	if(length == -1){
		return TRUE;
	}

	int32_t i;
	for(i = 0; i < length; ++i) {
		scanf("%d %d", &activities[i].from, &activities[i].to);
	}

	return FALSE;
}

int32_t solve() {
	sort_activities();

	memoized[length] = 1;

	int32_t curr = length - 1;
	
	int32_t left;
	int32_t right;
	int32_t mid;
	
	while(curr >= 0) {
		memoized[curr] = memoized[curr + 1];

		left = curr + 1;
		right = length;
		while(left < right) {
			mid = (left + right) >> 1;
			if(activities[mid].from < activities[curr].to) {
				left = mid + 1;
			} else {
				right = mid;
			}
		}

		memoized[curr] = (memoized[curr] + memoized[left]) % MOD;
		--curr;
	}

	return (memoized[0] - 1 + MOD) % MOD;
}

int main() {
	int should_terminate;
	int32_t result;
	while(TRUE) {
		should_terminate = read_test_case();
		if(should_terminate) {
			break;
		}

		result = solve();
		printf("%08d\n", result);
	}
	return 0;
}
