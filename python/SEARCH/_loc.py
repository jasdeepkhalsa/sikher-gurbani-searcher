# -*- coding: utf8 -*-
import locale
import os
os.system ( "chcp 1251 > /NULL" ) #�������������� ����� ������
v = "������"
print v
print v.lower()
locale.setlocale( locale.LC_ALL, "ru" )
print v.lower ()