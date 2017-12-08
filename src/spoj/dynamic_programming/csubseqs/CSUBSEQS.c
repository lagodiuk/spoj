// http://www.spoj.com/problems/CSUBSEQS/

#include <stdint.h>
#include <stdio.h>
#include <memory.h>

#define MAX_LEN 51

char s1[MAX_LEN + 1];
char s2[MAX_LEN + 1];
char s3[MAX_LEN + 1];
char s4[MAX_LEN + 1];

int len1;
int len2;
int len3;
int len4;

int prev1['z' - 'a' + 1];
int prev2['z' - 'a' + 1];
int prev3['z' - 'a' + 1];
int prev4['z' - 'a' + 1];

int64_t mem[MAX_LEN][MAX_LEN][MAX_LEN][MAX_LEN];

int i1;
int i2;
int i3;
int i4;
int ii1;
int ii2;
int ii3;
int ii4;

int main() {
	scanf("%s", s1);
	scanf("%s", s2);
	scanf("%s", s3);
	scanf("%s", s4);
	
	len1 = strlen(s1);
	len2 = strlen(s2);
	len3 = strlen(s3);
	len4 = strlen(s4);
	
	for(i1 = 0; i1 <= len1; ++i1) {
		for(i2 = 0; i2 <= len2; ++i2) {
			for(i3 = 0; i3 <= len3; ++i3) {
				mem[i1][i2][i3][0] = 1;
			}
		}
	}

	for(i1 = 0; i1 <= len1; ++i1) {
		for(i2 = 0; i2 <= len2; ++i2) {
			for(i4 = 0; i4 <= len4; ++i4) {
				mem[i1][i2][0][i4] = 1;
			}
		}
	}

	for(i1 = 0; i1 <= len1; ++i1) {
		for(i3 = 0; i3 <= len3; ++i3) {
			for(i4 = 0; i4 <= len4; ++i4) {
				mem[i1][0][i3][i4] = 1;
			}
		}
	}

	for(i2 = 0; i2 <= len2; ++i2) {
		for(i3 = 0; i3 <= len3; ++i3) {
			for(i4 = 0; i4 <= len4; ++i4) {
				mem[0][i2][i3][i4] = 1;
			}
		}
	}

	memset(prev1, -1, sizeof prev1);
	for(i1 = 1; i1 <= len1; ++i1) {
		memset(prev2, -1, sizeof prev2);
		for(i2 = 1; i2 <= len2; ++i2) {
			memset(prev3, -1, sizeof prev3);
			for(i3 = 1; i3 <= len3; ++i3) {
				memset(prev4, -1, sizeof prev4);
				for(i4 = 1; i4 <= len4; ++i4) {
				
					if((s1[i1 - 1] == s2[i2 - 1])
					 &&(s1[i1 - 1] == s3[i3 - 1])
					 &&(s1[i1 - 1] == s4[i4 - 1])) {
					 
						mem[i1][i2][i3][i4] = mem[i1 - 1][i2 - 1][i3 - 1][i4 - 1] << 1;

						ii1 = prev1[s1[i1 - 1] - 'a'];
						ii2 = prev2[s2[i2 - 1] - 'a'];
						ii3 = prev3[s3[i3 - 1] - 'a'];
						ii4 = prev4[s4[i4 - 1] - 'a'];
						
						if((ii1 >= 0)
						 &&(ii2 >= 0)
						 &&(ii3 >= 0)
						 &&(ii4 >= 0)) {
						 
							mem[i1][i2][i3][i4] -= mem[ii1][ii2][ii3][ii4];
						 }
						
					 } else {
					 
						mem[i1][i2][i3][i4]  = mem[i1 - 1][i2][i3][i4];
						mem[i1][i2][i3][i4] += mem[i1][i2 - 1][i3][i4];
						mem[i1][i2][i3][i4] += mem[i1][i2][i3 - 1][i4];
						mem[i1][i2][i3][i4] += mem[i1][i2][i3][i4 - 1];

						mem[i1][i2][i3][i4] -= mem[i1 - 1][i2 - 1][i3][i4];
						mem[i1][i2][i3][i4] -= mem[i1 - 1][i2][i3 - 1][i4];
						mem[i1][i2][i3][i4] -= mem[i1 - 1][i2][i3][i4 - 1];
						mem[i1][i2][i3][i4] -= mem[i1][i2 - 1][i3 - 1][i4];
						mem[i1][i2][i3][i4] -= mem[i1][i2 - 1][i3][i4 - 1];
						mem[i1][i2][i3][i4] -= mem[i1][i2][i3 - 1][i4 - 1];

						mem[i1][i2][i3][i4] += mem[i1 - 1][i2 - 1][i3 - 1][i4];
						mem[i1][i2][i3][i4] += mem[i1 - 1][i2 - 1][i3][i4 - 1];
						mem[i1][i2][i3][i4] += mem[i1 - 1][i2][i3 - 1][i4 - 1];
						mem[i1][i2][i3][i4] += mem[i1][i2 - 1][i3 - 1][i4 - 1];

						mem[i1][i2][i3][i4] -= mem[i1 - 1][i2 - 1][i3 - 1][i4 - 1];

					 }
				
					prev4[s4[i4 - 1] - 'a'] = i4 - 1;
				}
				prev3[s3[i3 - 1] - 'a'] = i3 - 1;	
			}
			prev2[s2[i2 - 1] - 'a'] = i2 - 1;
		}
		prev1[s1[i1 - 1] - 'a'] = i1 - 1;
	}

	printf("%lld\n", mem[len1][len2][len3][len4] - 1);

	return 0;
}
