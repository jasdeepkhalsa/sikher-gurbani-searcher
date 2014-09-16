# -*- coding: utf8 -*-
# $Header: /cvsroot/sikher/SIKHER/sikher_panels.py,v 1.5 2005/09/27 21:03:58 mamonts Exp $

# Module:  sikher_boxes.py

# Purpose: Folding panels

# Created: Alex Marmuzevich 2005-06-17

from page_devel      import *    
from page_paath      import *
from page_project    import *
from page_result     import *
from page_slideshow  import *
from page_view       import *
from wxPython.html import *
from wxPython.wx import *
import  wx.lib.rcsizer  as rcs

try:
    import psyco
    psyco.profile()
except:
    print 'Psyco not found, ignoring it'

class CFPSearch1(wxPanel):
    def __init__(self, parent, sikher):
        self.__sikher = sikher
        wxPanel.__init__(self, parent, -1)
        #self.html = kbHtmlWindow(self, -1, frame)
        #self.SetClientSize(wx.Size(100, 50))
        _sizer = rcs.RowColSizer()
        self.st = wx.StaticText(self, -1, "Search", (0,0)) 
        _sizer.Add ( self.st, row=0, col=0 )#, colspan = 5, rowspan=5)
        self.edt = wx.TextCtrl(self, -1, "test", (0,0) )
        _sizer.Add ( self.edt, row = 0, col = 1)
        button1 = wx.Button(self, wx.ID_ANY, "Open Keyboard", (0,0))
        #button1.Bind(wx.EVT_BUTTON, self.OnExpandMe)
        _sizer.Add ( button1, row = 1, col = 0)

        button1 = wx.Button(self, wx.ID_ANY, "Search", (0,0))
        button1.Bind(wx.EVT_BUTTON, self.OnSearch)
        _sizer.Add ( button1, row = 1, col = 1, flag=wx.EXPAND)
        #_sizer.AddGrowableCol(4)
        #_sizer.AddGrowableRow(4)
        #_sizer.AddSpacer(10,10, pos=(1,6))
        #_sizer.AddSpacer(10,10, pos=(13,1))
        self.SetSizerAndFit(_sizer)
        self.SetAutoLayout(True)
    def OnSearch (self, event):
      #kbInformationBox(self, u"Under construction")
      self.__sikher.Search ()
      event.Skip()
class CFPSearchOptions (wxPanel):
    def __init__(self,parent):
        wxPanel.__init__(self, parent, -1)
        #self.SetClientSize(wx.Size(100, 500))#150))
        _bs = wx.BoxSizer ( wx.VERTICAL )
        _box = wx.StaticBox ( self, -1, "Options", size=wx.Size (-1,-1) )
        #inside Options
        _sbs = wx.StaticBoxSizer ( _box, wx.VERTICAL )
        _sizer = rcs.RowColSizer ()
        _sizer.Add ( wx.StaticText (self, -1, "Search", size = wx.Size (50, -1)), row=0, col=0 )
        self.__cmbxSearch = wx.ComboBox( self, -1, "...", (0, 0), 
            (120, -1), 
             ['with all of the words (partial or whole)',
              'with any of the words (partial or whole)',
              'without the words (partial or whole)',
              'with the exact phrase',
              'first letters (start)',
              'first letters (anywhere)'
             ], wx.CB_READONLY #DROPDOWN #|wxTE_PROCESS_ENTER
            )
        self.__cmbxSearch.SetSelection ( 0 ) #Value ( 'with all of the words (partial or whole)' )    
        _sizer.Add ( self.__cmbxSearch, row=0, col=1, flag=wx.EXPAND | wx.ALL )
        _lRow = 1
        _sizer.Add ( wx.StaticText (self, -1, "Language"), row=_lRow, col=0 )
        self.__cmbxLanguage = wx.ComboBox( self, -1, "...", (0, 0), 
            (-1, -1), ['Gurmukhi','English','Russian'], wx.CB_READONLY #|wxTE_PROCESS_ENTER
            )
        self.__cmbxLanguage.SetSelection ( 1 )#Value ( 'Gurmukhi' )    
        _sizer.Add ( self.__cmbxLanguage, row=_lRow, col=1, flag=wx.EXPAND | wx.ALL ); _lRow = _lRow + 1

        _sizer.Add ( wx.StaticText (self, -1, "Bani"), row=_lRow, col=0 )
        self.__cmbxBani = wx.ComboBox( self, -1, "...", (0, 0), 
            (-1, -1), ['Guru Granth Sahib','Bhai Gurdaas Vaaran','Amrit Keertan','All Banis'], wx.CB_READONLY #|wxTE_PROCESS_ENTER
            )
        self.__cmbxBani.SetSelection ( self.__cmbxBani.GetCount()-1 )#Value ( 'Gurmukhi' )    
        _sizer.Add ( self.__cmbxBani, row=_lRow, col=1, flag=wx.EXPAND | wx.ALL ); _lRow = _lRow + 1

        _sizer.Add ( wx.StaticText ( self, -1, "Writers"), row=_lRow, col=0 )
        self.__cmbxWriters = wx.ComboBox( self, -1, "...", (0, 0), 
            (-1, -1), ['All writers', 'All Gurus', 'All Bhagats',
                       'All Divines', 'All Poets', '[Select specific...]'], wx.CB_READONLY #|wxTE_PROCESS_ENTER
            )
        self.__cmbxWriters.SetSelection(0)    
        _sizer.Add ( self.__cmbxWriters, row=_lRow, col=1, flag=wx.EXPAND | wx.ALL ); _lRow = _lRow + 1
        _sizer.Add ( wx.StaticText (self, -1, "Raag"), row=_lRow, col=0 )
        self.__cmbxRaag = wx.ComboBox( self, -1, "...", (0, 0), 
            (-1, -1), ['All Raags', '[Select specific]', 'No Raag'], wx.CB_READONLY #|wxTE_PROCESS_ENTER
            )
        self.__cmbxRaag.SetSelection(0)    
        _sizer.Add ( self.__cmbxRaag, row=_lRow, col=1, flag=wx.EXPAND | wx.ALL ); _lRow = _lRow + 1
        ###
        _sbs.Add ( _sizer, flag = wx.EXPAND | wx.ALL, border=2 )
        _sizer = rcs.RowColSizer ()
        _sizer.Add ( wx.StaticText (self, -1, "Page", size = wx.Size (50, -1)), row=0, col=0 )
        _rb = wx.RadioButton(self, -1, "&All")
        _sizer.Add ( _rb, row=0, col=1 )
        _rb = wx.RadioButton(self, -1, "&Range from")
        _sizer.Add ( _rb, row=1, col=1 )
        _rb = wx.RadioButton(self, -1, "&Exact")
        _sizer.Add ( _rb, row=3, col=1 )
        _edt = wx.TextCtrl(self, -1, "test", (0,0), size = wx.Size ( 30, -1 ) )
        _sizer.Add ( _edt, row = 1, col = 2)
        _sizer.Add ( wx.StaticText (self, -1, "to    "), row=2, col=1, flag=wx.ALIGN_RIGHT )
        _edt = wx.TextCtrl(self, -1, "test", (0,0), size = wx.Size ( 30, -1 ) )
        _sizer.Add ( _edt, row = 2, col = 2)
        _edt = wx.TextCtrl(self, -1, "test", (0,0), size = wx.Size ( 30, -1 ) )
        _sizer.Add ( _edt, row = 3, col = 2)
        ###
        _sbs.Add ( _sizer, flag = wx.EXPAND | wx.ALL, border = 2 )
        
        _bs.Add(_sbs, flag = wx.EXPAND | wx.ALL, border = 2)
        #_bs.AddSpacer (1)
        self.SetSizerAndFit ( _bs )
        self.SetAutoLayout(True)
    def getSearchOption(self):
      return self.__cmbxSearch.GetValue()
    def getSearchLocale(self):
      _locs = {
       -1:'',
       0:'gb',
       1:'en',
       2:'ru'
      }
      try:
        return _locs[self.__cmbxLanguage.GetSelection()]
      except Exception, info:
        print "unsupported selection ", self.__cmbxLanguage.GetSelection()
        return ""
      
class CFPPostSearch (wxPanel):
    def __init__(self,parent):
        wxPanel.__init__(self, parent, -1)
        #self.SetClientSize(wx.Size(100, 500))#150))
        _bs = wx.BoxSizer ( wx.VERTICAL )
        _sizer = rcs.RowColSizer ()
        _sizer.Add ( wx.StaticText (self, -1, "Gurbani", size = wx.Size (90, -1)), row=0, col=0 )
        self.__cmbxGurbani = wx.ComboBox( self, -1, "...", (0, 0), 
            (90, -1), ['Gurmukhi'], wx.CB_READONLY #|wxTE_PROCESS_ENTER
            )
        self.__cmbxGurbani.SetSelection(0)    
        _sizer.Add ( self.__cmbxGurbani, row=0, col=1, flag=wx.EXPAND | wx.ALL )
        _sizer.Add ( wx.StaticText (self, -1, "Translation", size = wx.Size (-1, -1)), row=1, col=0 )
        self.__cmbxTranslation = wx.ComboBox( self, -1, "...", (0, 0), 
            (-1, -1), ['English','Russian'], wx.CB_READONLY #|wxTE_PROCESS_ENTER
            )
        self.__cmbxTranslation.SetSelection ( 0 )    
        _sizer.Add ( self.__cmbxTranslation, row=1, col=1, flag=wx.EXPAND | wx.ALL )
        _sizer.Add ( wx.StaticText (self, -1, "Transliteration", size = wx.Size (-1, -1)), row=2, col=0 )
        self.__cmbxTranslit = wx.ComboBox( self, -1, "...", (0, 0), 
            (-1, -1), ['English','Russian'], wx.CB_READONLY #|wxTE_PROCESS_ENTER
            )
        self.__cmbxTranslit.SetSelection ( 0 )    
        _sizer.Add ( self.__cmbxTranslit, row=2, col=1, flag=wx.EXPAND | wx.ALL )
        _bs.Add ( _sizer, flag = wx.EXPAND | wx.ALL, border = 2 )
        _cb = wx.CheckBox(self, -1, "Show Gurbani")
        _bs.Add ( _cb, flag = wx.EXPAND | wx.ALL, border = 2 )
        _cb = wx.CheckBox(self, -1, "Show Translation")
        _bs.Add ( _cb, flag = wx.EXPAND | wx.ALL, border = 2 )
        _cb = wx.CheckBox(self, -1, "Show Transliteration")
        _bs.Add ( _cb, flag = wx.EXPAND | wx.ALL, border = 2 )

        self.SetSizerAndFit ( _bs )
        self.SetAutoLayout(True)
  
class CFPCommonTasks (wxPanel):
    def __init__(self,parent):
        wxPanel.__init__(self, parent, -1)
        _sizer = rcs.RowColSizer ()

        _sizer.Add ( wx.StaticText (self, -1, "Take a Hukamnama", size = wx.Size (90, -1)), row=0, col=0 )
        _btn = wx.Button(self, wx.ID_ANY, "Hukamnama")
        #button1.Bind(wx.EVT_BUTTON, self.OnExpandMe)
        _sizer.Add ( _btn, row=0, col=1 )        
        
        _sizer.Add ( wx.StaticText (self, -1, "Morning Prayers", size = wx.Size (90, -1)), row=1, col=0 )
        _btn = wx.Button(self, wx.ID_ANY, "Nitnem")
        #button1.Bind(wx.EVT_BUTTON, self.OnExpandMe)
        _sizer.Add ( _btn, row=1, col=1 )
        _sizer.Add ( wx.StaticText (self, -1, "Evening Prayers", size = wx.Size (90, -1)), row=2, col=0 )
        _btn = wx.Button(self, wx.ID_ANY, "Rehras")
        #button1.Bind(wx.EVT_BUTTON, self.OnExpandMe)
        _sizer.Add ( _btn, row=2, col=1 )
        _sizer.Add ( wx.StaticText (self, -1, "Bliss Prayers", size = wx.Size (90, -1)), row=3, col=0 )
        _btn = wx.Button(self, wx.ID_ANY, "Anand Sahib")
        #button1.Bind(wx.EVT_BUTTON, self.OnExpandMe)
        _sizer.Add ( _btn, row=3, col=1 )
        _sizer.Add ( wx.StaticText (self, -1, "Standing Prayer", size = wx.Size (90, -1)), row=4, col=0 )
        _btn = wx.Button(self, wx.ID_ANY, "Ardaas")
        #button1.Bind(wx.EVT_BUTTON, self.OnExpandMe)
        _sizer.Add ( _btn, row=4, col=1 )

        self.SetSizerAndFit ( _sizer )
        self.SetAutoLayout(True)
  
################################################################################

# panels
###################################
# HTML Panel (May need to be replaced at some point)
###################################
class kbHtmlPanel(wxPanel):
    def __init__(self, parent, id, frame, file): #parent - sikher
        wxPanel.__init__(self, parent, -1)
        #self.html = kbHtmlWindow(self, -1, frame)

        self.nb1 = wx.Notebook ( self )#, size=(200,200) )
        #results
        #self.html = kbHtmlWindow(self.nb1, -1, frame)
        self.resultPage = CSikherResultPanel ( self.nb1, parent )
        self.nb1.AddPage( self.resultPage,"Results")
        #view
        self.viewPage = CSikherViewPanel ( self.nb1 )
        self.nb1.AddPage( self.viewPage,"View")
        #project
        self.projectPage = CSikherProjectPanel ( self.nb1 )
        self.nb1.AddPage( self.projectPage,"Project")
        #slideshow
        self.slideshowPage = CSikherSlideshowPanel ( self.nb1 )
        self.nb1.AddPage( self.slideshowPage,"Slideshow")
        #akand
        self.akandPage = CSikherAkandPanel ( self.nb1 )
        self.nb1.AddPage( self.akandPage,"Akand Paath")
        #devel
        self.develPage = CSikherDevelPanel ( self.nb1, parent )
        self.nb1.AddPage( self.develPage,"Development")


        self.box = wxBoxSizer(wxVERTICAL)
        self.box.Add(self.nb1, 1, wxGROW)

        #subbox = wxBoxSizer(wxHORIZONTAL)
        #btn = wxButton(self, 1202, _("Load file"))
        #EVT_BUTTON(self, 1202, self.OnLoadFile)

        #subbox.Add(btn, 1, wxGROW | wxALL, 2)
        #btn = wxButton(self, 1203, _("Translate"))
        #EVT_BUTTON(self, 1203, self.OnTranslate)

        #subbox.Add(btn, 1, wxGROW | wxALL, 2)
        #self.box.Add(subbox, 0, wxGROW)
        self.SetSizer(self.box)
        self.SetAutoLayout(true)
        self.viewPage.LoadPage(file)
        self.resultPage.LoadPage("part1.htm")
        self.akandPage.LoadPage(file)
        self.develPage.Load ()
        EVT_CLOSE(self,self.OnClose)
    def setPage (self, PageID ):    
      self.nb1.SetSelection ( PageID )
    def OnClose (self, event):
      self.develPage.Close ()
      #self.Destroy()
      event.Skip()
      pass    
        
    def OnLoadFile(self, event):
        _dlg = wxFileDialog(self, wildcard="*.htm*", style=wxOPEN)
        if _dlg.ShowModal():
            path = _dlg.GetPath()
        self.viewPage.LoadFile(path)
        _dlg.Destroy()

    def OnTranslate(self, event):
        kbWarningBox(self, _("The translation function has not been implemented"))
  
if __name__ == '__main__':
    pass
    