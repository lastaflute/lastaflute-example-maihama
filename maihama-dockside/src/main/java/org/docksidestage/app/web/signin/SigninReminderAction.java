package org.docksidestage.app.web.signin;

import org.docksidestage.app.web.base.DocksideBaseAction;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.HtmlResponse;

/**
 * @author masaki.kamachi
 */
public class SigninReminderAction extends DocksideBaseAction {

    @Execute
    public HtmlResponse index() {
        return asHtml(path_Signin_SigninReminderJsp);
    }
}
