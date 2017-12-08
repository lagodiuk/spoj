// http://www.spoj.com/problems/ACPC10D/

#include <stdio.h>

long trigraph[100001][3];

inline long min(long a, long b) {
    return (a < b) ? a : b;
}

// Solution is very similar to the Viterbi Algorithm
long get_least_cost(long rows_number) {
    // source is trigraph[0][1]
    
    // top left cell (trigraph[0][0]) should not be considered
    // because, it is not reachable from trigraph[0][1]
    // just assign some big value to the top left cell
    trigraph[0][0] = trigraph[0][1] * trigraph[0][1];
    trigraph[0][2] += trigraph[0][1];
    
    long i;
    for(i = 1; i < rows_number; ++i) {
        trigraph[i][0] += min(trigraph[i - 1][0], trigraph[i - 1][1]);
        trigraph[i][1] += min(min(trigraph[i - 1][0], trigraph[i - 1][1]),
                              min(trigraph[i - 1][2], trigraph[i][0]));
        trigraph[i][2] += min(min(trigraph[i - 1][1], trigraph[i - 1][2]),
                              trigraph[i][1]);
    }
    
    return trigraph[rows_number - 1][1];
}

int main() {
    long i;
    long rows_number;
    long testCaseNum;
    
    scanf("%ld", &rows_number);
    
    testCaseNum = 1;
    while(rows_number) {
        for(i = 0; i < rows_number; ++i) {
            scanf("%ld %ld %ld", &trigraph[i][0], &trigraph[i][1], &trigraph[i][2]);
        }
        
        printf("%ld. %ld\n", testCaseNum, get_least_cost(rows_number));
        
        scanf("%ld", &rows_number);
        testCaseNum++;
    }
    
    return 0;
}