// http://www.spoj.com/problems/SCALES/

#include <stdio.h>

#define MAX_LENGTH 1000002

char bitstr[MAX_LENGTH];
int scales[MAX_LENGTH];
int length;
int mod;

int prev_carry_0;
int prev_carry_1;
int curr_carry_0;
int curr_carry_1;
int pos;

int solve() {
	prev_carry_0 = 1;
	prev_carry_1 = 0;
	pos = 0;

	while(pos < length) {
		if(scales[pos]) {
			curr_carry_1 = prev_carry_1;
			curr_carry_0 = (prev_carry_0 + prev_carry_1) % mod;
		} else {
			curr_carry_0 = prev_carry_0;
			curr_carry_1 = (prev_carry_0 + prev_carry_1) % mod;
		}
		++pos;
		prev_carry_0 = curr_carry_0;
		prev_carry_1 = curr_carry_1;
	}

	return prev_carry_0;
}

int main() {
	int testsNum;
	int bitstrLen;
	int i;
	int result;

	scanf("%d", &testsNum);
	while(testsNum--) {
		scanf("%d %d %d", &length, &bitstrLen, &mod);

		scanf("%s", bitstr);
		for(i = 0; i < length - bitstrLen; ++i) {
			scales[i] = 0;
		}
		for(i = 0; i < bitstrLen; ++i) {
			scales[i + length - bitstrLen] = (bitstr[i] == '1') ? 1 : 0;
		}

		result = solve();
		printf("%d\n", result);
	}

	return 0;
}
