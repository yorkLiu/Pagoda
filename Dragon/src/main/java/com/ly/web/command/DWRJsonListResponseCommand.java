package com.ly.web.command;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;
import org.directwebremoting.convert.ObjectConverter;

import java.util.Collection;
import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
@DataTransferObject(converter = ObjectConverter.class)
public class DWRJsonListResponseCommand<T> {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** DOCUMENT ME! */
  @RemoteProperty
  protected Collection<T> data;

  /** DOCUMENT ME! */
  @RemoteProperty
  protected String message;

  /** DOCUMENT ME! */
  @RemoteProperty
  protected boolean success;

  /** DOCUMENT ME! */
  @RemoteProperty
  protected int total;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new DWRJsonListResponseCommand object.
   *
   * @param  data     DOCUMENT ME!
   * @param  success  DOCUMENT ME!
   * @param  message  DOCUMENT ME!
   */
  public DWRJsonListResponseCommand(Collection<T> data, boolean success, String message) {
    this.data = data;
    this.success = success;
    this.message = message;

    if (data != null) {
      this.total = data.size();
    }
    else {
      this.total = 0;
    }
  }

  /**
   * Creates a new DWRJsonListResponseCommand object.
   *
   * @param  data     DOCUMENT ME!
   * @param  success  DOCUMENT ME!
   * @param  message  DOCUMENT ME!
   * @param  total    DOCUMENT ME!
   */
  public DWRJsonListResponseCommand(List<T> data, boolean success, String message, int total) {
    this.data = data;
    this.success = success;
    this.message = message;
    this.total = total;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public Collection<T> getData() {
    return data;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getMessage() {
    return message;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public int getTotal() {
    return total;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public boolean isSuccess() {
    return success;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  data  DOCUMENT ME!
   */
  public void setData(Collection<T> data) {
    this.data = data;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  message  DOCUMENT ME!
   */
  public void setMessage(String message) {
    this.message = message;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  success  DOCUMENT ME!
   */
  public void setSuccess(boolean success) {
    this.success = success;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  total  DOCUMENT ME!
   */
  public void setTotal(int total) {
    this.total = total;
  }

} // end class DWRJsonListResponseCommand
