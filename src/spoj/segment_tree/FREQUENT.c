// http://www.spoj.com/problems/FREQUENT/

#include <stdio.h>

typedef
struct {
    // number, which describes the maximal frequency on the interval
    long maxFreq;
    // length of the preffix, which consists of the same characters
    long preffixLen;
    // length of the suffix, which consists of the same characters
    long suffixLen;
    // value of the left element of interval
    long leftVal;
    // value of the right element of interval
    long rightVal;
    // length of the interval
    long length;
} SegTreeNode;

long ARR[100000];
long ARR_MIN_INDEX = 1;
long ARR_MAX_INDEX = 1;

SegTreeNode SEG_TREE[1 << 18];
long SEG_TREE_ROOT = 0;

inline long max(long a, long b) {
    return (a > b) ? a : b;
}

inline void merge(SegTreeNode * target,
                  SegTreeNode * left,
                  SegTreeNode * right) {
    
    int leftEndEqualsToRightStart = (left->rightVal == right->leftVal);
    
    target->maxFreq = max(max(left->maxFreq, right->maxFreq),
                          leftEndEqualsToRightStart ? left->suffixLen + right->preffixLen : -1);
    
    target->preffixLen = max(left->preffixLen,
                             ((left->leftVal == left->rightVal) && leftEndEqualsToRightStart) ? left->length + right->preffixLen : -1);
    
    target->suffixLen = max(right->suffixLen,
                            ((right->leftVal == right->rightVal) && leftEndEqualsToRightStart) ? right->length + left->suffixLen : -1);
    
    target->length = left->length + right->length;
    
    target->leftVal = left->leftVal;
    target->rightVal = right->rightVal;
}

void buildSegmentTree(long curr, long currL, long currR) {
    if(currL == currR) {
        SEG_TREE[curr].maxFreq = 1;
        SEG_TREE[curr].preffixLen = 1;
        SEG_TREE[curr].suffixLen = 1;
        SEG_TREE[curr].leftVal = ARR[currL];
        SEG_TREE[curr].rightVal = ARR[currL];
        SEG_TREE[curr].length = 1;
        return;
    }
    
    int left = (curr << 1) + 1;
    int right = left + 1;
    
    int mid = (currL + currR) >> 1;
    
    buildSegmentTree(left, currL, mid);
    buildSegmentTree(right, mid + 1, currR);
    
    merge(&SEG_TREE[curr], &SEG_TREE[left], &SEG_TREE[right]);
}

SegTreeNode query(long l, long r, long curr, long currL, long currR) {
    if((l <= currL) && (currR <= r)) {
        return SEG_TREE[curr];
    }
    
    long mid = (currL + currR) >> 1;
    long left = (curr << 1) + 1;
    long right = left + 1;
    
    if(r <= mid) {
        return query(l, r, left, currL, mid);
    } else if(l > mid) {
        return query(l, r, right, mid + 1, currR);
    }
    
    SegTreeNode result;
    SegTreeNode leftResult = query(l, r, left, currL, mid);
    SegTreeNode rightResult = query(l, r, right, mid + 1, currR);
    
    merge(&result, &leftResult, &rightResult);
    
    return result;
}

int main() {
    /*
    // -1 -1 1 1 1 1 3 10 10 10
    ARR[1] = -1;
    ARR[2] = -1;
    ARR[3] = 1;
    ARR[4] = 1;
    ARR[5] = 1;
    ARR[6] = 1;
    ARR[7] = 3;
    ARR[8] = 10;
    ARR[9] = 10;
    ARR[10] = 10;
    ARR_MAX_INDEX = 10;
    
    buildSegmentTree(SEG_TREE_ROOT, ARR_MIN_INDEX, ARR_MAX_INDEX);
    
    printf("%ld\n", query(2, 3, SEG_TREE_ROOT, ARR_MIN_INDEX, ARR_MAX_INDEX).maxFreq);
    printf("%ld\n", query(1, 10, SEG_TREE_ROOT, ARR_MIN_INDEX, ARR_MAX_INDEX).maxFreq);
    printf("%ld\n", query(5, 10, SEG_TREE_ROOT, ARR_MIN_INDEX, ARR_MAX_INDEX).maxFreq);
    
    printf("%ld\n", query(1, 1, SEG_TREE_ROOT, ARR_MIN_INDEX, ARR_MAX_INDEX).maxFreq);
    printf("%ld\n", query(2, 2, SEG_TREE_ROOT, ARR_MIN_INDEX, ARR_MAX_INDEX).maxFreq);
    printf("%ld\n", query(10, 10, SEG_TREE_ROOT, ARR_MIN_INDEX, ARR_MAX_INDEX).maxFreq);
    
    printf("%ld\n", query(1, 3, SEG_TREE_ROOT, ARR_MIN_INDEX, ARR_MAX_INDEX).maxFreq);
    printf("%ld\n", query(1, 4, SEG_TREE_ROOT, ARR_MIN_INDEX, ARR_MAX_INDEX).maxFreq);
    printf("%ld\n", query(1, 5, SEG_TREE_ROOT, ARR_MIN_INDEX, ARR_MAX_INDEX).maxFreq);
    printf("%ld\n", query(7, 10, SEG_TREE_ROOT, ARR_MIN_INDEX, ARR_MAX_INDEX).maxFreq);
    printf("%ld\n", query(3, 10, SEG_TREE_ROOT, ARR_MIN_INDEX, ARR_MAX_INDEX).maxFreq);
     */
    
    
    long elementsCount;
    long queriesCount;
    
    while(1) {
        scanf("%ld", &elementsCount);
        if(!elementsCount) {
            break;
        }
        
        scanf("%ld", &queriesCount);
   
        ARR_MAX_INDEX = elementsCount;

        int i;
        for(i = 1; i <= elementsCount; i++) {
            scanf("%ld", &ARR[i]);
        }
    
        buildSegmentTree(SEG_TREE_ROOT, ARR_MIN_INDEX, ARR_MAX_INDEX);
    
        long l;
        long r;
        while(queriesCount--) {
            scanf("%ld %ld", &l, &r);
            printf("%ld\n", query(l, r, SEG_TREE_ROOT, ARR_MIN_INDEX, ARR_MAX_INDEX).maxFreq);
        }
    }
    
    return 0;
}