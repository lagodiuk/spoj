// http://www.spoj.com/problems/MARTIAN/

#include <stdio.h>

#define MAX_DIMENSION 501

// East-to-West Cumulative-Sum-Array
long YEYENUM[MAX_DIMENSION][MAX_DIMENSION];

// South-to-North Cumulative-Sum-Array
long BLOGGIUM[MAX_DIMENSION][MAX_DIMENSION];

long RESULT[MAX_DIMENSION + 1][MAX_DIMENSION + 1];

void readYeyenum(int width, int height) {
    int w;
    int h;
    
    for(h = 0; h < height; h++) {
        scanf("%ld", &YEYENUM[h][0]);
        
        for(w = 1; w < width; w++) {
            scanf("%ld", &YEYENUM[h][w]);
            YEYENUM[h][w] += YEYENUM[h][w - 1];
        }
    }
}

void readBloggium(int width, int height) {
    int w;
    int h;
    
    for(w = 0; w < width; w++) {
        scanf("%ld", &BLOGGIUM[0][w]);
    }
    
    for(h = 1; h < height; h++) {
        for(w = 0; w < width; w++) {
            scanf("%ld", &BLOGGIUM[h][w]);
            BLOGGIUM[h][w] += BLOGGIUM[h - 1][w];
        }
    }
}

inline long max(long a, long b) {
    return (a > b) ? a : b;
}

long calculateMaxMined(int eastIndex, int southIndex) {
    int e;
    int s;
    
    for(s = 1; s <= southIndex + 1; s++) {
        for(e = 1; e <= eastIndex + 1; e++) {
            RESULT[s][e] = max(RESULT[s - 1][e] + YEYENUM[s - 1][e - 1],
                               RESULT[s][e - 1] + BLOGGIUM[s - 1][e - 1]);
        }
    }
    
    return RESULT[southIndex + 1][eastIndex + 1];
}

int main() {
    int height;
    int width;
    
    scanf("%d %d", &height, &width);
    
    while(height && width) {
        
        readYeyenum(width, height);
        readBloggium(width, height);
        printf("%ld\n", calculateMaxMined(width - 1, height - 1));
        
        scanf("%d %d", &height, &width);
    }
    
    return 0;
}