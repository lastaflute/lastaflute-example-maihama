package org.docksidestage.app.web.withdrawal;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.stream.Collectors;

import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.dbflute.allcommon.CDef;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exbhv.MemberWithdrawalBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.dbflute.exentity.MemberWithdrawal;
import org.docksidestage.mylasta.action.HangarMessages;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.core.util.LaStringUtil;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

import jakarta.annotation.Resource;

/**
 * @author black-trooper
 */
public class WithdrawalAction extends HangarBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private TimeManager timeManager;
    @Resource
    private MemberBhv memberBhv;
    @Resource
    private MemberWithdrawalBhv memberWithdrawalBhv;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<List<SimpleEntry<String, String>>> reason() {
        List<SimpleEntry<String, String>> reasonList = CDef.WithdrawalReason.listAll().stream().map(m -> {
            return new SimpleEntry<>(m.code(), m.alias());
        }).collect(Collectors.toList());
        return asJson(reasonList);
    }

    @Execute
    public JsonResponse<Integer> done(WithdrawalBody body) {
        validate(body, messages -> moreValidation(body, messages));
        Integer memberId = getUserBean().get().getMemberId();
        insertWithdrawal(body, memberId);
        updateStatusWithdrawal(memberId);
        return asJson(memberId);
    }

    // ===================================================================================
    //                                                                              Update
    //                                                                              ======
    private void insertWithdrawal(WithdrawalBody form, Integer memberId) {
        MemberWithdrawal withdrawal = new MemberWithdrawal();
        withdrawal.setMemberId(memberId);
        withdrawal.setWithdrawalReasonCodeAsWithdrawalReason(form.selectedReason);
        withdrawal.setWithdrawalReasonInputText(form.reasonInput);
        withdrawal.setWithdrawalDatetime(timeManager.currentDateTime());
        memberWithdrawalBhv.insert(withdrawal);
    }

    private void updateStatusWithdrawal(Integer memberId) {
        Member member = new Member();
        member.setMemberId(memberId);
        member.setMemberStatusCode_Withdrawal();
        memberBhv.updateNonstrict(member);
    }

    // ===================================================================================
    //                                                                            Validate
    //                                                                            ========
    private void moreValidation(WithdrawalBody form, HangarMessages messages) {
        if (form.selectedReason == null && LaStringUtil.isEmpty(form.reasonInput)) {
            messages.addConstraintsRequiredMessage("selectedReason");
        }
    }
}
