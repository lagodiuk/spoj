// http://www.spoj.com/problems/M3TILE/
// Problem: M3TILE - LATGACH3

#include <stdio.h>

#define MAX_LENGTH 31

#define _111 0
#define _011 1
#define _001 2

#define TRUE 1
long MEMOIZED[MAX_LENGTH][3];
int IS_MEMOIZED[MAX_LENGTH][3];

long calculateCombinations(int length, int mask) {
    if(mask == _111 && !length) {
        return 1;
    }
    
    if(length <= 0) {
        return 0;
    }
    
    if(IS_MEMOIZED[length][mask]) {
        return MEMOIZED[length][mask];
    }
    
    switch(mask) {
        case _111:
            MEMOIZED[length][mask] = calculateCombinations(length - 2, _111) + (calculateCombinations(length, _001) << 1);
            IS_MEMOIZED[length][mask] = 1;
            return MEMOIZED[length][mask];
            
        case _011:
            MEMOIZED[length][mask] = calculateCombinations(length - 1, _111) + calculateCombinations(length - 1, _001);
            IS_MEMOIZED[length][mask] = 1;
            return MEMOIZED[length][mask];
            
        case _001:
            MEMOIZED[length][mask] = calculateCombinations(length - 1, _011);
            IS_MEMOIZED[length][mask] = 1;
            return MEMOIZED[length][mask];
    }
}

int main() {
    /*
    printf("%ld\n", calculateCombinations(2, _111));
    printf("%ld\n", calculateCombinations(8, _111));
    printf("%ld\n", calculateCombinations(12, _111));
    printf("%ld\n", calculateCombinations(28, _111));
    printf("%ld\n", calculateCombinations(29, _111));
    printf("%ld\n", calculateCombinations(30, _111));
     */
    
    int length;
    scanf("%d", &length);
    while(length != -1) {
        printf("%ld\n", calculateCombinations(length, _111));
        scanf("%d", &length);
    }
    
    return 0;
}