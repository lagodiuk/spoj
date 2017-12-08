// http://www.spoj.com/problems/KRECT/

#include <stdio.h>
#include <stdint.h>
#include <memory.h>

#define MAX_SIZE 101
#define ALPHABET_SIZE 26

char buffer[MAX_SIZE];
int board[MAX_SIZE][MAX_SIZE];
int width;
int height;
int k_different;

int32_t aggregated_rows[MAX_SIZE];

// START: Bits Count routine
int lookup_table[256];
int cardinality_linear_time(int x) {
	int result = 0;
	while(x) {
		x &= x - 1;
		result++;
	}
	return result;
}
void initialize_lookup_table() {
	int i;
	for(i = 0; i < 256; ++i) {
		lookup_table[i] = cardinality_linear_time(i);
	}
}
int cardinality_constant_time(int32_t x) {
	return lookup_table[ x        & 0xff]
		 + lookup_table[(x >> 8)  & 0xff]
		 + lookup_table[(x >> 16) & 0xff]
		 + lookup_table[(x >> 24) & 0xff];
}
// END: Bits Count routine

int32_t solve() {
	int32_t result = 0;

	int y_min;
	int y_max;
	int x_min;
	int x_max;
	int x;
	int32_t curr_accumulated;
	int cardinality;

	int memset_size = width * sizeof(int32_t);

	for(y_min = 0; y_min < height; ++y_min) {
		memset(aggregated_rows, 0, memset_size);

		for(y_max = y_min; y_max < height; ++y_max) {
			
			for(x = 0; x < width; ++x) {
				aggregated_rows[x] |= board[y_max][x];
			}

			for(x_min = 0; x_min < width; ++x_min) {
				curr_accumulated = 0;

				for(x_max = x_min; x_max < width; ++x_max) {
					curr_accumulated |= aggregated_rows[x_max];

					// cardinality = cardinality_constant_time(curr_accumulated);
					cardinality = lookup_table[ curr_accumulated        & 0xff]
								+ lookup_table[(curr_accumulated >> 8)  & 0xff]
								+ lookup_table[(curr_accumulated >> 16) & 0xff]
								+ lookup_table[(curr_accumulated >> 24) & 0xff];

					if(cardinality == k_different) {
						++result;
					}
				}
			}
		}
	}

	return result;
}

int main() {
	initialize_lookup_table();

	scanf("%d %d %d", &height, &width, &k_different);

	int i;
	int j;
	for(i = 0; i < height; ++i) {
		scanf("%s", buffer);
		for(j = 0; j < width; ++j) {
			board[i][j] = 1 << (buffer[j] - 'A');
		}
	}

	int32_t result = solve();

	printf("%d\n", result);
}
