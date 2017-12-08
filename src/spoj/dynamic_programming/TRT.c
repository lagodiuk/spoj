// http://www.spoj.com/problems/TRT/

#include <stdio.h>

#define MAX_COUNT 2000

long MEMOIZED[MAX_COUNT][MAX_COUNT];

long YUMMY_PRICES[MAX_COUNT];
long YUMMIES_COUNT;

inline long max(long a, long b) {
    return (a > b) ? a : b;
}

long calculateBestPrice(int left, int right) {
    if(MEMOIZED[left][right]) {
        return MEMOIZED[left][right];
    }
    
    long time = left + (YUMMIES_COUNT - right);
    long bestPrice;
    
    if(left == right) {
        bestPrice = YUMMY_PRICES[left] * time;
        
        MEMOIZED[left][right] = bestPrice;
        
        return MEMOIZED[left][right];
    }
    
    bestPrice = max(YUMMY_PRICES[left] * time + calculateBestPrice(left + 1, right),
                    YUMMY_PRICES[right] * time + calculateBestPrice(left, right - 1));
    
    MEMOIZED[left][right] = bestPrice;
    
    return bestPrice;
}

int main() {
    scanf("%ld", &YUMMIES_COUNT);
    int i;
    for(i = 0; i < YUMMIES_COUNT; i++) {
        scanf("%ld", &YUMMY_PRICES[i]);
    }
    
    printf("%ld", calculateBestPrice(0, YUMMIES_COUNT - 1));
    
    return 0;
}