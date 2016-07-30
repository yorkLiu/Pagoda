package com.ly.web.view;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import org.springframework.web.util.UrlPathHelper;


/**
 * TODO: DOCUMENT ME!
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  07/26/2016 22:03
 */
public class ConstrainedUrlPathHelper extends UrlPathHelper {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String TRIGGER_404 = "NONEXISTING";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Set<String>              extensionToStrip           = new LinkedHashSet<String>();
  private Map<String, Set<String>> pathExtensionConstraint    = new HashMap<String, Set<String>>();
  private Set<String>              refuseUrlEndsWith          = new LinkedHashSet<String>();
  private boolean                  stripConstraintedExtension = true;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * This method looks at the pathExtensionConstraint (path, ext) and ensure the application only servers content for
   * those requests with matching path and extension. When path and extension match, this method will strip the
   * extension and return the result, otherwise a NONEXISTING view name is returned and triggers 404.
   *
   * <p>This class is injected into DefaultRequestToViewNameTranslator.</p>
   *
   * <p>Example: if pathExtensionConstraint = ("data", "js"), /data/someRes triggers 404 /data/someRes.abc triggers 404
   * /data/someRes.js serves correct content.</p>
   *
   * @param   request  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  @Override public String getLookupPathForRequest(HttpServletRequest request) {
    String result = super.getLookupPathForRequest(request);

    // perform validation
    String servletPath = super.getServletPath(request);
    String ext         = StringUtils.getFilenameExtension(result);


    for (String end : refuseUrlEndsWith) {
      if (result.endsWith(end)) {
        return TRIGGER_404;
      }
    }


    Set<String> keys = pathExtensionConstraint.keySet();

    for (String path : keys) {
      if (path.equals(servletPath)) {
        Set s = (Set) pathExtensionConstraint.get(path);

        if (!s.contains(ext)) {
          return TRIGGER_404;
        } else if (stripConstraintedExtension) {
          result = StringUtils.stripFilenameExtension(result);
        }
      }
    }

    // Only strip one extension - we will not strip extension recursively
    for (String e : extensionToStrip) {
      if (e.equals(ext)) {
        result = StringUtils.stripFilenameExtension(result);

        break;
      }
    }

    System.out.println("Result:" + result);

    return result;
  } // end method getLookupPathForRequest

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  extensionToStrip  DOCUMENT ME!
   */
  public void setExtensionToStrip(Set<String> extensionToStrip) {
    this.extensionToStrip = extensionToStrip;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  pathExtensionConstraint  DOCUMENT ME!
   */
  public void setPathExtensionConstraint(Map<String, Set<String>> pathExtensionConstraint) {
    this.pathExtensionConstraint = pathExtensionConstraint;

  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  refuseUrlEndsWith  DOCUMENT ME!
   */
  public void setRefuseUrlEndsWith(Set<String> refuseUrlEndsWith) {
    this.refuseUrlEndsWith = refuseUrlEndsWith;
  }
} // end class ConstrainedUrlPathHelper
