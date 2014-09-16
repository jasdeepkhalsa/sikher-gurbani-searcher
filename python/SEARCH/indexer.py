###################################################################################
# -*- coding: utf8 -*-
# $Header: /cvsroot/sikher/SIKHER/SEARCH/indexer.py,v 1.6 2005/09/24 18:43:10 mamonts Exp $
# part of the search engine (indexer)
# $Log: indexer.py,v $
# Revision 1.6  2005/09/24 18:43:10  mamonts
# Tested with Python 2.4.1
#
# Revision 1.5  2005/09/19 05:44:31  mamonts
# *** empty log message ***
#
# Revision 1.4  2005/08/29 23:14:38  mamonts
# +indexer
# i have problems with unicode wxTextCtrl.GetValue() and shelve key.
#
# Revision 1.3  2005/07/26 21:39:02  mamonts
# +index engine looks like complete. Time to convert it to shabadSearchEngine
#
# Revision 1.2  2005/07/17 13:12:48  mamonts
# +page_* refactoring
# +added devel panel (to test indexer and searcher)
#
#
"""
 Main idea: parse file and create kinds of vocabularies.
 One is alphabet
 Second - words + phrases.
"""

try:
    import psyco
    psyco.profile()
except:
    print 'Psyco not found, ignoring it'


from sse_content  import *
from sse_globals  import *
from sse_position import *
from sse_wordvoc  import *
#from sse_meta import *
import locale
import os
import os.path
import re
import time
try:
    from options import *
    from utils import *
except:
    from sys import * #import sys as _sys; 
    path.append("../utils")
    from options import *
    from utils import *
    
#self.locale = wx.Locale(wx.LANGUAGE_FRENCH)
#data structure
#text:
#(locale, original, [(locale, translation, transliteration),])
#vocabulary:
#{locale: voc}
    
  
class BaseIndexItems:
  def __init__(self, levelLimit, level ):
    self.__level = level
    self.__levelLimit = levelLimit
  def getLevelLimit(self):
    return self.__levelLimit
  def getLevel (self):
    return self.__level
  def SplitWord (self, loc, word ):
    _w = ''
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
    level - item level
    levelLimit
    position: map[fileid] = map[lineNum] = [wordNum] 
  """
  def __init__ (self, location, levelLimit, level, baseVoc, Config, meta ):
    BaseIndexItems.__init__ ( self, levelLimit, level )
    self.__location = location
    self.__cfg = Config
    self.__meta = meta #from sse_meta
    if hasattr ( baseVoc, "open" ):
      _file = self.__cfg.getDBFile ( location + "_ii.idx" )
      self.__items = baseVoc.open ( _file )
#      _file = self.__cfg.getDBFile ( location + "_fl.sdb" )
#      self.__files = baseVoc.open ( _file )
    else:
      self.__items = baseVoc.copy ()    
#      self.__files = baseVoc.copy ()    
  def __del__ (self):
    if hasattr(self.__items, "close"):
      self.__items.close ()
#    if hasattr(self.__files, "close"):
#      self.__files.close ()
    pass  
  def getItems (self):   
    return self.__items 
  def __AddLocation (self, key, wl, wlid ):
    """
    key - word
    wl - wordLocation (file, line, word)
    lid - exists location id
    """
    self.__meta.saveLocation ( wl.getFileid (), wl.getLinenum(), wl.getWordnum(), wlid )
#    if self.__files.has_key(key):
#     _w = self.__files[key]
#    else:
#     _w = {} 
#    if not _w.has_key ( wl.getFileid () ):
#      _w[wl.getFileid()] = {}
#    if not _w[wl.getFileid()].has_key(wl.getLinenum ()):
#      _w[wl.getFileid()][wl.getLinenum()] = []
#    _w[wl.getFileid()][wl.getLinenum()].append(wl.getWordnum())    
#    self.__files[key] = _w
     
  def __AddWord ( self, masterKey, word, wordLocation ):
    """
    Special private indexing procedure. Used by AddWord
    """
    if self.__items.has_key ( masterKey ):
      _item = self.__items[masterKey]
    else:
      _item = {}
      self.__items[masterKey] = _item
    (_itemKey, _itemRest)= self.SplitWord(None, word)
    if _item.has_key(_itemKey):
      (_val, _wlid) = _item[_itemKey]
    else:
      _val = 0
      _wlid = None #self.__meta.getNewWLID ( self.__location, masterKey + _itemKey )#_itemKey )
      _item[_itemKey] = (_val, _wlid)
    if _itemRest == "": #last item!!!
      _val = _val | ciEnd
      _wlid = self.__meta.getNewWLID ( self.__location, masterKey + _itemKey )#_itemKey )
      _item[_itemKey] = (_val, _wlid)
      self.__items[masterKey] = _item
      masterKey = masterKey + _itemKey
      self.__AddLocation ( masterKey, wordLocation, _wlid )
      # self.__files[masterKey] = wordLocation
      #save data to the file
      return
    else: #to be continued
      _val = _val | ciPart
      _item[_itemKey] = (_val, _wlid)
      self.__items[masterKey] = _item
      masterKey = masterKey + _itemKey
      self.__AddWord ( masterKey, _itemRest, wordLocation )
    
  def AddWord (self, word, wordLocation ):    
    if word is None or word == "":
      return
    #special optimization routine  
    (_w, _wlid, _flag) = self.Locate ( word )
    if _w is not None:
     self.__AddLocation ( word, wordLocation, _wlid )
     return
#    if self.__files.has_key ( word ):
#      #do something
#      self.__AddLocation ( word, wordLocation )                           
#      return
    #stage 0  
    _masterKey = ''
    self.__AddWord ( _masterKey, word, wordLocation )
              
  def Locate (self, word ):
    _res = emptyLocate ()
    if word == "":
      return _res
    _key = word[:-1]  
    if self.__items.has_key ( _key ):
      _subVoc = self.__items[_key]
    else:
      return _res
    _key = word[-1]
    if _subVoc.has_key ( _key ):
      (_val, _wlid) = _subVoc[_key]
    else:
      return _res
    if _val & ciEnd <> 0:
      #get location id
      return ( word, _wlid, _val)
    else:
      return ( word, None, _val )          

class IndexItems ( BaseIndexItems ): 
  """
  Index Items manager
  """
  def __init__(self, suffix, levelLimit, baseVoc, Config, meta ):
    BaseIndexItems.__init__( self, levelLimit, -1 )
    self.__items = {} #just temporary map!!!!
    self.__baseVoc = baseVoc 
    self.__cfg = Config
    if meta is None:
     import sse_meta
     self.__meta = sse_meta.CSikherDatabase ( self.__cfg.getDBasePath())
    else: 
     self.__meta = meta
    pass
  def __del__ (self):
    pass  
#    self.__wordvoc = {}
  def Dump (self):
      print "\n=======Dump IndexItems======="
      print "\n items:==>", self.__items
    
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
    if self.__items.has_key(loc):
      _ii = self.__items[loc]
    else:  
      _ii = IndexItem ( loc, self.getLevelLimit(), 0, self.__baseVoc, self.__cfg, self.__meta )
      self.__items[loc] = _ii
    _ii.AddWord ( _w, wordLocation )

  def Locate (self, loc, word ):
    _ii = None
    if self.__items.has_key(loc):
      _ii = self.__items[loc]
    else:
      _ii = IndexItem ( loc, self.getLevelLimit(), 0, self.__baseVoc, self.__cfg, self.__meta )
      self.__items[loc] = _ii
    return _ii.Locate ( word )

###############################################################################
# Indexer
###############################################################################
  
class Indexer:
    def __init__ ( self, levelLimit, baseVoc, Config, prog, meta = None ):
      """
       Vocs - map of the location:IndexItems
       Files - files database
      """
      self.__cfg = Config
      if meta is None:
       import sse_meta
       self.__meta = sse_meta.CSikherDatabase ( self.__cfg.getDBasePath())
      else: 
       self.__meta = meta
      self.__baseVoc = baseVoc
      self.__vocs = {}
      self.__levelLimit = levelLimit
      self.__prog = prog #progress indicator (log)
      pass

    def __del__ (self):  
      pass
      
    def Dump (self):  
      self.__prog.Info ( "\n=======Dump indexer=======" );
      self.__prog.Info ( "\n vocs:==>" )
      self.__prog.Info ( self.__vocs )
#      print "\n vocs[en]=>", self.__vocs["en"].Dump ()

    def __TestFile (self, file):
        if not os.path.exists ( file ):
          #try to select anothe path
          file = self.__cfg.getDFFile ( file )
          if not os.path.exists ( file ):
            return None
        return file    
    def __AddFile ( self, loc, file ):  
        """
        return (get existing or create new) fileID by file (full fileName)
        """  
        #try to check file path
        return self.__meta.getFileID ( loc, file )
    def __IndexFile ( self, voc, loc, baseFile, fileID ):        
        locale.setlocale(locale.LC_ALL, loc)
        _txt = file(baseFile).readlines()
        _ctr = 0 
        _max = len(_txt)
        for _line in _txt:
          _ctr = _ctr + 1   
          _words = re.split ( "(?u)(?L)\W+", _line )
          _wctr = 0
          for _word in _words:
            _wctr = _wctr + 1
            voc.AddWord ( loc, _word, WordLocation ( fileID, _ctr, _wctr ) )
          print "%d%%"%int(_ctr*100/_max), "\r",
        print "Done"
        #ok. we have voc!!!    
    def __SelectVocAndIndexFile ( self, loc, file ):    
        if self.__vocs.has_key ( loc ):
            _voc = self.__vocs[loc]
        else:
            _voc = IndexItems ( loc, self.__levelLimit, self.__baseVoc, self.__cfg, self.__meta )
            self.__vocs[loc] = _voc
        #save file information
        _file = self.__TestFile ( file )
        if _file is None:
          self.__prog.Info ( "Unknown of wrong file %s"% file )
          return
        _fileID = self.__AddFile ( loc, _file )    
        self.__prog.Info( "Index file %s"% file )
        self.__IndexFile ( _voc, loc, _file, _fileID )
        self.__meta.Commit ()   
    def Index ( self, content ):
        for _contentItem in content:
            #print 'ci:', _contentItem
            _locale = _contentItem.GetLocation()
            _baseFile = _contentItem.GetFileName()
            self.__SelectVocAndIndexFile ( _locale, _baseFile )
            #look for translations and so on
            #print "tr:", _contentItem.GetTranslation ()
            for _item in _contentItem.GetTranslation():
                _locale = _item.GetLocation()
                _translation = _item.GetFileName ()
                if _translation <> "":
                    self.__SelectVocAndIndexFile ( _locale, _translation )
    
    def Search (self, location, word ):
      """
      Try to locate the all entries by location and word
      Return (word, position, code)
      Next problem is to convert result to the real text
      """
      _voc = None
      if not self.__vocs.has_key(location):
        _voc = IndexItems(location, self.__levelLimit, self.__baseVoc, 
                          self.__cfg, self.__meta)
        self.__vocs[location] = _voc
        #return emptyLocate()
      return self.__vocs[location].Locate ( location, word )                
    def SearchPhrase (self, location, phrase ):  
        locale.setlocale(locale.LC_ALL, location)
        _words = re.split ( "(?u)(?L)\W+", phrase )
        _res = []
        for _word in _words:
         (_key, _pos, _id ) = self.Search ( location, _word )
         if _key is None :
           continue
         _res.append ( ( _key, _pos, _id ) )
        return _res   
    def SearchPhraseU (self, location, phrase ):  
        """
        purpose: convert phrase to non-unicode string and continue
        """
        locale.setlocale(locale.LC_ALL, location)
        _words = re.split ( "(?u)(?L)\W+", phrase )
        _res = []
        for _word in _words:
         (_key, _pos, _id ) = self.Search ( location, _word )
         if _key is None :
           continue
         _res.append ( ( _key, _pos, _id ) )
        return _res   
    def DecodeFile (self, pos):
        """
        position decoding (search result). 
        Main idea: change fileid to real file name!
        """ 
        if pos is None:
         return None 
        _res = {}  
        for _key in pos.keys():
         _resfile = self.__meta.getFileName ( _key );
         if _resFile is not None:
          #we have file + word (hid + wlid)
          #load strings
          _res[_resFile] = pos[_key]
          _txt = file(_resFile).readlines()
          for _line in pos[_key].keys():
           pos[_key][_line] = _txt[_line-1]
          #_txt.close () 
         else:
          _res["?%d"%_key] = pos[_key]
        return _res  
##################################################
# testing
##################################################

if __name__ == "__main__":
#    unittest.main() 
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
         print 'files::: ', _res#idx.DecodeFile ( _res[1] )
    def test_ii ():     
     _ii = IndexItems ( "en", 999, {}, _config, None )
     _ii.AddWord ( "en", "probe", WordLocation (1, 1, 1) )
     _ii.AddWord ( "en", "probe", WordLocation (1, 1, 2) )
     _ii.AddWord ( "en", "probe", WordLocation (1, 2, 1) )
     _ii.AddWord ( "en", "probe", WordLocation (2, 1, 1) )
     _ii.AddWord ( "en", "test", WordLocation (1, 1, 2) )
     _ii.AddWord ( "en", "master", WordLocation (1, 1, 3) )
     #_ii.AddWord ( "ru", u"проверка", WordLocation (3, 1, 3) )
     print _ii
     print 'Locate probe: ', _ii.Locate ( "en", "probe" )
     print 'Locate pro: ', _ii.Locate ( "en", "pro" )
     print "Locate xxl: ", _ii.Locate ( "en", "xxl" )
     print "проверка: ", _ii.Locate ( "ru", u"проверка" )
     print "пров: ", _ii.Locate ( "ru", u"пров" )
     print "верка: ", _ii.Locate ( "ru", u"верка" )
    def test_indexer(): 
     _i = Indexer ( 999, shelve, _config, Progress () )
     _i.Dump ()
     _content = [
                Content ( "", "Gurmukhi Gurbani Parser Test File.txt",
                         [
                          Content ("en", "English Translation Parser Test File.txt"), 
                          Content ("",   "English Transliteration Parser Test File.txt"),
                          Content ("ru", "Russian Translation Parser Test File.txt" )
                         ]),
                Content ( "ru", "Klinki.txt", [] )
               ]
     _i.Index ( _content )
     PerformSearch ( _i )
#    _tpl = [("en","gurbani"), ("en","lord"), ("","chapai"), ("","cih"), ("ru",u"стрел")]
#    for _item in _tpl:
#      print 'search ',_item[1].encode ( 'cp1251' ),':', _i.Search ( _item[0], _item[1] )
#    _i.Dump ()
    #import profile
    #profile.run ( 'test_ii()' )
    #profile.run ( 'test_indexer()' )
    test_ii()
    test_indexer()
    