package org.docksidestage.app.web.wx.remogen;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.stream.Collectors;

import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.dbflute.allcommon.CDef;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 */
public class WxRemogenListAction extends ShowbaseBaseAction {

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<List<SimpleEntry<String, String>>> getter() {
        List<SimpleEntry<String, String>> reasonList = CDef.WithdrawalReason.listAll().stream().map(reason -> {
            return new SimpleEntry<>(reason.code(), reason.alias());
        }).collect(Collectors.toList());
        return asJson(reasonList);
    }
}
