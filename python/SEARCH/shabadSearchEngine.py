# -*- coding: utf8 -*-
# $Header: /cvsroot/sikher/SIKHER/SEARCH/shabadSearchEngine.py,v 1.4 2005/09/24 18:43:10 mamonts Exp $
# Module:  shabadSearchEngine.py
# Purpose: allow fast searching of gurbani via gurmukhi or transliteration
# Created: Kulwant S. Bhatia, Sept 22 2004

#import sys

class shabadSearchEngine:
    def __init__(self, database="../DATABASE"):
        self.database = database
    def searchTransliteration(self, s):
        pass
    def searchGurmukhi(self, s):
        pass
    def runTests(self):
        print "***"
        print "testing shabad search engine"
        self.searchGurmukhi("test")
        self.searchTransliteration("test")
        print "finished tests"
        print "***"
    
if __name__ == "__main__":
    s = shabadSearchEngine()
    s.runTests()

