# http://www.spoj.com/problems/MREPLBRC/en/

class Problem:
    coefficient_1 = set([
        ('(', ')'), ('(', '?'), ('?', ')'),
        ('[', ']'), ('[', '?'), ('?', ']'),
        ('{', '}'), ('{', '?'), ('?', '}')
    ])

    coefficient_3 = ('?', '?')

    def __init__(self, s):
        self.s = s
        self.mem = {}

    def solve(self, *args):
        if not args:
            return self.solve(0, len(self.s) - 1)

        i = args[0]
        j = args[1]

        if i > j:
            return 1
        if (i, j) in self.mem:
            return self.mem[i, j]
        self.mem[i, j] = 0
        for k in xrange(i + 1, j + 1, 2):
            c = self.coeff(i, k)
            if c > 0:
                self.mem[i, j] += c * self.solve(i + 1, k - 1) * self.solve(k + 1, j)
        return self.mem[i, j]

    def coeff(self, i, j):
        brck = (self.s[i], self.s[j])
        if brck == self.coefficient_3:
            return 3
        if brck in self.coefficient_1:
            return 1
        return 0

for s in ['()', '??', '()()()', '()()?(', '()()?)', '()()??', '(?([?)]?}?', '(??)', '??(?', '?](?', '((?)???)()',
'?((?((???]})???)??())?']:
    print s
    print Problem(s).solve()
    print
