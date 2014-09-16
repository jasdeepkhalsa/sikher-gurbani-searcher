# -*- coding: utf8 -*-
# $Header: /cvsroot/sikher/SIKHER/sikher_boxes.py,v 1.3 2005/09/27 21:03:58 mamonts Exp $

# Module:  sikher_boxes.py

# Purpose: predefined dialog boxes

# Created: Alex Marmuzevich 2005-06-17

from wxPython.wx import *
from wxPython.html import *

try:
    import psyco
    psyco.profile()
except:
    print 'Psyco not found, ignoring it'

def kbWarningBox(commander, msg):
    info_dlg = wxMessageDialog(commander, msg, "Sikher", wxOK|wxICON_WARNING)
    info_dlg.CentreOnParent()
    info_dlg.ShowModal()

def AMBQ(commander, msg):
    info_dlg = wxMessageDialog(commander, msg, "Sikher", wxYES_NO|wxICON_QUESTION)
    info_dlg.CentreOnParent()
    return info_dlg.ShowModal()

def AMBQX(commander, msg):
    info_dlg = wxMessageDialog(commander, msg, "Sikher", wxYES_NO|wxCANCEL|wxICON_QUESTION)
    info_dlg.CentreOnParent()
    return info_dlg.ShowModal()

def kbInformationBox(commander, msg):
    info_dlg = wxMessageDialog(commander, msg, "Sikher", wxOK|wxICON_INFORMATION)
    info_dlg.CentreOnParent()
    info_dlg.ShowModal()

if __name__ == '__main__':
    pass
    