// http://www.spoj.com/problems/DANGER/

#include <stdio.h>

long getJosephusIndex(long amount) {
    int each = 2;
    long firstNumber = 1;
    long difference = 1;
    
    int eachMod2;
    int amountMod2;
    
    while(amount > 1) {
        eachMod2 = each & 1;
        amountMod2 = amount & 1;
        
        if(amountMod2 && eachMod2) {
            each = 2;
            amount >>= 1;
            firstNumber += difference;
            difference <<= 1;
            
        } else if(eachMod2 && !amountMod2) {
            each = 1;
            amount >>= 1;
            firstNumber += difference;
            difference <<= 1;
            
        } else if(amountMod2 && !eachMod2) {
            each = 1;
            amount >>= 1;
            amount++;
            difference <<= 1;
            
        } else { // ((amountMod2 == 0) && (eachMod2 == 0))
            amount >>= 1;
            difference <<= 1;
            
        }
    }
    
    return firstNumber;
}

inline long readNextAmount() {
    static char line[5];
    static int i;

    static long amount;
    
    gets(line);
    amount = (long)(line[0] - '0') * 10 + (long)(line[1] - '0');
    for(i = 0; i < (line[3] - '0'); i++) {
        amount *= 10;
    }
    
    return amount;
}

int main() {
    /*
    printf("%ld\n", getJosephusIndex(5));
    printf("%ld\n", getJosephusIndex(10));
    printf("%ld\n", getJosephusIndex(42));
    printf("%ld\n", getJosephusIndex(66000000));
    printf("%ld\n", getJosephusIndex(40));
    printf("%ld\n", getJosephusIndex(99000000));
     */
    
    long amount = readNextAmount();
    
    while(amount > 0) {
        printf("%ld\n", getJosephusIndex(amount));
        amount = readNextAmount();
    }
    
    return 0;
}