// http://www.spoj.com/problems/PHIDIAS/

#include <stdio.h>

#define MAX_PLATES 201
#define MAX_DIMENSION 601

typedef
struct {
    int width;
    int height;
} Plate;

Plate plates[MAX_PLATES];
int platesCount;

long MEMOIZED[MAX_DIMENSION][MAX_DIMENSION];
int IS_MEMOIZED[MAX_DIMENSION][MAX_DIMENSION];
int testNum = 0;

inline long min(long a, long b) {
    return (a > b) ? b : a;
}

long getMinWastedArea(int width, int height) {
    
    if(IS_MEMOIZED[width][height] == testNum) {
        return MEMOIZED[width][height];
    }
    
    int wasted = width * height;
    
    int i;
    for(i = 0; i < platesCount; ++i) {
        
        if((plates[i].width > width) || (plates[i].height > height)) {
            continue;
        }
        
        if((plates[i].width == width) && (plates[i].height == height)) {
            wasted = 0;
        }
        
        if(wasted == 0) {
            break;
        }
        
        wasted = min(wasted,
                     min(getMinWastedArea(width - plates[i].width, height) + getMinWastedArea(plates[i].width, height - plates[i].height),
                         getMinWastedArea(width, height - plates[i].height) + getMinWastedArea(width - plates[i].width, plates[i].height)));
    }
    
    IS_MEMOIZED[width][height] = testNum;
    MEMOIZED[width][height] = wasted;
    
    return wasted;
}

int main() {
    int testsNum;
    int width;
    int height;
    int i;
    long result;
    
    scanf("%d", &testsNum);
    
    while(testsNum--) {
        scanf("%d %d %d", &width, &height, &platesCount);
        for(i = 0; i < platesCount; ++i) {
            scanf("%d %d", &plates[i].width, &plates[i].height);
        }
        
        testNum++;
        result = getMinWastedArea(width, height);
        printf("%ld\n", result);
    }
    
    return 0;
}