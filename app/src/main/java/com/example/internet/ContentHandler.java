package com.example.internet;

import android.util.Log;
import android.view.View;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ContentHandler extends DefaultHandler  {
    private  StringBuffer res;
    private  String nodeName;

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startDocument() throws SAXException {
        res=new StringBuffer();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        nodeName=localName;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
       if("app".equals(localName)){
           Log.d("test",res.toString());
           res.setLength(0);
       }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if("id".equals(nodeName)||"name".equals(nodeName)||"version".equals(nodeName)){

            res.append(new String(ch,start,length).trim()+"   ");

        }
    }
}
