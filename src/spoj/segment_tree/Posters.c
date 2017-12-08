// http://www.spoj.com/problems/POSTERS/

#include <stdio.h>
#include <memory.h>

// Begin: BitSet
#define MAX_INDEX_IN_BIT_SET 40000
#define BITS_IN_CHAR 8

char BIT_SET[(MAX_INDEX_IN_BIT_SET / BITS_IN_CHAR) + 1];

inline void setBit(int n) {
    BIT_SET[n / BITS_IN_CHAR] |= 1 << (n % BITS_IN_CHAR);
}

inline int getBit(int n) {
    return (BIT_SET[n / BITS_IN_CHAR] >> (n % BITS_IN_CHAR)) & 1;
}

inline void clearBitSet() {
    memset(BIT_SET, 0, sizeof(BIT_SET));
}
// End: BitSet

#define MAX_RIGHT 10000000

// 33554432 = ceil(log2(MAX_RIGHT)) * 2
#define MAX_INDEX 33554432

// storing index of poster in positions l and r
long SEG_TREE[MAX_INDEX];

#define NOT_VISITED 0
#define PROPAGATED -1

void update(long posterId, long l, long r, long curr, long currL, long currR) {
    if((l <= currL) && (currR <= r)) {
        SEG_TREE[curr] = posterId;
        return;
    }
    
    long mid = (currL + currR) >> 1;
    
    long left = (curr << 1) + 1;
    long right = left + 1;
    
    
    if(SEG_TREE[curr] != PROPAGATED) {
        SEG_TREE[left] = SEG_TREE[curr];
        SEG_TREE[right] = SEG_TREE[curr];
        SEG_TREE[curr] = PROPAGATED;
    }
    
    if(l > mid) {
        update(posterId, l, r, right, mid + 1, currR);
    } else if(r <= mid) {
        update(posterId, l, r, left, currL, mid);
    } else {
        update(posterId, l, r, right, mid + 1, currR);
        update(posterId, l, r, left, currL, mid);
    }
}

long dfs_and_clean(long curr) {
    if(SEG_TREE[curr] == NOT_VISITED) {
        return 0;
    }
    
    long result = 0;
    if(SEG_TREE[curr] == PROPAGATED) {
        long left = (curr << 1) + 1;
        long right = left + 1;
        
        //if(left <= MAX_INDEX) {
            result += dfs_and_clean(left);
        //}
        
        //if(right <= MAX_INDEX) {
            result += dfs_and_clean(right);
        //}
        
        SEG_TREE[curr] = NOT_VISITED;
    } else {
        //printf("%ld ", SEG_TREE[curr]);
        if(!getBit(SEG_TREE[curr])) {
            result = 1;
        }
        setBit(SEG_TREE[curr]);
    }
    
    return result;
}

int main() {
    int root = 0;
    int left = 0;
    int right = MAX_RIGHT;
    
    /*
    update(1, 1, 4, root, left, right);
    update(2, 2, 6, root, left, right);
    update(3, 8, 10, root, left, right);
    update(4, 3, 4, root, left, right);
    update(5, 7, 10, root, left, right);
    
    printf("%ld\n", dfs_and_clean(root)); // expected 4
    clearBitSet();
    
    update(1, 2, 2, root, left, right);
    update(2, 4, 8, root, left, right);
    update(3, 3, 5, root, left, right);
    update(4, 9, 15, root, left, right);
    update(5, 5, 5, root, left, right);
    update(6, 1, 1, root, left, right);
    update(7, 3, 4, root, left, right);
    update(8, 4, 4, root, left, right);
    update(9, 2, 3, root, left, right);
    
    printf("%ld\n", dfs_and_clean(root)); // expected 6
    clearBitSet();
    
    update(1, 3, 7, root, left, right);
    update(2, 1, 4, root, left, right);
    update(3, 5, 10, root, left, right);
    
    printf("%ld\n", dfs_and_clean(root)); // expected 2
    clearBitSet();
     */
    
    long numOfTestCases;
    long numOfPosters;
    
    scanf("%ld", &numOfTestCases);
    
    long l;
    long r;
    
    long i;
    long j;
    for(i = 0; i < numOfTestCases; i++) {
        scanf("%ld", &numOfPosters);
        for(j = 1; j <= numOfPosters; j++) {
            scanf("%ld %ld", &l, &r);
            update(j, l, r, root, left, right);
        }
        printf("%ld\n", dfs_and_clean(root));
        clearBitSet();
    }
    
    return 0;
}