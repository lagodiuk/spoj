// http://www.spoj.com/problems/AIBOHP/

#include <stdio.h>
#include <string.h>

#define MAX_LENGTH 6101

char str[MAX_LENGTH];
int mem[2][MAX_LENGTH];

// f(i, j) = f(i + 1, j - 1) // if (str[i] == str[j])
// f(i, j) = min(f(i + 1, j), f(i, j - 1)) + 1 // if (str[i] != str[j])

// Optimized bottom-up solution
int get_chars_num_to_palindrome(int len) {
    int prev;
    int curr;
    int j;
    int i;
    
    prev = 0;
    curr = 1;
    
    mem[prev][len - 1] = 0;
    
    for(j = len - 2; j >= 0; --j) {
        mem[curr][j] = 0;
        for(i = j + 1; i < len; ++i) {
            mem[curr][i] = (str[j] == str[i])
            ? ((i == j + 1) ? 0 : mem[prev][i - 1])
            : (((mem[curr][i - 1] < mem[prev][i]) ? mem[curr][i - 1] : mem[prev][i]) + 1);
        }
        curr ^= 1;
        prev ^= 1;
    }
    
    return mem[prev][len - 1];
}

int main() {
    int testsNum;
    int len;
    int result;
    
    scanf("%d", &testsNum);
    
    while(testsNum--) {
        scanf("%s", &str);
        len = strlen(str);
        result = get_chars_num_to_palindrome(len);
        printf("%d\n", result);
    }
    
    return 0;
}