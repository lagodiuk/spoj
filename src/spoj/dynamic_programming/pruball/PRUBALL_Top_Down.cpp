// http://www.spoj.com/problems/PRUBALL/

#include <algorithm>
#include <stdio.h>

#define MAX_FLOORS 1001
#define MAX_BALLS 101
#define POSITIVE_INFINITY 10000

int memoized[MAX_BALLS][MAX_FLOORS];

int solve(int balls, int floors) {

    if((balls < 1) || (floors < 1)) {
        return 0;
    }

    if(balls == 1) {
        return floors;
    }

    if(memoized[balls][floors] != 0) {
        return memoized[balls][floors];
    }

    memoized[balls][floors] = POSITIVE_INFINITY;
    for(int i = 1; i <= (floors / 2) + 1; ++i) {
        memoized[balls][floors] = std::min(
                                    memoized[balls][floors],
                                    std::max(
                                        solve(balls - 1, i - 1), 
                                        solve(balls, floors - i)));
    }

    memoized[balls][floors]++;

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
