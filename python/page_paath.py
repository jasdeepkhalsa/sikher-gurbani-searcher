# -*- coding: utf8 -*-
# $Header: /cvsroot/sikher/SIKHER/page_paath.py,v 1.3 2005/09/19 05:44:30 mamonts Exp $

# Module:  page_paath.py

# Purpose: notebook akand paath panel

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

class CSikherAkandPanel (wxPanel):
  def __init__(self,parent):
    wxPanel.__init__(self, parent, -1)
    _box = wxBoxSizer(wxVERTICAL)
    _pnl = wxPanel ( self, -1 )
    _box2 = wxBoxSizer(wxHORIZONTAL)
    _flag = wxALIGN_CENTER_VERTICAL | wxALIGN_CENTER_HORIZONTAL 
    _box2.Add ( wx.StaticText ( _pnl, -1, "Page"), flag = _flag )         
    _sc = wx.SpinCtrl(_pnl, -1, "", size = wx.Size(50, -1))
    _sc.SetRange ( 1, 1430 )
    _sc.SetValue ( 1430 )
    _box2.Add ( _sc, flag = _flag )
    _box2.Add ( (10,0), 0, wx.EXPAND )
    
    _btn = wx.Button(_pnl, wx.ID_ANY, "Start", size = wx.Size ( 50, -1 ) )
    _box2.Add ( _btn )
    _btn = wx.Button(_pnl, wx.ID_ANY, "Stop", size = wx.Size ( 50, -1 ))
    _box2.Add ( _btn )
    _box2.Add ( (10,0), 0, wx.EXPAND )
    _btn = wx.Button(_pnl, wx.ID_ANY, "Fullscreen")
    _box2.Add ( _btn )
    _box2.Add ( (10,0), 0, wx.EXPAND )
    _box2.Add ( wx.StaticText ( _pnl, -1, "Go to Page"), flag = _flag )         
    _edt = wx.TextCtrl(self, -1, "test", (0,0), size = wx.Size ( 30, -1 ) )
    _box2.Add ( _edt, flag = _flag )
    _btn = wx.Button(_pnl, wx.ID_ANY, "Go", size = wx.Size ( 30, -1 ) )
    _box2.Add ( _btn )
    _box2.Add ( (10,0), 0, wx.EXPAND )
    _box2.Add ( wx.StaticText ( _pnl, -1, "Page"), flag = _flag )         
    _spin = wx.SpinButton(self, -1,
                         #(w, 50),
                         #(h*2/3, h),
                         style=wx.SP_HORIZONTAL)
    _box2.Add ( _spin, flag = _flag )
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
  def LoadPage ( self, path ):
    self.html.LoadPage ( path )  
  pass

if __name__ == '__main__':
    pass
    