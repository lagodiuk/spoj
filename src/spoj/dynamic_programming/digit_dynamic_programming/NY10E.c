// http://www.spoj.com/problems/NY10E/

#include <stdio.h>
#include <memory.h>

#define MAX_RADIX 64
#define DIGITS_NUM 10

long long mem[MAX_RADIX + 1][DIGITS_NUM + 1];

long long non_decreasing_amount(int radix, int last_digit) {
    if((radix == 1) || (last_digit == 0)) {
        return 1;
    }
    
    if(mem[radix][last_digit] == -1) {
        int digit;
        mem[radix][last_digit] = 0;
        for(digit = 0; digit <= last_digit; ++digit) {
            mem[radix][last_digit] += non_decreasing_amount(radix - 1, digit);
        }
    }
    
    return mem[radix][last_digit];
}

long long solve(int radix) {
    long long result = 0;
    int digit;
    for(digit = 0; digit <= 9; ++digit) {
        result += non_decreasing_amount(radix, digit);
    }
    return result;
}

int main() {
    memset(mem, -1, sizeof mem);
    
    /*
    printf("%lld\n", solve(2));
    printf("%lld\n", solve(3));
    printf("%lld\n", solve(4));
    printf("%lld\n", solve(10));
    printf("%lld\n", solve(20));
    printf("%lld\n", solve(30));
    printf("%lld\n", solve(40));
    printf("%lld\n", solve(50));
    printf("%lld\n", solve(60));
    printf("%lld\n", solve(64));
     */
    
    int tests_num;
    scanf("%d", &tests_num);
    
    int test_num;
    int radix;
    
    while(tests_num--) {
        scanf("%d %d", &test_num, &radix);
        printf("%d %lld\n", test_num, solve(radix));
    }
    
    return 0;
}