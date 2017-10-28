package org.docksidestage.app.web.wx.remogen;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.stream.Collectors;

import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.app.web.wx.remogen.bean.recycle.KeyValueResult;
import org.docksidestage.app.web.wx.remogen.bean.recycle.ValueGenericsResult;
import org.docksidestage.app.web.wx.remogen.bean.simple.SuperSimpleResult;
import org.docksidestage.dbflute.allcommon.CDef;
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 */
@AllowAnyoneAccess
public class WxRemogenListAction extends ShowbaseBaseAction {

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<List<KeyValueResult>> basic() {
        List<KeyValueResult> reasonList = CDef.WithdrawalReason.listAll().stream().map(reason -> {
            return new KeyValueResult(reason.code(), reason.alias());
        }).collect(Collectors.toList());
        return asJson(reasonList);
    }

    @Execute
    public JsonResponse<List<ValueGenericsResult<SuperSimpleResult>>> genebean() {
        List<ValueGenericsResult<SuperSimpleResult>> reasonList = CDef.WithdrawalReason.listAll().stream().map(reason -> {
            return new ValueGenericsResult<SuperSimpleResult>(reason.code(), new SuperSimpleResult(reason.alias(), reason.hashCode()));
        }).collect(Collectors.toList());
        return asJson(reasonList);
    }

    @Execute
    public JsonResponse<List<ValueGenericsResult<String>>> genestring() {
        List<ValueGenericsResult<String>> reasonList = CDef.WithdrawalReason.listAll().stream().map(reason -> {
            return new ValueGenericsResult<String>(reason.code(), reason.alias());
        }).collect(Collectors.toList());
        return asJson(reasonList);
    }

    @Execute
    public JsonResponse<List<SimpleEntry<String, String>>> innergene() {
        List<SimpleEntry<String, String>> reasonList = CDef.WithdrawalReason.listAll().stream().map(reason -> {
            return new SimpleEntry<>(reason.code(), reason.alias());
        }).collect(Collectors.toList());
        return asJson(reasonList);
    }
}
