package org.docksidestage.app.web.member;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.helper.HandyDate;
import org.dbflute.optional.OptionalThing;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.docksidestage.app.web.base.paging.SearchPagingResult;
import org.docksidestage.dbflute.allcommon.CDef;
import org.docksidestage.dbflute.exbhv.PurchaseBhv;
import org.docksidestage.unit.UnitHangarTestCase;
import org.lastaflute.web.exception.RequestIllegalTransitionException;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.validation.exception.ValidationErrorException;

/**
 * @author black-trooper
 */
public class MemberListActionTest extends UnitHangarTestCase {

    @Resource
    private PurchaseBhv purchaseBhv;

    public void test_index_all_condition() {
        // ## Arrange ##
        MemberListAction action = new MemberListAction();
        inject(action);
        MemberSearchBody body = new MemberSearchBody();
        body.memberName = "s";
        body.purchaseProductName = "a";
        body.memberStatus = CDef.MemberStatus.Formalized;
        body.unpaid = true;
        body.formalizedFrom = new HandyDate("2006-09-04").getLocalDate();
        body.formalizedTo = new HandyDate("2006-09-04").getLocalDate();

        // ## Act ##
        JsonResponse<SearchPagingResult<MemberSearchRowResult>> response = action.index(OptionalThing.of(1), body);

        // ## Assert ##
        showJson(response);
        TestingJsonData<SearchPagingResult<MemberSearchRowResult>> data = validateJsonData(response);
        List<MemberSearchRowResult> rows = data.getJsonResult().rows;
        assertHasAnyElement(rows);
        rows.forEach(bean -> {
            assertContainsIgnoreCase(bean.memberName, body.memberName);
            assertEquals(bean.memberStatusName, body.memberStatus.alias());
            assertTrue(bean.formalizedDate.isAfter(body.formalizedFrom) || bean.formalizedDate.isEqual(body.formalizedFrom));
            assertTrue(bean.formalizedDate.isBefore(body.formalizedTo.plusDays(1)));
        });

        List<Integer> memberIdList = rows.stream().map(bean -> bean.memberId).collect(Collectors.toList());
        Set<Integer> unpaidMemberIds = purchaseBhv.selectList(cb -> {
            cb.specify().columnMemberId();
            cb.query().setMemberId_InScope(memberIdList);
            cb.query().setPaymentCompleteFlg_Equal_False();
        }).stream().map(bean -> bean.getMemberId()).collect(Collectors.toSet());
        memberIdList.forEach(memberId -> {
            assertTrue(unpaidMemberIds.contains(memberId));
        });

        Set<Integer> purchaseMemberIds = purchaseBhv.selectList(cb -> {
            cb.specify().columnMemberId();
            cb.query().setMemberId_InScope(memberIdList);
            cb.query().queryProduct().setProductName_LikeSearch(body.purchaseProductName, op -> op.likeContain());
        }).stream().map(bean -> bean.getMemberId()).collect(Collectors.toSet());
        memberIdList.forEach(memberId -> {
            assertTrue(purchaseMemberIds.contains(memberId));
        });
    }

    public void test_index_formalizedFrom() {
        // ## Arrange ##
        MemberListAction action = new MemberListAction();
        inject(action);
        MemberSearchBody body = new MemberSearchBody();
        body.formalizedFrom = new HandyDate("2006-09-04").getLocalDate();

        // ## Act ##
        JsonResponse<SearchPagingResult<MemberSearchRowResult>> response = action.index(OptionalThing.of(1), body);

        // ## Assert ##
        showJson(response);
        TestingJsonData<SearchPagingResult<MemberSearchRowResult>> data = validateJsonData(response);
        List<MemberSearchRowResult> rows = data.getJsonResult().rows;
        assertHasAnyElement(rows);
        rows.forEach(bean -> {
            assertTrue(bean.formalizedDate.isAfter(body.formalizedFrom) || bean.formalizedDate.isEqual(body.formalizedFrom));
        });
    }

    public void test_index_formalizedTo() {
        // ## Arrange ##
        MemberListAction action = new MemberListAction();
        inject(action);
        MemberSearchBody body = new MemberSearchBody();
        body.formalizedTo = new HandyDate("2006-09-04").getLocalDate();

        // ## Act ##
        JsonResponse<SearchPagingResult<MemberSearchRowResult>> response = action.index(OptionalThing.of(1), body);

        // ## Assert ##
        showJson(response);
        TestingJsonData<SearchPagingResult<MemberSearchRowResult>> data = validateJsonData(response);
        List<MemberSearchRowResult> rows = data.getJsonResult().rows;
        assertHasAnyElement(rows);
        rows.forEach(bean -> {
            assertTrue(bean.formalizedDate.isBefore(body.formalizedTo.plusDays(1)));
        });
    }

    public void test_index_all_data() {
        // ## Arrange ##
        MemberListAction action = new MemberListAction();
        inject(action);
        MemberSearchBody body = new MemberSearchBody();

        // ## Act ##
        JsonResponse<SearchPagingResult<MemberSearchRowResult>> response = action.index(OptionalThing.of(1), body);

        // ## Assert ##
        showJson(response);
        TestingJsonData<SearchPagingResult<MemberSearchRowResult>> data = validateJsonData(response);
        List<MemberSearchRowResult> rows = data.getJsonResult().rows;
        assertHasAnyElement(rows);
        rows.forEach(bean -> {});
    }

    public void test_index_notFound() {
        // ## Arrange ##
        MemberListAction action = new MemberListAction();
        inject(action);
        MemberSearchBody body = new MemberSearchBody();
        body.purchaseProductName = "not found";

        // ## Act ##
        JsonResponse<SearchPagingResult<MemberSearchRowResult>> response = action.index(OptionalThing.of(1), body);

        // ## Assert ##
        showJson(response);
        TestingJsonData<SearchPagingResult<MemberSearchRowResult>> data = validateJsonData(response);
        assertHasZeroElement(data.getJsonResult().rows);
    }

    public void test_index_validate_request() {
        // ## Arrange ##
        MemberListAction action = new MemberListAction();
        inject(action);
        MemberSearchBody body = new MemberSearchBody();
        body.memberName = "12345"; // max = 5
        body.purchaseProductName = "1234567890"; // max = 10

        // ## Act ##
        action.index(OptionalThing.of(1), body);
    }

    public void test_index_validate_request_error() {
        // ## Arrange ##
        MemberListAction action = new MemberListAction();
        inject(action);
        MemberSearchBody body = new MemberSearchBody();
        body.memberName = "123456"; // max = 5
        body.purchaseProductName = "12345678901"; // max = 10

        // ## Act ##
        // ## Assert ##
        assertException(ValidationErrorException.class, () -> {
            action.index(OptionalThing.of(1), body);
        });
    }

    public void test_index_bad_request() {
        // ## Arrange ##
        MemberListAction action = new MemberListAction();
        inject(action);
        MemberSearchBody body = new MemberSearchBody();

        // ## Act ##
        // ## Assert ##
        assertException(RequestIllegalTransitionException.class, () -> {
            action.index(OptionalThing.of(0), body);
        });
    }
}
