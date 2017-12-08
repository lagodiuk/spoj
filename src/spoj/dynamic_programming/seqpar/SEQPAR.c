
#include <stdint.h>
#include <stdio.h>

#define MAX_ARR_SIZE 15005
#define INFINITY 1000000000

int32_t arr[MAX_ARR_SIZE];
int32_t arr_len;
int32_t count; // amount of intervals

int32_t memoized[MAX_ARR_SIZE][2];
int32_t prev_seg;
int32_t curr_seg;

int32_t solution;

void solve() {
	int32_t segments;
	int32_t curr;
	int32_t i;
	int32_t curr_sum;
	int32_t result;
	int32_t sub_problem;

	prev_seg = 0;
	curr_seg = 1;
	for(curr = arr_len; curr >= 0; --curr) {
		memoized[curr][prev_seg] = INFINITY;
	}
	memoized[arr_len][prev_seg] = -INFINITY;

	for(segments = 1; segments <= count; ++segments) {
		
		memoized[arr_len - segments + 1][curr_seg] = INFINITY;
		if(count - segments - 1 >= 0) {
			memoized[count - segments - 1][curr_seg] = INFINITY;
		}

		for(curr = arr_len - segments; curr >= count - segments; --curr) {
			
			result = INFINITY;
			curr_sum = 0;

			for(i = curr + 1; i <= arr_len - segments + 1; ++i) {
				
				curr_sum += arr[i - 1];
				sub_problem = (curr_sum > memoized[i][prev_seg])
							   ? curr_sum
							   : memoized[i][prev_seg];

				result = (sub_problem < result)
						  ? sub_problem
						  : result;
			}

			memoized[curr][curr_seg] = result;
		}

		prev_seg ^= 1;
		curr_seg ^= 1;
	}

	solution = memoized[0][prev_seg];
}

void read_iput_data() {
	scanf("%d %d", &arr_len, &count);
	int32_t i;
	for(i = 0; i < arr_len; ++i) {
		scanf("%d", &arr[i]);
	}
}

int main() {
	int32_t tests_num;
	scanf("%d", &tests_num);

	while(tests_num--) {
		read_iput_data();
		solve();
		printf("%d\n", solution);
	}

	return 0;
}
