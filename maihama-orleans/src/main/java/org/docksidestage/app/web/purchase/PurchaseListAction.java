package org.docksidestage.app.web.purchase;

import java.util.List;

import javax.annotation.Resource;

import org.dbflute.cbean.result.PagingResultBean;
import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.web.base.OrleansBaseAction;
import org.docksidestage.dbflute.exbhv.PurchaseBhv;
import org.docksidestage.dbflute.exentity.Purchase;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.HtmlResponse;

/**
 * Action class for purchase list page.
 * @author toshiaki.arai
 */
public class PurchaseListAction extends OrleansBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private PurchaseBhv purchaseBhv;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public HtmlResponse index(OptionalThing<Integer> pageNumber, PurchaseSearchForm form) {

        // TODO toshiaki.arai Purchaseアクションをとりあえず最低限記述 (2015/07/05)
        Integer memberId = getUserBean().get().getMemberId();
        PagingResultBean<Purchase> page = selectPurchasePage(pageNumber.orElse(1), memberId);
        List<PurchaseBean> beans = page.mappingList(purchase -> {
            return mappingToBean(purchase);
        });
        return asHtml(path_Purchase_PurchaseListJsp).renderWith(data -> {
            data.register("beans", beans);
            registerPagingNavi(data, page, form);
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
            cb.paging(getPagingPageSize(), pageNumber);
        });
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private PurchaseBean mappingToBean(Purchase purchase) {
        PurchaseBean bean = new PurchaseBean();
        bean.purchaseId = purchase.getPurchaseId();
        bean.productId = purchase.getProductId();
        bean.purchaseDatetime = toStringDate(purchase.getPurchaseDatetime()).get();
        bean.purchaseCount = purchase.getPurchaseCount();
        bean.purchasePrice = purchase.getPurchasePrice();
        purchase.getProduct().alwaysPresent(product -> {
            bean.productName = product.getProductName();
        });
        return bean;
    }
}
