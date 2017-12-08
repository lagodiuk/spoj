// http://www.spoj.com/problems/AMR11A/

#include <stdio.h>

#define MAX_DIM 500
#define NEGATIVE_INFINITY -10000000

long GRID[MAX_DIM][MAX_DIM];
long PATH[MAX_DIM][MAX_DIM];

inline long max(long a, long b) {
    return (a > b) ? a : b;
}

inline void readGrid(int rows, int columns) {
    int r;
    int c;
    for(r = 0; r < rows; r++) {
        for(c = 0; c < columns; c++) {
            scanf("%ld", &GRID[r][c]);
        }
    }
}

inline int isInitialValueFeasible(int rows, int columns, long initial) {
    int r;
    int c;
    
    PATH[0][0] = initial;
    for(c = 1; c < columns; c++) {
        PATH[0][c] = PATH[0][c - 1] + GRID[0][c];
        if(PATH[0][c] <= 0) {
            PATH[0][c] = NEGATIVE_INFINITY;
        }
    }
    
    for(r = 1; r < rows; r++) {
        PATH[r][0] = PATH[r - 1][0] + GRID[r][0];
        if(PATH[r][0] <= 0) {
            PATH[r][0] = NEGATIVE_INFINITY;
        }
        
        for(c = 1; c < columns; c++) {
            PATH[r][c] = max(PATH[r][c - 1], PATH[r - 1][c]) + GRID[r][c];
            if(PATH[r][c] <= 0) {
                PATH[r][c] = NEGATIVE_INFINITY;
            }
        }
    }
    
    /*
     // Debug print of PATH
     printf("\n");
     for(r = 0; r < rows; r++) {
     for(c = 0; c < columns; c++) {
     printf("%ld\t", PATH[r][c]);
     }
     printf("\n");
     }
     printf("\n");
     */
    
    return (PATH[rows - 1][columns - 1] > 0);
}

int main() {
    int testCasesNum;
    int rows;
    int columns;
    
    long left;
    long right;
    long mid;
    
    scanf("%d", &testCasesNum);
    while(testCasesNum--) {
        scanf("%d %d", &rows, &columns);
        readGrid(rows, columns);
        
        // Binary search for the best initial value
        // (which will help to achieve the optimal path to target - with only positive cells across path)
        // Complexity is: O((N^2)*log((Rows + Columns)*1000))
        // 1000 - is just a max value from GRID
        left = 1;
        right = ((long)rows + (long)columns) * 1000;
        while(left < right) {
            mid = (left + right) >> 1;
            if(isInitialValueFeasible(rows, columns, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        
        printf("%ld\n", left);
    }
    return 0;
}