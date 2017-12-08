// http://www.spoj.com/problems/MPILOT/

#include <stdio.h>

#define MAX_PILOTS 10000
#define INFINITY 10000000000

typedef
struct {
    long captain;
    long assistant;
} PilotSalary;

PilotSalary pilots[MAX_PILOTS + 1]; // pilots sorted by increase of age

long spendings[MAX_PILOTS / 2 + 1][MAX_PILOTS / 2 + 1];

inline long min(long a, long b) {
    return (a < b) ? a : b;
}

long calculate_min_spendings(int pilots_count) {
    int captains;
    int assistants;
    int pilotIdx;
    long captainSubproblem;
    long assistantSubproblem;
    
    spendings[0][0] = 0;
    
    for(captains = 1; captains <= pilots_count / 2; ++captains) {
        for(assistants = 0; assistants <= captains; ++assistants) {
            
            pilotIdx = pilots_count - captains - assistants;
            
            captainSubproblem = (assistants <= (captains - 1)) ? spendings[captains - 1][assistants] : INFINITY;
            assistantSubproblem = (assistants > 0) ? spendings[captains][assistants - 1] : INFINITY;
            
            spendings[captains][assistants] = min(captainSubproblem + pilots[pilotIdx].captain,
                                                  assistantSubproblem + pilots[pilotIdx].assistant);
        }
    }
    
    return spendings[pilots_count / 2][pilots_count / 2];
}

int main() {
    int pilots_count;
    int i;
    long min_spendings;
    
    scanf("%d", &pilots_count);
    for(i = 0; i < pilots_count; ++i) {
        scanf("%ld %ld", &pilots[i].captain, &pilots[i].assistant);
    }
    
    min_spendings = calculate_min_spendings(pilots_count);
    printf("%ld\n", min_spendings);
    
    return 0;
}