// http://www.spoj.com/problems/MBEEWALK/

#include <stdio.h>

// Step 1: precalculate for all path length [1..14]
// Step 2: use precalculated walues to solve the problem

// Precalculate with complexity O(N^7)
#ifdef __PRECALCULATE_O_N_7__

#include <memory.h>

#define MAX_PATH_LENGTH 14

int MEMOIZED[MAX_PATH_LENGTH + 1][MAX_PATH_LENGTH / 2 + 2][MAX_PATH_LENGTH / 2 + 2][MAX_PATH_LENGTH / 2 + 2][MAX_PATH_LENGTH / 2 + 2][MAX_PATH_LENGTH / 2 + 2][MAX_PATH_LENGTH / 2 + 2];

inline int abs(int x) {
    return (x > 0) ? x : -x;
}

// Complexity O(N^7)
int calculateDifferentWalks(int availableSteps, int right, int left, int rightUp, int leftDown, int leftUp, int rightDown) {
    
    if((right > MAX_PATH_LENGTH / 2 + 1) || (left > MAX_PATH_LENGTH / 2 + 1) ||
       (rightUp > MAX_PATH_LENGTH / 2 + 1) || (leftDown > MAX_PATH_LENGTH / 2 + 1) ||
       (rightDown > MAX_PATH_LENGTH / 2 + 1) || (leftUp > MAX_PATH_LENGTH / 2 + 1)) {
        
        return 0;
    }
    
    if(MEMOIZED[availableSteps][right][left][rightUp][leftDown][leftUp][rightDown] != -1) {
        return MEMOIZED[availableSteps][right][left][rightUp][leftDown][leftUp][rightDown];
    }
    
    int result;
    
    if(availableSteps == 0) {
        int rightLeftDiff = right - left;
        int rightUpLeftDownDiff = rightUp - leftDown;
        int leftUpRightDownDiff = leftUp - rightDown;
        
        if((abs(rightLeftDiff) == abs(rightUpLeftDownDiff))
           && (abs(rightLeftDiff) == abs(leftUpRightDownDiff))
           && ((rightUpLeftDownDiff == rightLeftDiff) && (rightUpLeftDownDiff == -leftUpRightDownDiff))) {
            
            result = 1;
        } else {
            result = 0;
        }
    } else {
        
        result =  calculateDifferentWalks(availableSteps - 1, right + 1, left, rightUp, leftDown, leftUp, rightDown)
                + calculateDifferentWalks(availableSteps - 1, right, left + 1, rightUp, leftDown, leftUp, rightDown)
                + calculateDifferentWalks(availableSteps - 1, right, left, rightUp + 1, leftDown, leftUp, rightDown)
                + calculateDifferentWalks(availableSteps - 1, right, left, rightUp, leftDown + 1, leftUp, rightDown)
                + calculateDifferentWalks(availableSteps - 1, right, left, rightUp, leftDown, leftUp + 1, rightDown)
                + calculateDifferentWalks(availableSteps - 1, right, left, rightUp, leftDown, leftUp, rightDown + 1);
    }
    
    MEMOIZED[availableSteps][right][left][rightUp][leftDown][leftUp][rightDown] = result;
    
    return result;
}

int calculateDifferentWalksWrapper(int availableSteps) {
    return calculateDifferentWalks(availableSteps, 0, 0, 0, 0, 0, 0);
}

int main() {
    memset(MEMOIZED, -1, sizeof MEMOIZED);
    int i;
    for(i = 1; i <= 14; ++i) {
        printf("%d -> %d\n", i, calculateDifferentWalksWrapper(i));
    }
    return 0;
}

// Precalculate with complexity O(N^4)
#elif __PRECALCULATE_O_N_4__

#include <memory.h>

#define MAX_PATH_LENGTH 14

int MEMOIZED[MAX_PATH_LENGTH + 1][2 * MAX_PATH_LENGTH + 1][2 * MAX_PATH_LENGTH + 1][2 * MAX_PATH_LENGTH + 1];

inline int abs(int x) {
    return (x > 0) ? x : -x;
}

// Complexity O(N^4)
int calculateDifferentWalks(int availableSteps, int leftRightDiff, int rightUpLeftDownDiff, int leftUpRightDownDiff) {
    
    if(MEMOIZED[availableSteps][leftRightDiff + MAX_PATH_LENGTH][rightUpLeftDownDiff + MAX_PATH_LENGTH][leftUpRightDownDiff + MAX_PATH_LENGTH] != -1) {
        
        return MEMOIZED[availableSteps][leftRightDiff + MAX_PATH_LENGTH][rightUpLeftDownDiff + MAX_PATH_LENGTH][leftUpRightDownDiff + MAX_PATH_LENGTH];
    }
    
    int result;
    
    if(availableSteps == 0) {
        if(abs(leftRightDiff) == abs(rightUpLeftDownDiff)
           && abs(leftRightDiff) == abs(leftUpRightDownDiff)
           && leftRightDiff == rightUpLeftDownDiff
           && leftRightDiff == -leftUpRightDownDiff) {
            
            result = 1;
            
        } else {
            
            result = 0;
        }
        
    } else {
        
        result = calculateDifferentWalks(availableSteps - 1, leftRightDiff + 1, rightUpLeftDownDiff, leftUpRightDownDiff)
               + calculateDifferentWalks(availableSteps - 1, leftRightDiff - 1, rightUpLeftDownDiff, leftUpRightDownDiff)
               + calculateDifferentWalks(availableSteps - 1, leftRightDiff, rightUpLeftDownDiff + 1, leftUpRightDownDiff)
               + calculateDifferentWalks(availableSteps - 1, leftRightDiff, rightUpLeftDownDiff - 1, leftUpRightDownDiff)
               + calculateDifferentWalks(availableSteps - 1, leftRightDiff, rightUpLeftDownDiff, leftUpRightDownDiff + 1)
               + calculateDifferentWalks(availableSteps - 1, leftRightDiff, rightUpLeftDownDiff, leftUpRightDownDiff - 1);
        
    }
    
    MEMOIZED[availableSteps][leftRightDiff + MAX_PATH_LENGTH][rightUpLeftDownDiff + MAX_PATH_LENGTH][leftUpRightDownDiff + MAX_PATH_LENGTH] = result;
    return result;
}

int calculateDifferentWalksWrapper(int availableSteps) {
    return calculateDifferentWalks(availableSteps, 0, 0, 0);
}

int main() {
    memset(MEMOIZED, -1, sizeof MEMOIZED);
    int i;
    for(i = 1; i <= 14; ++i) {
        printf("%d -> %d\n", i, calculateDifferentWalksWrapper(i));
    }
    return 0;
}

// Precalculate with complexity O(N^3)
#elif __PRECALCULATE_O_N_3__

#include <memory.h>

#define MAX_PATH_LENGTH 14

int MEMOIZED[MAX_PATH_LENGTH + 1][2 * MAX_PATH_LENGTH + 1][2 * MAX_PATH_LENGTH + 1];

// Complexity O(N^3)
int calculateDifferentWalks(int availableSteps, int x, int y, int startX, int startY) {
    
    if(MEMOIZED[availableSteps][x + MAX_PATH_LENGTH][y + MAX_PATH_LENGTH] != -1) {
        return MEMOIZED[availableSteps][x + MAX_PATH_LENGTH][y + MAX_PATH_LENGTH];
    }
    
    int result;
    
    if(availableSteps == 0) {
        
        result = ((x == startX) && (y == startY)) ? 1 : 0;
        
    } else {
        
        result = calculateDifferentWalks(availableSteps - 1, x + 1, y, startX, startY)
               + calculateDifferentWalks(availableSteps - 1, x - 1, y, startX, startY)
               + calculateDifferentWalks(availableSteps - 1, x, y + 1, startX, startY)
               + calculateDifferentWalks(availableSteps - 1, x, y - 1, startX, startY)
               + calculateDifferentWalks(availableSteps - 1, x + 1, y + 1, startX, startY)
               + calculateDifferentWalks(availableSteps - 1, x - 1, y - 1, startX, startY);
        
    }
    
    MEMOIZED[availableSteps][x + MAX_PATH_LENGTH][y + MAX_PATH_LENGTH] = result;
    return result;
}

int calculateDifferentWalksWrapper(int availableSteps) {
    return calculateDifferentWalks(availableSteps, 0, 0, 0, 0);
}

int main() {
    memset(MEMOIZED, -1, sizeof MEMOIZED);
    int i;
    for(i = 1; i <= 14; ++i) {
        printf("%d -> %d\n", i, calculateDifferentWalksWrapper(i));
    }
    return 0;
}

#else

int main() {
    int testsNum;
    int pathLength;
    
    scanf("%d", &testsNum);
    
    while(testsNum--) {
        scanf("%d", &pathLength);
        
        switch(pathLength) {
            case 1:
                printf("0\n");
                break;
            case 2:
                printf("6\n");
                break;
            case 3:
                printf("12\n");
                break;
            case 4:
                printf("90\n");
                break;
            case 5:
                printf("360\n");
                break;
            case 6:
                printf("2040\n");
                break;
            case 7:
                printf("10080\n");
                break;
            case 8:
                printf("54810\n");
                break;
            case 9:
                printf("290640\n");
                break;
            case 10:
                printf("1588356\n");
                break;
            case 11:
                printf("8676360\n");
                break;
            case 12:
                printf("47977776\n");
                break;
            case 13:
                printf("266378112\n");
                break;
            case 14:
                printf("1488801600\n");
                break;
        }
    }
    
    return 0;
}

#endif