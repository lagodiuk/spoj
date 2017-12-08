#!/usr/bin/python

"""
Solution of: http://www.spoj.com/problems/MKBUDGET/
"""
def get_min_budget(month_count, hire, salary, severance):
    INF = 1000000
    length = len(month_count)
    cost = [[0] * length for i in xrange(length)]
    for c in xrange(length):
        cost[0][c] = (month_count[c] * (hire + salary)) if month_count[c] >= month_count[0] else INF
    for m in xrange(1, length):
        for c in xrange(length):
            cost[m][c] = INF
            if month_count[c] < month_count[m]:
                continue
            for k in xrange(length):
                curr = month_count[c] * salary + cost[m - 1][k]
                if month_count[c] < month_count[k]:
                    curr += (month_count[k] - month_count[c]) * severance
                elif month_count[c] > month_count[k]:
                    curr += (month_count[c] - month_count[k]) * hire
                cost[m][c] = min(cost[m][c], curr)
    result = INF
    for c in xrange(length):
        result = min(result, cost[length - 1][c])
    return result

print get_min_budget([10, 9, 11], 400, 500, 600)
print get_min_budget([11, 9, 10, 14, 9, 9, 13, 15], 400, 600, 600)
