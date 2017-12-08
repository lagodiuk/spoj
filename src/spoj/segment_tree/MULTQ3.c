// http://www.spoj.com/problems/MULTQ3/

#include <stdio.h>

typedef
struct {
    long mod0;
    long mod1;
    long mod2;
} Info;

// ceil(log2(100000)) == 17
Info SEG_TREE[1 << (17 + 1)];
long LAZY[1 << (17 + 1)];

void initialize(long curr, long currL, long currR) {
    if(currL == currR) {
        SEG_TREE[curr].mod0 = 1;
        SEG_TREE[curr].mod1 = 0;
        SEG_TREE[curr].mod2 = 0;
        LAZY[curr] = 0;
        return;
    }
    
    long mid = (currL + currR) >> 1;
    
    long left = (curr << 1) + 1;
    long right = left + 1;
    
    initialize(left, currL, mid);
    initialize(right, mid + 1, currR);
    
    SEG_TREE[curr].mod0 = SEG_TREE[left].mod0 + SEG_TREE[right].mod0;
    SEG_TREE[curr].mod1 = 0;
    SEG_TREE[curr].mod2 = 0;
}

inline void lazyUpdate(long curr, long currL, long currR) {
    long mod = LAZY[curr] % 3;
    
    if(mod != 0) {
        if(currL != currR) {
            long left = (curr << 1) + 1;
            long right = left + 1;
        
            LAZY[left] += mod;
            LAZY[right] += mod;
        }
        
        long tmp;
        if(mod == 1) {
            tmp = SEG_TREE[curr].mod2;
            SEG_TREE[curr].mod2 = SEG_TREE[curr].mod1;
            SEG_TREE[curr].mod1 = SEG_TREE[curr].mod0;
            SEG_TREE[curr].mod0 = tmp;
        } else { // mod == 2
            tmp = SEG_TREE[curr].mod2;
            SEG_TREE[curr].mod2 = SEG_TREE[curr].mod0;
            SEG_TREE[curr].mod0 = SEG_TREE[curr].mod1;
            SEG_TREE[curr].mod1 = tmp;
        }
    }
    LAZY[curr] = 0;
}

void plusOne(long l, long r, long curr, long currL, long currR) {
    lazyUpdate(curr, currL, currR);
    
    if((r < currL) || (currR < l)) {
        return;
    }
    
    if((l <= currL) && (currR <= r)) {
        LAZY[curr]++;
        lazyUpdate(curr, currL, currR);
        return;
    }
    
    long mid = (currL + currR) >> 1;
    
    long left = (curr << 1) + 1;
    long right = left + 1;
    
    plusOne(l, r, left, currL, mid);
    plusOne(l, r, right, mid + 1, currR);
    
    SEG_TREE[curr].mod0 = SEG_TREE[left].mod0 + SEG_TREE[right].mod0;
    SEG_TREE[curr].mod1 = SEG_TREE[left].mod1 + SEG_TREE[right].mod1;
    SEG_TREE[curr].mod2 = SEG_TREE[left].mod2 + SEG_TREE[right].mod2;
}

long query(long l, long r, long curr, long currL, long currR) {
    lazyUpdate(curr, currL, currR);
    
    if((r < currL) || (currR < l)) {
        return 0;
    }
    
    if((l <= currL) && (currR <= r)) {
        return SEG_TREE[curr].mod0;
    }
    
    long mid = (currL + currR) >> 1;
    
    long left = (curr << 1) + 1;
    long right = left + 1;
    
    return query(l, r, left, currL, mid) + query(l, r, right, mid + 1, currR);
}

int main() {
    /*
    long root = 0;
    long rootL = 0;
    long rootR = 100;
    
    initialize(root, rootL, rootR);
    
    printf("%ld\n", query(0, 3, root, rootL, rootR).mod0);
    plusOne(1, 2, root, rootL, rootR);
    plusOne(1, 3, root, rootL, rootR);
    printf("%ld\n", query(0, 0, root, rootL, rootR).mod0);
    plusOne(0, 3, root, rootL, rootR);
    printf("%ld\n", query(3, 3, root, rootL, rootR).mod0);
    printf("%ld\n", query(0, 3, root, rootL, rootR).mod0);
     */
    
    long N;
    scanf("%ld", &N);
    
    long root = 0;
    long rootL = 0;
    long rootR = N - 1;
    
    
    
    initialize(root, rootL, rootR);
    
    long queriesNum;
    scanf("%ld", &queriesNum);
    
    long queryType;
    long l;
    long r;
    
    while(queriesNum--) {
        scanf("%ld %ld %ld", &queryType, &l, &r);
        if(queryType == 1) {
            printf("%ld\n", query(l, r, root, rootL, rootR));
        } else {
            plusOne(l, r, root, rootL, rootR);
        }
    }
    
    return 0;
}