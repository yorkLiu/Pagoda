package com.ly.test.service;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * DOCUMENT ME!
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
@ContextConfiguration(locations={
        "/applicationContext-resources.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class BaseManagerTestCase extends AbstractTransactionalJUnit4SpringContextTests {

        protected ResourceBundle rb;

        //~ Constructors -----------------------------------------------------------------------------------------------------

        /**
         * Default constructor - populates "rb" variable if properties file exists for the class in src/test/resources.
         */
        public BaseManagerTestCase() {
                // Since a ResourceBundle is not required for each class, just
                // do a simple check to see if one exists
                String className = this.getClass().getName();

                try {
                        rb = ResourceBundle.getBundle(className);
                }
                catch (MissingResourceException mre) {
                        // log.warn("No resource bundle found for: " + className);
                }
        }

        /**
         * Utility method to populate a javabean-style object with values from a Properties file.
         *
         * @param   obj  the model object to populate
         *
         * @return  Object populated object
         *
         * @throws  Exception  if BeanUtils fails to copy properly
         */
        protected Object populate(Object obj) throws Exception {
                // loop through all the beans methods and set its properties from its .properties file
                Map<String, String> map = new HashMap<String, String>();

                for (Enumeration<String> keys = rb.getKeys(); keys.hasMoreElements();) {
                        String key = keys.nextElement();
                        map.put(key, rb.getString(key));
                }

                BeanUtils.populate(obj, map);
                return obj;
        }
} // end class BaseManagerTestCase
