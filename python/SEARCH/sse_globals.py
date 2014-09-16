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
#G_BasePath = "D:\\projects.mal\\master\\offshore\\SIKHER\\SIKHER\\datafiles\\"

#indexer constants
ciUnreal = 0 #impossibe situation
ciPart   = 1 #key is a part of word     
ciEnd    = 2 #key is a end of word (has location!!!)
ciMix    = 3 #key is a part and end of word 

def emptyLocate ():
  #word (key), position, ci* code
  return (None,None,ciUnreal)

import os
import os.path as osp
import sys as _sys

class CSSEConfig:
  """
  Universal configuration file
  """
  def __init__ (self, StartPath = None ):
    """
    bTestStart = true - 
    """
    print _sys.argv[0]
    if StartPath is None:
     (self.__path,_f) = osp.split ( _sys.argv[0] ) #basepath - current directory
    else:
     #os.chdir( StartPath ) 
     (self.__path,_f) =  osp.split ( _sys.argv[0] )
     if self.__path == '':
       self.__path = os.getcwd() #current directory 
     if StartPath == '..':
      (self.__path,_f) =  osp.split ( self.__path )
    if self.__path == '':
      self.__path = os.getcwd() #current directory 
    self.__dbpath = osp.join ( self.__path, 'DATABASE' )
    self.__dfpath = osp.join ( self.__path, 'datafiles' ) 
    pass
  def getDBasePath (self):
    return self.__dbpath
  def getDBFile (self, file):
    return osp.join ( self.__dbpath, file )  
  def getDFPath (self):
    return self.__dfpath
  def getDFFile (self, file):
    return osp.join ( self.__dfpath, file )  
    

##################################################
# testing
##################################################
import unittest

class ConfigTest ( unittest.TestCase ):
  """
  """
  def setUp(self):
    pass
  def tearDown (self):  
    pass
  def testPath (self):  
    _cfg = CSSEConfig ( '..' )
    print "DBASEPATH:", _cfg.getDBasePath ()
    print "README.txt", _cfg.getDBFile('README.txt')
    print "DATAFILES:", _cfg.getDFPath ()
    print "Klinki.txt:", _cfg.getDFFile ( 'Klinki.txt' )
   
if __name__ == "__main__":
    unittest.main() 
