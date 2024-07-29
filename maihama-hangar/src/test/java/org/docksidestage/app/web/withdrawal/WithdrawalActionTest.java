package org.docksidestage.app.web.withdrawal;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.docksidestage.dbflute.allcommon.CDef;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exbhv.MemberWithdrawalBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.dbflute.exentity.MemberWithdrawal;
import org.docksidestage.unit.UnitHangarTestCase;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.validation.exception.ValidationErrorException;

import jakarta.annotation.Resource;

public class WithdrawalActionTest extends UnitHangarTestCase {

    @Resource
    private MemberBhv memberBhv;
    @Resource
    private MemberWithdrawalBhv memberWithdrawalBhv;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mockLogin();
    }

    public void test_reason() {
        // ## Arrange ##
        WithdrawalAction action = new WithdrawalAction();
        inject(action);

        // ## Act ##
        JsonResponse<List<SimpleEntry<String, String>>> response = action.reason();

        // ## Assert ##
        showJson(response);
        TestingJsonData<List<SimpleEntry<String, String>>> data = validateJsonData(response);
        List<SimpleEntry<String, String>> rows = data.getJsonResult();
        assertHasAnyElement(rows);
        assertEquals(rows.size(), CDef.WithdrawalReason.listAll().size());
        rows.forEach(entry -> {
            assertTrue(CDef.WithdrawalReason.of(entry.getKey()).isPresent());
        });
    }

    public void test_withdrawal_success() {
        // ## Arrange ##
        WithdrawalAction action = new WithdrawalAction();
        inject(action);
        WithdrawalBody body = new WithdrawalBody();
        body.selectedReason = CDef.WithdrawalReason.Frt;

        // ## Act ##
        JsonResponse<Integer> response = action.done(body);

        // ## Assert ##
        showJson(response);
        TestingJsonData<Integer> data = validateJsonData(response);
        Integer memberId = data.getJsonResult();
        Member member = memberBhv.selectByPK(memberId).get();
        MemberWithdrawal withdrawal = memberWithdrawalBhv.selectByPK(memberId).get();
        assertEquals(member.getMemberStatusCodeAsMemberStatus(), CDef.MemberStatus.Withdrawal);
        assertEquals(withdrawal.getWithdrawalReasonCodeAsWithdrawalReason(), body.selectedReason);
    }

    public void test_withdrawal_request_validate_required() {
        // ## Arrange ##
        WithdrawalAction action = new WithdrawalAction();
        inject(action);
        WithdrawalBody body = new WithdrawalBody();

        // ## Act ##
        // ## Assert ##
        assertException(ValidationErrorException.class, () -> {
            action.done(body);
        });
    }

    public void test_withdrawal_request_validate_maxlength() {
        // ## Arrange ##
        WithdrawalAction action = new WithdrawalAction();
        inject(action);
        WithdrawalBody body = new WithdrawalBody();
        body.reasonInput = StringUtils.repeat("*", 30);

        // ## Act ##
        action.done(body);

        // ## Assert ##
        assertException(ValidationErrorException.class, () -> {
            body.reasonInput = StringUtils.repeat("*", 30 + 1);
            action.done(body);
        });
    }
}
