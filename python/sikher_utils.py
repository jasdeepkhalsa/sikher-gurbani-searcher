###################################################################################
# -*- coding: utf8 -*-
# $Header$
#
# Module:  
# Purpose:
# Created: 

# $Log$
# Revision 1.3  2005/09/24 18:43:11  mamonts
# Tested with Python 2.4.1
#
# Revision 1.2  2005/09/19 05:44:30  mamonts
# *** empty log message ***
#
# Revision 1.1  2005/08/29 23:14:38  mamonts
# +indexer
# i have problems with unicode wxTextCtrl.GetValue() and shelve key.
#
#
#

__revision__ = "$Revision$"
__version__ = "0.0.0.0"

##################################################
# import section
##################################################
from wxPython.wx import *
#
try:
    import psyco
    psyco.profile()
except:
    print 'Psyco not found, ignoring it'

##################################################
# primary section
##################################################

class SikherProgress:
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
    wxLogMessage ("")
    self.__ctr = 0;
  def Info (self, data):
    wsLogMessage ("")

##################################################
# testing
##################################################
import unittest
   
if __name__ == "__main__":
    unittest.main() 
