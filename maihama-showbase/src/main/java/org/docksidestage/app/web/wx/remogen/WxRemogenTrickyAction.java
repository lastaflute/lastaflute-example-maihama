package org.docksidestage.app.web.wx.remogen;

import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.app.web.wx.remogen.bean.KeyValueResult;
import org.docksidestage.app.web.wx.remogen.bean.SameObjectResult;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 */
public class WxRemogenTrickyAction extends ShowbaseBaseAction {

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<KeyValueResult> nobody() {
        return asJson(new KeyValueResult("sea", "mystic"));
    }

    @Execute
    public JsonResponse<SameObjectResult> sameobj() {
        return asJson(new SameObjectResult("maihama"));
    }
}
