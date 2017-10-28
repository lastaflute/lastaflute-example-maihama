package org.docksidestage.app.web.wx.remogen;

import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.app.web.wx.remogen.bean.KeyValueResult;
import org.docksidestage.app.web.wx.remogen.bean.NoSuffixParade;
import org.docksidestage.app.web.wx.remogen.bean.RecycleParadeResult;
import org.docksidestage.app.web.wx.remogen.bean.SelfReferenceResult;
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
    public JsonResponse<NoSuffixParade> nosuffix() {
        return asJson(new NoSuffixParade("maihama"));
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
