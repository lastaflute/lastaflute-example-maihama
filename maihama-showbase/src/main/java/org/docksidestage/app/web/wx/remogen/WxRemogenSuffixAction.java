package org.docksidestage.app.web.wx.remogen;

import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.app.web.wx.remogen.bean.suffix.AllSuffixResult;
import org.docksidestage.app.web.wx.remogen.bean.suffix.NoSuffixCompletely;
import org.docksidestage.app.web.wx.remogen.bean.suffix.PartOnlySuffix;
import org.docksidestage.app.web.wx.remogen.bean.suffix.TonOnlySuffixResult;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 */
public class WxRemogenSuffixAction extends ShowbaseBaseAction {

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<AllSuffixResult> allsuffix() {
        return asJson(new AllSuffixResult("maihama"));
    }

    @Execute
    public JsonResponse<NoSuffixCompletely> nosuffix() {
        return asJson(new NoSuffixCompletely("maihama"));
    }

    @Execute
    public JsonResponse<PartOnlySuffix> partonly() {
        return asJson(new PartOnlySuffix("maihama"));
    }

    @Execute
    public JsonResponse<TonOnlySuffixResult> toponly() {
        return asJson(new TonOnlySuffixResult("maihama"));
    }
}
