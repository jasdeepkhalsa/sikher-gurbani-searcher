# -*- coding: utf8 -*-
filename = "a.db"
import shelve

d = shelve.open(filename) # open -- file may get suffix added by low-level
                          # library
d[u"привет"] = u"Привет"
d.close()       # close it

