// http://www.spoj.com/problems/CCHESS/

#include <stdio.h>
#include <stdint.h>

#define DIM 8
#define INFINITY 1000000

uint32_t dist[DIM * DIM][DIM * DIM];

int moves[8][2] = {
	{1, 2},
	{1, -2},
	{-1, 2},
	{-1, -2},
	{2, 1},
	{2, -1},
	{-2, 1},
	{-2, -1}
};

inline int vertex_idx(int x, int y) {
	return (y * DIM) + x;
}

inline uint32_t distance(int currX, int currY, int destX, int destY) {
	return currX * destX + currY * destY;
}

inline int is_valid(int x, int y) {
	return (x >= 0) && (x < DIM) && (y >= 0) && (y < DIM);
}

void precalculate_distances() {
	int i;
	int j;
	int k;
	for(i = 0; i < DIM * DIM; ++i) {
		for(j = 0; j < DIM * DIM; ++j) {
			dist[i][j] = INFINITY;
		}
		dist[i][i] = 0;
	}

	int currX;
	int currY;
	int destX;
	int destY;
	for(currX = 0; currX < DIM; ++currX) {
		for(currY = 0; currY < DIM; ++currY) {
			for(i = 0; i < 8; ++i) {
				destX = currX + moves[i][0];
				destY = currY + moves[i][1];
				if(is_valid(destX, destY)) {
					dist[vertex_idx(currX, currY)][vertex_idx(destX, destY)] = distance(currX, currY, destX, destY);
				}
			}
		}
	}

	for(k = 0; k < DIM * DIM; ++k) {
		for(i = 0; i < DIM * DIM; ++i) {
			for(j = 0; j < DIM * DIM; ++j) {
				if(dist[i][j] > dist[i][k] + dist[k][j]) {
					dist[i][j] = dist[i][k] + dist[k][j];
				}
			}
		}
	}
}

int main() {
	int srcX;
	int srcY;
	int destX;
	int destY;
	uint32_t result;
	
	precalculate_distances();

	while(scanf("%d %d %d %d", &srcX, &srcY, &destX, &destY) == 4 && !feof(stdin)) {
		result = dist[vertex_idx(srcX, srcY)][vertex_idx(destX, destY)]; 
		printf("%d\n", result);
	}

	return 0;
}
