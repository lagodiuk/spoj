// http://www.spoj.com/problems/XMAX/

#include <iostream>
#include <stdint.h>
#include <vector>

#define MAX_RADIX 64

using namespace std;

inline int get_most_significant_bit_position(uint64_t x) {
    int pos = 0;
    while(x) {
        x >>= 1;
        pos++;
    }
    return pos;
}

// Map<Integer, List<Integer>>
// key - index of the most significant bit (msb)
// value - list of numbers, with msb in given position
vector< vector<uint64_t> > buckets(MAX_RADIX + 1);

// Array for input numbers
vector<uint64_t> arr;

uint64_t get_max_xor(const vector<uint64_t> & arr) {
    int msb;
    uint64_t first;
    uint64_t curr;
    
    // Put each number into its bucket, depending on value of msb
    for(int i = 0; i < arr.size(); ++i) {
        msb = get_most_significant_bit_position(arr[i]);
        buckets[msb].push_back(arr[i]);
    }

    // "Gauss elimination"
    // For each bucket preserve only one number with given value of msb
    //
    // E.g.: if [x1, x2, x3] - have the same position of most significant bit
    // than:
    // x2 ^= x1
    // x3 ^= x1
    // After this transformation x2 and x3 will have smaller value of position of most significant bit
    for(int i = MAX_RADIX; i > 0; --i) {
        vector<uint64_t> & bucket = buckets[i];
        if(bucket.size() == 0) {
            continue;
        }
        
        first = bucket[0];
        for(int j = 1; j < bucket.size(); ++j) {
            curr = bucket[j] ^ first;
            msb = get_most_significant_bit_position(curr);
            buckets[msb].push_back(curr);
        }
        bucket.clear();
        bucket.push_back(first);
    }
    // Now, each bucket - has only one element

    uint64_t result = 0;

    // Now, we can just XOR numbers from each bucket - in a greedy way
    for(int i = MAX_RADIX; i > 0; --i) {
        vector<uint64_t> & bucket = buckets[i];
        if(bucket.size() == 0) {
            continue;
        }
        
        first = bucket[0];
        curr = first ^ result;
        result = (curr > result) ? curr : result;
    }

    return result;
}

int main(){
    int size;
    cin >> size;
    uint64_t tmp;
    
    arr.reserve(size + 1);
    
    while(size--) {
        cin >> tmp;
        arr.push_back(tmp);
    }
    
    cout << get_max_xor(arr) << endl;
    
    return 0;
}