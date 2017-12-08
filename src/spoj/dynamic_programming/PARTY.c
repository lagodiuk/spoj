// http://www.spoj.com/problems/PARTY/

#include <stdio.h>
#include <memory.h>

#define MAX_PARTIES 100
#define MAX_BUDGET 501
#define UNINITIALIZED -1

typedef
struct {
    int fee;
    int fun;
} Party;

Party parties[MAX_PARTIES];

int memoized[MAX_PARTIES][MAX_BUDGET];

inline int max(int a, int b) {
    return (a > b) ? a : b;
}

int getMaxFun(int partyIndex, int budget) {
    if(partyIndex < 0) {
        return 0;
    }
    
    if(memoized[partyIndex][budget] == UNINITIALIZED) {
        
        // do not include current party
        memoized[partyIndex][budget] = getMaxFun(partyIndex - 1, budget);
        
        // check, whether cost of current party - fits into budget
        if(budget >= parties[partyIndex].fee) {
            // in this case - try to check, whether we can include current party
            memoized[partyIndex][budget] = max(memoized[partyIndex][budget],
                                               parties[partyIndex].fun + getMaxFun(partyIndex - 1, budget - parties[partyIndex].fee));
        }
    }
    
    return memoized[partyIndex][budget];
}

int main() {
    int budget;
    int partiesCount;
    int i;
    int maxFun;
    int minBudget;
    int maxBudget;
    int midBudget;
    int midFun;
    
    
    scanf("%d %d", &budget, &partiesCount);
    while(budget || partiesCount) {
        
        // read information about parties
        for(i = 0; i < partiesCount; ++i) {
            scanf("%d %d", &parties[i].fee, &parties[i].fun);
        }
        // clean memoized memory
        memset(memoized, UNINITIALIZED, sizeof memoized);
        
        // calculate max possible fun
        maxFun = getMaxFun(partiesCount - 1, budget);
        
        // use binary search - to check, whether we can achive max possible fun
        // using smaller amount of budget
        maxBudget = budget;
        minBudget = 0;
        while(minBudget < maxBudget) {
            midBudget = (minBudget + maxBudget) / 2;
            midFun = getMaxFun(partiesCount - 1, midBudget);
            if(midFun < maxFun) {
                minBudget = midBudget + 1;
            } else {
                maxBudget = midBudget;
            }
        }
        
        // display solution
        printf("%d %d\n", minBudget, maxFun);
        
        // information for the next loop
        scanf("%d %d", &budget, &partiesCount);
    }
    
    return 0;
}