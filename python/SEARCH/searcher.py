# -*- coding: utf8 -*-
#          
import os
import re
import locale
#self.locale = wx.Locale(wx.LANGUAGE_FRENCH)
locale.setlocale(locale.LC_ALL, 'ru')

def runTests ():
   _key = "списка"
   import a
   print "looking for ", _key
   if ( a.wordvoc.has_key(_key)):
       print "found at files and lines", a.wordvoc[_key]
   else:
       print "oops"    
if __name__ == "__main__":
    runTests()
