import sys
from decimal import *
import math

mapa = dict({})
counter = 0
with open('5StateNFAvsDFA3861409.txt') as openfileobject:
    for line in openfileobject:
        x,y = map(int,line.split())
        if (1):
            counter = counter + 1
            if y in mapa:
                mapa[y] = mapa[y] + 1
            else:
                mapa[y] = 1

avg = 0

for k in sorted(mapa.keys()):
    #print str(k) + " " + str(Decimal(100.0) * Decimal(mapa[k])/Decimal(counter))
    avg = avg + k * mapa[k]
    print str(k) + " " + str(mapa[k])
    
avg = Decimal(avg) / Decimal(counter)

print str(avg) + "    " + str(counter)
