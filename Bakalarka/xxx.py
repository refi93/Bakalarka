import sys
print sys.argv


mapa = dict({})

with open('sortedDistribution.txt') as openfileobject:
    for line in openfileobject:
        x = int(line)
        if x in mapa:
            mapa[x] = mapa[x] + 1
        else:
            mapa[x] = 1

for k in sorted(mapa.keys()):
    print str(k / 2) + " " + str(mapa[k])
