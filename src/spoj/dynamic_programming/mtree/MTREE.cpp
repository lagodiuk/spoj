// http://www.spoj.com/problems/MTREE/

#include <stdio.h>
#include <algorithm>
#include <stdint.h>

const int MAX_VERTICE_NUM = 100000;
const int64_t MOD = 1000000007;
const int64_t _2_INV_MOD = (MOD + 1) / 2;

typedef
struct {
    int from;
    int to;
    int weight;
} Edge;

typedef
struct {
    bool visited;
    int64_t fwd;
    int64_t excl;
    int64_t bkwd;
    int edge;
} Vertex;

bool operator<(Edge const & e1, Edge const & e2) {
    return e1.from < e2.from;
}

Vertex vertices[MAX_VERTICE_NUM + 1];
Edge edges[(MAX_VERTICE_NUM - 1) * 2];

int vertices_cnt;
int edges_cnt;
int64_t result = 0;

inline bool has_unvisited_children(int v) {
    
    Vertex & vx = vertices[v];
    
    while((vx.edge < edges_cnt)
          && (edges[vx.edge].from == v)
          && (vertices[edges[vx.edge].to].visited == true)) {
        
        vx.edge++;
    }
    
    return ((vx.edge < edges_cnt)
            && (edges[vx.edge].from == v)
            && (vertices[edges[vx.edge].to].visited == false));
}

void visit(int e) {
    Edge & ed = edges[e];
    Vertex & c_vx = vertices[ed.to];
    Vertex & p_vx = vertices[ed.from];
    
    c_vx.visited = true;
    c_vx.fwd = ((p_vx.fwd + 1) * ed.weight) % MOD;
    c_vx.excl = (p_vx.fwd + p_vx.excl) % MOD;
    c_vx.bkwd = ed.weight;
    
    if(!has_unvisited_children(ed.to)) {
        result = (result + c_vx.fwd + c_vx.excl) % MOD;
        return;
    }
    
    int visited = 0;
    int64_t suf = 0;
    int64_t sqsuf = 0;
    
    while((c_vx.edge < edges_cnt)
          && (edges[c_vx.edge].from == ed.to)) {
        
        if(vertices[edges[c_vx.edge].to].visited == false) {
            visited++;

            visit(c_vx.edge);

            suf = (suf + vertices[edges[c_vx.edge].to].bkwd) % MOD;
            sqsuf = (sqsuf + (vertices[edges[c_vx.edge].to].bkwd * vertices[edges[c_vx.edge].to].bkwd)) % MOD;
        }
        
        c_vx.edge++;
    }
    c_vx.bkwd = (c_vx.bkwd + suf * ed.weight) % MOD;
    
    if(visited > 1) {
        result = (result - (c_vx.fwd + c_vx.excl) * (visited - 1)) % MOD;
        if(result < 0) {
            result += MOD;
        }
        result = (result + ((suf * suf - sqsuf) % MOD) * _2_INV_MOD) % MOD;
        if(result < 0) {
            result += MOD;
        }
    }
}

int main(){
    scanf("%d", &vertices_cnt);
    edges_cnt = (vertices_cnt - 1) * 2;
    int e = 0;
    for(int i = 0; i < vertices_cnt - 1; ++i) {
        scanf("%d %d %d", &edges[e].from, &edges[e].to, &edges[e].weight);
        edges[e + 1].from = edges[e].to;
        edges[e + 1].to = edges[e].from;
        edges[e + 1].weight = edges[e].weight;
        e += 2;
    }
    
    std::sort(edges, edges + edges_cnt);
    
    for(int i = 1; i <= vertices_cnt; ++i) {
        // just some very big number, which represents unexisting edge indx
        vertices[i].edge = MAX_VERTICE_NUM * 3;
    }
    for(int i = 0; i < edges_cnt; ++i) {
        vertices[edges[i].from].edge = std::min(i, vertices[edges[i].from].edge);
    }
    
    int start_edge;
    for(int i = 1; i <= vertices_cnt; ++i) {
        if((vertices[i].edge == edges_cnt - 1)
           || (edges[vertices[i].edge].from != edges[vertices[i].edge + 1].from)) {
            start_edge = vertices[i].edge;
            break;
        }
    }
    
    /*
    // Debug output
    printf("\n");
    for(int i = 0; i < edges_cnt; ++i) {
        printf("%d:   %d %d %d\n", i, edges[i].from, edges[i].to, edges[i].weight);
    }
    for(int i = 1; i <= vertices_cnt; ++i) {
        printf("%d -> %d\n", i, vertices[i].edge);
    }
    printf("Start edge: %d\n\n", start_edge);
    */
    
    vertices[edges[start_edge].from].visited = true;
    visit(start_edge);
    
    printf("%ld\n", result);
    
    return 0;
}