###################################################################################
# -*- coding: utf8 -*-
# $Header$
# $Log$
# Revision 1.1  2005/09/19 05:44:31  mamonts
# *** empty log message ***
#
#
#
"""
"""

import os
import os.path
import re
import datetime
import gadfly    
try:
    import psyco
    psyco.profile()
except:
    print 'Psyco not found, ignoring it'

#############################################################
# Database modification. Notation: version : SQL statements
#    "create table location (lid integer, hid integer, lineid integer, wordid integer, wlid integer)",
#    "create unique index i_location_lid on location (lid)",
#    "create unique index i_location_all on location (hid, lineid, wordid)"
#   ],
#   ]  
#}
_connection = None
try:
 _connection = gadfly.gadfly ( "test", "" )
 print "use exists database"
except Exception, info:
 #we have to create new database
 print "Open error: ", info
 pass  
if _connection is None:
 print "create new database" 
 _connection = gadfly.gadfly ()
 _connection.startup ( "test", "" ) 
_cursor = _connection.cursor()
try:
 _cursor.execute ( "create table words (wlid integer, locale varchar, word varchar)" )
 _cursor.execute ( "create unique index i_words_wlid on words (wlid)" )
 _cursor.execute ( "create unique index i_words_word on words (locale, word)" )
except:
 pass
# _cursor.close ()
for _i in range (1,1000):
 print _i, '\r',
 _c = _connection.cursor()
 _c.execute ( "insert into words (wlid, locale, word) values (?,?,?)", (_i, str(_i), str(_i) ) )
 _c.close()
 _connection.commit()
print "\n\ncommit"
_connection.commit()
_cursor.close ()
