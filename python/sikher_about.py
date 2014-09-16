# -*- coding: utf8 -*-
# $Header: /cvsroot/sikher/SIKHER/sikher_about.py,v 1.2 2005/09/19 05:44:30 mamonts Exp $

# Module:  sikher_about.py

# Purpose: about form

# Created: Alex Marmuzevich 2005-06-17

from sys import *
from wxPython.wx import *
from wxPython.html import *


try:
    import psyco
    psyco.profile()
except:
    print 'Psyco not found, ignoring it'

    
#############################
# About Us Box
#############################
class SikherAboutBox(wx.Dialog):
    text =  """
    <html><body bgcolor="#FF44D7">
    <center><h1>Sikher</h1>
    <h3>Sikhing Truth</h3>
    <p>sikher.sourceforge.net</p>
    <hr/>
    <h2>
    <p>Python Version: %s </p>
    <p>wxPython Version: %s </p>
    <p>
    <wxp module="wx" class="Button">
        <param name="label" value="Ok">
        <param name="id" value="ID_OK">
    </wxp>
    </p></body></center></html>
    """%(version, wx.VERSION_STRING, )
    
    def __init__(self, parent):
        wx.Dialog.__init__(self, parent, -1, "About Sikher")
        html = wx.html.HtmlWindow(self, -1,  size=(300,350))
        html.SetPage(self.text)
        btn = html.FindWindowById(wx.ID_OK)
        btn.SetDefault()
        self.SetClientSize(html.GetSize())
        self.CentreOnParent(wx.BOTH)

if __name__ == '__main__':
    print version
    print version_info
    pass
    