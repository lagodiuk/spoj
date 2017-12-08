// http://www.spoj.com/problems/GNY07H/

#include <stdio.h>
#include <memory.h>

#define _1111 0
#define _1001 1
#define _0110 2
#define _0011 3

long memoized[100][4];

long coverageVariants(int length, int mask) {
    
    if(length < 0) {
        return 0;
    }
    
    if(length == 0) {
        return (mask == _1111);
    }

    if(memoized[length][mask] == -1) {
        switch (mask) {
            case _1111:
                memoized[length][mask] =
                    coverageVariants(length - 1, _1111) +
                    coverageVariants(length - 2, _1111) +
                    coverageVariants(length, _1001) +
                    (coverageVariants(length - 1, _0011) << 1);
                break;
                
            case _1001:
                memoized[length][mask] =
                    coverageVariants(length - 1, _0110);
                break;
                
            case _0110:
                memoized[length][mask] =
                    coverageVariants(length - 1, _1111) +
                    coverageVariants(length - 1, _1001);
                break;
                
            case _0011:
                memoized[length][mask] =
                    coverageVariants(length - 1, _1111) +
                    coverageVariants(length - 1, _0011);
                break;
        }
    }
    
    return memoized[length][mask];
}

int main() {
    int testsNum;
    int width;
    int testCaseNum;
    
    memset(memoized, -1, sizeof memoized);
    scanf("%d", &testsNum);
    testCaseNum = 1;
    
    while (testsNum--) {
        scanf("%d", &width);
        printf("%d %ld\n", testCaseNum, coverageVariants(width, _1111));
        testCaseNum++;
    }
    
    return 0;
}