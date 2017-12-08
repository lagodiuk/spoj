// http://www.spoj.com/problems/ANARC09A/

#include <stdio.h>
#include <string.h>

#define MAX_LENGTH 2001
#define OPEN_BRACKET '{'

char str[MAX_LENGTH];
int memoized[2][MAX_LENGTH];

int solve(int length) {
    
    int prev = 0;
    int curr = 1;
    int idx;
    int stackSize;
    
    memoized[prev][0] = 0;
    
    for(idx = 1; idx <= length; ++idx) {
        for(stackSize = idx; stackSize >= 0; stackSize -= 2) {
            
            if(str[idx - 1] == OPEN_BRACKET) {
                
                if(stackSize == 0) {
                    memoized[curr][stackSize] = memoized[prev][stackSize + 1] + 1;
                    
                } else {
                    memoized[curr][stackSize] = memoized[prev][stackSize - 1];
                    
                    if((stackSize + 1 <= idx - 1)
                       && (memoized[prev][stackSize + 1] + 1 < memoized[prev][stackSize - 1])) {
                        
                        memoized[curr][stackSize] = memoized[prev][stackSize + 1] + 1;
                    }
                }
                
            } else {
                
                if(stackSize == 0) {
                    memoized[curr][stackSize] = memoized[prev][stackSize + 1];
                    
                } else {
                    memoized[curr][stackSize] = memoized[prev][stackSize - 1] + 1;
                    
                    if((stackSize + 1 <= idx - 1)
                       && (memoized[prev][stackSize + 1] < memoized[prev][stackSize - 1] + 1)) {
                        
                        memoized[curr][stackSize] = memoized[prev][stackSize + 1];
                    }
                }
            }
        }
        prev ^= 1;
        curr ^= 1;
    }
    
    return memoized[prev][0];
}

int main() {
    int result;
    int testNum;
    
    testNum = 1;
    
    for(;;) {
        scanf("%s", str);
        if(str[0] == '-') {
            break;
        }
        result = solve(strlen(str));
        printf("%d. %d\n", testNum, result);
        ++testNum;
    }
    return 0;
}