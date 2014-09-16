# -*- coding: utf8 -*-
# $Header: /cvsroot/sikher/SIKHER/sikher.py,v 1.18 2005/10/07 21:42:53 mamonts Exp $

# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
# Sikher - An Open Source Gurbani Searcher Program
# "Deh Shiva Bar Mohe Ihe Shubh Karman Se Kabhu Na Taraon"
# "O God give me this one boon, that I never desist from doing good deeds"
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
# RAJA TODO:
# (1) Ensure all menus are logically placed, have logical identifiers & write events for them [40%] ~ Had a look through them
# (2) Update toolbar icons to modern ones [50%] ~ I've found the icons just need to convert all to standard image format
# 
# TWINKLE TODO:
# (1) Use wxWindows implementation of gettext for il8n [50%]
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
# IMPORTANT NOTES:
# Twinkle you may want to start using SPE 0.5.1 Stani's Python Editor....it's Wicked!!!
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

##########################
# Import the things we need
##########################
from sikher_about import *
from sikher_boxes import *
from sikher_images import *
from sikher_panels import *
from sikher_splash import *
from wxPython.html import *
from wxPython.wx import *
import gettext, os, time
import wx, wx.html
import wx.lib.foldpanelbar as fpb
import wx.lib.wxpTag

import sys as _sys 
_sys.path.append("SEARCH")
_sys.path.append("UTILS")

import shabadSearchEngine
import sse_globals
#print _sys.stdout.encoding

try:
    import psyco
    psyco.profile()
except:
    print 'Psyco not found, ignoring it'

"""
Status bar constants
"""
SB_INFO = 0
SB_DATETIME = 2
SB_INFO_BTN = 3
SB_GAUGE    = 4
SB_LANGUAGE = 5

#############################
# Get Text Implementation for Localization
#############################
gettext.install('sikher', './locale', unicode=True)

#self.lang_english = gettext.translation("sikher", "./locale", languages=['en'])
#self.lang_french  = gettext.translation("sikher", "./locale", languages=['fr'])
#self.lang_english.install()

#######################################
# The Main Sikher Frame
#######################################
class kbFrame(wxFrame):
    def __init__(self, parent, ID, title, file):
        wxFrame.__init__(self, parent, ID, title, wxDefaultPosition, wxSize(1200,750))
        self.__indexer = None
        # Try to load the sikher.ico icon        
        try:
            self.SetIcon(wxIcon("sikher.ico", wxBITMAP_TYPE_ICO))
        finally:
            pass
        self.panel = kbHtmlPanel(self, -1, self, file)

        self.__CreateMenu ()
        self.__CreateStatusBar ()
        self.__CreateToolBar ()

        self.timer = wxPyTimer(self.Notify)
        self.timer.Start(500)
        self.Notify()
        

        ############################### 
        # Events for Tool Bar menus
        ###############################
        # EVT_MENU(self, wxID_NEW, self.doNew)
        #layouts
        self._leftWindow1 = wx.SashLayoutWindow(self, 101, wx.DefaultPosition,
                                                wx.Size(200, 1000), wx.NO_BORDER |
                                                wx.SW_3D | wx.CLIP_CHILDREN)

        self._leftWindow1.SetDefaultSize(wx.Size(220, 1000))
        self._leftWindow1.SetOrientation(wx.LAYOUT_VERTICAL)
        self._leftWindow1.SetAlignment(wx.LAYOUT_LEFT)
        self._leftWindow1.SetSashVisible(wx.SASH_RIGHT, True)
        self._leftWindow1.SetExtraBorderSize(10)
        self.remainingSpace = self.panel#wx.Panel(self, -1, style=wx.SUNKEN_BORDER)

        self.ID_WINDOW_TOP = 100
        self.ID_WINDOW_LEFT1 = 101
        self.ID_WINDOW_RIGHT1 = 102
        self.ID_WINDOW_BOTTOM = 103

        self._leftWindow1.Bind(wx.EVT_SASH_DRAGGED_RANGE, self.OnFoldPanelBarDrag,
                               id=100, id2=103)
        self.Bind(wx.EVT_SIZE, self.OnSize)
        self.Bind(wx.EVT_SCROLL, self.OnSlideColour)
        
        self.ReCreateFoldPanel(0)
        EVT_CLOSE(self,self.OnClose)
    def setPage (self, PageID):
      self.panel.setPage ( PageID )    
    def OnClose (self, event):
      self.panel.Close ()
      self.Destroy()
      event.Skip()
      pass    
    def __CreateMenu (self):    
        ###############################
        # Create the Menus
        ###############################
        # Create the menu file
        mnu_file = wxMenu()
        mnu_file.Append(100, _("&Open\tCtrl+O"), "Open file")
        mnu_file.AppendSeparator()
        mnu_file.Append(101, _("&Save\tCtrl+S"), "Save file")
        mnu_file.Append(102, _("Save &As..."), "Save file as something else")
        mnu_file.AppendSeparator()
        mnu_file.Append(103, _("Print Pre&view"), "Preview the document before printing")
        mnu_file.Append(104, _("&Print...\tCtrl+P"), "Print the document")
        mnu_file.AppendSeparator()
        mnu_file.Append(109, _("E&xit\tAlt+X"), "Exit Sikher")

        # Create the menu edit
        mnu_edit = wxMenu()
        mnu_edit.Append(110, _("&Cut\tCtrl+X"), "Cut")
        mnu_edit.Append(111, _("C&opy\tCtrl+C"), "Copy")
        mnu_edit.Append(112, _("&Paste\tCtrl+V"), "Paste")
        mnu_edit.AppendSeparator()
        mnu_edit.Append(113, _("&Undo\tCtrl+Z"), "Undo")           
        mnu_edit.Append(114, _("&Redo\tCtrl+Y"), "Redo")

        # Create the menu view     
        mnu_view = wxMenu()
        mnu_view.Append(120, _("&Standard"), "Switch to standard view")
        mnu_view.AppendSeparator()
        mnu_view.Append(121, _("F&ull screen"), "Switch to full screen view")
        mnu_view.AppendSeparator()
        mnu_view.Append(122, _("&Zoom..."), "Zoom in or out")

        # Create the menu insert      
        mnu_insert = wxMenu()
        mnu_insert.Append(130, _("&Note"), "Add a sticky note")

        # Create the menu format
        mnu_format = wxMenu()

        # Create the menu tools
        mnu_tools  = wxMenu()
        # Create the sub menu language (Needs to be replaced with a wxdialog where the language can easily be selected)
        submenu_language = wxMenu()
        submenu_languages = wxMenu()
        submenu_languages.Append(13001, _("&French"))
        submenu_languages.Append(13002, _("&German"))
        submenu_languages.Append(13003, _("&Italian"))
        submenu_languages.Append(13004, _("&Spanish"))                                          
        submenu_language.AppendMenu(1301, _("Set &Language"), submenu_languages)
        submenu_language.Append(1302, _("Learn Gurmukhi Script"), "Learn Gurmukhi Script")
        # Attaches sub menu to menu Language
        mnu_tools.AppendMenu(141, _("&Language"), submenu_language)
        mnu_tools.AppendSeparator()
        mnu_tools.Append(140, _("&Options"), "Different options of Sikher can be set up here")

        # Create the menu window
        mnu_window = wxMenu()

        # Create the menu help
        mnu_help = wxMenu()
        mnu_help.Append(150, _("&Help\tF1"), "Help on Sikher")
        mnu_help.AppendSeparator()
        mnu_help.Append(151, _("Chec&k for updates"), "Check for Updates to the Sikher program")
        mnu_help.AppendSeparator()
        mnu_help.Append(155, _("About Sikher"), "About Sikher")

        # Create the Menu Bar
        menuBar = wxMenuBar()
        menuBar.Append(mnu_file, _("&File"))
        menuBar.Append(mnu_edit, _("&Edit"))
        menuBar.Append(mnu_view, _("&View"))
        menuBar.Append(mnu_insert, _("&Insert"))
        menuBar.Append(mnu_format, _("F&ormat"))
        menuBar.Append(mnu_tools, _("&Tools"))
        menuBar.Append(mnu_window, _("&Window"))
        menuBar.Append(mnu_help, _("&Help"))
        self.SetMenuBar(menuBar)

        #################################
        # Events for Menu Bar Menus
        #################################
        EVT_MENU(self, 100, self.panel.OnLoadFile)
        EVT_MENU(self, 109, self.Exit)
        EVT_MENU(self, 121, self.FullScreenMode)
        EVT_MENU(self, 1301, self.SetLanguage) # This entry will not work because its been set to a submenu on which you can't click
        EVT_MENU(self, 140, self.Options)
        EVT_MENU(self, 150, self.Help)
        EVT_MENU(self, 151, self.CheckForUpdates)
        EVT_MENU(self, 155, self.About)
    def __CreateStatusBar (self):  
        # Create the Status Bar
        self.sb = self.CreateStatusBar(6)
        self.sb.SetStatusWidths([-1, 65, 150,20,65,65])
        self.sb.PushStatusText(_("Ready"), SB_INFO)
        _place = self.sb.GetFieldRect ( SB_GAUGE )
        self.gauge = wx.Gauge ( self.sb, -1, 50 )
        self.gauge.SetBezelFace(3)
        self.gauge.SetShadowWidth(3)
        self.gauge.SetValue ( 0 )
        _bmp = wxBitmap ( "images/noia/Help And Support2.png", wxBITMAP_TYPE_PNG )
        self.infoBtn = wx.BitmapButton(self.sb, -1, _bmp, (0, 0),
                       (_bmp.GetWidth(), _bmp.GetHeight()), style = wxNO_BORDER)
        self.infoBtn.SetToolTipString("Help button.")
        self.Reposition ()
        #self.sb.SetStatusText(_("???"), SB_INFO_BTN)
        #sb.SetStatusText(_("===>==="), SB_GAUGE)
        self.sb.SetStatusText(_("English"), SB_LANGUAGE)
    def __CreateToolBar (self):   
        ##################################
        # Create the Tool Bar
        ##################################
        self.toolbar = self.CreateToolBar(wxTB_HORIZONTAL|wxNO_BORDER|wxTB_3DBUTTONS|wxTB_DOCKABLE)
#        self.toolbar.AddSimpleTool(100, wxBitmap("images/document.gif", wxBITMAP_TYPE_GIF), "New")
#        self.toolbar.AddControl(wx.StaticBitmap(self.toolbar, -1, wxBitmap("images/sep.gif", wxBITMAP_TYPE_GIF), wx.DefaultPosition, wx.DefaultSize))
        
        self.toolbar.AddSimpleTool(100, wxBitmap("images/noia/Deffault Document2.png", wxBITMAP_TYPE_PNG), "New(?)")
        self.toolbar.AddSimpleTool(100, wxBitmap("images/noia/Folder Clossed2.png", wxBITMAP_TYPE_PNG), "Open")
        self.toolbar.AddSimpleTool(100, wxBitmap("images/noia/Floppy Drive 5 17.png", wxBITMAP_TYPE_PNG), "Save")
        #self.toolbar.AddSimpleTool(100, wxBitmap("images/saveas.gif", wxBITMAP_TYPE_GIF), "Save As")
        self.toolbar.AddControl(wx.StaticBitmap(self.toolbar, -1, wxBitmap("images/sep.gif", wxBITMAP_TYPE_GIF), wx.DefaultPosition, wx.DefaultSize))
        
        self.toolbar.AddSimpleTool(100, wxBitmap("images/print.gif", wxBITMAP_TYPE_GIF), "Print")
        self.toolbar.AddSimpleTool(100, wxBitmap("images/preview.gif", wxBITMAP_TYPE_GIF), "Print Preview")
        self.toolbar.AddSimpleTool(150, wxBitmap("images/magnify.gif", wxBITMAP_TYPE_GIF), "Zoom")
        self.toolbar.AddControl(wx.StaticBitmap(self.toolbar, -1, wxBitmap("images/sep.gif", wxBITMAP_TYPE_GIF), wx.DefaultPosition, wx.DefaultSize))
        
        self.toolbar.AddSimpleTool(150, wxBitmap("images/help.gif", wxBITMAP_TYPE_GIF), "Help")
        self.toolbar.AddSimpleTool(109, wxBitmap("images/door.gif", wxBITMAP_TYPE_GIF), "Exit")
        
        self.toolbar.SetToolBitmapSize((26,26))
        self.toolbar.AddControl(wx.StaticBitmap(self.toolbar, -1, wxBitmap("images/sep.gif", wxBITMAP_TYPE_GIF), wx.DefaultPosition, wx.DefaultSize))
        self.toolbar.Realize()
      
    ############################
    # Events
    ############################
    def Reposition ( self ):
        _place = self.sb.GetFieldRect ( SB_GAUGE )
        #self.gauge.SetPositon ( _place.x, _place.y )
        #self.gauge.SetSize ( (_place.width, _place.height) )
        self.gauge.SetDimensions ( _place.x, _place.y+2, _place.width-4, _place.height-4 )
        _place = self.sb.GetFieldRect ( SB_INFO_BTN )
        self.infoBtn.SetDimensions ( _place.x, _place.y+2, 16, 16 )
        # = wx.Gauge(sb, -1, 50, (_place.GetLeft(), _place.GetTop()), (_place.GetWidth(), _place.GetHeight()))
      
    def OnSize(self, event):

        wx.LayoutAlgorithm().LayoutWindow(self, self.remainingSpace)
        self.Reposition ()
        event.Skip()
    
    def doNew(self, event):
        kbWarningBox(self, _("You just clicked me stupid"))

    def Exit(self, event):
        if ( AMBQ(self, _("Are you sure you want to exit?")) == wxID_YES ):
          self.Close(true)
        else:
          event.Skip ()  

    def FullScreenMode(self, event):
        kbInformationBox(self, _("This will activate full-screen mode"))

    def Options(self, event):
        kbInformationBox(self, _("Options Menu"))                        

    def SetLanguage(self, event):
        kbInformationBox(self, _("Select English, French, German, Spanish etc!"))                        
        
    def About(self, event):
        dlg = SikherAboutBox(self)
        dlg.ShowModal()
        dlg.Destroy()

    def CheckForUpdates(self, event):
        #self.panel.html.LoadPage("http://sourceforge.net/projects/sikher") # changed website to the project page
        pass

    def Help(self, event):
        kbWarningBox(self, _("The help system has not been implemented yet."))

    def Notify(self):
        t = time.localtime(time.time())
        st = time.strftime(" %b-%d-%Y  %I:%M %p", t)
        self.SetStatusText(st, SB_DATETIME)
        if self.gauge.GetValue() < self.gauge.GetRange():
          self.gauge.SetValue( self.gauge.GetValue() + 1)
        else:
          self.gauge.SetValue( 0 )
            
    
    def OnToggleWindow(self, event):
        
        self._leftWindow1.Show(not self._leftWindow1.IsShown())
        # Leaves bits of itself behind sometimes
        wx.LayoutAlgorithm().LayoutWindow(self, self.remainingSpace)
        self.remainingSpace.Refresh()

        event.Skip()
        

    def OnFoldPanelBarDrag(self, event):

        if event.GetDragStatus() == wx.SASH_STATUS_OUT_OF_RANGE:
            return

        if event.GetId() == self.ID_WINDOW_LEFT1:
            self._leftWindow1.SetDefaultSize(wx.Size(event.GetDragRect().width, 1000))


        # Leaves bits of itself behind sometimes
        wx.LayoutAlgorithm().LayoutWindow(self, self.remainingSpace)
        self.remainingSpace.Refresh()

        event.Skip()
        

        
    def ReCreateFoldPanel(self, fpb_flags):

        # delete earlier panel
        self._leftWindow1.DestroyChildren()

        # recreate the foldpanelbar

        self._pnl = fpb.FoldPanelBar(self._leftWindow1, -1, wx.DefaultPosition,
                                     wx.Size(-1,-1), fpb.FPB_DEFAULT_STYLE, fpb_flags)

        Images = wx.ImageList(16,16)
        Images.Add(GetExpandedIconBitmap())
        Images.Add(GetCollapsedIconBitmap())
            
        item = self._pnl.AddFoldPanel("Gurbani Search", collapsed=False,
                                      foldIcons=Images)
        self.__fpSearch = CFPSearch1 ( item, self )                              
        self._pnl.AddFoldPanelWindow ( item, self.__fpSearch, fpb.FPB_ALIGN_WIDTH )

        self._pnl.AddFoldPanelSeparator(item)
        self.__fpOptions = CFPSearchOptions ( item )
        self._pnl.AddFoldPanelWindow ( item, self.__fpOptions, fpb.FPB_ALIGN_WIDTH )


        # put down some caption styles from which the user can
        # select to show how the current or all caption bars will look like

        item = self._pnl.AddFoldPanel("Post Search Options", False, foldIcons=Images)
        self.__fpPostSearch = CFPPostSearch ( item )
        self._pnl.AddFoldPanelWindow(item, self.__fpPostSearch, fpb.FPB_ALIGN_WIDTH ) 

        item = self._pnl.AddFoldPanel("Common tasks", False, foldIcons=Images)
        self.__fpCommonTasks = CFPCommonTasks ( item )
        self._pnl.AddFoldPanelWindow(item, self.__fpCommonTasks, fpb.FPB_ALIGN_WIDTH ) 


        item = self._pnl.AddFoldPanel("Favorites", False, foldIcons=Images)
        _pnl = wxPanel ( item, -1 )
        _list = wx.ListCtrl ( _pnl, -1, style=wx.LC_REPORT 
                                 #| wx.BORDER_SUNKEN
                                 | wx.BORDER_NONE
                                 | wx.LC_EDIT_LABELS
                                 | wx.LC_SORT_ASCENDING
                                 #| wx.LC_NO_HEADER
                                 #| wx.LC_VRULES
                                 #| wx.LC_HRULES
                                 #| wx.LC_SINGLE_SEL 
                                 )
        _list.InsertColumn(0, "Artist")
        _list.InsertColumn(1, "Title")#, wx.LIST_FORMAT_RIGHT)
        _list.InsertColumn(2, "Genre")
        _list.SetColumnWidth(0, 70 )#wx.LIST_AUTOSIZE)
        _list.SetColumnWidth(1, 60)#wx.LIST_AUTOSIZE)
        _list.SetColumnWidth(2, 60)
        _box = wxBoxSizer ( wxVERTICAL )
        _box.Add ( _list, 0, wxGROW )
        _pnl.SetSizerAndFit(_box)
        _pnl.SetAutoLayout(true)
        self._pnl.AddFoldPanelWindow(item, _pnl, fpb.FPB_ALIGN_WIDTH ) 


        # one more panel to finish it

#        cs = fpb.CaptionBarStyle()
#        cs.SetCaptionStyle(fpb.CAPTIONBAR_RECTANGLE)

#        item = self._pnl.AddFoldPanel("Misc Stuff", collapsed=True, foldIcons=Images,
#                                      cbstyle=cs)
#
#        button2 = wx.Button(item, wx.NewId(), "Collapse All")        
#        self._pnl.AddFoldPanelWindow(item, button2) 
#        self._pnl.AddFoldPanelWindow(item, wx.StaticText(item, -1, "Enter Some Comments"),
#                                     fpb.FPB_ALIGN_WIDTH, 5, 20) 
#        self._pnl.AddFoldPanelWindow(item, wx.TextCtrl(item, -1, "Comments"),
#                                     fpb.FPB_ALIGN_WIDTH, fpb.FPB_DEFAULT_SPACING, 10)
#
#        button2.Bind(wx.EVT_BUTTON, self.OnCollapseMe)
#        self.radiocontrols = [currStyle, radio1, radio2, radio3, radio4]
        
        self._leftWindow1.SizeWindows()
        

    def OnCreateBottomStyle(self, event):

        # recreate with style collapse to bottom, which means
        # all panels that are collapsed are placed at the bottom,
        # or normal
        
        if event.IsChecked():
            self._flags = self._flags | fpb.FPB_COLLAPSE_TO_BOTTOM
        else:
            self._flags = self._flags & ~fpb.FPB_COLLAPSE_TO_BOTTOM

        self.ReCreateFoldPanel(self._flags)


    def OnCreateNormalStyle(self, event):

        # recreate with style where only one panel at the time is
        # allowed to be opened
        
        # TODO: Not yet implemented even in the C++ class!!!!

        if event.IsChecked():
            self._flags = self._flags | fpb.FPB_SINGLE_FOLD
        else:
            self._flags = self._flags & ~fpb.FPB_SINGLE_FOLD

        self.ReCreateFoldPanel(self._flags)


    def OnCollapseMe(self, event):

        for i in range(0, self._pnl.GetCount()):
            item = self._pnl.GetFoldPanel(i)
            self._pnl.Collapse(item)


    def OnExpandMe(self, event):

        col1 = wx.Colour(self._rslider1.GetValue(), self._gslider1.GetValue(),
                         self._bslider1.GetValue())
        col2 = wx.Colour(self._rslider2.GetValue(), self._gslider2.GetValue(),
                         self._bslider2.GetValue())

        style = fpb.CaptionBarStyle()

        style.SetFirstColour(col1)
        style.SetSecondColour(col2)

        counter = 0
        for items in self.radiocontrols:
            if items.GetValue():
                break
            counter = counter + 1
            
        if counter == 0:
            mystyle = fpb.CAPTIONBAR_GRADIENT_V
        elif counter == 1:
            mystyle = fpb.CAPTIONBAR_GRADIENT_H
        elif counter == 2:
            mystyle = fpb.CAPTIONBAR_SINGLE
        elif counter == 3:
            mystyle = fpb.CAPTIONBAR_RECTANGLE
        else:
            mystyle = fpb.CAPTIONBAR_FILLED_RECTANGLE

        style.SetCaptionStyle(mystyle)
        self._pnl.ApplyCaptionStyleAll(style)


    def OnSlideColour(self, event):

        col1 = wx.Colour(self._rslider1.GetValue(), self._gslider1.GetValue(),
                         self._bslider1.GetValue())
        col2 = wx.Colour(self._rslider2.GetValue(), self._gslider2.GetValue(),
                         self._bslider2.GetValue())

        style = fpb.CaptionBarStyle()

        counter = 0
        for items in self.radiocontrols:
            if items.GetValue():
                break
            
            counter = counter + 1

        if counter == 0:
            mystyle = fpb.CAPTIONBAR_GRADIENT_V
        elif counter == 1:
            mystyle = fpb.CAPTIONBAR_GRADIENT_H
        elif counter == 2:
            mystyle = fpb.CAPTIONBAR_SINGLE
        elif counter == 3:
            mystyle = fpb.CAPTIONBAR_RECTANGLE
        else:
            mystyle = fpb.CAPTIONBAR_FILLED_RECTANGLE
            
        style.SetFirstColour(col1)
        style.SetSecondColour(col2)
        style.SetCaptionStyle(mystyle)

        item = self._pnl.GetFoldPanel(0)
        self._pnl.ApplyCaptionStyle(item, style)
        

    def OnStyleChange(self, event):

        style = fpb.CaptionBarStyle()
        
        eventid = event.GetId()
        
        if eventid == self.ID_USE_HGRADIENT:
            style.SetCaptionStyle(fpb.CAPTIONBAR_GRADIENT_H)
            
        elif eventid == self.ID_USE_VGRADIENT:
            style.SetCaptionStyle(fpb.CAPTIONBAR_GRADIENT_V)
            
        elif eventid == self.ID_USE_SINGLE:
            style.SetCaptionStyle(fpb.CAPTIONBAR_SINGLE)
            
        elif eventid == self.ID_USE_RECTANGLE:
            style.SetCaptionStyle(fpb.CAPTIONBAR_RECTANGLE)
            
        elif eventid == self.ID_USE_FILLED_RECTANGLE:
            style.SetCaptionStyle(fpb.CAPTIONBAR_FILLED_RECTANGLE)
                
        else:
            raise "ERROR: Undefined Style Selected For CaptionBar: " + repr(eventid)

        col1 = wx.Colour(self._rslider1.GetValue(), self._gslider1.GetValue(),
                         self._bslider1.GetValue())
        col2 = wx.Colour(self._rslider2.GetValue(), self._gslider2.GetValue(),
                         self._bslider2.GetValue())

        style.SetFirstColour(col1)
        style.SetSecondColour(col2)

        if self._single.GetValue():
            item = self._pnl.GetFoldPanel(1)
            self._pnl.ApplyCaptionStyle(item, style)
        else:
            self._pnl.ApplyCaptionStyleAll(style)
    
    def Search (self):
        """
        Usual search routine
        """
        #create query
        if self.__indexer is None: #create indexer
          _config = sse_globals.CSSEConfig ()
          import indexer_txt
          #self.__indexer = indexer.Indexer ( 999, shelve, _config, sikher_utils.SikherProgress () )
          self.__indexer = indexer_txt.IndexerTxt ( _config )
        wxLogMessage ( "sikher->try to search" )
        #get search pattern
        _ptn = self.__fpSearch.edt.GetValue()
        wxLogMessage ( _ptn )
        _locale = self.__fpOptions.getSearchLocale()
        wxLogMessage ( _locale )
        #perform search
        if hasattr ( self.__indexer, 'UnicodeSearch' ):
          _res = self.__indexer.UnicodeSearch ( _ptn )
        else:  
          _res = self.__indexer.Search ( _locale, _ptn.encode ( 'cp1251' ) )
        print 'res:',_res
        #print result information
        self.panel.resultPage.ShowResult ( _res )
        pass
    def ShowResultFile (self, sFileName, LineNum ):
      """
      output result file and set page 1
      """
      _config = sse_globals.CSSEConfig ()
      self.panel.viewPage.LoadPage ( _config.getDFFile(sFileName) )
      self.setPage(1)
      pass

#################################
# Bind everything together to make the Application
#################################
class kbApp(wxApp):
    def OnInit(self):
        MySplash = MySplashScreen()
        MySplash.Show()
        htmlFile = "waheguru.html"
        frame = kbFrame(NULL, -1, "Sikher", htmlFile)
        frame.SetSize((800,600))
        frame.Show(true)
        self.SetTopWindow(frame)
        return true

################################
# Exception Handler
################################
class ExceptionHandler:
    """ A simple error-handling class to write exceptions to a text file."""

    def __init__(self):
        """ Standard constructor.
        """
        self._buff = ""
        if os.path.exists("errors.txt"):
            os.remove("errors.txt") # Delete previous error log, if any.

    def write(self, s):
        """ Write the given error message to a text file."""
        if (s[-1] != "\n") and (s[-1] != "\r"):
            self._buff = self._buff + s
            return
        try:
            s = self._buff + s
            self._buff = ""
            if s[:9] == "Traceback":
                # Tell the user than an exception occurred.
                wxMessageBox("An internal error has occurred.\nPlease " + \
                             "refer to the 'errors.txt' file for details.",
                             "Error", wxOK | wxCENTRE | wxICON_EXCLAMATION)
            f = open("errors.txt", "a")
            f.write(s)
            f.close()
        except:
            pass # Don't recursively crash on errors.

###################################
# Main Program Function
###################################
def main():
    """ Start up Sikher. """
    # Redirect python exceptions to a log file.
    _sys.stderr = ExceptionHandler()

    # Create and start Sikher
    app = kbApp(0)
    app.MainLoop()
    
if __name__ == "__main__":
    main()

