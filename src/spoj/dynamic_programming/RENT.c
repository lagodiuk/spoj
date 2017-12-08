// http://www.spoj.com/problems/RENT/

#include <stdio.h>

#define MAX_ORDERS_NUM 10002

typedef
struct {
    long price;
    long startTime;
    long endTime;
} Order;

Order orders[MAX_ORDERS_NUM];
long bestPrice[MAX_ORDERS_NUM];

// Solution:
//
// 1)
// sort all orders by end time
//
// 2)
// maxCost(i) = max(maxCost(i - 1), maxCost(t) + price(i))
// where t - is the index of the closest compatible order,
// which end time is still smaller than start time of order with index i

// Sorting orders by end time using Heap Sort: O(N*log(N))
inline void pushDown(int index, int length) {
    int max = index * 2 + 1;
    if(max >= length) {
        return;
    }
    
    int right = max + 1;
    if((right < length) && (orders[right].endTime > orders[max].endTime)) {
        max = right;
    }
    
    if(orders[max].endTime > orders[index].endTime) {
        Order tmp = orders[index];
        orders[index] = orders[max];
        orders[max] = tmp;
        
        pushDown(max, length);
    }
}

void sortOrdersByEndTime(int length) {
    int i;
    for(i = length / 2; i >= 0; --i) {
        pushDown(i, length);
    }
    
    Order tmp;
    while(length > 0) {
        tmp = orders[0];
        orders[0] = orders[length - 1];
        orders[length - 1] = tmp;
        length--;
        pushDown(0, length);
    }
}
// end of implementation of sorting algorithm

inline long max(long a, long b) {
    return (a > b) ? a : b;
}

// O(N^2) solution
long solve(int length) {
    sortOrdersByEndTime(length);
    
    bestPrice[0] = orders[0].price;
    int i;
    int j;
    
    for(i = 1; i < length; ++i) {
        // find the closest order, with end time <= start time of order i
        // using linear search
        for(j = i - 1; j >= 0; --j) {
            if(orders[j].endTime <= orders[i].startTime) {
                break;
            }
        }
        
        bestPrice[i] = max(bestPrice[i - 1], bestPrice[j] + orders[i].price);
    }
    
    return bestPrice[length - 1];
}

// O(N*log(N)) solution
long solve_optimized(int length) {
    sortOrdersByEndTime(length);
    
    bestPrice[0] = 0;
    int i;
    int left;
    int right;
    int mid;
    
    for(i = 1; i <= length; ++i) {
        // find the closest order, with end time <= start time of order i
        // using binary search
        left = 0;
        right = i - 1;
        while(left < right) {
            mid = (left + right + 1) / 2;
            if(orders[mid - 1].endTime > orders[i - 1].startTime) {
                right = mid - 1;
            } else {
                left = mid;
            }
        }
        
        
        bestPrice[i] = max(bestPrice[i - 1], bestPrice[left] + orders[i - 1].price);
    }
    
    return bestPrice[length];
}

int main() {
    int testsNum;
    int ordersNum;
    long price;
    long startTime;
    long duration;
    int i;
    long result;
    
    scanf("%d", &testsNum);
    
    while(testsNum--) {
        scanf("%d", &ordersNum);
        for(i = 0; i < ordersNum; ++i) {
            scanf("%ld %ld %ld", &startTime, &duration, &price);
            orders[i].startTime = startTime;
            orders[i].endTime = startTime + duration;
            orders[i].price = price;
        }
        result = solve_optimized(ordersNum);
        printf("%ld\n", result);
    }
}