// http://www.spoj.com/problems/BORW/

#include <stdint.h>
#include <stdio.h>

#define DIM 205
#define INFINITY 1000000

uint32_t arr[DIM];
uint32_t mem[DIM + 2][DIM + 2][DIM + 2];
uint32_t length;

inline uint32_t min(uint32_t a, uint32_t b) {
	return (a > b) ? b : a;
}

uint32_t solve() {
	uint32_t curr_idx;
	uint32_t prev_black_idx;
	uint32_t prev_white_idx;
	uint32_t ignoring;
	uint32_t black;
	uint32_t white;

	for(curr_idx = length + 1; curr_idx >= 1; --curr_idx) {
		for(prev_black_idx = 0; prev_black_idx <= curr_idx; ++prev_black_idx) {
			for(prev_white_idx = 0; prev_white_idx <= curr_idx; ++prev_white_idx) {

				if(curr_idx == length + 1) {
					mem[curr_idx][prev_black_idx][prev_white_idx] = 0;

				} else {
					ignoring = mem[curr_idx + 1][prev_black_idx][prev_white_idx] + 1;

					black = ((prev_black_idx == 0) || (arr[prev_black_idx - 1] < arr[curr_idx - 1]))
							? mem[curr_idx + 1][curr_idx][prev_white_idx]
							: INFINITY;

					white = ((prev_white_idx == 0) || (arr[prev_white_idx - 1] > arr[curr_idx - 1]))
							? mem[curr_idx + 1][prev_black_idx][curr_idx]
							: INFINITY;

					mem[curr_idx][prev_black_idx][prev_white_idx] = min(ignoring, min(black, white));
				}
			}
		}
	}

	return mem[1][0][0];
}

int main() {
	uint32_t i;
	uint32_t result;

	scanf("%d", &length);
	while(length != -1) {
		for(i = 0; i < length; ++i) {
			scanf("%d", &arr[i]);
		}
		result = solve();
		printf("%d\n", result);
		scanf("%d", &length);
	}
	return 0;
}
