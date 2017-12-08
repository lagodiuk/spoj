// http://www.spoj.com/problems/STREET/

#include <stdint.h>
#include <stdio.h>

#define MAX_WIDTH 502
#define POSITIVE_INFINITY 60000

int32_t max_height[MAX_WIDTH];
int32_t width;

int32_t memoized[MAX_WIDTH][MAX_WIDTH];

int32_t max_width;
int32_t available_buildings;

// Loop variables:
int32_t curr_start_idx;
int32_t b; // curent amount of available buildings
int32_t w; // current width
int32_t min_height;
int32_t sub_problem_solution;
int32_t result;

static inline int32_t min(int32_t a, int32_t b) {
	return (a < b) ? a : b;
}

static inline int32_t max(int32_t a, int32_t b) {
	return (a > b) ? a : b;
}

static void solve() {
	for(curr_start_idx = width - 1; curr_start_idx >= 0; --curr_start_idx) {
		for(b = 1; b <= available_buildings; ++b) {
			
			min_height = POSITIVE_INFINITY;
			
			for(w = 0; (w < max_width) && (curr_start_idx + w < width); ++w) {
				
				min_height = min(min_height, max_height[curr_start_idx + w]);

				sub_problem_solution = (curr_start_idx + w + 1 < width) && (b > 1)
										? memoized[curr_start_idx + w + 1][b - 1]
										: 0;

				memoized[curr_start_idx][b] = max(
								memoized[curr_start_idx][b], 
								sub_problem_solution + (w + 1) * min_height);
			}

			sub_problem_solution = (curr_start_idx + 1 < width) 
									? memoized[curr_start_idx + 1][b]
									: 0;
			
			memoized[curr_start_idx][b] = max(
								memoized[curr_start_idx][b], 
								sub_problem_solution);

		}
	}

	result = 0;
	for(b = 1; b <= available_buildings; ++b) {
		result = max(result, memoized[0][b]);
	}
}

int main() {
	int32_t i;

	scanf("%d %d %d", &width, &available_buildings, &max_width);
	for(i = 0; i < width; ++i) {
		scanf("%d", &max_height[i]);
	}
	solve();
	printf("%d\n", result);
	
	return 0;
}
