// http://www.spoj.com/problems/MNERED/

#include <stdio.h>

#define DIMENSION 105

int field[DIMENSION][DIMENSION];
int dimension;
int amount_of_entities;

void calculate_cumulative_2d_matrix() {
	int y;
	int x;

	for(x = 1; x <= dimension; ++x) {
		for(y = 0; y <= dimension; ++y) {
			field[y][x] += field[y][x - 1];
		}
	}

	for(y = 1; y <= dimension; ++y) {
		for(x = 0; x <= dimension; ++x) {
			field[y][x] += field[y - 1][x];
		}
	}
}

int solve() {
	// w - width of sliding window
	// h - height of sliding window

	int w = 1;
	int h;
	int tmp;
	int y;
	int x;
	int curr_non_empty_cells_cnt;
	int max_non_empty_cells_cnt;

	while(w * w <= amount_of_entities + 1) {
		if(amount_of_entities % w == 0) {
			h = amount_of_entities / w;

			if((w <= dimension) && (h <= dimension)) {
				
				// horizontal sliding window
				for(y = h; y <= dimension; ++y) {
					for(x = w; x <= dimension; ++x) {
						curr_non_empty_cells_cnt =   field[y][x] 
												   - field[y - h][x] 
												   - field[y][x - w] 
												   + field[y - h][x - w];
					
						if(curr_non_empty_cells_cnt > max_non_empty_cells_cnt) {
							max_non_empty_cells_cnt = curr_non_empty_cells_cnt;
						}
					}
				}
			
				// swap w and h
				tmp = w;
				w = h;
				h = tmp;

				// vertical sliding window
				for(y = h; y <= dimension; ++y) {
					for(x = w; x <= dimension; ++x) {
						curr_non_empty_cells_cnt =   field[y][x] 
												   - field[y - h][x] 
												   - field[y][x - w] 
												   + field[y - h][x - w];
					
						if(curr_non_empty_cells_cnt > max_non_empty_cells_cnt) {
							max_non_empty_cells_cnt = curr_non_empty_cells_cnt;
						}
					}
				}

				// restore original value of w
				w = h;
			}
		}
		w++;
	}

	return amount_of_entities - max_non_empty_cells_cnt;
}

int main() {
	scanf("%d %d", &dimension, &amount_of_entities);

	int i;
	int x;
	int y;
	for(i = 0; i < amount_of_entities; ++i) {
		scanf("%d %d", &y, &x);
		field[y][x] = 1;
	}

	calculate_cumulative_2d_matrix();

	int result = solve();
	printf("%d\n", result);

	return 0;
}
