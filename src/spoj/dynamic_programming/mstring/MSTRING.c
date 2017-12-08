// http://www.spoj.com/problems/MSTRING/

#include <stdio.h>
#include <string.h>

#define MAX_LEN 1005
#define INF 10000
#define ALPHABET_SIZE 26

char source_str[MAX_LEN];
int source_str_len;

char target_str[MAX_LEN];
int target_str_len;

int next_letter_pos[MAX_LEN][ALPHABET_SIZE];

int memoized[2][MAX_LEN];
int prev;
int curr;

void generate_next_letter_pos_mapping() {
	int i;
	int j;
	for(i = 0; i < ALPHABET_SIZE; ++i) {
		next_letter_pos[target_str_len][i] = INF;
	}
	for(i = target_str_len - 1; i >= 0; --i) {
		for(j = 0; j < ALPHABET_SIZE; ++j) {
			
			next_letter_pos[i][j] = (target_str[i] == j + 'a') 
									? (i + 1) 
									: next_letter_pos[i + 1][j];
		}
	}
}

int solve() {
	generate_next_letter_pos_mapping();
	
	curr = 1;
	prev = 0;

	int srcPos;
	int tgtPos;

	int excluding_curr_letter;
	int including_curr_letter;

	for(srcPos = source_str_len - 1; srcPos >= 0; --srcPos) {
		for(tgtPos = target_str_len; tgtPos >= 0; --tgtPos) {
			
			excluding_curr_letter = (srcPos + 1 == source_str_len)
									? INF
									: memoized[prev][tgtPos];
		
			including_curr_letter = ((next_letter_pos[tgtPos][source_str[srcPos] - 'a'] == INF)
									 ? 0
									 : ((srcPos + 1 == source_str_len)
									    ? INF
									    : memoized[prev][next_letter_pos[tgtPos][source_str[srcPos] - 'a']]
									   )
									) + 1;
		
			memoized[curr][tgtPos] = (including_curr_letter < excluding_curr_letter)
									 ? including_curr_letter
									 : excluding_curr_letter;
		}

		curr ^= 1;
		prev ^= 1;
	}

	return memoized[prev][0];
}

int main() {
	scanf("%s", source_str);
	source_str_len = strlen(source_str);

	scanf("%s", target_str);
	target_str_len = strlen(target_str);

	int result = solve();
	printf("%d\n", result);

	return 0;
}
