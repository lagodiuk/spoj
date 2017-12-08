// http://www.spoj.com/problems/BAT3/

#include <stdio.h>

#define MAX_SIZE 1005

int height[MAX_SIZE];
int memoized[MAX_SIZE];
int length;
int mini_bat_pos;

int curr;
int i;
int result;
void solve() {
	for(curr = length - 1; curr >= 0; --curr) {
		memoized[curr] = 0;
		for(i = curr + 1; i < length; ++i) {
			
			if(((height[i] < height[curr]) || (mini_bat_pos == curr)) 
				&& (memoized[i] > memoized[curr])) {

				memoized[curr] = memoized[i];
			}
		}
		memoized[curr] += 1;
	}

	result = -1;
	for(i = 0; i < length; i++) {
		if(memoized[i] > result) {
			result = memoized[i];
		}
	}
}

int main() {
	int testsNum;
	scanf("%d", &testsNum);

	while(testsNum--) {
		scanf("%d %d", &length, &mini_bat_pos);
		for(i = 0; i < length; ++i) {
			scanf("%d", &height[i]);
		}
		solve();
		printf("%d\n", result);
	}

	return 0;
}
