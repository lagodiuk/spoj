// http://www.spoj.com/problems/ANARC05H/

#include <stdio.h>
#include <string.h>

#define MAX_SIZE 26

char DIGITS[MAX_SIZE];

long CUMULATIVE_SUM[MAX_SIZE + 1];

long MEMOIZED_VALUES[MAX_SIZE][MAX_SIZE * 9];

int IS_MEMOIZED[MAX_SIZE][MAX_SIZE * 9];

void calculateCumulativeSum(int length) {
    int i;
    
    CUMULATIVE_SUM[0] = 0;
    for(i = 1; i <= length; i++) {
        CUMULATIVE_SUM[i] = CUMULATIVE_SUM[i - 1] + (DIGITS[i - 1] - '0');
    }
}

long calculateVariants(int left, int prevSum, int length) {
    if(left >= length) {
        return 1;
    }
    
    if(IS_MEMOIZED[left][prevSum]) {
        return MEMOIZED_VALUES[left][prevSum];
    }
    
    long variants = 0;
    
    int k;
    long preffixSum;
    
    for(k = left; k < length; k++) {
        preffixSum = CUMULATIVE_SUM[k + 1] - CUMULATIVE_SUM[left];
        
        if(preffixSum >= prevSum) {
            variants += calculateVariants(k + 1, preffixSum, length);
        }
    }
    
    MEMOIZED_VALUES[left][prevSum] = variants;
    IS_MEMOIZED[left][prevSum] = 1;
    return variants;
}

int main() {
    
    scanf("%s", DIGITS);
    int length;
    int testCaseNum = 1;
    
    while(DIGITS[0] != 'b') {
        
        length = strlen(DIGITS);
        
        memset(IS_MEMOIZED, 0, sizeof(IS_MEMOIZED));
        calculateCumulativeSum(length);
        printf("%d. %ld\n", testCaseNum, calculateVariants(0, 0, length));
        
        scanf("%s", DIGITS);
        testCaseNum++;
    }
}