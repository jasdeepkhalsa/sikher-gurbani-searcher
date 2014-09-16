# -*- coding: utf8 -*-
# $Header: /cvsroot/sikher/SIKHER/page_project.py,v 1.4 2005/09/24 18:43:11 mamonts Exp $

# Module:  page_project.py

# Purpose: notebook project panel

# Created: Alex Marmuzevich 2005-06-17

from wxPython.wx import *
from wxPython.html import *
import  wx.lib.rcsizer  as rcs

try:
    import psyco
    psyco.profile()
except:
    print 'Psyco not found, ignoring it'

class CSikherProjectPanel (wxPanel):
  def __init__(self,parent):
    wxPanel.__init__(self, parent, -1)
    _box = wxBoxSizer(wxVERTICAL)
    _pnl = wxPanel ( self, -1 )
    _box2 = wxBoxSizer(wxHORIZONTAL)
    _flag = wxALIGN_CENTER_VERTICAL | wxALIGN_CENTER_HORIZONTAL 
    _rb = wx.RadioButton( _pnl, -1, "List view" )
    _rb.SetValue ( True )
    _box2.Add ( _rb, flag = _flag )         
    _rb = wx.RadioButton( _pnl, -1, "Document view" )
    _box2.Add ( _rb, flag = _flag )         
    _btn = wx.Button(_pnl, wx.ID_ANY, "Up")
    _box2.Add ( _btn )
    _btn = wx.Button(_pnl, wx.ID_ANY, "Down")
    _box2.Add ( _btn )
    _btn = wx.Button(_pnl, wx.ID_ANY, "Top")
    _box2.Add ( _btn )
    _btn = wx.Button(_pnl, wx.ID_ANY, "Bottom")
    _box2.Add ( _btn )
    _box2.Add ( (10,0), 0, wx.EXPAND )
    _btn = wx.Button(_pnl, wx.ID_ANY, "Delete")
    _box2.Add ( _btn )
    _pnl.SetSizer(_box2)
    _pnl.SetAutoLayout(true)
    _box.Add ( _pnl )

    _list = wx.ListCtrl ( self, -1, style=wx.LC_REPORT 
                                 | wx.BORDER_NONE
                                 | wx.LC_EDIT_LABELS | wx.LC_SORT_ASCENDING )
    _list.InsertColumn(0, "Line")
    _list.InsertColumn(1, "Book")#, wx.LIST_FORMAT_RIGHT)
    _list.InsertColumn(2, "Page")
    _list.InsertColumn(3, "Author")
#    _list.SetColumnWidth(0, 70 )#wx.LIST_AUTOSIZE)
#    _list.SetColumnWidth(1, 60)#wx.LIST_AUTOSIZE)
#    _list.SetColumnWidth(2, 60)
#    _list.SetStringItem(0, 1, 'test1')
#    _list.SetStringItem(0, 2, '----')
#    _list.SetItemData ( 0, 0 )

#    self.html = kbHtmlWindow(self, -1)
    _box.Add ( _list, 1, wxGROW)
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
    