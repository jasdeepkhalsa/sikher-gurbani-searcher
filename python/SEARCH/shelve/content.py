###################################################################################
# -*- coding: utf8 -*-
# $Header $
# part of the search engine (content base class)
# $Log$
# Revision 1.2  2005/09/19 05:44:30  mamonts
# *** empty log message ***
#
# Revision 1.1  2005/07/26 21:39:02  mamonts
# +index engine looks like complete. Time to convert it to shabadSearchEngine
#
#
#

import os
import re
import locale
import time
class Content:
  """
  content information. Usualy:
    locale, filename, translation (also Content)
  """
  def __init__ (self, loc, fname, translation = [] ):
    self.__loc = loc
    self.__fname = fname
    self.__translation = translation
  def GetLocation (self):  
    return self.__loc
  def GetFileName (self):
    return self.__fname
  def GetTranslation (self):    
    return self.__translation



##################################################
# testing
##################################################
import unittest

class TestIndexItems ( unittest.TestCase ):
  def setUp(self):
    pass
#  def testSimpleIndexTest (self):  
   
if __name__ == "__main__":
    unittest.main()
