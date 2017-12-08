// http://www.spoj.com/problems/EDIST/

#include <stdio.h>
#include <string.h>

#define SIZE 2001

int dist[2][SIZE];

char s1[SIZE];
char s2[SIZE];

inline int min(int a, int b) {
    return (a > b) ? b : a;
}

int distance() {
    int l1 = strlen(s1);
    int l2 = strlen(s2);
    
    int i;
    int j;
    int prev;
    int curr;
    
    for(i = 0; i <= l1; ++i) {
        dist[0][i] = i;
    }
    
    prev = 0;
    curr = 1;
    for(j = 1; j <= l2; ++j) {
        dist[curr][0] = j;
        for(i = 1; i <= l1; ++i) {
            dist[curr][i] = min(min(dist[prev][i], dist[curr][i - 1]) + 1,
                                dist[prev][i - 1] + ((s2[j - 1] == s1[i - 1]) ? 0 : 1));
        }
        // swap prev and curr
        prev ^= 1;
        curr ^= 1;
    }
    
    return dist[prev][l1];
}

int main() {
    int testsNum;
    scanf("%d", &testsNum);
    
    while(testsNum--) {
        scanf("%s %s", &s1, &s2);
        printf("%d\n", distance());
    }
    
    return 0;
}