# -*- coding: utf8 -*-
# Module:  sikher_splash.py
# Purpose: to show a splash screen on startup
# Created: Jasdeep Singh 24/09/2005
# To Do: Add Code to Show What's Loading
#        Place this status text in bottom right of splash screen

import wx

try:
    import psyco
    psyco.profile()
except:
    print 'Psyco not found, ignoring it'

class MySplashScreen(wx.SplashScreen):
    """
Create a splash screen widget.
    """
    def __init__(self):
        # This is a recipe to a the screen.
        # Modify the following variables as necessary.
        aBitmap = wx.Image(name = "SplashScreen.png").ConvertToBitmap()
        splashStyle = wx.SPLASH_CENTRE_ON_SCREEN | wx.SPLASH_TIMEOUT
        splashDuration = 3000 # milliseconds
        splashCallback = None
        # Call the constructor with the above arguments in exactly the
        # following order.
        wx.SplashScreen.__init__(self, aBitmap, splashStyle,
                                 splashDuration, splashCallback)
        self.Bind(wx.EVT_CLOSE, self.OnExit)
        wx.Yield()

    def OnLoading(self):
        pass

    def OnExit(self, evt):
        self.Hide()
        evt.Skip()

if __name__ == '__main__':
    pass
    