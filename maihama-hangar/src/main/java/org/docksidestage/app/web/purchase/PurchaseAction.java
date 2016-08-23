package org.docksidestage.app.web.purchase;

import javax.annotation.Resource;

import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exbhv.ProductBhv;
import org.docksidestage.dbflute.exbhv.PurchaseBhv;
import org.docksidestage.dbflute.exentity.Product;
import org.docksidestage.dbflute.exentity.Purchase;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author iwamatsu0430
 * @author jflute
 */
public class PurchaseAction extends HangarBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private MemberBhv memberBhv;
    @Resource
    private ProductBhv productBhv;
    @Resource
    private PurchaseBhv purchaseBhv;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<PurchasePriceResult> count(PurchaseProductBody body) {
        validate(body, messages -> {});
        Product product = selectProduct(body.productId);
        PurchasePriceResult result = mappingToBean(body, product);
        return asJson(result);
    }

    @Execute
    public JsonResponse<PurchasePriceResult> contract(PurchaseProductBody body) {
        validate(body, messages -> {});
        Integer memberId = getUserBean().get().getMemberId();
        Product product = selectProduct(body.productId);
        Integer price = calculatePrice(product.getRegularPrice(), body.purchaseCount);
        insertPurchase(body, memberId, product, price);
        PurchasePriceResult result = mappingToBean(body, product);
        return asJson(result);
    }

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    private Product selectProduct(Integer productId) {
        return productBhv.selectEntity(cb -> cb.query().setProductId_Equal(productId)).get();
    }

    // ===================================================================================
    //                                                                              Update
    //                                                                              ======
    private void insertPurchase(PurchaseProductBody body, Integer memberId, Product product, Integer price) {
        Purchase purchase = new Purchase();
        purchase.setMemberId(memberId);
        purchase.setProductId(product.getProductId());
        purchase.setPaymentCompleteFlg_False();
        purchase.setPurchaseCount(body.purchaseCount);
        purchase.setPurchasePrice(price);
        purchaseBhv.insert(purchase);
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private PurchasePriceResult mappingToBean(PurchaseProductBody body, Product product) {
        Integer price = calculatePrice(product.getRegularPrice(), body.purchaseCount);
        return new PurchasePriceResult(price);
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private Integer calculatePrice(Integer unitPrice, Integer purchaseCount) {
        return unitPrice * purchaseCount;
    }
}
