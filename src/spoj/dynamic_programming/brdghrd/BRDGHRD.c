// http://www.spoj.com/problems/BRDGHRD/

#include <stdio.h>
#include <memory.h>

#define MAX_BRIDGES 100001
#define MAX_X 1000001

typedef
struct {
	int from;
	int to;
} Bridge;

Bridge bridges[MAX_BRIDGES];
Bridge bridges_tmp[MAX_BRIDGES];
int bridges_cnt;

int count[2 * MAX_X];

int lis[MAX_BRIDGES];
int lis_size;

int i;
void normalize() {
	for(i = 0; i < bridges_cnt; ++i) {
		bridges[i].from += MAX_X;
		bridges[i].to += MAX_X;
	}
}

void counting_sort_by_to() {
	memset(count, 0, sizeof count);
	for(i = 0; i < bridges_cnt; ++i) {
		count[bridges[i].to]++;
	}

	// cumulative sum
	count[0]--;
	for(i = 1; i < MAX_X * 2; ++i) {
		count[i] += count[i - 1];
	}

	for(i = bridges_cnt - 1; i >= 0; --i) {
		bridges_tmp[count[bridges[i].to]] = bridges[i];
		count[bridges[i].to]--;
	}
}

void counting_sort_by_from() {
	memset(count, 0, sizeof count);
	for(i = 0; i < bridges_cnt; ++i) {
		count[bridges_tmp[i].from]++;
	}

	// cumulative sum
	count[0]--;
	for(i = 1; i < MAX_X * 2; ++i) {
		count[i] += count[i - 1];
	}

	for(i = bridges_cnt - 1; i >= 0; --i) {
		bridges[count[bridges_tmp[i].from]] = bridges_tmp[i];
		count[bridges_tmp[i].from]--;
	}
}

void sort() {
	normalize();
	counting_sort_by_to();
	counting_sort_by_from();
}

void read_data() {
	scanf("%d", &bridges_cnt);
	for(i = 0; i < bridges_cnt; ++i) {
		scanf("%d", &bridges[i].from);
	}
	for(i = 0; i < bridges_cnt; ++i) {
		scanf("%d", &bridges[i].to);
	}
}

int needle;
int l;
int r;
int m;
int binary_search_rightmost() {
	l = 0;
	r = lis_size - 1;
	while(l < r) {
		m = (l + r + 1) >> 1;
		if(lis[m] > needle) {
			r = m - 1;
		} else {
			l = m;
		}
	}
	if(lis[l] > needle) {
		l--;
	}
	return l;
}

int idx;
void solve() {
	sort();
	//for(i = 0; i < bridges_cnt; ++i) {
	//	printf("%d -> %d \n", bridges[i].from, bridges[i].to);
	//}

	lis_size = 0;
	for(i = 0; i < bridges_cnt; ++i) {
		needle = bridges[i].to;
		if(lis_size == 0) {
			lis[0] = needle;
			lis_size++;
		} else {
			idx = binary_search_rightmost();
			if(idx + 1 < lis_size) {
				lis[idx + 1] = needle;
			} else {
				lis[idx + 1] = needle;
				lis_size++;
			}
		}
	}

	//printf("Result: %d\n", lis_size);
	printf("%d\n", lis_size);
}

int tests_num;
int main() {
	scanf("%d", &tests_num);
	while(tests_num--) {
		read_data();
		solve();
	}
	return 0;
}
