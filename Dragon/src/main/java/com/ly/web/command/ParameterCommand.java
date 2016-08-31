package com.ly.web.command;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;
import org.directwebremoting.annotations.RemoteProxy;

import org.directwebremoting.convert.ObjectConverter;


/**
 * Created by yongliu on 8/31/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/31/2016 14:31
 */
@DataTransferObject(converter = ObjectConverter.class)
public class ParameterCommand {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** Search by these fields only. */
  @RemoteProperty protected String[] fields;

  /** Limit Search results. */
  @RemoteProperty protected Integer limit;

  /** Search query string. */
  @RemoteProperty protected String query;

  /** Search start position. */
  @RemoteProperty protected Integer start;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new ParameterCommand object.
   */
  public ParameterCommand() { }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * getter method for fields.
   *
   * @return  String[]
   */
  public String[] getFields() {
    return fields;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for limit.
   *
   * @return  Integer
   */
  public Integer getLimit() {
    return limit;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for query.
   *
   * @return  String
   */
  public String getQuery() {
    return query;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * getter method for start.
   *
   * @return  Integer
   */
  public Integer getStart() {
    return start;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for fields.
   *
   * @param  fields  String[]
   */
  public void setFields(String[] fields) {
    this.fields = fields;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for limit.
   *
   * @param  limit  Integer
   */
  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for query.
   *
   * @param  query  String
   */
  public void setQuery(String query) {
    this.query = query;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * setter method for start.
   *
   * @param  start  Integer
   */
  public void setStart(Integer start) {
    this.start = start;
  }
} // end class ParameterCommand
