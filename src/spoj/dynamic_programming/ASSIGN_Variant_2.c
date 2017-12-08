// http://www.spoj.com/problems/ASSIGN/

#include <stdio.h>

#define MAX_STUDENTS 20
#define MAX_TASKS 20

long studentsPreferences[MAX_STUDENTS];
// according to description of the problem:
// students count == tasks count
int tasksCount;

// memeory consumption might be decreased: from (N * 2^N) -> to (2 ^ N)
// in case of bottom-up Dynamic Programming solution (instead of top-down solution)
unsigned long long memoized[MAX_STUDENTS][1 << MAX_TASKS];
int is_memoized[MAX_STUDENTS][1 << MAX_TASKS];
int testCaseNum = 0;

inline int isBitSet(long mask, int index) {
    return (mask & (1 << index));
}

inline void toggleBit(long * mask, int index) {
    (*mask) ^= (1 << index);
}

// Complexity is: O((N^2) * (2^N))
unsigned long long calculatePossibleAssignments(int studentIndex, long unavailableTasksMask) {
    if(studentIndex < 0) {
        return 1;
    }
    
    if(is_memoized[studentIndex][unavailableTasksMask] == testCaseNum) {
        return memoized[studentIndex][unavailableTasksMask];
    }
    
    long currentStudentPreferences = studentsPreferences[studentIndex];
    
    unsigned long long result = 0;
    
    int i; // bit mask of the next non-zero bit
    
    long x = currentStudentPreferences & (~unavailableTasksMask);
    long x_1; // variable for "x - 1"
    
    // iterating over only non-zero bits
    while(x) {
        x_1 = x - 1;
        i = ((x ^ x_1) >> 1) + 1;

        unavailableTasksMask ^= i; // toggle bit
        result += calculatePossibleAssignments(studentIndex - 1, unavailableTasksMask);
        unavailableTasksMask ^= i; // toggle bit

        x &= x_1;
    }
    
    memoized[studentIndex][unavailableTasksMask] = result;
    is_memoized[studentIndex][unavailableTasksMask] = testCaseNum;
    
    return result;
}

int main() {
    int testsNum;
    int i;
    int j;
    int isPreferred;
    long unavailableTasksMask;
    
    scanf("%d", &testsNum);
    
    while(testsNum--) {
        scanf("%d", &tasksCount);
        
        for(i = 0; i < tasksCount; ++i) {
            studentsPreferences[i] = 0;
            
            for(j = 0; j < tasksCount; ++j) {
                scanf("%d", &isPreferred);
                
                if(isPreferred) {
                    toggleBit(&studentsPreferences[i], j);
                }
            }
        }
        
        unavailableTasksMask = 0;
        testCaseNum++;
        
        printf("%llu\n", calculatePossibleAssignments(tasksCount - 1, unavailableTasksMask));
    }
    return 0;
}