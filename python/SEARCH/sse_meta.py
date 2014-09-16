###################################################################################
# -*- coding: utf8 -*-
# $Header$
# $Log$
# Revision 1.3  2005/09/24 18:43:10  mamonts
# Tested with Python 2.4.1
#
# Revision 1.2  2005/09/20 21:39:35  mamonts
# just a small changes
#
# Revision 1.1  2005/09/19 05:44:31  mamonts
# *** empty log message ***
#
#
#
"""
"""
try:
    import psyco
    psyco.profile()
except:
    print 'Psyco not found, ignoring it'

import datetime
try:
    import gadfly    
except:
    import sys as _sys
    _sys.exit ( "gadfly not found. Please, download and install it.\n"\
               "see  http://gadfly.sf.net/" )
import os
import os.path
import re

#############################################################
# Database modification. Notation: version : SQL statements
G_dbChanges = {
 1: [
    "create table v_hierarchy (vhid integer, descr varchar)",
    "create unique index i_v_hierarchy on v_hierarchy (vhid)",
    #                                                   
    "create table db_version (version integer, stamp varchar)",
    "create unique index i_db_version on db_version (version)"
    #
   ],
 2: [
    "create table hierarchy (hid integer, parentid integer, vhid integer, descr varchar)",
    "create unique index i_hierarchy_hid on hierarchy (hid)",
    #                                                   
   ] ,
 3: [
    "create table location (lid integer, hid integer, lineid integer, wordid integer, wlid integer)",
    "create unique index i_location_lid on location (lid)",
    "create unique index i_location_all on location (hid, lineid, wordid)"
   ],
 4: [
    "create table words (wlid integer, locale varchar, word varchar)",
    "create unique index i_words_wlid on words (wlid)",
    "create unique index i_words_word on words (locale, word)"
   ]  
}
#############################################################
# Data modification. Notation: version : SQL statements
G_dataChanges = {
 1: [           
  "insert into v_hierarchy ( vhid, descr ) values ( 10, 'File')"
 ],
 3: [           
  "insert into v_hierarchy ( vhid, descr ) values ( 5, 'Locale')"
 ]
}
class CSikherDatabase:
  """
  database management. Allow to create, update database.
  """
  def __initCache ( self ):
   #cache support ###################################################
   self.__lid = None  
   self.__wlid = None  
   self.__hid = None  
   self.__wordmap = {}
   ##################################################################
  def __init__(self, sDirectory):
   self.__initCache() 
   self.__dbVersion = 4 #version information to update database (see G_*Changes constats)
   self.__sDir = sDirectory
   self.__sDBName = "sikher"
   self.__connection = None
   if os.path.exists(self.__sDir):
    print "dir exists ", self.__sDir 
    #try to open database
    try:
      self.__connection = gadfly.gadfly ( self.__sDBName, self.__sDir )
      print "use exists database"
    except Exception, info:
      #we have to create new database
      print "Open error: ", info
      pass  
   if self.__connection is None:
    print "create new database" 
    self.__connection = gadfly.gadfly ()
    self.__connection.startup ( self.__sDBName, self.__sDir ) 
   self.CheckDatabase ( self.__connection ) 
   pass
  def getConnection (self): 
   return self.__connection
  def __CreateTables (self, connection, usedVersion): 
   if not G_dbChanges.has_key ( usedVersion ):
    return
   _cursor = connection.cursor()
   print "Upgrade to version %d"%usedVersion
   for _stmt in G_dbChanges[usedVersion]:
#    try:
     print "apply stmt '%s'"%_stmt
     _cursor.execute ( _stmt )
#    except NameError, info:
#     print "Error:: ", info
#    except:
#     print "Unexpected Error" 
   connection.commit ()
   _cursor.close ()
   print "Upgrade to version %d complete"%usedVersion
   
  def __FillTables (self, connection, usedVersion):
   if not G_dataChanges.has_key ( usedVersion ):
    return
   _cursor = connection.cursor()
   print "Upgrade data to version %d"%usedVersion
   for _stmt in G_dataChanges[usedVersion]:
#    try:
     print "apply stmt '%s'"%_stmt
     _cursor.execute ( _stmt )
#    except Exception, info:
#     print "Error::", info 
#    except:
#     print "Unknown error" 
   connection.commit ()
   _cursor.close ()
   print "Upgrade data to version %d complete"%usedVersion
  def CheckDatabase ( self, connection ):
   _cursor = connection.cursor()
   try:
     _cursor.execute ( "select max(version) from db_version" )
     _x = _cursor.fetchone()
     print "db_version:", _x
   except NameError, info:
     print "Get DBVersion error:", info
     _x = [0]
   if _x[0] < self.__dbVersion:
     self.UpgradeDatabase ( connection, _x[0] )
   _cursor.close ()
   pass
  def UpgradeDatabase (self, connection, oldVersion ):
   _cursor = connection.cursor()
   for _v in range (oldVersion+1, self.__dbVersion+1):
    self.__CreateTables ( connection, _v )
    self.__FillTables(connection, _v )
   _cursor.execute ( "insert into db_version ( version, stamp ) values ( %d, '%s')"%(self.__dbVersion, datetime.datetime.now()) )
   connection.commit ()
   _cursor.close ()
   pass
  #############################################################################
  # indexer support
  def saveLocation ( self, hid, lineid, wordid, wlid ):
   _c = self.getConnection().cursor()
   if self.__lid is None:
     try:
       _c.execute ( 'select max(lid) from location' )
       _x = _c.fetchone ()
       _lid = _x[0] + 1
     except ValueError:
       _lid = 1 
   else:
     _lid = self.__lid + 1  
   self.__lid = _lid
   try: 
    _c.execute ( 'insert into location ( lid, lineid, wordid, hid, wlid ) values ( ?, ?, ?, ?, ? )', 
                (_lid,lineid, wordid, hid, wlid) )
    #self.getConnection().commit()
   except Exception, info:
#    print 'saveLocation error::', info 
#    if wlid is None:
#      print ' data: lid:%d, lineid:%d, wordid:%d, hid:%d, wlid is None'%(_lid,lineid, wordid, hid)
#    else:
#      print ' data: lid:%d, lineid:%d, wordid:%d, hid:%d, wlid:%d'%(_lid,lineid, wordid, hid, wlid)
    _lid = None
   _c.close()
   return _lid 
  def Commit(self):
    self.getConnection().commit() 
  def getNewWLID (self, locale, word): 
   #cache
   _sKey = "%s:%s"%(locale,word)
   if self.__wordmap.has_key( _sKey ):
     return self.__wordmap[_sKey]
   _c = self.getConnection().cursor()
   #print "select wlid"
   _c.execute ( 'select wlid from words where locale = ? and word = ?', (locale,word) )
   try:
     _x = _c.fetchone ()
   except: #no result
     _x = [None] 
   if _x[0] is None: #add new
    if self.__wlid is None:
      try:
        _c.execute ( "select max(wlid) from words" )
        _x = _c.fetchone()
        _lid = _x[0] + 1
      except ValueError:
        _lid = 1
    else:
      _lid = self.__wlid + 1
    self.__wlid = _lid    
    _c.execute ( "insert into words ( wlid, locale, word ) values (?,?,?)", (_lid, locale, word) )
    #self.getConnection().commit()  
   else: 
    _lid = _x[0]
   _c.close()
   self.__wordmap[_sKey] = _lid
   return _lid
  def __getNextHID (self, c):   
     if self.__hid is None:
      try:
        c.execute ( "select max(hid) from hierarchy" )
        _x = c.fetchone()
        _id = _x[0] + 1
      except ValueError:
        _id = 1
     else:
       _id = self.__hid + 1
     self.__hid = _id     
     return _id
  def getLocaleID (self, locale): 
   _c = self.getConnection().cursor()
   #get locale id
   _c.execute ( 'select hid from hierarchy where vhid = 5 and descr = ?', (locale,) )
   try:
     _x = _c.fetchone ()
   except: #no result
     _x = [None] 
   if _x[0] is None: #add new
     _id = self.__getNextHID ( _c )
     _c.execute ( "insert into hierarchy ( hid, parentid, descr, vhid ) values (?,?,?,?)", (_id, None, locale, 5) )
    #self.getConnection().commit()  
   else: 
    _id = _x[0]
   _c.close()
   return _id
    
  def getFileID (self, locale, file):  
   _lid = self.getLocaleID(locale) 
   _c = self.getConnection().cursor()
   #get locale id
   _c.execute ( 'select hid from hierarchy where vhid = 10 and parentid = ? and descr = ?', (_lid, file) )
   try:
     _x = _c.fetchone ()
   except: #no result
     _x = [None] 
   if _x[0] is None: #add new
    _id = self.__getNextHID ( _c )
    _c.execute ( "insert into hierarchy ( hid, parentid, descr, vhid ) values (?,?,?,?)", (_id, _lid, file, 10) )
    #self.getConnection().commit()  
   else: 
    _id = _x[0]
   _c.close()
   return _id
  def getFileName (self, id):  
   _c = self.getConnection().cursor()
   #get locale id
   _c.execute ( 'select descr from hierarchy where vhid = 10 and hid = ?', (id,) )
   try:
     _x = _c.fetchone ()
   except: #no result
     _x = [None]
   _c.close()
   return _x[0]
if __name__ == "__main__":
    #TEST ONLY
    _dir = "D:\\projects.mal\\master\\offshore\\SIKHER\\SIKHER\\DATABASE\\"
    _db = CSikherDatabase ( _dir )
    _c = _db.getConnection().cursor()
    _c.execute ( "select * from db_version order by version" )
    print _c.pp()
    _c.execute ( "delete from v_hierarchy where vhid = 666" )
    _c.execute ( "insert into v_hierarchy ( vhid, descr ) values ( ?, ? )", (666, u"Проверка") )
    _c.execute ( "select * from v_hierarchy" )
    #print _c.pp()
    _c.execute ( "delete from v_hierarchy where vhid = 666" )
    _c = _db.getConnection().commit()
    print "probeid=",_db.getNewWLID("en","probe")
    print "---"
    print "Привер=",_db.getNewWLID("ru",u"привет")
    print "---"
    print "locale en=",_db.getLocaleID("en")
    print "---"
    print "file en, test.txt=",_db.getFileID("en", "text.txt")
    print "---"
    