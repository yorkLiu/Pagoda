package com.ly.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yongliu on 12/2/16.
 */


@XStreamAlias("ns1:BOIUniversalInput")
public class BI {
  
  @XStreamAsAttribute
  @XStreamAlias("xsi:schemaLocation")
  private String schemaLocation;

  @XStreamAsAttribute
  @XStreamAlias("xmlns:xsi")
  private String xmlnsXsi;

  @XStreamAsAttribute
  @XStreamAlias("xmlns:ns1")
  private String xmlnsNs1;
  
  
  @XStreamAlias("Header")
  private Header header;

  //@XStreamImplicit(itemFieldName = "Accounts")
  @XStreamAlias("Accounts")
  private List<Account> accounts = new ArrayList<>();


  public BI() {
    this.schemaLocation="http://vedaxml.com/vxml2/boi-universal-input-v1-0.xsd ../boi-universal-input-v1-0-0.xsd";
    this.xmlnsXsi="http://www.w3.org/2001/XMLSchema-instance";
    this.xmlnsNs1="http://vedaxml.com/vxml2/boi-universal-input-v1-0.xsd";
    
    
    header = new Header("20160805SB1", "DACC", "003", "CNTREF0001", "2016-08-04T12:00:00.000");

    Account account = new Account(1, "2016-08-03", "20160805SB1", "10");
    accounts.add(account);

  }
}
