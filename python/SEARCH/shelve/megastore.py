# a
# pro
# probe
# left
# less
# lock

# (teminator, haschilds)
#  0          0          - impossible (reserve!!!) ciUnreal   0
#  0          1          - part of word            ciPart     1
#  1          0          - end of word             ciEnd      2
#  1          1          - end and part            ciMix      3
#  


index= {
 '': { 'a':(2,LID), 'p': (1,None), 'l':(1,None)},
 'p': { 'r': 1 },
 'pr': { 'o': 2 },
 'pro': { 'b': 1},
 'prob': { 'e': 2},
 'l': {'e':1,'o':1},
 'le': { 's': 1 },
 'lo': { 'c': 1 },
 'les': { 's': 2 },
 'loc': { 'k': 2 },
}

file={
 'pro': 1,
 'probe': 2,
 'left': 3,
 'less': 4,
 'lock': 5
}


