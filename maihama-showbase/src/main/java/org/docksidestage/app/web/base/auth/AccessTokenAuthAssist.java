package org.docksidestage.app.web.base.auth;

import org.lastaflute.web.ruts.process.ActionRuntime;

// if you don't use this, you can delete this class
/**
 * @author jflute
 */
public class AccessTokenAuthAssist {

    public void determineAuth(ActionRuntime runtime) {
        // if you need to auth your API, you can implement here
        //...requestManager.getHeader(headerKey);
        //throw new AccessTokenUnauthorizedException("Unauthorized request: " + runtime, UserMessages.xxx());
    }
}
