// http://www.spoj.com/problems/CPCRC1C/

// Idea of solution inspired by:
// http://stackoverflow.com/questions/22394257/how-to-count-integers-between-large-a-and-b-with-a-certain-property/22394258#22394258

#include <stdio.h>
#include <memory.h>

#define SIZE 12

int target[SIZE];
int buffer[SIZE];

long long memoized[SIZE + 1][SIZE + 1][SIZE + 1][(SIZE + 1) * 10];
long long is_memoized[SIZE + 1][SIZE + 1][SIZE + 1][(SIZE + 1) * 10];
long long test_case_num = 0;

long long digitsSum(int position, int leftmostSmaller, int leftmostGreater, int currSum) {
    // (buffer < target) only in case if (leftmostSmaller < leftmostGreater)
    
    if(position == SIZE) {
        return (leftmostSmaller < leftmostGreater) ? currSum : 0;
    }
    
    if((leftmostGreater < leftmostSmaller) && (leftmostSmaller <= position)) {
        return 0;
    }
    
    if(is_memoized[position][leftmostSmaller][leftmostGreater][currSum] == test_case_num) {
        // this trick helps to avoid invalidation of "memoized" cache:
        // each call of function "digitsSumUpToNumber" - generates new value "test_case_num"
        // and value from array "memoized" extracted only in case, if "is_memoized" equal to "test_case_num"
        return memoized[position][leftmostSmaller][leftmostGreater][currSum];
    }
    
    int digit;
    int newLeftmostSmaller;
    int newLeftmostGreater;
    long long result;
    
    result = 0;
    for(digit = 0; digit <= 9; ++digit) {
        newLeftmostSmaller = leftmostSmaller;
        newLeftmostGreater = leftmostGreater;
        
        buffer[position] = digit;
        
        if((buffer[position] < target[position]) && (position < leftmostSmaller)) {
            newLeftmostSmaller = position;
        }
        
        if((buffer[position] > target[position]) && (position < leftmostGreater)) {
            newLeftmostGreater = position;
        }
        
        result += digitsSum(position + 1, newLeftmostSmaller, newLeftmostGreater, currSum + digit);
    }
    
    memoized[position][leftmostSmaller][leftmostGreater][currSum] = result;
    is_memoized[position][leftmostSmaller][leftmostGreater][currSum] = test_case_num;
    
    return result;
}

long long digitsSumUpToNumber(long long targetNum) {
    targetNum++;
    
    // transform targetNum to array of digits
    memset(target, 0, sizeof target);
    int i = SIZE - 1;
    while(targetNum) {
        target[i] = targetNum % 10;
        i--;
        targetNum /= 10;
    }
    
    memset(buffer, 0, sizeof buffer);
    
    test_case_num++;
    return digitsSum(i - 1, SIZE, SIZE, 0);
}

long long digitsSumOnInterval(long long from, long long to) {
    return digitsSumUpToNumber(to) - digitsSumUpToNumber(from - 1);
}

int main() {
    /*
    printf("%lld\n", digitsSumOnInterval(1, 10));
    printf("%lld\n", digitsSumOnInterval(100, 777));
    printf("%lld\n", digitsSumOnInterval(1, 1000000));
    printf("%lld\n", digitsSumOnInterval(1, 10000000));
    printf("%lld\n", digitsSumOnInterval(1, 100000000));
    printf("%lld\n", digitsSumOnInterval(1, 1000000000));
    printf("%lld\n", digitsSumOnInterval(12345, 1000000000));
     */
    
    long long from;
    long long to;
    
    scanf("%lld %lld", &from, &to);
    while((from != -1) && (to != -1)) {
        printf("%lld\n", digitsSumOnInterval(from, to));
        scanf("%lld %lld", &from, &to);
    }
    
    return 0;
}