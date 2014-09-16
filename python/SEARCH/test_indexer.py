###################################################################################
# -*- coding: utf8 -*-
# $Header$
#
# Module:  
# Purpose:
# Created: 

# $Log$
# Revision 1.4  2005/09/24 18:43:10  mamonts
# Tested with Python 2.4.1
#
# Revision 1.3  2005/09/19 05:44:31  mamonts
# *** empty log message ***
#
# Revision 1.2  2005/08/29 23:14:38  mamonts
# +indexer
# i have problems with unicode wxTextCtrl.GetValue() and shelve key.
#
# Revision 1.1  2005/07/26 21:39:02  mamonts
# +index engine looks like complete. Time to convert it to shabadSearchEngine
#
#
#

__revision__ = "$Revision$"
__version__ = "0.0.0.0"

##################################################
# import section
##################################################
try:
    import psyco
    psyco.profile()
except:
    print 'Psyco not found, ignoring it'

##################################################
# primary section
##################################################

##################################################
# testing
##################################################
from indexer import *
import unittest
import os
import shelve
from utils import *
#import sys
import sse_globals
import utils
_config = CSSEConfig ( '..' )
print '_config_path ', _config.getDBasePath ()
search_tpl = [("en","gurbani"), ("en","lord"), ("","chapai"), ("","cih"), ("ru",u"стрел")]
def PerformSearch ( idx ):
    for _item in search_tpl:
      _res = idx.Search ( _item[0], _item[1].encode ( 'cp1251' ) )
      print 'search ',  _item[1].encode ( 'cp1251' ),':', _res
      if _res is None: 
       return
      for _ritem in _res:
       print 'files::: ', idx.DecodeFile ( _res[1] )

class PrimaryIndexItemsTests ( unittest.TestCase ):
  """
  Now we'll try to perform simple tests of index engine
  All index files will be destroyed after testing
  """
  def setUp(self):
#    for file in listFiles ( ".", "*.sdb" ):
#      print "delete ", file
#      #os.remove ( file )
    pass
  def tearDown (self):  
#    for file in listFiles ( ".", "*.sdb" ):
#      print "delete ", file
#      os.remove ( file )
    pass
  def testSimpleIndexTest (self):  
    self._ii = IndexItems ( "en", 999, {}, _config )
    self._ii.AddWord ( "en", "probe", WordLocation (1, 1, 1) )
    self._ii.AddWord ( "en", "test", WordLocation (1, 1, 2) )
    self._ii.AddWord ( "en", "master", WordLocation (1, 1, 3) )
    print self._ii
    print 'Locate probe: ', self._ii.Locate ( "en", "probe" )
    print 'Locate pro: ', self._ii.Locate ( "en", "pro" )
    print "Locate xxl: ", self._ii.Locate ( "en", "xxl" )
  def testIndexer1 (self):
    _i = Indexer ( 999, {}, _config, Progress () )
    _content = [
                Content ( "", "Gurmukhi Gurbani Parser Test File.txt",
                         [
                          Content ("en", "English Translation Parser Test File.txt"), 
                          Content ("",   "English Transliteration Parser Test File.txt")
                         ])
               ]
    _i.Index ( _content )
    PerformSearch ( _i )
#    search_tpl = [("en","gurbani"), ("en","lord"), ("","chapai"), ("","cih"), (u"ru",u"стрел")]
#    for _item in search_tpl:
#      print 'search ',_item[1].encode ( "cp1251" ),':', _i.Search ( _item[0], _item[1] )
#  def testLargeText (self):
#    raise 'Skipped'
#    _i = Indexer ( 999, {}, _config )
#    _content = [
#                Content ( "ru", "a1.txt" ),
#                Content ( "ru", "a2.txt" ), 
#                Content ( "ru", "nd1.txt" ),
#                Content ( "ru", "Smyerti_ili_slava_-_1._Chyernaya_astafyeta.txt" ),
#                Content ( "ru", "Smyerti_ili_slava_-_2._Smyerti_ili_slava.txt" ),
#                Content ( "ru", "Tyehnik_Bolishogo_Kiyeva_-_1._Tyehnik_Bolishogo_Kiyeva.txt" ),
#                Content ( "ru", "Klinki.txt" )
#               ]
#    _i.Index ( _content )
#    _tpl = [("ru","�����"), ("en","lord"), ("","chapai"), ("","cih")]
#    for _item in _tpl:
#      print 'search ',_item[1],':', _i.Search ( _item[0], _item[1] )
   
class StorageTests ( unittest.TestCase ):
  """
  Now we can create something and test it
  """
  def setUp(self):
    pass
  def tearDown (self):  
    pass
#  def testSimpleIndexTest (self):  
#    self._ii = IndexItems ( "en", 999, shelve, _config )
#    #self._ii.AddWord ( "en", "probe", WordLocation (1, 1, 1) )
#    #self._ii.AddWord ( "en", "test", WordLocation (1, 1, 2) )
#    #self._ii.AddWord ( "en", "master", WordLocation (1, 1, 3) )
#    print self._ii
#    print 'Locate probe: ', self._ii.Locate ( "en", "probe" )
#    print 'Locate pro: ', self._ii.Locate ( "en", "pro" )
#    print "Locate xxl: ", self._ii.Locate ( "en", "xxl" )
  def testIndexer1 (self):
    _i = Indexer ( 999, shelve, _config, Progress () )
    _i.Dump ()
    _content = [
                Content ( "", "Gurmukhi Gurbani Parser Test File.txt",
                         [
                          Content ("en", "English Translation Parser Test File.txt"), 
                          Content ("",   "English Transliteration Parser Test File.txt"),
                          Content ("ru", "Russian Translation Parser Test File.txt" )
                         ])
               ]
    _i.Index ( _content )
    PerformSearch ( _i )
#    _tpl = [("en","gurbani"), ("en","lord"), ("","chapai"), ("","cih"), ("ru",u"стрел")]
#    for _item in _tpl:
#      print 'search ',_item[1].encode ( 'cp1251' ),':', _i.Search ( _item[0], _item[1] )
    _i.Dump ()
  def testPersistentIndexer (self):
    _i = Indexer ( 999, shelve, _config, Progress () )
    _i.Dump ()
#    for _item in _tpl:
#      print 'search ',_item[1].encode ( 'cp1251' ),':', _i.Search ( _item[0], _item[1] )
    PerformSearch ( _i )
    _i.Dump ()
  def testPersistentPhrase (self):
    _i = Indexer ( 999, shelve, _config, Progress () )
    _i.Dump ()
    PerformSearch ( _i )
#    _tpl = [("en","gurbani"), ("en","lord one"), ("","chapai stanza"), ("","cih"), ("ru",u"стрел")]
#    for _item in _tpl:
#      print 'search ',_item[1].encode ( 'cp1251' ),':', _i.SearchPhrase ( _item[0], _item[1] )
    _i.Dump ()
   
if __name__ == "__main__":
    unittest.main() 
