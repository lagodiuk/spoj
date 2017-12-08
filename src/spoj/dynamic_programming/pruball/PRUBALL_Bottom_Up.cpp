// http://www.spoj.com/problems/PRUBALL/

#include <algorithm>
#include <stdio.h>

#define MAX_FLOORS 1001
#define MAX_BALLS 101
#define POSITIVE_INFINITY 10000

int memoized[MAX_BALLS][MAX_FLOORS];

int solve(int balls, int floors) {

    if(balls == 1) {
        return floors;
    }

    if(memoized[balls][floors] != 0) {
        return memoized[balls][floors];
    }

    int result;
    int left_result;
    int right_result;

    for(int b = 1; b <= balls; ++b) {
        for(int f = 1; f <= floors; ++f) {
            
            result = POSITIVE_INFINITY;
        
            for(int i = 1; i <= ((floors / 2) + 1); ++i) {
                
                left_result = ((i < 2) || (b < 2))
                                ? 0
                                : ((b == 2) ? (i - 1) : memoized[b - 1][i - 1]);

                right_result = ((f < (i + 1)) || (b < 1))
                                ? 0
                                : ((b == 1) ? (f - i) : memoized[b][f - i]);

                result = std::min(result, std::max(left_result, right_result));

            }

            memoized[b][f] = result + 1;
        }
    }

    return memoized[balls][floors];
}

int main() {
    int tests_num;
    scanf("%d", &tests_num);

    int num;
    int balls;
    int floors;
    int result;

    while(tests_num--) {
        scanf("%d %d %d", &num, &balls, &floors);
        result = solve(balls, floors);
        printf("%d %d\n", num, result);
    }

    return 0;
}
