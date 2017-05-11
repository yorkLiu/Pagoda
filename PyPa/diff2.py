import difflib
import sys

before = """
h4. User story title: FlexSite - Configuration FlexSite - "Logout" button  Action

*As a* _FlexSite user_,
*I want* _to be able to configure the action for the *Logout* to *close the current browser window*_
*so that I can* _force a FlexSite customer to *close* the browser window automatically_.
 
h4. Acceptance Criteria:
# This should be an addition to CA-5426 where the Menu and Footer of FlexSite can be hidden.
# Ability to configure in the back end - When the "Logout" (close for MOM) button is clicked, it should *terminate* the current user session and *close* the current browser window/tab. This configuration will  be added to the current existing two configurations where the Logout can take a Customer to CA FlexSite login page or a client specific URL.
# When the current user session has timed-out, a message should display a message to inform the user to close the current browser/tab.
# In addition to the acceptance criteria listed above all CA tickets must also meet *all* of the acceptance criteria in the [Standard Product Management Acceptance 
"""

after = """
h4. User story title: FlexSite - Configuration FlexSite - "Logout" button  Action

*As a* _FlexSite user_,
*I want* _to be able to configure the action for the *Logout* to *close the current browser window*_
*so that I can* _force a FlexSite customer to *close* the browser window automatically_.
 
h4. Acceptance Criteria:
# This should be an addition to CA-5426 where the Menu and Footer of FlexSite can be hidden.
# Ability to configure in the back end - When the "Logout" (close for MOM) button is clicked, it should *terminate* the current user session and *close* the current browser window/tab. This configuration will  be added to the current existing two configurations where the Logout can take a Customer to CA FlexSite login page or a client specific URL.
# When the current user session has timed-out, a message should display a message to inform the user to close the current browser/tab.
# In addition to the acceptance criteria listed above all CA tickets must also meet *all* of the acceptance criteria in the [Standard Product Management Acceptance 
"""


issuekey = 'CA-4414'

d = difflib.unified_diff(before.splitlines(), after.splitlines(),
                         'Original',
                         u'{0} Description (modified)'.format(issuekey), n=1, lineterm=u'\n')
# sys.stdout.write(u'\n'.join(list(d))+'\n')
print list(d).__len__()
# diff = (u'\n'.join(list(d))+'\n')
# print (u'\n'.join(list(d))+'\n')