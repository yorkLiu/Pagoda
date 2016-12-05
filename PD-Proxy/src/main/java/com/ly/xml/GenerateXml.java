
package com.ly.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yongliu on 12/1/16.
 */

public class GenerateXml {


  public static void main(String[] args) {

    StringBuilder sb = new StringBuilder();
    sb.append("<ns1:BOIUniversalInput xsi:schemaLocation=\"http://vedaxml.com/vxml2/boi-universal-input-v1-0.xsd ../boi-universal-input-v1-0-0.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns1=\"http://vedaxml.com/vxml2/boi-universal-input-v1-0.xsd\">\n");


    XStream xStream = new XStream();
    xStream.autodetectAnnotations(true);

    BI bi = new BI();

    String xml = xStream.toXML(bi);


    System.out.println(xml);


  }


}
