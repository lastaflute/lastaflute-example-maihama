package org.docksidestage.app.web.purchase;

import java.util.List;

import org.dbflute.cbean.result.PagingResultBean;
import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.web.base.DocksideBaseAction;
import org.docksidestage.app.web.base.paging.PagingAssist;
import org.docksidestage.dbflute.exbhv.PurchaseBhv;
import org.docksidestage.dbflute.exentity.Purchase;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.HtmlResponse;

import jakarta.annotation.Resource;

/**
 * Action class for purchase list page.
 * @author toshiaki.arai
 * @author jflute
 */
public class PurchaseListAction extends DocksideBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private PurchaseBhv purchaseBhv;
    @Resource
    private PagingAssist pagingAssist;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public HtmlResponse index(OptionalThing<Integer> pageNumber, PurchaseSearchForm form) {
        // #for_now toshiaki.arai anyway, small implementation (2015/07/05)
        Integer memberId = getUserBean().get().getMemberId();
        PagingResultBean<Purchase> page = selectPurchasePage(pageNumber.orElse(1), memberId);
        List<PurchaseBean> beans = page.mappingList(purchase -> {
            return mappingToBean(purchase);
        });
        return asHtml(path_Purchase_PurchaseListHtml).renderWith(data -> {
            data.register("beans", beans);
            pagingAssist.registerPagingNavi(data, page, form);
        });
    }

    // ===================================================================================
    //                                                                              Select 
    //                                                                              ======
    private PagingResultBean<Purchase> selectPurchasePage(int pageNumber, Integer memberId) {
        return purchaseBhv.selectPage(cb -> {
            cb.setupSelect_Product();
            cb.query().setMemberId_Equal(memberId);
            cb.query().addOrderBy_PurchaseDatetime_Desc();
            cb.paging(4, pageNumber);
        });
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private PurchaseBean mappingToBean(Purchase purchase) {
        PurchaseBean bean = new PurchaseBean();
        bean.purchaseId = purchase.getPurchaseId();
        bean.productId = purchase.getProductId();
        bean.purchaseDatetime = purchase.getPurchaseDatetime();
        bean.purchaseCount = purchase.getPurchaseCount();
        bean.purchasePrice = purchase.getPurchasePrice();
        purchase.getProduct().alwaysPresent(product -> {
            bean.productName = product.getProductName();
        });
        return bean;
    }
}
