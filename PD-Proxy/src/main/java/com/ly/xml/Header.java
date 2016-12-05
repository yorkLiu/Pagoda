package com.ly.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by yongliu on 12/2/16.
 */

@XStreamAlias("Header")
public class Header {
  private String BatchId;
  private String SubscriberCode;
  private String BranchCode;
  private String ClientReferenceId;
  private String DatetimeGenerated;

  public Header() {
  }

  public Header(String BatchId, String SubscriberCode, String BranchCode, String ClientReferenceId, String DatetimeGenerated) {
    this.BatchId = BatchId;
    this.SubscriberCode = SubscriberCode;
    this.BranchCode = BranchCode;
    this.ClientReferenceId = ClientReferenceId;
    this.DatetimeGenerated = DatetimeGenerated;
  }
}
