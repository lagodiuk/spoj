// http://www.spoj.com/problems/MREPLBRC/

#include <stdint.h>
#include <stdio.h>

#define MAX_LEN 205
#define MOD 100000L
#define FALSE 0
#define TRUE 1

char s[MAX_LEN];
int32_t s_len;

int32_t mem[MAX_LEN][MAX_LEN];

int32_t result_is_large;

int32_t coeff(int32_t i, int32_t j) {
	if(s[i] == '?' && s[j] == '?') {
		return 3;
	}

	if((s[i] == '(' && s[j] == ')')
		|| (s[i] == '(' && s[j] == '?')
		|| (s[i] == '?' && s[j] == ')')
		|| (s[i] == '[' && s[j] == ']')
		|| (s[i] == '[' && s[j] == '?')
		|| (s[i] == '?' && s[j] == ']')
		|| (s[i] == '{' && s[j] == '}')
		|| (s[i] == '{' && s[j] == '?')
		|| (s[i] == '?' && s[j] == '}')) {
	
		return 1;
	}

	return 0;
}

int32_t solve() {
	int32_t i;
	int32_t j;
	int32_t k;
	int32_t c;
	int32_t left;
	int32_t right;
	int64_t tmp_result;

	result_is_large = FALSE;

	for(i = s_len - 1; i >= 0; --i) {
		for(j = i + 1; j < s_len; ++j) {
			mem[i][j] = 0;
			for(k = i + 1; k <= j; ++k) {
				c = coeff(i, k);
				if(c > 0) {
					left = (i + 1 > k - 1) ? 1 : mem[i + 1][k - 1];
					right = (k + 1 > j) ? 1 : mem[k + 1][j];

					tmp_result = (int64_t)mem[i][j] + (int64_t)c * (int64_t)left * (int64_t)right;
					mem[i][j] = tmp_result % MOD;

					if(mem[i][j] != tmp_result) {
						result_is_large = TRUE;
					}
				}
			}
		}
	}

	return mem[0][s_len - 1];
}

int main(){

	scanf("%d", &s_len);
	scanf("%s", s);

	result_is_large = FALSE;
	int32_t result = (s_len % 2 == 0) ? solve() : 0;
	
	if(result_is_large) {
		printf("%05d\n", result);
	} else {
		printf("%d\n", result);
	}

	return 0;
}
