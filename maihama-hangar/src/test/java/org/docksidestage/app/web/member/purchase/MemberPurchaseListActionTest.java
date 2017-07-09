package org.docksidestage.app.web.member.purchase;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.exception.EntityAlreadyDeletedException;
import org.dbflute.optional.OptionalThing;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.docksidestage.app.web.base.paging.SearchPagingResult;
import org.docksidestage.dbflute.exbhv.PurchaseBhv;
import org.docksidestage.unit.UnitHangarTestCase;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author black-trooper
 */
public class MemberPurchaseListActionTest extends UnitHangarTestCase {

    @Resource
    PurchaseBhv purchaseBhv;

    // ===================================================================================
    //                                                                             index()
    //                                                                             =======
    public void test_index() {
        // ## Arrange ##
        MemberPurchaseListAction action = new MemberPurchaseListAction();
        inject(action);
        Integer memberId = 1;

        // ## Act ##
        JsonResponse<SearchPagingResult<MemberPurchaseSearchRowResult>> response = action.index(memberId, OptionalThing.of(1));

        // ## Assert ##
        showJson(response);
        TestingJsonData<SearchPagingResult<MemberPurchaseSearchRowResult>> data = validateJsonData(response);
        List<MemberPurchaseSearchRowResult> rows = data.getJsonResult().rows;
        assertHasAnyElement(rows);

        List<Long> purchaseIdList = rows.stream().map(bean -> bean.purchaseId).collect(Collectors.toList());
        Set<Integer> memberIds = purchaseBhv.selectList(cb -> {
            cb.query().setPurchaseId_InScope(purchaseIdList);
        }).stream().map(entity -> entity.getMemberId()).collect(Collectors.toSet());
        assertEquals(memberIds.size(), 1);
        assertTrue(memberIds.contains(memberId));
    }

    public void test_index_not_found() {
        // ## Arrange ##
        MemberPurchaseListAction action = new MemberPurchaseListAction();
        inject(action);
        Integer memberId = -1;

        // ## Act ##
        JsonResponse<SearchPagingResult<MemberPurchaseSearchRowResult>> response = action.index(memberId, OptionalThing.of(1));

        // ## Assert ##
        showJson(response);
        TestingJsonData<SearchPagingResult<MemberPurchaseSearchRowResult>> data = validateJsonData(response);
        List<MemberPurchaseSearchRowResult> rows = data.getJsonResult().rows;
        assertHasZeroElement(rows);
    }

    // ===================================================================================
    //                                                                            delete()
    //                                                                            ========
    public void test_delete() {
        // ## Arrange ##
        MemberPurchaseListAction action = new MemberPurchaseListAction();
        inject(action);
        long purchaseId = 67L;

        // ## Act ##
        action.delete(purchaseId);

        // ## Assert ##
        assertFalse(purchaseBhv.selectByPK(purchaseId).isPresent());
    }

    public void test_delete_not_found() {
        // ## Arrange ##
        MemberPurchaseListAction action = new MemberPurchaseListAction();
        inject(action);
        long purchaseId = -1L;

        // ## Act ##
        // ## Assert ##
        assertException(EntityAlreadyDeletedException.class, () -> {
            action.delete(purchaseId);
        });
    }

    public void test_delete_dupulicate() {
        // ## Arrange ##
        MemberPurchaseListAction action = new MemberPurchaseListAction();
        inject(action);
        long purchaseId = 67L;

        // ## Act ##
        action.delete(purchaseId);

        // ## Assert ##
        assertException(EntityAlreadyDeletedException.class, () -> {
            action.delete(purchaseId);
        });
    }
}
