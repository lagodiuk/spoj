// http://www.spoj.com/problems/PARTIT/

#include <stdio.h>
#include <memory.h>

#define MAX_ARR_SIZE 10
#define MAX_SUM 220
#define MAX_VALUE 220

int dp[MAX_ARR_SIZE + 1][MAX_VALUE + 1][MAX_SUM + 1];
int arr_size;
int target_sum;
int kth;

int count_representations(int pos, int prev_val, int sum) {
    
    if(pos == arr_size) {
        return (sum == target_sum) ? 1 : 0;
    }
    
    if(dp[pos][prev_val][sum] == -1) {
        
        dp[pos][prev_val][sum] = 0;
        
        int i;
        for(i = prev_val; i <= MAX_VALUE; ++i) {
            
            if(sum + i <= target_sum) {
                
                dp[pos][prev_val][sum] += count_representations(pos + 1, i, sum + i);
                
            } else {
                break;
            }
        }
    }
    
    return dp[pos][prev_val][sum];
}

int count_representations_wrapper() {
    memset(dp, -1, sizeof dp);
    return count_representations(0, 1, 0);
}

void print_kth(int pos, int prev_val, int sum) {

    int i;
    for(i = prev_val; i <= MAX_VALUE; ++i) {
        
        if((sum + i <= target_sum) && (dp[pos + 1][i][sum + i] != -1)) {
            
            if(dp[pos + 1][i][sum + i] >= kth) {
                
                printf("%d ", i);
                print_kth(pos + 1, i, sum + i);
                return;
                
            } else {
                kth -= dp[pos + 1][i][sum + i];
            }
        } else {
            break;
        }
    }
    
    printf("%d\n", target_sum - sum);
}

void print_kth_wrapper() {
    print_kth(0, 1, 0);
}

int main() {
    /*
    // test
    arr_size = 4;
    target_sum = 9;
    int i;
    int cnt = count_representations_wrapper();
    for(i = 1; i <= cnt; ++i) {
        kth = i;
        print_kth_wrapper();
    }
     */
    
    int testsNum;
    scanf("%d", &testsNum);
    
    while(testsNum--) {
        scanf("%d %d %d", &target_sum, &arr_size, &kth);
        count_representations_wrapper();
        print_kth_wrapper();
    }
    
    return 0;
}