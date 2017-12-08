// http://www.spoj.com/problems/PRIME1/

#include <stdio.h>
#include <stdint.h>
#include <math.h>

#define true 1
#define false 0

// TODO: use https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
void print_primes(uint32_t min, uint32_t max) {
    uint32_t x;
    uint32_t i;
    uint32_t sqrtX;
    int is_prime;
    
    if(min <= 2) {
        printf("2\n");
        min = 3;
    }
    min = (min % 2 == 0) ? min + 1 : min;
    
    for(x = min; x <= max; x += 2) {
        
        i = 3;
        is_prime = true;
        sqrtX = sqrt(x);
        while(i <= sqrtX) {
            if(x % i == 0) {
                is_prime = false;
                break;
            }
            i += 2;
        }
        if(is_prime) {
            printf("%d\n", x);
        }
    }
}

int main() {
    int testsNum;
    uint32_t min;
    uint32_t max;
    scanf("%d", &testsNum);
    
    while(testsNum--) {
        scanf("%d %d", &min, &max);
        
        print_primes(min, max);
        
        if(testsNum) {
            printf("\n");
        }
    }
    
    return 0;
}