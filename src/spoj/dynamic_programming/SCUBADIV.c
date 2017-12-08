// http://www.spoj.com/problems/SCUBADIV/

#include <stdio.h>
#include <memory.h>

#define OXYGEN_SIZE 22
#define NITROGEN_SIZE 80
#define BALLONS_MAX_COUNT 1001

#define INFINITY 10000000

typedef
struct {
    long oxygen;
    long nitrogen;
    long weight;
} Ballon;

Ballon BALLONS[BALLONS_MAX_COUNT];

long MEMOIZED[BALLONS_MAX_COUNT][OXYGEN_SIZE][NITROGEN_SIZE];
int IS_MEMOIZED[BALLONS_MAX_COUNT][OXYGEN_SIZE][NITROGEN_SIZE];

static long min(long a, long b) {
    return (a > b) ? b : a;
}

long calculateOptimalWeight(int ballonIdx, long requiredOxygen, long requiredNitrogen) {
    if(ballonIdx < 0) {
        return ((requiredOxygen > 0) || (requiredNitrogen > 0)) ? INFINITY : 0;
    }
    
    if(requiredOxygen < 0) {
        requiredOxygen = 0;
    }
    if(requiredNitrogen < 0) {
        requiredNitrogen = 0;
    }
    
    if(IS_MEMOIZED[ballonIdx][requiredOxygen][requiredNitrogen]) {
        return MEMOIZED[ballonIdx][requiredOxygen][requiredNitrogen];
    }
    
    MEMOIZED[ballonIdx][requiredOxygen][requiredNitrogen] = min(calculateOptimalWeight(ballonIdx - 1, requiredOxygen, requiredNitrogen),
               calculateOptimalWeight(ballonIdx - 1, requiredOxygen - BALLONS[ballonIdx].oxygen, requiredNitrogen - BALLONS[ballonIdx].nitrogen) + BALLONS[ballonIdx].weight);
    
    IS_MEMOIZED[ballonIdx][requiredOxygen][requiredNitrogen] = 1;
    
    return MEMOIZED[ballonIdx][requiredOxygen][requiredNitrogen];
}

int main() {
    long testsNum;
    long requiredOxygen;
    long requiredNitrogen;
    int ballonsCount;
    int i;
    
    scanf("%ld", &testsNum);
    while(testsNum--) {
        scanf("%ld %ld %d", &requiredOxygen, &requiredNitrogen, &ballonsCount);
        
        memset(IS_MEMOIZED, 0, sizeof IS_MEMOIZED);
        
        for(i = 0; i < ballonsCount; i++) {
            scanf("%ld %ld %ld", &BALLONS[i].oxygen, &BALLONS[i].nitrogen, &BALLONS[i].weight);
        }
        
        printf("%ld\n", calculateOptimalWeight(ballonsCount - 1, requiredOxygen, requiredNitrogen));
    }
    return 0;
}