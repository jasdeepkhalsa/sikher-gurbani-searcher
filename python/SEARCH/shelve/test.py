# -*- coding: utf8 -*-
filename = "a.db"
key = 'hello'
data = [1,2,3,4,5,6,8,7,7,8,8,8,8]
import shelve

d = shelve.open(filename) # open -- file may get suffix added by low-level
                          # library

d[key] = data   # store data at key (overwrites old data if
                # using an existing key)
data = d[key]   # retrieve a COPY of data at key (raise KeyError if no
                # such key)
#del d[key]      # delete data stored at key (raises KeyError
#                # if no such key)
#Test1(d, key)

# as d was opened WITHOUT writeback=True, beware:
d['xx'] = range(4)  # this works as expected, but...
d['xx'].append(5)   # *this doesn't!* -- d['xx'] is STILL range(4)!!!
# having opened d without writeback=True, you need to code carefully:
temp = d['xx']      # extracts the copy
temp.append(5)      # mutates the copy
d['xx'] = temp      # stores the copy right back, to persist it
# or, d=shelve.open(filename,writeback=True) would let you just code
# d['xx'].append(5) and have it work as expected, BUT it would also
# consume more memory and make the d.close() operation slower.
d['yyyy'] = {'x':{'a':'b', 1:12},'y':'zdf','z':[1,2,3],'mail':(3,4,5)}
d[''] = "simple test, man"
print '::::',key,'==>',d[key]
print ':::: xx ==>',d['xx']
#print d["a"]
d["a"] = [{"b":[True],"c":[{"d":[True]}]}]
d[u"привет"] = u"Привет"
d.close()       # close it
#from indexer import *

#d2 = shelve.open ("db_ru.idb")
d2 = shelve.open ( filename )
print "----------",d2['yyyy']
for key in d2.keys():
 print key,'==>',d2[key]
d2.close()

def Test1(d, key):
    flag = d.has_key(key)   # true if the key exists
    list = d.keys() # a list of all existing keys (slow!)


