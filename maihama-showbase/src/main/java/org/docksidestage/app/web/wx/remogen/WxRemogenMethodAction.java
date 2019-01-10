package org.docksidestage.app.web.wx.remogen;

import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.app.web.wx.remogen.bean.simple.SuperSimpleBody;
import org.docksidestage.app.web.wx.remogen.bean.simple.SuperSimpleForm;
import org.docksidestage.app.web.wx.remogen.bean.simple.SuperSimpleResult;
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jflute
 */
@AllowAnyoneAccess
public class WxRemogenMethodAction extends ShowbaseBaseAction {

    private static final Logger logger = LoggerFactory.getLogger(WxRemogenMethodAction.class);

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // GET http://localhost:8098/showbase/wx/remogen/method?sea=mystic&land=7
    @Execute
    public JsonResponse<SuperSimpleResult> get$index(SuperSimpleForm form) {
        logger.debug("form: {}" + form);
        return asJson(new SuperSimpleResult(form.sea, form.land));
    }

    // POST http://localhost:8098/showbase/wx/remogen/method
    @Execute
    public JsonResponse<SuperSimpleResult> post$index(SuperSimpleBody body) {
        logger.debug("body: {}" + body);
        return asJson(new SuperSimpleResult(body.sea, body.land));
    }

    // DELETE http://localhost:8098/showbase/wx/remogen/method
    @Execute
    public JsonResponse<SuperSimpleResult> delete$index(SuperSimpleForm form) {
        logger.debug("form: {}" + form);
        return asJson(new SuperSimpleResult(form.sea, form.land));
    }

    // DELETE http://localhost:8098/showbase/wx/remogen/method/enclosing
    @Execute
    public JsonResponse<SuperSimpleResult> delete$enclosing(SuperSimpleBody body) {
        logger.debug("body: {}" + body);
        return asJson(new SuperSimpleResult(body.sea, body.land));
    }

    // DELETE http://localhost:8098/showbase/wx/remogen/method/noquery
    @Execute
    public JsonResponse<SuperSimpleResult> delete$noquery() {
        return asJson(new SuperSimpleResult("no query", -1));
    }
}
