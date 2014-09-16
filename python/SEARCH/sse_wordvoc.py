###################################################################################
# -*- coding: utf8 -*-
# $Header$
#
# Module:  
# Purpose:
# Created: 
# $Log$
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

"""
Words Vocabulaty. Just store map like key:location (file,line,wordnum)
Used by indexer to obtain location(s) by previously located word(s)
"""
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

import shelve
from sse_position import *  
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
  def getWord (self, word):
    """
    Get word positions
    """
    if ( self.__voc.has_key ( word ) ):
      return self.__voc[word]
    else:
      return None   
##################################################
# testing
##################################################
import unittest
   
if __name__ == "__main__":
    unittest.main() 
