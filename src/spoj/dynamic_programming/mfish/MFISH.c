
#include <stdint.h>
#include <stdio.h>
#include <memory.h>

#define MAX_RIVER_LEN 100005
#define NEGATIVE_INFINITY -100000000
#define BOAT_ABSENT -1

typedef 
struct {
	int32_t anchor;
	int32_t length;
	int32_t min_pos;
} Boat;

Boat boats_input[MAX_RIVER_LEN];
int32_t boat_position[MAX_RIVER_LEN];
Boat boats[MAX_RIVER_LEN];
int32_t boats_num;

int32_t fish[MAX_RIVER_LEN];
int32_t fish_num;

int32_t fish_acc[MAX_RIVER_LEN];
int32_t fish_covered[MAX_RIVER_LEN];
int32_t boat_idxs[MAX_RIVER_LEN];

int32_t result[MAX_RIVER_LEN];

void calculate_cumulative_preffix_sum() {
	fish_acc[0] = 0;
	int32_t i;
	for(i = 1; i <= fish_num; ++i) {
		fish_acc[i] = fish_acc[i - 1] + fish[i - 1];
	}
}

int32_t count_fish(int32_t from, int32_t to) {
	return fish_acc[to] - fish_acc[from - 1];
}

int32_t min(int32_t a, int32_t b) {
	return (a < b) ? a : b;
}

int32_t max(int32_t a, int32_t b) {
	return (a > b) ? a : b;
}

void prepare_helpful_data() {
	int32_t b;
	int32_t from;
	int32_t to;
	int32_t i;

	// initialize available start positions of the boats
	memset(boat_idxs, BOAT_ABSENT, sizeof boat_idxs);
	// initialize amount of covered fish
	for(i = 0; i <= fish_num; ++i) {
		fish_covered[i] = NEGATIVE_INFINITY;
	}

	for(b = 0; b < boats_num; ++b) {
		// the range of avaulable start positions for the current boat
		from = max(boats[b].min_pos, ((b > 0) ? (boats[b - 1].anchor + 1) : -1));
		to = min(fish_num + 1 - boats[b].length, boats[b].anchor) + 1;
	
		// calculate amount of covered fish for each start position
		for(i = from; i < to; ++i) {
			fish_covered[i] = count_fish(i, i + boats[b].length - 1);
			boat_idxs[i] = b;
		}
	}

	// just get rid of "-1" in array of boat start positions
	for(i = fish_num - 1; i >= 0; --i) {
		if(boat_idxs[i] == -1) {
			boat_idxs[i] = boat_idxs[i + 1];
		}
	}
}

int32_t solve() {
	calculate_cumulative_preffix_sum();
	prepare_helpful_data();

	// skip unreachable cells in the end of the river
	int32_t curr = fish_num;
	while(boat_idxs[curr] == -1) {
		--curr;
	}

	// now, we reached to the last boat
	int32_t curr_boat_idx = boat_idxs[curr];
	while((curr >= 0) && (curr_boat_idx == boat_idxs[curr])) {
		result[curr] = fish_covered[curr];
		result[curr] = max(result[curr], ((curr + 1) <= fish_num ? result[curr + 1] : 0));
		--curr;
	}

	int32_t next;
	while(curr >= 0) {
		curr_boat_idx = boat_idxs[curr];
		if(curr_boat_idx == -1) {
			break;
		}

		result[curr] = NEGATIVE_INFINITY;
		next = curr + boats[curr_boat_idx].length;
		// if index of the next board differs only by 1 from the index of current boat
		if((next <= fish_num) && (boat_idxs[next] == curr_boat_idx + 1)) {
			result[curr] = fish_covered[curr] + result[next];
		}

		// in case if the following position belongs to the current boat as well
		if((curr + 1 <= fish_num) && (boat_idxs[curr + 1] == curr_boat_idx)) {
			result[curr] = max(result[curr], result[curr + 1]);
		}

		--curr;
	}

	return result[curr + 1];
}

void read_input_data() {
	FILE * in;
	in = fopen("brodovi.in", "r");

	fscanf(in, "%d", &fish_num);
	int32_t i;

	for(i = 0; i < fish_num; ++i) {
		fscanf(in, "%d", &fish[i]);
	}

	fscanf(in, "%d", &boats_num);
	int32_t anchor;
	int32_t length;
	int32_t min_pos;

	for(i = 0; i < boats_num; ++i) {
		fscanf(in, "%d %d", &anchor, &length);

		boats_input[i].anchor = anchor;
		boats_input[i].length = length;
		
		min_pos = anchor - length + 1;
		min_pos = (min_pos) < 1 ? 1 : min_pos;
		boats_input[i].min_pos = min_pos;

		// first step of Counting Sort
		boat_position[anchor] = 1;
	}

	fclose(in);

	// second step of Counting Sort (calculation of cumulative sum)
	for(i = 1; i <= fish_num; ++i) {
		boat_position[i] += boat_position[i - 1];
	}

	// sort boats by ascending of anchor position
	for(i = 0; i < boats_num; ++i) {
		boats[boat_position[boats_input[i].anchor] - 1] = boats_input[i];
	}
}

int main() {
	read_input_data();
	int32_t result = solve();

	FILE * out;
	out = fopen("brodovi.out", "w");

	fprintf(out, "%d\n", result);

	fclose(out);

	/*
	int32_t i;

	for(i = 0; i < boats_num; i++) {
		printf("~ boat: %d %d %d\n", boats[i].anchor, boats[i].length, boats[i].min_pos);
	}

	for(i = 0; i <= fish_num; ++i) {
		printf("%d ", fish_acc[i]);
	}
	printf("\n");

	for(i = 0; i <= fish_num; ++i) {
		printf("%d ", fish_covered[i]);
	}
	printf("\n");

	for(i = 0; i <= fish_num; ++i) {
		printf("%d ", boat_idxs[i]);
	}
	printf("\n");
	*/

	return 0;
}
