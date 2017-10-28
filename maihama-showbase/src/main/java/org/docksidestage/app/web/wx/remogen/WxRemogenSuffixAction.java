package org.docksidestage.app.web.wx.remogen;

import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.app.web.wx.remogen.bean.suffix.NoSuffixBean;
import org.docksidestage.app.web.wx.remogen.bean.suffix.NoSuffixResult;
import org.docksidestage.app.web.wx.remogen.bean.suffix.StandardSuffixBean;
import org.docksidestage.app.web.wx.remogen.bean.suffix.StandardSuffixResult;
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
    public JsonResponse<NoSuffixBean> nosuffix() {
        return asJson(new NoSuffixBean("maihama"));
    }

    @Execute
    public JsonResponse<NoSuffixResult> nosuffixres() {
        return asJson(new NoSuffixResult("maihama"));
    }

    @Execute
    public JsonResponse<StandardSuffixBean> stdsuffix() {
        return asJson(new StandardSuffixBean("maihama"));
    }

    @Execute
    public JsonResponse<StandardSuffixResult> stdsuffixres() {
        return asJson(new StandardSuffixResult("maihama"));
    }
}
