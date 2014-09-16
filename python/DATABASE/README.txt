$Header: /cvsroot/sikher/SIKHER/DATABASE/README.txt,v 1.2 2005/08/29 23:14:39 mamonts Exp $

PURPOSE:

We are developing a custom file format for efficient storage, search and retrieval of Gurbani text. This can take many forms:

* Original text in Gurmukhi

* English, French etc transliteration

* English, French etc translation

This directory will contain the software needed to process the custom file format outlined above. 

DESIGN:

The data files themselves may need to be stored in compressed form to save disk space and ensure fast download times.

The Python zlib module is an obvious candidate for this approach.

HISTORY:

Created by kb498, 22 Sept 2004 as part of the Sikher project
