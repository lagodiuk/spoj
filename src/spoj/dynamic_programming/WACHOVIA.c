// http://www.spoj.com/problems/WACHOVIA/

#include <stdio.h>
#include <memory.h>

#define MAX_WEIGHT 10001
#define MAX_BAGS_COUNT 51

#define NEGATIVE_INFINITY -10000000

typedef
struct {
    long weight;
    long cost;
} Bag;

Bag BAGS[MAX_BAGS_COUNT];

long MEMOIZED[MAX_BAGS_COUNT][MAX_WEIGHT];
int IS_MEMOIZED[MAX_BAGS_COUNT][MAX_WEIGHT];

inline long max(long a, long b) {
    return (a > b) ? a : b;
}

long calculateMaxRevenue(int currBagIndex, int maxWeight) {
    if(maxWeight < 0) {
        return NEGATIVE_INFINITY;
    }
    
    if(currBagIndex < 0) {
        return 0;
    }
    
    if(IS_MEMOIZED[currBagIndex][maxWeight]) {
        return MEMOIZED[currBagIndex][maxWeight];
    }
    
    MEMOIZED[currBagIndex][maxWeight] = max(calculateMaxRevenue(currBagIndex - 1, maxWeight),
                                            calculateMaxRevenue(currBagIndex - 1, maxWeight - BAGS[currBagIndex].weight) + BAGS[currBagIndex].cost);
    
    IS_MEMOIZED[currBagIndex][maxWeight] = 1;
    
    return MEMOIZED[currBagIndex][maxWeight];
}

int main() {
    int testCasesNum;
    int maxWeight;
    int bagsCount;
    int i;
    
    scanf("%d", &testCasesNum);
    while(testCasesNum--) {
        scanf("%d %d", &maxWeight, &bagsCount);
        for(i = 0; i < bagsCount; ++i) {
            scanf("%ld %ld", &BAGS[i].weight, &BAGS[i].cost);
        }
        memset(IS_MEMOIZED, 0, sizeof IS_MEMOIZED);
        
        printf("Hey stupid robber, you can get %ld.\n", calculateMaxRevenue(bagsCount - 1, maxWeight));
    }
    
    return 0;
}