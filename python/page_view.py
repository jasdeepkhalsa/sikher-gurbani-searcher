# -*- coding: utf8 -*-
# $Header: /cvsroot/sikher/SIKHER/page_view.py,v 1.3 2005/09/19 05:44:30 mamonts Exp $

# Module:  page_view.py

# Purpose: notebook view panel

# Created: Alex Marmuzevich 2005-06-17

from wxPython.wx import *
from wxPython.html import *
import  wx.lib.rcsizer  as rcs

try:
    import psyco
    psyco.profile()
except:
    print 'Psyco not found, ignoring it'
from page_result import *
class CSikherViewPanel (wxPanel):
  def __init__(self,parent):
    wxPanel.__init__(self, parent, -1)
    self.html = kbHtmlWindow(self, -1)
    self.box = wxBoxSizer(wxVERTICAL)
    self.box.Add ( self.html, 1, wxGROW)
    self.SetSizer(self.box)
    self.SetAutoLayout(true)
  def LoadPage ( self, path ):
    self.html.LoadPage ( path )  
  pass


if __name__ == '__main__':
    pass
    