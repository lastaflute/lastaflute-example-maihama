package org.docksidestage.app.web.wx.remogen;

import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.app.web.wx.remogen.bean.recycle.KeyValueResult;
import org.docksidestage.app.web.wx.remogen.bean.recycle.RecycleParadeResult;
import org.docksidestage.app.web.wx.remogen.bean.selfref.SelfReferenceResult;
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
    public JsonResponse<RecycleParadeResult> recycle() {
        return asJson(new RecycleParadeResult("maihama"));
    }

    @Execute
    public JsonResponse<SelfReferenceResult> selfref() {
        return asJson(new SelfReferenceResult("maihama"));
    }
}
