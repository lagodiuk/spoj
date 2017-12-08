// http://www.spoj.com/problems/GSS3/

#include <stdio.h>

typedef
struct {
    long maxSum;
    long maxPrefix;
    long maxSuffix;
    long sum;
} intervalInfo;

int ARR[50000];
intervalInfo SEG_TREE[1 << 17];

inline long max(long a, long b) {
    return (a > b) ? a : b;
}

void construct(long curr, long currL, long currR) {
    if(currL == currR) {
        SEG_TREE[curr].maxSum = ARR[currL];
        SEG_TREE[curr].sum = ARR[currL];
        SEG_TREE[curr].maxPrefix = ARR[currL];
        SEG_TREE[curr].maxSuffix = ARR[currL];
        return;
    }
    
    long left = (curr << 1) + 1;
    long right = left + 1;
    
    long mid = (currL + currR) >> 1;
    construct(left, currL, mid);
    construct(right, mid + 1, currR);
    
    SEG_TREE[curr].sum = SEG_TREE[left].sum + SEG_TREE[right].sum;
    
    SEG_TREE[curr].maxSum =
        max(max(SEG_TREE[left].maxSum, SEG_TREE[right].maxSum),
            SEG_TREE[left].maxSuffix + SEG_TREE[right].maxPrefix);
    
    SEG_TREE[curr].maxPrefix =
        max(SEG_TREE[left].maxPrefix,
            SEG_TREE[left].sum + SEG_TREE[right].maxPrefix);
    
    SEG_TREE[curr].maxSuffix =
        max(SEG_TREE[right].maxSuffix,
            SEG_TREE[right].sum + SEG_TREE[left].maxSuffix);
}

void update(long index, int newVal, long curr, long currL, long currR) {
    if(currL == currR) {
        SEG_TREE[curr].maxSum = newVal;
        SEG_TREE[curr].sum = newVal;
        SEG_TREE[curr].maxPrefix = newVal;
        SEG_TREE[curr].maxSuffix = newVal;
        return;
    }
    
    long left = (curr << 1) + 1;
    long right = left + 1;
    
    long mid = (currL + currR) >> 1;
    
    if(index <= mid) {
        update(index, newVal, left, currL, mid);
    } else {
        update(index, newVal, right, mid + 1, currR);
    }
    
    SEG_TREE[curr].sum = SEG_TREE[left].sum + SEG_TREE[right].sum;
    
    SEG_TREE[curr].maxSum =
    max(max(SEG_TREE[left].maxSum, SEG_TREE[right].maxSum),
        SEG_TREE[left].maxSuffix + SEG_TREE[right].maxPrefix);
    
    SEG_TREE[curr].maxPrefix =
    max(SEG_TREE[left].maxPrefix,
        SEG_TREE[left].sum + SEG_TREE[right].maxPrefix);
    
    SEG_TREE[curr].maxSuffix =
    max(SEG_TREE[right].maxSuffix,
        SEG_TREE[right].sum + SEG_TREE[left].maxSuffix);
}

intervalInfo query(long l, long r, long curr, long currL, long currR) {
    if((l <= currL) && (currR <= r)) {
        return SEG_TREE[curr];
    }
    
    long left = (curr << 1) + 1;
    long right = left + 1;
    
    long mid = (currL + currR) >> 1;
    
    if(r <= mid) {
        return query(l, r, left, currL, mid);
    } else if(l > mid) {
        return query(l, r, right, mid + 1, currR);
    }
    
    intervalInfo leftVal = query(l, r, left, currL, mid);
    intervalInfo rightVal = query(l, r, right, mid + 1, currR);

    intervalInfo result;
    
    result.sum = leftVal.sum + rightVal.sum;
    
    result.maxSum =
        max(max(leftVal.maxSum, rightVal.maxSum),
            leftVal.maxSuffix + rightVal.maxPrefix);
    
    result.maxPrefix =
        max(leftVal.maxPrefix,
            leftVal.sum + rightVal.maxPrefix);
    
    result.maxSuffix =
        max(rightVal.maxSuffix,
            rightVal.sum + leftVal.maxSuffix);
    
    return result;
}

int main() {
    long count;
    scanf("%ld", &count);
    
    int i;
    for(i = 0; i < count; i++) {
        scanf("%i", &ARR[i]);
    }
    
    long left = 0;
    long right = count - 1;
    
    construct(0, left, right);
    
    long queryCount;
    long queryType;
    long l;
    long r;
    scanf("%ld", &queryCount);
    for(i = 0; i < queryCount; i++) {
        scanf("%ld %ld %ld", &queryType, &l, &r);
        
        if(queryType == 1) {
            printf("%ld\n", query(l - 1, r - 1, 0, left, right).maxSum);
        } else {
            update(l - 1, r, 0, left, right);
        }
    }
    
    return 0;
}