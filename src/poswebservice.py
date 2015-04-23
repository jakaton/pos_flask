#!/usr/bin/env python
# -*- coding: utf-8
# ----------------------------------------------------------------------
# Flask web service for POS taggin
# ----------------------------------------------------------------------
# Ivan Vladimir Meza-Ruiz/ ivanvladimir at turing.iimas.unam.mx
# 2015/IIMAS/UNAM
# ----------------------------------------------------------------------
from __future__ import print_function

from flask import Flask, request
from tempfile import NamedTemporaryFile
from os import remove
from subprocess import Popen, PIPE
import codecs
import json


app = Flask('poswebservice')

languages={
    'es':{
     'cmd':'java -classpath lib/*:src SpanishTagger {0}'},
    'en':{
     'cmd':'java -classpath lib/*:src EnglishTagger {0}'},
}



@app.route('/pos/api/v1.0/languages',methods=['GET'])
def get_languages():
    res=[lan['lang'] for lan in languages]
    return json.dumps({"languages":res}) 

@app.route('/pos/api/v1.0/tag/<string:sntc>',defaults={'lang':'es'},methods=['GET'] )
@app.route('/pos/api/v1.0/tag/<string:lang>/<string:sntc>',methods=['GET'] )
def tag(lang,sntc):
    pos=languages[lang]
    pos_=[]
    try:
        temp=NamedTemporaryFile(delete=False)
        temp.close()
        file = codecs.open(temp.name, "w", "utf-8")
        file.write(sntc)
        file.close()

        cmd=pos['cmd'].format(temp.name).split()
        p = Popen(cmd, stdin=None, stdout=PIPE, stderr=None)
        output,err = p.communicate()
        for line in output.decode('utf-8').split(u'\n'):
            line=line.strip()
            if len(line)==0:
                continue
            line_=line.split()
            pos_.append((line_[0],line_[1]))
    finally:
        remove(temp.name)
    return json.dumps({"POS":pos_},ensure_ascii=False)

@app.route('/pos/api/v1.0/tag/<string:lang>',defaults={'lang':'es'},methods=['POST'] )
@app.route('/pos/api/v1.0/tag/<string:lang>',methods=['POST'] )
def tag_post(lang):
    pos=languages[lang]
    pos_=[]
    try:
        text=request.data
        temp=NamedTemporaryFile(delete=False)
        temp.close()
        file = codecs.open(temp.name, "w", "utf-8")
        file.write(text.decode('utf-8'))
        file.close()

        cmd=pos['cmd'].format(temp.name).split()
        p = Popen(cmd, stdin=None, stdout=PIPE, stderr=None)
        output,err = p.communicate()
        for line in output.decode('utf-8').split(u'\n'):
            line=line.strip()
            if len(line)==0:
                continue
            line_=line.split()
            pos_.append((line_[0],line_[1]))
    finally:
        remove(temp.name)
    return json.dumps({"POS":pos_},ensure_ascii=False) 

if __name__ == '__main__':
    app.run(debug="True")

