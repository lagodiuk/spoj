// http://www.spoj.com/problems/UPSUB/

#include <stdio.h>
#include <string.h>
#include <memory.h>

#define MAX_LENGTH 105
#define NEGATIVE_INFINITY -100000

char str[MAX_LENGTH];
int str_len;

char stack[MAX_LENGTH];
int top = -1;

int memoized[MAX_LENGTH][MAX_LENGTH];

void find_longest_subseq() {

	memset(memoized, 0, sizeof memoized);

	int curr; // current index
	int prev; // previous index
	int incl;
	int excl;

	for(curr = str_len; curr >= 0; --curr) {
		for(prev = str_len - 1; prev >= -1; --prev) {
			
			if(curr == str_len) {
				memoized[prev + 1][curr] = 0;

			} else {
				
				incl = memoized[prev + 1][curr + 1];

				excl = ((prev == -1) || (str[curr] >= str[prev]))
					   ? memoized[curr + 1][curr + 1] + 1
					   : NEGATIVE_INFINITY;
			
				memoized[prev + 1][curr] = (incl > excl) ? incl : excl;
			}
		}
	}
}

void backtrack(int curr, int prev) {
	if(curr == str_len) {
		stack[top + 1] = '\0';
		printf("%s\n", stack);
		return;
	}

	if(memoized[prev + 1][curr] == memoized[prev + 1][curr + 1]) {
		backtrack(curr + 1, prev);
	}

	if(memoized[prev + 1][curr] == memoized[curr + 1][curr + 1] + 1) {
		++top;
		stack[top] = str[curr];
		backtrack(curr + 1, curr);
		--top;
	}
}

int main() {
	int testsNum;
	scanf("%d", &testsNum);

	while(testsNum--) {
		scanf("%s", str);
		str_len = strlen(str);
		find_longest_subseq();
		backtrack(0, -1);
		printf("\n");
	}

	return 0;
}
