package org.docksidestage.app.web.wx.remogen;

import java.util.List;

import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.app.web.wx.remogen.bean.simple.SuperSimpleBody;
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jflute
 */
@AllowAnyoneAccess
public class WxRemogenBodyAction extends ShowbaseBaseAction {

    private static final Logger logger = LoggerFactory.getLogger(WxRemogenBodyAction.class);

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<Void> basic(SuperSimpleBody body) {
        logger.debug("body: {}" + body);
        return JsonResponse.asEmptyBody();
    }

    @Execute
    public JsonResponse<Void> list(List<SuperSimpleBody> bodyList) {
        logger.debug("bodyList: {}" + bodyList);
        return JsonResponse.asEmptyBody();
    }
}
