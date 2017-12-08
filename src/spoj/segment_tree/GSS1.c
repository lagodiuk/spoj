// http://www.spoj.com/problems/GSS1/

#include <stdio.h>

typedef
struct {
    long sum;
    long maxSum;
    long maxPrefix;
    long maxSuffix;
} partial;

partial SEG_TREE[300000];
int ARR[50005];
int left;
int right;
int root;

inline long max(long a, long b) {
    return (a > b) ? a : b;
}

inline void merge(partial * target, partial * left, partial * right) {
    target->sum = left->sum + right->sum;
    
    target->maxPrefix = max(left->maxPrefix,
                            left->sum + right->maxPrefix);
    
    target->maxSuffix = max(right->maxSuffix,
                            right->sum + left->maxSuffix);
    
    /*
     target->maxSum = max(max(left->maxSum, right->maxSum),
     max(left->maxSuffix + right->maxPrefix,
     max(target->maxPrefix, target->maxSuffix)));
     */
    target->maxSum = max(max(left->maxSum, right->maxSum),
                         left->maxSuffix + right->maxPrefix);
}

void initialize(int curr, int currL, int currR) {
    if(currL == currR) {
        SEG_TREE[curr].sum = ARR[currL];
        SEG_TREE[curr].maxSum = ARR[currL];
        SEG_TREE[curr].maxPrefix = ARR[currL];
        SEG_TREE[curr].maxSuffix = ARR[currL];
        return;
    }
    
    int left = (curr << 1) + 1;
    int right = left + 1;
    
    int mid = (currL + currR) >> 1;
    initialize(left, currL, mid);
    initialize(right, mid + 1, currR);
    
    merge(&SEG_TREE[curr], &SEG_TREE[left], &SEG_TREE[right]);
}

partial query(int l, int r, int curr, int currL, int currR) {
    if(l <= currL && currR <= r) {
        return SEG_TREE[curr];
    }
    
    int left = (curr << 1) + 1;
    int right = left + 1;
    
    int mid = (currL + currR) >> 1;
    if(l > mid) {
        return query(l, r, right, mid + 1, currR);
    } else if (r <= mid) {
        return query(l, r, left, currL, mid);
    }
    
    partial result;
    partial lResult = query(l, r, left, currL, mid);
    partial rResult = query(l, r, right, mid + 1, currR);
    
    merge(&result, &lResult, &rResult);
    return result;
}

int main(void) {
    
    /*
     left = 0;
     right = 2;
     root = 0;
     
     ARR[0] = -1;
     ARR[1] = 2;
     ARR[2] = 3;
     initialize(root, left, right);
     printf("%d\n", query(0, 1, root, left, right).maxSum);
     printf("%d\n", query(0, 2, root, left, right).maxSum);
     printf("%d\n", query(0, 0, root, left, right).maxSum);
     printf("%d\n", query(1, 1, root, left, right).maxSum);
     printf("%d\n", query(2, 2, root, left, right).maxSum);
     */
    
    int size;
    scanf("%d", &size);
    int i;
    for(i = 0; i < size; i++) {
        scanf("%d", &ARR[i]);
    }
    
    root = 0;
    left = 0;
    right = size - 1;
    initialize(root, left, right);
    
    int qNum;
    scanf("%i", &qNum);
    
    int l;
    int r;
    for(i = 0; i < qNum; i++) {
        scanf("%d %d", &l, &r);
        printf("%ld\n", query(l - 1, r - 1, root, left, right).maxSum);
    }
    
    return 0;
}