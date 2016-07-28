package com.ly.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;


/**
 * HttpRequestWrapper overriding methods getLocale(), getLocales() to include the user's preferred locale.
 *
 * @author $author$
 * @version $Revision$, $Date$
 */
public class LocaleRequestWrapper extends HttpServletRequestWrapper {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private final transient Logger logger = LoggerFactory.getLogger(getClass());

  private final Locale preferredLocale;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Sets preferred local to user's locale.
   *
   * @param decorated  the current decorated request
   * @param userLocale the user's locale
   */
  public LocaleRequestWrapper(final HttpServletRequest decorated, final Locale userLocale) {
    super(decorated);
    preferredLocale = userLocale;

    if (null == preferredLocale) {
      logger.error("preferred locale = null, it is an unexpected value!");
    } // end if
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override
  public Locale getLocale() {
    if (null != preferredLocale) {
      return preferredLocale;
    } // end if
    else {
      return super.getLocale();
    } // end else
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public Enumeration<Locale> getLocales() {
    if (null != preferredLocale) {
      List<Locale> l = Collections.list(super.getLocales());

      if (l.contains(preferredLocale)) {
        l.remove(preferredLocale);
      } // end if

      l.add(0, preferredLocale);

      return Collections.enumeration(l);
    } // end if
    else {
      return super.getLocales();
    } // end else
  }
} // end LocaleRequestWrapper
