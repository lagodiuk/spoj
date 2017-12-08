// http://www.spoj.com/problems/SAMER08F/

#include <stdio.h>
#include <stdint.h>

#define MAX_DIMENSION 100

uint32_t squares_count[MAX_DIMENSION + 1];

void precalculate_squares_count() {
    uint32_t i;
    squares_count[0] = 0;
    squares_count[1] = 1;
    for(i = 2; i <= MAX_DIMENSION; ++i) {
        squares_count[i] = (squares_count[i - 1] << 1) - squares_count[i - 2] + ((i - 1) << 1) + 1;
    }
}

int main() {
    precalculate_squares_count();
    
    uint32_t dimension;
    scanf("%d", &dimension);
    
    while(dimension) {
        printf("%d\n", squares_count[dimension]);
        scanf("%d", &dimension);
    }
    
    return 0;
}