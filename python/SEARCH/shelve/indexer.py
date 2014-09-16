###################################################################################
# -*- coding: utf8 -*-
# $Header$
# part of the search engine (indexer)
# $Log$
# Revision 1.2  2005/09/19 05:44:30  mamonts
# *** empty log message ***
#
# Revision 1.1  2005/07/26 21:39:02  mamonts
# +index engine looks like complete. Time to convert it to shabadSearchEngine
#
# Revision 1.2  2005/07/17 13:12:48  mamonts
# +page_* refactoring
# +added devel panel (to test indexer and searcher)
#
#

import os
import os.path
import re
import locale
import time
from content import *
try:
    import psyco
    psyco.profile()
except:
    print 'Psyco not found, ignoring it'

#self.locale = wx.Locale(wx.LANGUAGE_FRENCH)
#data structure
#text:
#(locale, original, [(locale, translation, transliteration),])
#vocabulary:
#{locale: voc}
G_BasePath = "D:\\projects.mal\\master\\offshore\\SIKHER\\SIKHER\\datafiles\\"

def emptyLocate ():
  return (None,None)

class Progress:
  def __init__(self):
    self.__ctr = 0
    pass
  def Next (self, sym):
    self.__ctr = self.__ctr + 1
    if self.__ctr > 80:
      self.__ctr = 0
#      print sym
    else:
#      print sym,  
      pass
  def Reset (self):
    print ""
    self.__ctr = 0;
  def Info (self, data):
    print data
       
prog = Progress ()
      
class WordLocation:
  """
  Primitive location structure
  """
  def __init__ (self, fileid, linenum, wordnum ):
    self.__fileid = fileid
    self.__linenum = linenum
    self.__wordnum = wordnum
  def getFileid (self):  
    return self.__fileid
  def getLinenum (self):  
    return self.__linenum
  def getWordnum (self):  
    return self.__wordnum
    
class WordPositions:
  """
  Locations map
  """
  def __init__ (self, wl = None ):
    self.__positions = {}
    if wl is not None:
      self.AddLocation(wl)
  def AddLocation (self, wl):  
    if ( not self.__positions.has_key(wl.getFileid ()) ):  
      self.__positions[wl.getFileid ()] = {}
    if ( not self.__positions[wl.getFileid ()].has_key(wl.getLinenum()) ):
      self.__positions[wl.getFileid ()][wl.getLinenum()] = []
    self.__positions[wl.getFileid ()][wl.getLinenum()].append(wl.getWordnum())
    
class BaseIndexItems:
  def __init__(self, levelLimit, level ):
    self.__level = level
    self.__levelLimit = levelLimit
  def getLevelLimit(self):
    return self.__levelLimit
  def getLevel (self):
    return self.__level
  def SplitWord (self, loc, word ):
    if loc is not None:
     locale.setlocale(locale.LC_ALL, loc)
     if loc <> "" :
      _w = word.lower()
     else:
      _w = word
    else:
     _w = word
    _w = _w.strip()
    if _w == "":
      return emptyLocate()
    if self.__levelLimit > 0 and self.__level <= self.__levelLimit:
      _key = _w[0]
      _w = _w[1:]
    else:
      _key = _w
      _w = "";
    return (_key, _w)  
      
class IndexItem (BaseIndexItems):
  """
  Item of the binary tree. Used by index engine
  property:
    parent - ParentIndexItem
    chunk - part of the word
    level - item level
    levelLimit
    position: map[fileid] = map[lineNum] = [wordNum] 
    parentItem
    Items - child items (map[char] = [IndexItem])
  """
  def __init__ (self, parent, levelLimit, level ):
    BaseIndexItems.__init__ ( self, levelLimit, level )
    self.__parent = parent
    self.__key = ""
    self.__positions = None
    self.__items = None
  def getKey (self):
    return self.__key
  def getPositions (self):   
    return self.__positions 
  def getItems (self):   
    return self.__items 
  def getWord (self):
    if self.__parent is None:
      return self.__key
    else:
      return self.__parent.getWord () + self.__key  
        
  def AddWord (self, key, chunk, wordLocation ):    
    self.__key = key
    #print 'key:',key
    if chunk <> "": #has data
     (_itemKey, _itemRest)= self.SplitWord(None, chunk)
     if self.__items is None:
       self.__items = {}
     if not self.__items.has_key( _itemKey ):
       self.__items[_itemKey] = IndexItem(self, self.getLevelLimit(), self.getLevel()+1 )
     self.__items[_itemKey].AddWord(_itemKey, _itemRest, wordLocation)    
    else: #finish
     if self.__positions is None:
       self.__positions = WordPositions ()
     self.__positions.AddLocation ( wordLocation )
       
  def Locate (self, loc, word ):
    #print "Locate keys:", self.__items.keys()
    #print "Locate key:", word
    #Problem: when len(key) < len of each self.__items.keys
    #         we can lost right key.
    #example: we have probe in the index.
    # we have split it onto p-r-obe
    # search pattern: pro - p-r-o
    # in char 'o' we have __items.keys "obe", not "o".
    if word == "":
      #last!
      return (self.getWord (), self.getPositions ())
    else:
     (_key,_w) = self.SplitWord(loc, word)   
     if _key is None:
       return emptyLocate()
     if self.__items.has_key(_key):
       return self.__items[_key].Locate ( loc, _w )
     else:
       return emptyLocate()    
  pass   

import shelve
  
class WordVoc: 
  """
  Primary word indexer
  """ 
  def __init__ (self, loc):
    self.__loc = loc
    self.__file = "db_" + loc + ".sdb"
    self.__voc = shelve.open(self.__file)
  def __del__ (self):
    print "close ", self.__loc
    self.__voc.close ()  
  def getFileName (self):
    return self.__file  
  def AddWord (self, word, wordLocation ):
    if ( self.__voc.has_key(word) ):
      self.__voc[word].AddLocation ( wordLocation )
      return False
    else:  
      #print "+",word
      self.__voc[word] = WordPositions ( wordLocation )
      return True
        
class IndexItems ( BaseIndexItems ): 
  """
  Index Items manager
  """
  def __init__(self, suffix, levelLimit):
    BaseIndexItems.__init__( self, levelLimit, -1 )
    self.__items = {}
    self.__locvoc = {}
  def __del__ (self):
    pass  
#    self.__wordvoc = {}
  def AddWord (self, loc, word, wordLocation ):
    if loc is not None:
     locale.setlocale(locale.LC_ALL, loc)
     if loc <> "" :
      _w = word.lower()
     else:
      _w = word
    else:
     _w = word
    _w = _w.strip()
    if not self.__locvoc.has_key(loc):
      self.__locvoc[loc] = WordVoc ( loc )
    _ww = self.__locvoc[loc]
    if not _ww.AddWord ( _w, wordLocation ): #already indexed!!!
      prog.Next("+")
      return  
    prog.Next("-")  
    (_key,_w) = self.SplitWord(loc, word)
    if _key is None:
      return
    if not self.__items.has_key(_key):
      self.__items[_key] = IndexItem ( None, self.getLevelLimit(), 0 )
    self.__items[_key].AddWord ( _key, _w, wordLocation )
    #store word!
  def Locate (self, loc, word ):
    (_key,_w) = self.SplitWord(loc, word)
    if _key is None:
      return emptyLocate()
    if not self.__items.has_key(_key):
      return emptyLocate()
    else:
      return self.__items[_key].Locate ( loc, _w )  

###############################################################################
# Indexer
###############################################################################
  
class Indexer:
    def __init__ ( self, levelLimit ):
      """
       Vocs - map of the location:IndexItems
       Files - files database
      """
      self.__vocs = {}
      self.__files = []
      self.__levelLimit = levelLimit
      pass
    def TestFile (self, file):
        if not os.path.exists ( file ):
          #try to select anothe path
          file = os.path.join ( G_BasePath, file )
          if not os.path.exists ( file ):
            return None
        return file    
    def AddFile ( self, file ):    
        #try to check file path
        try:
            return self.__files.index(file)
        except:
            self.__files.append ( file )
            return len( self.__files ) - 1     
    def IndexFile ( self, voc, loc, baseFile, fileID ):        
        locale.setlocale(locale.LC_ALL, loc)
        _txt = file(baseFile).readlines()
        _ctr = 0 
        for _line in _txt:
          _ctr = _ctr + 1   
          _words = re.split ( "(?u)(?L)\W+", _line )
          _wctr = 0
          for _word in _words:
            _wctr = _wctr + 1
            voc.AddWord ( loc, _word, WordLocation ( fileID, _ctr, _wctr ) )
        #ok. we have voc!!!    
    def SelectVocAndIndex ( self, loc, file ):    
        if self.__vocs.has_key ( loc ):
            _voc = self.__vocs[loc]
        else:
            _voc = IndexItems ( loc, self.__levelLimit )
            self.__vocs[loc] = _voc
        #save file information
        _file = self.TestFile ( file )
        if _file is None:
          print "Unknown of wrong file ", file
          return
        _fileID = self.AddFile ( _file )    
        prog.Info( "Index file %s"% file )
        self.IndexFile ( _voc, loc, _file, _fileID )
    
    def Index ( self, content ):
        for _contentItem in content:
            #print 'ci:', _contentItem
            _locale = _contentItem.GetLocation()
            _baseFile = _contentItem.GetFileName()
            self.SelectVocAndIndex ( _locale, _baseFile )
            #look for translations and so on
            #print "tr:", _contentItem.GetTranslation ()
            for _item in _contentItem.GetTranslation():
                _locale = _item.GetLocation()
                _translation = _item.GetFileName ()
                if _translation <> "":
                    self.SelectVocAndIndex ( _locale, _translation )
    
    def Search (self, location, word ):
      if not self.__vocs.has_key(location):
        return emptyLocate()
      return self.__vocs[location].Locate ( location, word )                
         

##################################################
# testing
##################################################
import unittest

class TestIndexItems ( unittest.TestCase ):
  def setUp(self):
    pass
  def testSimpleIndexTest (self):  
    self._ii = IndexItems ( "en", 999 )
    self._ii.AddWord ( "en", "probe", WordLocation (1, 1, 1) )
    self._ii.AddWord ( "en", "test", WordLocation (1, 1, 2) )
    self._ii.AddWord ( "en", "master", WordLocation (1, 1, 3) )
    print self._ii
    print 'Locate probe: ', self._ii.Locate ( "en", "probe" )
    print 'Locate pro: ', self._ii.Locate ( "en", "pro" )
    print "Locate xxl: ", self._ii.Locate ( "en", "xxl" )
  def testIndexer1 (self):
    _i = Indexer ( 999 )
    _content = [
                Content ( "", "Gurmukhi Gurbani Parser Test File.txt",
                         [
                          Content ("en", "English Translation Parser Test File.txt"), 
                          Content ("",   "English Transliteration Parser Test File.txt")
                         ])
               ]
    _i.Index ( _content )
    _tpl = [("en","gurbani"), ("en","lord"), ("","chapai"), ("","cih"), ("ru","стрел")]
    for _item in _tpl:
      print 'search ',_item[1],':', _i.Search ( _item[0], _item[1] )
  def testLargeText (self):
    return
    _i = Indexer ( 999 )
    _content = [
                Content ( "ru", "a1.txt" ),
                Content ( "ru", "a2.txt" ), 
                Content ( "ru", "nd1.txt" ),
                Content ( "ru", "Smyerti_ili_slava_-_1._Chyernaya_astafyeta.txt" ),
                Content ( "ru", "Smyerti_ili_slava_-_2._Smyerti_ili_slava.txt" ),
                Content ( "ru", "Tyehnik_Bolishogo_Kiyeva_-_1._Tyehnik_Bolishogo_Kiyeva.txt" ),
                Content ( "ru", "Klinki.txt" )
               ]
    _i.Index ( _content )
    _tpl = [("ru","стрел"), ("en","lord"), ("","chapai"), ("","cih")]
    for _item in _tpl:
      print 'search ',_item[1],':', _i.Search ( _item[0], _item[1] )
   
if __name__ == "__main__":
    unittest.main() #TestIndexItems ()
    
    
#    content = [
#     (   "", "Gurmukhi Gurbani Parser Test File.txt", 
#      [ 
#        ( 
#         ("en", "English Translation Parser Test File.txt"),
#         ("",   "English Transliteration Parser Test File.txt") 
#        ) 
#      ] 
#     )
#    ]
#    runTests()
#    indexer = Indexer ()
#    indexer.Index ( content )
#    indexer.Save ()
