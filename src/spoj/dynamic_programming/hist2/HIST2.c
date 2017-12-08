// http://www.spoj.com/problems/HIST2/

#include <stdint.h>
#include <stdio.h>

#define MAX_ARR_SIZE 15

uint64_t permutations_count[] = {
	-1, -1, 2, 2, 8, 12, 72, 144, 1152, 2880, 28800, 86400, 1036800, 3628800, 50803200, 203212800
};

uint32_t arr[MAX_ARR_SIZE];
uint32_t length;

void sort_arr() {
	int i;
	int j;
	uint32_t curr;
	for(i = 1; i < length; ++i) {
		curr = arr[i];
		j = i - 1;
		while(curr < arr[j]) {
			arr[j+1] = arr[j];
			--j;
		}
		arr[j+1] = curr;
	}
}

uint32_t calculate_max_perimeter() {
	sort_arr();

	uint32_t perimeter = 0;
	
	uint32_t i;
	if((length % 2) == 1) {
		for(i = 0; i < length; ++i) {
			if(i < length / 2) {
				perimeter -= 2 * arr[i];
			} else {
				perimeter += 2 * arr[i];
			}
		}
	} else {
		for(i = 0; i < length; ++i) {
			if(i < (length / 2) - 1) {
				perimeter -= 2 * arr[i];
			} else {
				if(i != (length / 2) - 1) {
					perimeter += 2 * arr[i];
				}
			}
		}
	}

	perimeter += 2 * length;

	return perimeter;
}

int main() {
	uint32_t i;
	uint32_t max_perimeter;
	uint64_t permutations_cnt;
	scanf("%d", &length);
	while(length) {
		for(i = 0; i < length; ++i) {
			scanf("%d", &arr[i]);
		}
		max_perimeter = calculate_max_perimeter();
		permutations_cnt = permutations_count[length];
		printf("%d %ld\n", max_perimeter, permutations_cnt);
		scanf("%d", &length);
	}
	return 0;
}
