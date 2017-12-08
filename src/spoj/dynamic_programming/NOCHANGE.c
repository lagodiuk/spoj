// http://www.spoj.com/problems/NOCHANGE/

#include <stdio.h>
#include <memory.h>

#define COINS 5
#define MAX_PAYMENT 100001

long nominals[COINS];
long cumulative_nominals[COINS];
int memoized[MAX_PAYMENT][COINS];

void calculate_cumulative_nominals(int nominals_count) {
    int i;
    cumulative_nominals[0] = nominals[0];
    for(i = 1; i < nominals_count; ++i) {
        cumulative_nominals[i] = cumulative_nominals[i - 1] + nominals[i];
    }
}

int solve(long money, int cumulative_index) {
    if(money < 0) {
        return 0;
    }
    
    if(money == 0) {
        //printf("> %ld\n", cumulative_nominals[cumulative_index < 0 ? 0 : cumulative_index]);
        return 1;
    }
    
    if(cumulative_index < 0) {
        return 0;
    }
    
    if(money % cumulative_nominals[cumulative_index] == 0) {
        //printf(">> %ld * %d\n", cumulative_nominals[cumulative_index], money / cumulative_nominals[cumulative_index]);
        return 1;
    }
    
    if(memoized[money][cumulative_index] == -1) {
        memoized[money][cumulative_index] =
            solve(money, cumulative_index - 1)
            || solve(money - cumulative_nominals[cumulative_index], cumulative_index);
    }
    
//    if(memoized[money][cumulative_index]) {
//        printf(">>> %ld\n", cumulative_nominals[cumulative_index]);
//    }
    
    return memoized[money][cumulative_index];
}

int main() {
    long money;
    int nominals_count;
    int i;
    
    memset(memoized, -1, sizeof memoized);
    
    // read all available nominals
    scanf("%ld %d", &money, &nominals_count);
    for(i = 0; i < nominals_count; ++i) {
        scanf("%ld", &nominals[i]);
    }
    
    calculate_cumulative_nominals(nominals_count);
    
    if(solve(money, nominals_count - 1)) {
        printf("YES\n");
    } else {
        printf("NO\n");
    }
    
    return 0;
}