# -*- coding: utf8 -*-
# $Header: /cvsroot/sikher/SIKHER/page_slideshow.py,v 1.3 2005/09/19 05:44:30 mamonts Exp $

# Module:  page_slideshow.py

# Purpose: notebook slideshow panel

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

class CSikherSlideshowPanel (wxPanel):
  def __init__(self,parent):
    wxPanel.__init__(self, parent, -1)
    _box = wxBoxSizer(wxVERTICAL)
    _pnl = wxPanel ( self, -1 )
    _box2 = wxBoxSizer(wxHORIZONTAL)
    _flag = wxALIGN_CENTER_VERTICAL | wxALIGN_CENTER_HORIZONTAL 
    _rb = wx.RadioButton( _pnl, -1, "Slide view" )
    _rb.SetValue ( True )
    _box2.Add ( _rb, flag = _flag )         
    _rb = wx.RadioButton( _pnl, -1, "timeline view" )
    _box2.Add ( _rb, flag = _flag )         
    _btn = wx.Button(_pnl, wx.ID_ANY, "Export")
    _box2.Add ( _btn )
    _btn = wx.Button(_pnl, wx.ID_ANY, "Fullscreen")
    _box2.Add ( _btn )
    _pnl.SetSizer(_box2)
    _pnl.SetAutoLayout(true)
    _box.Add ( _pnl )
    self.html = kbHtmlWindow(self, -1)
    _box.Add ( self.html, 1, wxGROW)
    _pnl = wxPanel ( self, -1 )
    _box2 = wxBoxSizer(wxHORIZONTAL)
    _btn = wx.Button(_pnl, wx.ID_ANY, "Print")
    _box2.Add ( _btn )
    _btn = wx.Button(_pnl, wx.ID_ANY, "Print Preview")
    _box2.Add ( _btn )
    _pnl.SetSizer(_box2)
    _pnl.SetAutoLayout(true)
    _box.Add ( _pnl )
    self.SetSizer(_box)
    self.SetAutoLayout(true)
  pass


if __name__ == '__main__':
    pass
    