// http://www.spoj.com/problems/BYTESE2/

#include <stdio.h>

typedef
struct {
    long start;
    long end;
} Interval;

Interval time_intervals[101];

typedef
struct {
    int lazy;
    int max;
} Node;

Node segment_tree[1 << 25];

void lazy_propagate(long root, long rootL, long rootR) {
    segment_tree[root].max += segment_tree[root].lazy;
    long left = (root << 1) + 1;
    long right = left + 1;
    segment_tree[left].lazy += segment_tree[root].lazy;
    segment_tree[right].lazy += segment_tree[root].lazy;
    segment_tree[root].lazy = 0;
}

void aggregate(long l, long r, int value, long root, long rootL, long rootR) {
    lazy_propagate(root, rootL, rootR);
    
    if((r < rootL) || (rootR < l)) {
        return;
    }
    
    if((l <= rootL) && (rootR <= r)) {
        segment_tree[root].lazy += value;
        lazy_propagate(root, rootL, rootR);
        return;
    }
    
    long mid = (rootL + rootR) >> 1;
    long left = (root << 1) + 1;
    long right = left + 1;
    
    aggregate(l, r, value, left, rootL, mid);
    aggregate(l, r, value, right, mid + 1, rootR);
    
    segment_tree[root].max = (segment_tree[left].max > segment_tree[right].max)
                            ? segment_tree[left].max
                            : segment_tree[right].max;
}

int main() {
    /*
    aggregate(1, 7, 1, 0, 0, 1000);
    aggregate(2, 4, 1, 0, 0, 1000);
    aggregate(6, 9, 1, 0, 0, 1000);
    aggregate(3, 8, 1, 0, 0, 1000);
    aggregate(5, 10, 1, 0, 0, 1000);
    printf("%d\n", segment_tree[0].max);
    
    aggregate(1, 7, -1, 0, 0, 1000);
    aggregate(2, 4, -1, 0, 0, 1000);
    aggregate(6, 9, -1, 0, 0, 1000);
    aggregate(3, 8, -1, 0, 0, 1000);
    aggregate(5, 10, -1, 0, 0, 1000);
    printf("%d\n", segment_tree[0].max);
    
    aggregate(0, 19, 1, 0, 0, 1000);
    aggregate(0, 6, 5, 0, 0, 1000);
    aggregate(7, 10, 12, 0, 0, 1000);
    aggregate(10, 19, 100, 0, 0, 1000);
    printf("%d\n", segment_tree[0].max);
     */
    
    int testsNum;
    int time_intervals_num;
    long max_interval;
    int i;
    
    scanf("%d", &testsNum);
    
    while(testsNum--) {
        scanf("%d", &time_intervals_num);
        
        max_interval = -1;
        for(i = 0; i < time_intervals_num; ++i) {
            scanf("%d %d", &time_intervals[i].start, &time_intervals[i].end);
            max_interval = (time_intervals[i].end > max_interval) ? time_intervals[i].end : max_interval;
        }
        max_interval++;
        
        for(i = 0; i < time_intervals_num; ++i) {
            aggregate(time_intervals[i].start, time_intervals[i].end, 1, 0, 0, max_interval);
        }
        
        printf("%d\n", segment_tree[0].max);
        
        for(i = 0; i < time_intervals_num; ++i) {
            aggregate(time_intervals[i].start, time_intervals[i].end, -1, 0, 0, max_interval);
        }
    }
    
    return 0;
}