// http://www.spoj.com/problems/ACODE/

#include <stdio.h>
#include <string.h>

#define MAX_LENGTH 5002

char digits[MAX_LENGTH];

inline int digit(const int pos) {
    return digits[pos] - '0';
}

long long get_variants_number(int length) {
    long long prevPrev = 0;
    long long prev = 1;
    long long curr;
    int i;
    
    for(i = length - 1; i >= 0; --i) {
        curr = 0;
        
        if(digit(i) == 0) {
            curr = 0;
            
        } else {
            curr = prev;
        
            if((i != length - 1) && ((digit(i) == 1) || ((digit(i) == 2) && (digit(i) <= 6)))) {
                curr += prevPrev;
            }
        }
        
        prevPrev = prev;
        prev = curr;

        if(!(prev || prevPrev)) {
            break;
        }
    }
    
    return prev;
}

int main() {
    long long result;
    int len;
    
    scanf("%s", &digits);
    while(digits[0] != '0') {
        len = strlen(digits);
        result = get_variants_number(len);
        printf("%lld\n", result);
        scanf("%s", &digits);
    }
    
    return 0;
}