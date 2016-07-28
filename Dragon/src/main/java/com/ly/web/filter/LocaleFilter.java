package com.ly.web.filter;

import java.io.IOException;

import java.util.Locale;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;

import org.springframework.context.i18n.LocaleContextHolder;

import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Filter to wrap request with a request including user preferred locale.
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
public class LocaleFilter extends OncePerRequestFilter {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String PREFERRED_LOCALE_KEY = "org.apache.struts2.action.LOCALE";

  /** The name of the Administrator role, as specified in web.xml. */
  private static final String ADMIN_ROLE = "ROLE_ADMIN";

  /** The name of the User role, as specified in web.xml. */
  private static final String USER_ROLE = "ROLE_USER";

  /** The name of the CSS Theme setting. */
  private static final String CSS_THEME = "csstheme";

  /** The name of the configuration hashmap stored in application scope. */
  private static final String CONFIG = "appConfig";

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * This method looks for a "locale" request parameter. If it finds one, it sets it as the preferred locale and also
   * configures it to work with JSTL.
   *
   * @param   request   the current request
   * @param   response  the current response
   * @param   chain     the chain
   *
   * @throws  IOException       when something goes wrong
   * @throws  ServletException  when a communication failure happens
   */
  @Override
  @SuppressWarnings("unchecked")
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws IOException, ServletException {
    String locale          = request.getParameter("locale");
    Locale preferredLocale = null;

    if (locale != null) {
      int indexOfUnderscore = locale.indexOf('_');

      if (indexOfUnderscore != -1) {
        String language = locale.substring(0, indexOfUnderscore);
        String country  = locale.substring(indexOfUnderscore + 1);
        preferredLocale = new Locale(language, country);
      } // end if
      else {
        preferredLocale = new Locale(locale);
      } // end else
    }   // end if

    HttpSession session = request.getSession(false);

    if (session != null) {
      if (preferredLocale == null) {
        preferredLocale = (Locale) session.getAttribute(
            PREFERRED_LOCALE_KEY);
      } // end if
      else {
        session.setAttribute(PREFERRED_LOCALE_KEY,
          preferredLocale);
        Config.set(session, Config.FMT_LOCALE, preferredLocale);
      } // end else

      if ((preferredLocale != null)
            && !(request instanceof LocaleRequestWrapper)) {
        request = new LocaleRequestWrapper(request, preferredLocale);
        LocaleContextHolder.setLocale(preferredLocale);
      } // end if
    }   // end if

    String theme = request.getParameter("theme");

    if ((theme != null) && request.isUserInRole(ADMIN_ROLE)) {
      Map<String, Object> config = (Map) getServletContext().getAttribute(
          CONFIG);
      config.put(CSS_THEME, theme);
    } // end if

    chain.doFilter(request, response);

    // Reset thread-bound LocaleContext.
    LocaleContextHolder.setLocaleContext(null);
  } // end method doFilterInternal
} // end LocaleFilter
