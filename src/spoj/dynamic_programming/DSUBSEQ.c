// http://www.spoj.com/problems/DSUBSEQ/

#include <stdio.h>
#include <string.h>

#define MAX_STR_LEN 100002
#define MAX_CHAR_IDX 256
#define MOD 1000000007

char str[MAX_STR_LEN];
long memoized[MAX_STR_LEN + 1];

long charIndex[MAX_CHAR_IDX];
// the trick, which helps to get rid of invalidation of array charIndex
// for each test case
int charIndexCheck[MAX_CHAR_IDX];
int testCaseNum = 1;

// F(N) = 2 * F(N - 1) - F(t - 1)
// where t is maximal index <= N
// such that str[t] == str[N]
// if, str[t] != str[N] for every t <= N
// then F(N) = 2 * F(N - 1)
long calculateUniqueSubseq(long str_len) {
    memoized[0] = 1;
    
    long i;
    for(i = 0; i < str_len; ++i) {
        memoized[i + 1] = (memoized[i] << 1) % MOD;
        
        if(charIndexCheck[str[i]] == testCaseNum) {
            memoized[i + 1] -= memoized[charIndex[str[i]]];
            if(memoized[i + 1] < 0) {
                memoized[i + 1] += MOD;
            }
        }
        
        charIndexCheck[str[i]] = testCaseNum;
        charIndex[str[i]] = i;
    }
    
    return memoized[str_len];
}

int main() {
    int testsNum;
    scanf("%d", &testsNum);
    
    long result;
    
    while(testsNum--) {
        scanf("%s", str);
        result = calculateUniqueSubseq(strlen(str));
        printf("%ld\n", result);
        ++testCaseNum;
    }
    
    return 0;
}