package com.ly.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by yongliu on 12/2/16.
 */

@XStreamAlias("Account")
public class Account {
  @XStreamAsAttribute
  private Integer recordId;
  @XStreamAsAttribute
  private String dateCorrected;
  @XStreamAsAttribute
  private String previousBatchId;
  @XStreamAsAttribute
  private String previousRecordId;

  public Account() {
  }

  public Account(Integer recordId, String dateCorrected, String previousBatchId, String previousRecordId) {
    this.recordId = recordId;
    this.dateCorrected = dateCorrected;
    this.previousBatchId = previousBatchId;
    this.previousRecordId = previousRecordId;
  }
}
