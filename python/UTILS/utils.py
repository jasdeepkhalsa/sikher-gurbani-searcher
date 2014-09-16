###################################################################################
# -*- coding: utf8 -*-
# $Header $
# part of the sikher (utilites file)
# $Log$
# Revision 1.3  2005/09/19 05:44:31  mamonts
# *** empty log message ***
#
# Revision 1.2  2005/08/29 23:14:39  mamonts
# +indexer
# i have problems with unicode wxTextCtrl.GetValue() and shelve key.
#
# Revision 1.1  2005/07/26 21:39:03  mamonts
# +index engine looks like complete. Time to convert it to shabadSearchEngine
#
#
#

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


import os.path, fnmatch
def listFiles ( root, patterns="*", recurse = 1, return_folders = 0):
  """
  From Python Cookbook 4.19 Walking Directory Trees
  """
  #expand patterns from semicolon-separated string to list
  pattern_list = patterns.split ( ";" )
  #collect input and output arguments into one bunch 
  class Bunch:
    def __init__ (self, **kwds):
      self.__dict__.update(kwds)
  arg = Bunch ( recurse=recurse, pattern_list=pattern_list, 
                return_folders=return_folders, results = [])    
  def Visit ( arg, dirname, files ):
    #Append to arg.results all relevant files (and perhaps folders)
    for name in files:
      fullname = os.path.normpath(os.path.join(dirname, name))              
      if arg.return_folders or os.path.isfile(fullname):
        for pattern in arg.pattern_list:
          if fnmatch.fnmatch(name, pattern):
            arg.results.append ( fullname )
            break
    #Block recursion if recursion was disallowed
    if not arg.recurse: files[:] = []
  os.path.walk ( root, Visit, arg )
  return arg.results
            
##################################################
# testing
##################################################

   
if __name__ == "__main__":
  import unittest
  class TestUtils ( unittest.TestCase ):
    def setUp(self):
      pass
    def testListFiles (self):  
      print "\ntest list files:"
      print listFiles ( ".", "*.py", 0 )
#  def testSimpleIndexTest (self):  
  unittest.main()
