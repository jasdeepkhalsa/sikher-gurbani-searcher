###################################################################################
# -*- coding: utf8 -*-
# $Header$
#
# Module:  indexer_xml
# Purpose: very special xml indexer
# Created: 25.09.2005

# $Log$
# Revision 1.1  2005/10/07 21:42:53  mamonts
# +simple test searcher. It works, but i have problem with text encoding (it is very strange problem. if we have text file in cp1251 encoding, and type russian text in search field (wxTextCtrl component) we can not found it or got error when try to call string function FIND.
# Just one idea: recognize text file encoding and perform search only with the encoding information. But it is not so good as i like
#
#
#

__revision__ = "$Revision$"
__version__ = "0.0.0.0"

##################################################
# import section
##################################################
try:
    import psyco
    psyco.profile()
except:
    print 'Psyco not found, ignoring it'

##################################################
# primary section
##################################################

##################################################
# testing
##################################################
import unittest
   
if __name__ == "__main__":
    unittest.main() 
