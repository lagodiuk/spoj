// http://www.spoj.com/problems/IOIPALIN/

#include <stdio.h>

#define MAX_STRING_SIZE 5001

char STRING[MAX_STRING_SIZE];

int MEMOIZED[MAX_STRING_SIZE][MAX_STRING_SIZE];

inline int min(int a, int b) {
    return (a > b) ? b : a;
}

// Top-down
int amountOfCharsForBeingPalindrome(int left, int right) {
    if(left >= right) {
        return 0;
    }
    
    if(STRING[left] == STRING[right]) {
        MEMOIZED[left][right] = amountOfCharsForBeingPalindrome(left + 1, right - 1);
        return MEMOIZED[left][right];
    }
    
    MEMOIZED[left][right] = min(amountOfCharsForBeingPalindrome(left + 1, right),
                                amountOfCharsForBeingPalindrome(left, right - 1)) + 1;
    
    return MEMOIZED[left][right];
}

// Bottom-up
int amountOfCharsForBeingPalindrome2(int length) {
    int left;
    int right;
    
    for(left = length - 1; left >= 0; left--) {
        MEMOIZED[left][left] = 0;
        for(right = left + 1; right < length; right++) {
            if(STRING[left] == STRING[right]) {
                MEMOIZED[left][right] = MEMOIZED[left + 1][right - 1];
            } else {
                MEMOIZED[left][right] = min(MEMOIZED[left + 1][right], MEMOIZED[left][right - 1]) + 1;
            }
        }
    }
    
    return MEMOIZED[0][length - 1];
}

int main() {
    int length;
    scanf("%d", &length);
    scanf("%s", STRING);
    
    // Time-limit exceeded
    //printf("%d\n", amountOfCharsForBeingPalindrome(0, length - 1));
    
    // Accepted
    printf("%d", amountOfCharsForBeingPalindrome2(length));
    
    return 0;
}