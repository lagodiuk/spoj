// http://www.spoj.com/problems/GNYR09F/

#include <stdio.h>

#define TRUE 1

typedef
struct {
    long endsWith_0;
    long endsWith_1;
    int isMemoized;
} MemoryCell;

#define MAX 100

MemoryCell MEMOIZED[MAX + 1][MAX + 1];

void calculate(int bitStrLen, int adjBitsCount) {
    if(bitStrLen == 1) {
        MEMOIZED[bitStrLen][adjBitsCount].endsWith_0 = 1;
        MEMOIZED[bitStrLen][adjBitsCount].endsWith_1 = 1;
        MEMOIZED[bitStrLen][adjBitsCount].isMemoized = TRUE;
        return;
    }
    
    if(adjBitsCount == 0) {
        if(!MEMOIZED[bitStrLen - 1][adjBitsCount].isMemoized) {
            calculate(bitStrLen - 1, adjBitsCount);
        }
        
        MEMOIZED[bitStrLen][adjBitsCount].endsWith_0 = MEMOIZED[bitStrLen - 1][adjBitsCount].endsWith_0 + MEMOIZED[bitStrLen - 1][adjBitsCount].endsWith_1;
        MEMOIZED[bitStrLen][adjBitsCount].endsWith_1 = MEMOIZED[bitStrLen - 1][adjBitsCount].endsWith_0;
        MEMOIZED[bitStrLen][adjBitsCount].isMemoized = TRUE;
        return;
    }
    
    if(adjBitsCount == bitStrLen - 1) {
        MEMOIZED[bitStrLen][adjBitsCount].endsWith_0 = 0;
        MEMOIZED[bitStrLen][adjBitsCount].endsWith_1 = 1;
        MEMOIZED[bitStrLen][adjBitsCount].isMemoized = TRUE;
        return;
    }
    
    if(!MEMOIZED[bitStrLen - 1][adjBitsCount - 1].isMemoized) {
        calculate(bitStrLen - 1, adjBitsCount - 1);
    }
    
    if(!MEMOIZED[bitStrLen - 1][adjBitsCount].isMemoized) {
        calculate(bitStrLen - 1, adjBitsCount);
    }
    
    // To get sequence of n bits, where (AdjBC == k), we have to:
    //
    // 1) Append '1' to:
    // - either to sequence of (n - 1) bits, where (AdjBC == (k - 1)), and ends with '1'
    // - or to sequence of (n - 1) bits, where (AdjBC == k), and ends with '0'
    //
    // 2) Append '0' to sequence of (n - 1) bits, where (AdjBC == k), which might ens either with '0', or with '1'
    
    // 1)
    MEMOIZED[bitStrLen][adjBitsCount].endsWith_1 = MEMOIZED[bitStrLen - 1][adjBitsCount - 1].endsWith_1 + MEMOIZED[bitStrLen - 1][adjBitsCount].endsWith_0;
    
    // 2)
    MEMOIZED[bitStrLen][adjBitsCount].endsWith_0 = MEMOIZED[bitStrLen - 1][adjBitsCount].endsWith_1 + MEMOIZED[bitStrLen - 1][adjBitsCount].endsWith_0;
    
    MEMOIZED[bitStrLen][adjBitsCount].isMemoized = TRUE;
}

long solve(int bitStrLen, int adjBitsCount) {
    calculate(bitStrLen, adjBitsCount);
    return MEMOIZED[bitStrLen][adjBitsCount].endsWith_0 + MEMOIZED[bitStrLen][adjBitsCount].endsWith_1;
}

int main() {
    /*
    printf("%ld\n", solve(5, 2));
    printf("%ld\n", solve(20, 8));
    printf("%ld\n", solve(30, 17));
    printf("%ld\n", solve(40, 24));
    printf("%ld\n", solve(50, 37));
    printf("%ld\n", solve(60, 52));
    printf("%ld\n", solve(70, 59));
    printf("%ld\n", solve(80, 73));
    printf("%ld\n", solve(90, 84));
    printf("%ld\n", solve(100, 90));
    */
    
    int testCases;
    int testCaseNum;
    int bitStrLen;
    int adjBitsCount;
    
    scanf("%d", &testCases);
    
    while(testCases--) {
        scanf("%d %d %d", &testCaseNum, &bitStrLen, &adjBitsCount);
        printf("%d %ld\n", testCaseNum, solve(bitStrLen, adjBitsCount));
    }
    
    return 0;
}