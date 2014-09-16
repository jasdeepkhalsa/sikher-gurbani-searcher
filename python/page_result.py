# -*- coding: utf8 -*-
# $Header: /cvsroot/sikher/SIKHER/page_result.py,v 1.6 2005/10/07 21:42:53 mamonts Exp $

# Module:  page_result.py

# Purpose: notebook result panel

# Created: Alex Marmuzevich 2005-06-17

from wxPython.wx import *
from wxPython.html import *
import  wx.lib.rcsizer  as rcs

try:
    import psyco
    psyco.profile()
except:
    print 'Psyco not found, ignoring it'

class kbHtmlWindow(wxHtmlWindow):
    def __init__(self, parent, id ):
        wxHtmlWindow.__init__(self, parent, id)
    def OnLinkClicked (self, linkinfo):
      if hasattr ( self.GetParent(), 'OnLinkClicked' ):
        self.GetParent().OnLinkClicked ( linkinfo )
      else:  
        wxLogMessage ( 'own OnLinkClicked: %s\n' % linkinfo.GetHref() );
      #self.base_OnLinkClicked(linkinfo)
class CSikherResultPanel (wxPanel):
  def __init__(self,parent, sikher):
    wxPanel.__init__(self, parent, -1)
    self.__sikher = sikher
    self.__resmap = {}
    _box = wxBoxSizer(wxVERTICAL)
    _pnl = wxPanel ( self, -1 )
    _box2 = wxBoxSizer(wxHORIZONTAL)
    _flag = wxALIGN_CENTER_VERTICAL | wxALIGN_CENTER_HORIZONTAL 
    _rb = wx.RadioButton( _pnl, -1, "List view" )
    _rb.Bind ( wx.EVT_RADIOBUTTON, self.OnListView )
    _box2.Add ( _rb, flag = _flag )         
    _rb = wx.RadioButton( _pnl, -1, "Document view" )
    _rb.Bind ( wx.EVT_RADIOBUTTON, self.OnDocView )
    _rb.SetValue ( True )
    _box2.Add ( _rb, flag = _flag )         
    _pnl.SetSizerAndFit(_box2)
    _pnl.SetAutoLayout(true)
    _box.Add ( _pnl, 0 )
    self.html = kbHtmlWindow(self, -1)
    _box.Add ( self.html, 1, wxGROW)
    self.list = wx.ListCtrl ( self, -1, style=wx.LC_REPORT | wx.BORDER_NONE | wx.LC_EDIT_LABELS | wx.LC_SORT_ASCENDING )
    self.list.InsertColumn(0, "Line")
    self.list.InsertColumn(1, "Book")#, wx.LIST_FORMAT_RIGHT)
    self.list.InsertColumn(2, "Page")
    self.list.InsertColumn(3, "Author")
    _box.Add ( self.list, 1, wxGROW)
    self.list.Show ( False )
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
    #self.html.LoadPage ( path )
    #TODO: create search result class and output procedures
    #search result:
    #line num, content
    #book
    #page
    #author  
    self.html.SetPage ( """
    <html>
    <title> Test page </title>
    <body>
    <p><b><a href="line1">Line 1:</a></b> what is going on, man</p>
    <p><b>Book:</b> My mega book <b>Page:</b> 1 <b>Author:</b> silent man</p>
    <hr>
    <p>Res2</p>
    <hr>
    </body>
    </html>
    """)
    #self.list.ClearAll()
    self.list.DeleteAllItems ()
    index = self.list.InsertStringItem ( 0, 'test1' )
    self.list.SetStringItem(index, 1, 'r12')
    index = self.list.InsertStringItem ( 0, 'test2' )
#    self.list.Append('test1')
#    self.list.Append('test2')
  def ShowResult (self, results):
    self.__resmap.clear ()
    #HTML loading
    _page = """
    <html> <title> Search results </title><body>
    """  
    _lCtr = 0
    #DONE: load listctrl too
    self.list.DeleteAllItems()
    for _res in results:
      _lCtr = _lCtr + 1
      self.__resmap['t%d'%_lCtr] = _res
      _page = _page + "<p><b><a href=\"t%d\">Line %d:</a></b> %s</p>"%(_lCtr,_res.getLineNum(), _res.getLine())
      _page = _page + "<p><b>Book:</b> %s <b>Page:</b> %d <b>Author:</b> %s</p>"%(_res.getBook(), _res.getPage(), _res.getAuthor())
      _page = _page + "<p><b>File:</b> %s </p>"%_res.getFile()
      _page = _page + "<hr>"
      _idx = self.list.InsertStringItem ( 0, _res.getLine ())
      self.list.SetStringItem(_idx,1,_res.getBook())
      self.list.SetStringItem(_idx,2,str(_res.getPage()))
      self.list.SetStringItem(_idx,3,_res.getAuthor())
    _page = _page + """
    </body>
    </html>
    """
    self.html.SetPage(_page)
  def OnLinkClicked (self, linkinfo):
      wxLogMessage ( '!!!OnLinkClicked: %s\n' % linkinfo.GetHref() );
      _res = self.__resmap[linkinfo.GetHref()]
      self.__sikher.ShowResultFile ( _res.getFile(), _res.getLineNum() )
  def OnListView (self, event):
    self.html.Show ( False )
    self.list.Show ( True )
    self.Layout()
    event.Skip ()
    pass
  def OnDocView (self, event):  
    self.list.Show ( False )
    self.html.Show ( True )
    self.Layout()
    event.Skip ()
    pass
    

if __name__ == '__main__':
    pass
    