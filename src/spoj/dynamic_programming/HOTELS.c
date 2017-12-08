// http://www.spoj.com/problems/HOTELS/

#include <stdio.h>

long long hotels[300001];

int main() {
    long hotelsNum;
    long long availableMoney;
    
    scanf("%ld %lld", &hotelsNum, &availableMoney);
    
    long right;
    long left;
    long long maxSum;
    long long sumSoFar;
    
    left = 0;
    sumSoFar = 0;
    
    for(right = 0; right < hotelsNum; ++right) {
        scanf("%ld", &hotels[right]);
        sumSoFar += hotels[right];
        
        while(sumSoFar > availableMoney) {
            sumSoFar -= hotels[left];
            left++;
        }
        
        maxSum = (sumSoFar > maxSum) ? sumSoFar : maxSum;
    }
    
    printf("%lld\n", maxSum);
    
    return 0;
}