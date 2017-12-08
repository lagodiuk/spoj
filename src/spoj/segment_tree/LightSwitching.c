// http://www.spoj.com/problems/LITE/

#include <stdio.h>

int ON[600000];
int LAZY[600000];

inline void lazyUpdate(int curr, int currL, int currR) {
	if(LAZY[curr]) {
		LAZY[curr] ^= 1;
		ON[curr] = (currR - currL + 1) - ON[curr];
		if(currR != currL) {
			int left = (curr << 1) + 1;
			int right = left + 1;
			LAZY[left] ^= 1;
			LAZY[right] ^= 1;
		}
	}
}

void toggle(int l, int r, int curr, int currL, int currR) {
	lazyUpdate(curr, currL, currR);
	
	if((r < currL) || (currR < l)) {
		return;
	}
	
	if((l <= currL) && (currR <= r)) {
		LAZY[curr] ^= 1;
		lazyUpdate(curr, currL, currR);
		return;
	}
	
	int left = (curr << 1) + 1;
	int right = left + 1;
	int mid = (currL + currR) >> 1;
	toggle(l, r, left, currL, mid);
	toggle(l, r, right, mid + 1, currR);
	ON[curr] = ON[left] + ON[right];
}

int query(int l, int r, int curr, int currL, int currR) {
	lazyUpdate(curr, currL, currR);
	
	if((r < currL) || (currR < l)) {
		return 0;
	}
	
	if((l <= currL) && (currR <= r)) {
		return ON[curr];
	}
	
	int left = (curr << 1) + 1;
	int right = left + 1;
	int mid = (currL + currR) >> 1;
	return query(l, r, left, currL, mid) + query(l, r, right, mid + 1, currR);
}

int main(void) {
	/*
	int left = 1;
	int right = 4;
	toggle(1, 2, 0, left, right);
	toggle(2, 4, 0, left, right);
	printf("%d\n", query(2, 3, 0, left, right));
	toggle(2, 4, 0, left, right);
	printf("%d\n", query(1, 4, 0, left, right));
	*/
	int left = 1;
	int right;
	int qNum;
	int qType;
	int l;
	int r;
	int i;
	scanf("%d %d", &right, &qNum);
	for(i = 0; i < qNum; i++) {
		scanf("%d %d %d", &qType, &l, &r);	
		if(qType) {
			printf("%d\n", query(l, r, 0, left, right));
		} else {
			toggle(l, r, 0, left, right);
		}
	}
	return 0;
}
