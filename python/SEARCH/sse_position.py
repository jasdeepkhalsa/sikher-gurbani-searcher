###################################################################################
# -*- coding: utf8 -*-
# $Header$
#
# Module:  
# Purpose: word positions classes. Part of Search Engine
# Created: 
# $Log$
# Revision 1.4  2005/10/07 21:42:53  mamonts
# +simple test searcher. It works, but i have problem with text encoding (it is very strange problem. if we have text file in cp1251 encoding, and type russian text in search field (wxTextCtrl component) we can not found it or got error when try to call string function FIND.
# Just one idea: recognize text file encoding and perform search only with the encoding information. But it is not so good as i like
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
  def getString (self):
    return '{%d, {%d. [%d]}}'%(self.__fileid, self.__linenum, self.__wordnum)  
    
class WordPositions:
  """
  Locations map
  """
  def __init__ (self, wl = None ):
    self.__positions = {}
    if wl is not None:
      self.AddLocation(wl)
  def getPositions ( self ):
    return self.__positions    
  def setPositions (self, pos):
    self.__positions = pos  
    print 'pos:', pos
  def AddLocation (self, wl):  
    if ( not self.__positions.has_key(wl.getFileid ()) ):  
      self.__positions[wl.getFileid ()] = {}
    if ( not self.__positions[wl.getFileid ()].has_key(wl.getLinenum()) ):
      self.__positions[wl.getFileid ()][wl.getLinenum()] = []
    self.__positions[wl.getFileid ()][wl.getLinenum()].append(wl.getWordnum())
    print 'AddLocation for ', wl.getString(), ' : ', self.__positions

class SearchResult:
  """
  Search result will be converted to this class
  """
  def __init__(self, lineNum, line, page, book, author, file ):
    self.__lineNum = lineNum
    self.__line    = line
    self.__page    = page
    self.__book    = book
    self.__author  = author
    self.__file    = file
    pass
  def getLineNum (self):
    return self.__lineNum
  def getLine (self):
    return self.__line
  def getBook(self):
    return self.__book
  def getPage(self):
    return self.__page
  def getAuthor(self):
    return self.__author
  def getFile (self):
    return self.__file
##################################################
# testing
##################################################
import unittest
   
if __name__ == "__main__":
    unittest.main() 
