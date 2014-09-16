###################################################################################
# -*- coding: utf8 -*-
# $Header$
#
# Module:  indexer_txt
# Purpose: very special text indexer
# Created: 25.09.2005

# $Log$
# Revision 1.1  2005/10/07 21:42:53  mamonts
# +simple test searcher. It works, but i have problem with text encoding (it is very strange problem. if we have text file in cp1251 encoding, and type russian text in search field (wxTextCtrl component) we can not found it or got error when try to call string function FIND.
# Just one idea: recognize text file encoding and perform search only with the encoding information. But it is not so good as i like
#
#
#

__revision__ = "$Revision$"
__version__ = "0.0.0.0"

##################################################
# import section
##################################################
import os
import re
from os.path import join, getsize, split, splitext
from sse_position import SearchResult
try:
    import psyco
    psyco.profile()
except:
    print 'Psyco not found, ignoring it'

##################################################
# primary section
##################################################
"""
We can provide search only by text files. 
All text files are from DATAFILES directory!
"""
class IndexerTxt:
  def __init__ (self, Config):
    self.__cfg = Config
    pass
  def UnicodeSearch (self, ptn):
    """
    browse DATAFILES directory.
    get all txt files
    search in files
    """
    #print "text unicode search. pth=", ptn
    _res = []
    for _root, _dirs, _files in os.walk(self.__cfg.getDFPath ()):
      for _file in _files:
        _fname = join ( _root, _file )
        (_name, _ext) = splitext ( _fname )
        if _ext.upper() <> ".TXT":
          continue
        #show time
        print "search in ", _fname
        _lines = file(_fname).readlines()
        _lCtr = 0
        for _line in _lines:
          #print 'line ', _lCtr, ':', _line
          try:
            _line = unicode ( _line, 'cp1251' )
          except:
            pass
          #_line = unicode ( _line, 'utf8' )
          _lCtr = _lCtr + 1
          try:
            if ptn in _line: #.find ( ptn ) >= 0:
              print 'found in :', _lCtr
            else:
              continue  
          except:  
            #encoding problem!!!
            try:
              _line = unicode(_line, 'cp1251' )
              if ptn.encode ( 'utf8' ) in _line:#.find ( ptn ) >= 0:
                print 'found 2 in :', _lCtr
              else:
                continue  
            except:
              print "wrong line!!! ", _lCtr, ":"#,_line
              continue
#  def __init__(self, lineNum, line, page, book, author, file ):
          _res.append ( SearchResult ( _lCtr, _line, 0, '<undef>', '<unknown>', _file ) )
    return _res
##################################################
# testing
##################################################
import unittest
   
if __name__ == "__main__":
    unittest.main() 
