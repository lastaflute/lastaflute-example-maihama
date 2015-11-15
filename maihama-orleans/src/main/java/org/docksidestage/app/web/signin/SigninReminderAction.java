package org.docksidestage.app.web.signin;

import org.docksidestage.app.web.base.OrleansBaseAction;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.HtmlResponse;

/**
 * @author masaki.kamachi
 * @author jflute
 */
public class SigninReminderAction extends OrleansBaseAction {

    @Execute
    public HtmlResponse index() {
        return asHtml(path_Signin_SigninReminderHtml);
    }
}
