# -*- coding: utf8 -*-
# $Header$

# Module:  page_devel.py

# Purpose: notebook developent panel

# Created: Alex Marmuzevich 2005-06-27

from wxPython.wx import *
from wxPython.html import *
from sikher_boxes import *
try:
    import indexer
except Exception, info:
    print 'page_devel.py:', info
try:
    import shelve
except Exception, info:
    print 'page_devel.py:',info
try:
    import sse_globals
except Exception, info:
    print 'page_devel.py:',info
try:
    import sikher_utils
except Exception, info:
    print 'page_devel.py:',info
try:
    import utils
except Exception, info:
    print 'page_devel.py:',info
try:
    import psyco
    psyco.profile()
except:
    print 'Psyco not found, ignoring it'

class CSikherDevelPanel (wxPanel):
  def __init__(self,parent, sikher):
    wxPanel.__init__(self, parent, -1)
    _box = wxBoxSizer(wxVERTICAL)
    self.__sikher = sikher
    #_splitter = wx.SplitterWindow(self,-1,style = wx.SP_LIVE_UPDATE)
    ##############################################
    # content
    _stb = wx.StaticBox(self, -1, "Content")
    _bsizer = wx.StaticBoxSizer ( _stb, wx.VERTICAL )
    self.edtContent = wx.TextCtrl ( self, -1, "#sikher_content.py",
                                    style=wx.TE_MULTILINE, size = wx.Size ( -1, 100 ) )
    _bsizer.Add ( self.edtContent, 0, wxGROW )#wx.TOP | wx.LEFT )
    _box.Add ( _bsizer, flag = wxGROW )
    ##############################################
    # tool panel
    _pnl = wxPanel ( self, -1 )
    _box2 = wxBoxSizer(wxHORIZONTAL)
    _flag = wxALIGN_CENTER_VERTICAL | wxALIGN_CENTER_HORIZONTAL
    _btn = wx.Button(_pnl, wx.ID_ANY, "Index", size = wx.Size ( 50, -1 ) )
    self.Bind(wx.EVT_BUTTON, self.OnIndexClick, _btn)
    _box2.Add ( _btn )
    _box2.Add ( wx.StaticText ( _pnl, -1, " Locale "), flag = _flag )
    self.edtLocale = wx.TextCtrl ( _pnl, -1, "" )
    _box2.Add ( self.edtLocale, 1, flag = wxGROW )
    _box2.Add ( wx.StaticText ( _pnl, -1, " Search pattern "), flag = _flag )
    self.edtSearch = wx.TextCtrl ( _pnl, -1, "" )
    _box2.Add ( self.edtSearch, 1, flag = wxGROW )
    _btn = wx.Button(_pnl, wx.ID_ANY, "Search", size = wx.Size ( 50, -1 ) )
    self.Bind(wx.EVT_BUTTON, self.OnSearchClick, _btn)
    _box2.Add ( _btn )
    _pnl.SetSizer(_box2)
    _pnl.SetAutoLayout(true)
    _box.Add ( _pnl )
    #_splitter.SetMinimumPaneSize(20)
    #_splitter.SplitHorizontally(_stb, _pnl, -100)
    #_box.Add ( _splitter, flag = wxGROW  )
    ##############################################
    # log panel
    _stb = wx.StaticBox(self, -1, "Result (log)")
    _bsizer = wx.StaticBoxSizer ( _stb, wx.VERTICAL )
    self.edtLog = wx.TextCtrl ( self, -1, "", style=wx.TE_MULTILINE|wxTE_READONLY|wxHSCROLL )
    _bsizer.Add ( self.edtLog, 1, wxGROW )#wx.TOP | wx.LEFT )
    wxLog_SetActiveTarget(wxLogTextCtrl(self.edtLog))
    _box.Add ( _bsizer, 1, flag = wxEXPAND )
    self.SetSizerAndFit(_box)
    self.SetAutoLayout(true)
    EVT_CLOSE (self,self.OnClose)
  def Load (self):
    try:
      self.edtContent.LoadFile ( "_content.py" );
    except:
      pass
    pass
  def Save (self):
    self.edtContent.SaveFile ( "_content.py" );
    pass
  def OnClose (self, event):
    self.Save ()
  def OnIndexClick (self, event):
    #kbInformationBox(self, _("Index button"))
    self.Save ()
    import sys as _sys
    try:
      del _sys.modules["_content"]
    except:
      pass
    try:
      import _content
#    _module = sys.modules.get("_content")
#    if _module:
#      reload ( _module )
      wxLogMessage ("imported!!!")
      #index time!!!
    except:
      wxLogMessage ("import problem!!!")
      return
    _config = sse_globals.CSSEConfig ()
    _i = indexer.Indexer ( 999, shelve, _config, utils.Progress() )#sikher_utils.SikherProgress () )
    wxLogMessage ( "try to index" )
    _i.Index ( _content.gurbaniContent )
    wxLogMessage ( "done" )
    try:
      del _sys.modules["_content"]
    except:
      pass
    event.Skip()
  def OnSearchClick (self, event):
    #kbInformationBox(self, _("Search button"))
    _config = sse_globals.CSSEConfig ()
    _i = indexer.Indexer ( 999, shelve, _config, sikher_utils.SikherProgress () )
    wxLogMessage ( "try to search" )
    wxLogMessage ( self.edtSearch.GetValue() )
    #bad new. we can not use Unicode to search something
    #good news: we can use locale_alias
    _ptn = self.edtSearch.GetValue()
    _ptn = _ptn.encode ('utf8')
    print _ptn
    _locale = self.edtLocale.GetValue().encode('utf8')
    _res = _i.SearchPhrase ( _locale, _ptn )
    print _res
    _lCtr = 0
    for _sr in _res:
     _lCtr = _lCtr + 1 
     _key = _sr[0]
     if _key is None:
       wxLogMessage ( "%d Empty key" % _lCtr ) 
     else:
       wxLogMessage ( "%d key '%s', ID %d" %(_lCtr, _key, _sr[2]) )  
       if _sr[1] is not None:
         #decode phrase
         wxLogMessage ( "--->" )
    event.Skip()
  pass
#import module
#reload module
if __name__ == '__main__':
    pass

