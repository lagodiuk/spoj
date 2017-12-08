// http://www.spoj.com/problems/ALIEN/

#include <stdio.h>

int humans_on_station[100001];

void solve(long stations_count, long long humans_threshold, long * max_stations_passed, long long * min_humans_met) {
    
    long long humans_met_so_far = 0;
    long stations_passed_so_far = 0;
    
    long long min_humans_met_so_far = 100000001;
    long max_stations_passed_so_far = 0;
    
    long left = 0;
    long right;
    
    for(right = 0; right < stations_count; ++right) {
        humans_met_so_far += humans_on_station[right];
        stations_passed_so_far++;
        
        while(humans_met_so_far > humans_threshold) {
            humans_met_so_far -= humans_on_station[left];
            stations_passed_so_far--;
            left++;
        }
        
        if((stations_passed_so_far > max_stations_passed_so_far)
           || ((stations_passed_so_far == max_stations_passed_so_far) && (humans_met_so_far < min_humans_met_so_far))) {
            
            max_stations_passed_so_far = stations_passed_so_far;
            min_humans_met_so_far = humans_met_so_far;
        }
    }
    
    *max_stations_passed = max_stations_passed_so_far;
    *min_humans_met = min_humans_met_so_far;
}

int main() {
    
    long testCasesNum;
    long stations_count;
    long long humans_threshold;
    long max_stations_passed;
    long long min_humans_met;
    long i;
    
    scanf("%ld", &testCasesNum);
    
    while(testCasesNum--) {
        scanf("%ld %lld", &stations_count, &humans_threshold);
        for(i = 0; i < stations_count; ++i) {
            scanf("%d", &humans_on_station[i]);
        }
        solve(stations_count, humans_threshold, &max_stations_passed, &min_humans_met);
        printf("%lld %ld\n", min_humans_met, max_stations_passed);
    }
}