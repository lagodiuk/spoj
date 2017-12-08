#include <stdio.h>

// http://www.spoj.com/problems/ROADS/en/

#define MAX_CITIES 102
#define MAX_ROADS 10002
#define MAX_COINS 10002
#define INFINITY 1000000000
#define SOURCE_CITY 1
#define HEAP_FIRST_POSITION 1

typedef
struct {
    // id of target city
    int target;
    // length of road
    int length;
    // cost of moving through given road
    int toll;
} Road;

// "ArrayList" of roads
typedef
struct {
    Road arr[MAX_ROADS];
    int count;
} Roads;

// "HashMap": from source city id -> to "ArrayList" of outcoming roads
Roads sourceToOutcomeRoads[MAX_CITIES];

typedef
struct {
    // minimal distance for given combination of
    // city and available money so far
    long distance;
    int city;
    int money;
    // index of given item inside min-heap
    // if minHeapIndex == 0 -> item is absent in min-heap
    int minHeapIndex;
} Cell;

// field, with all possible combinations of cities
// and money, which available so far
Cell field[MAX_CITIES][MAX_COINS];

//-------------------------------------------------------------
// min-heap
Cell * minHeap[MAX_CITIES * MAX_COINS];
int minHeapVacantPosition = 1;

inline int leftChild(int i) {
    return (i << 1);
}

inline int rightChild(int i) {
    return (i << 1) + 1;
}

inline int parent(int i) {
    return (i >> 1);
}

inline void swap(int i, int j) {
    Cell * tmp = minHeap[i];
    
    minHeap[i] = minHeap[j];
    minHeap[j] = tmp;
    
    minHeap[i]->minHeapIndex = i;
    minHeap[j]->minHeapIndex = j;
}

void pushUp(int index) {
    if(index > HEAP_FIRST_POSITION) {
        int p = parent(index);
        if(minHeap[p]->distance > minHeap[index]->distance) {
            swap(index, p);
            pushUp(p);
        }
    }
}

void pushDown(int index) {
    int min = leftChild(index);
    if(min >= minHeapVacantPosition) {
        return;
    }
    
    int r = rightChild(index);
    if(r < minHeapVacantPosition
       && (minHeap[r]->distance < minHeap[min]->distance)) {
        
        min = r;
    }
    
    if(minHeap[index]->distance > minHeap[min]->distance) {
        swap(index, min);
        pushDown(min);
    }
}

inline Cell * getNext() {
    Cell * top = minHeap[HEAP_FIRST_POSITION];
    swap(HEAP_FIRST_POSITION, minHeapVacantPosition - 1);
    minHeapVacantPosition--;
    pushDown(HEAP_FIRST_POSITION);
    return top;
}

inline void addToHeap(Cell * cell) {
    minHeap[minHeapVacantPosition] = cell;
    cell->minHeapIndex = minHeapVacantPosition;
    pushUp(minHeapVacantPosition);
    minHeapVacantPosition++;
}

inline int heapIsNotEmpty() {
    return (minHeapVacantPosition > 1);
}
// min-heap
//-------------------------------------------------------------

void initialize(int target, int money) {
    // reset minHeap
    minHeapVacantPosition = 1;
    
    // initialize all distances with Infinity
    int i;
    int j;
    for(i = 1; i <= target; i++) {
        for(j = 0; j <= money; j++) {
            field[i][j].distance = INFINITY;
            field[i][j].minHeapIndex = 0;
            field[i][j].city = i;
            field[i][j].money = j;
        }
    }
    
    // initialize source city
    field[SOURCE_CITY][money].distance = 0;
    addToHeap(&field[SOURCE_CITY][money]);
}

long findLengthOfShortestPathForGivenMoney(int target, int money) {
    
    initialize(target, money);
    
    Cell * curr;
    Road road;
    int newMoney;
    long newDist;
    int i;
    int targetCity;
    
    while(heapIsNotEmpty()) {
        // find the "nearest" city and available money
        curr = getNext();
        
        if(curr->city == target) {
            return curr->distance;
        }
        
        // check all outcoming roads
        for(i = 0; i < sourceToOutcomeRoads[curr->city].count; i++) {
            road = sourceToOutcomeRoads[curr->city].arr[i];
            targetCity = road.target;
            
            newMoney = curr->money - road.toll;
            newDist = curr->distance + road.length;
            
            // "relax"
            if((newMoney >= 0)
               && (field[targetCity][newMoney].distance > newDist)) {
                
                field[targetCity][newMoney].distance = newDist;
                    
                if(!field[targetCity][newMoney].minHeapIndex) {
                    // item was not in min-heap yet
                    addToHeap(&field[targetCity][newMoney]);
                } else {
                    // update priority of item - inside min-heap
                    pushUp(field[targetCity][newMoney].minHeapIndex);
                }
            }
        }
    }
    
    return -1;
}

int main() {
    int testCasesNum;
    int money;
    int citiesCount;
    int roadsCount;
    int i;
    int source;
    int target;
    int toll;
    int length;
    int roadIndex;
    
    scanf("%d", &testCasesNum);
    while(testCasesNum--) {
        scanf("%d %d %d", &money, &citiesCount, &roadsCount);
        for(i = 1; i <= citiesCount; i++) {
            sourceToOutcomeRoads[i].count = 0;
        }
        for(i = 0; i < roadsCount; i++) {
            scanf("%d %d %d %d",
                  &source,
                  &target,
                  &length,
                  &toll);
            
            roadIndex = sourceToOutcomeRoads[source].count;
            sourceToOutcomeRoads[source].arr[roadIndex].target = target;
            sourceToOutcomeRoads[source].arr[roadIndex].toll = toll;
            sourceToOutcomeRoads[source].arr[roadIndex].length = length;
            sourceToOutcomeRoads[source].count++;
        }
        long result = findLengthOfShortestPathForGivenMoney(citiesCount, money);
        printf("%ld\n", (result < INFINITY) ? result : -1);
    }
    
    return 0;
}