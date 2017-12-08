// http://www.spoj.com/problems/JEDNAKOS/

#include <stdio.h>
#include <stdlib.h>

#define MAX_TARGET_NUM 5015
#define MAX_DIGITS_CNT 1015
#define boolean int
#define TRUE 1

typedef
struct {
	int curr_digit_pos;
	int target_sum;
	int dist;
} State;

char input[MAX_DIGITS_CNT];
int digits[MAX_DIGITS_CNT];
int digits_cnt;
int target_sum;

int result;

boolean enqueued[MAX_DIGITS_CNT][MAX_TARGET_NUM];
State queue[MAX_TARGET_NUM * MAX_DIGITS_CNT];
int left; // index of a head of the queue
int right; // index of a tail of the queue

void read_input_data() {
	scanf("%s", input);
	
	int i = 0;
	while(input[i] != '=') {
		digits[i] = input[i] - '0';
		i++;
	}
	digits_cnt = i;

	target_sum = atoi(input + i + 1);
}

void solve() {
	// initialize queue and add initial state to the queue
	left = 0;
	right = 0;
	queue[0].curr_digit_pos = 0;
	queue[0].target_sum = target_sum;
	queue[0].dist = 0;
	enqueued[0][target_sum] = TRUE;

	// "preffix" number, which been shrinked from the sequence of digits
	// starting from the current position
	int curr_num;

	// current target sum - "preffix" number
	int new_target_sum;

	// current position + length of "preffix" number
	int new_digit_pos;

	while(left <= right) { // while queue is not empty
		
		// queue[left] - item from the head of the queue
		// queue[left] - is a current state

		// cut first digit, starting from the current position
		curr_num = digits[queue[left].curr_digit_pos];
		new_digit_pos = queue[left].curr_digit_pos + 1;
		
		// generate new states, based on the current state
		while((curr_num <= queue[left].target_sum) 
			  && (new_digit_pos <= digits_cnt)) {
			
			new_target_sum = queue[left].target_sum - curr_num;

			// new_digit_pos and new_target_sum - represents new state

			if((new_target_sum >= 0) // if state is enabled
			   && (!enqueued[new_digit_pos][new_target_sum])) { // and not enqueued yet

				// check if new state represents target state 
				if(new_digit_pos == digits_cnt) {
					if(new_target_sum == 0) {
						result = queue[left].dist;
						return;
					}
				}

				// enqueue new state
				++right;
				queue[right].curr_digit_pos = new_digit_pos;
				queue[right].target_sum = new_target_sum;
				queue[right].dist = queue[left].dist + 1;
				enqueued[new_digit_pos][new_target_sum] = TRUE;
			}

			// if reached the end of the sequence of digits
			if(new_digit_pos == digits_cnt) {
				break;
			}

			// skip leading zeros
			// (because multiple states with different amount of zeros are useless)
			if(curr_num == 0) {
				while(new_digit_pos < digits_cnt - 1 && digits[new_digit_pos] == 0) {
					++new_digit_pos;
				}
			} 

			// cut one more "preffix" digit
			curr_num = curr_num * 10 + digits[new_digit_pos];
			++new_digit_pos;	
		}

		// remove the head of the queue
		++left;
	}
}

int main() {
	read_input_data();
	solve();
	printf("%d\n", result);
	return 0;
}
