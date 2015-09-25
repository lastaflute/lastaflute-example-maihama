package org.docksidestage.app.web.purchase;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.dbflute.optional.OptionalEntity;
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
 */
public class PurchaseAction extends HangarBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                          DI Component
    //                                          ------------
    @Resource
    private MemberBhv memberBhv;

    @Resource
    private ProductBhv ProductBhv;

    @Resource
    private PurchaseBhv purchaseBhv;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<PriceBean> count(PurchaseProductBody body) {
        validate(body, messages -> {});
        PriceBean bean = selectProduct(body.productId).map(product -> {
            Integer price = calculatePrice(product.getRegularPrice(), body.purchaseCount);
            return mappingToBean(price);
        }).get();
        return asJson(bean);
    }

    @Execute
    public JsonResponse<PriceBean> contract(PurchaseProductBody body) {
        validate(body, messages -> {});
        PriceBean bean = selectProduct(body.productId).map(product -> {
            Integer price = calculatePrice(product.getRegularPrice(), body.purchaseCount);
            insertPurchase(body, product, price);
            return mappingToBean(price);
        }).get();
        return asJson(bean);
    }

    private void insertPurchase(PurchaseProductBody body, Product product, Integer price) {
        Purchase purchase = new Purchase();
        purchase.setMemberId(getUserBean().get().getMemberId());
        purchase.setProductId(product.getProductId());
        purchase.setPaymentCompleteFlg_False();
        purchase.setPurchaseCount(body.purchaseCount);
        purchase.setPurchasePrice(price);
        purchaseBhv.insert(purchase);
    }

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    private OptionalEntity<Product> selectProduct(Integer productId) {
        return ProductBhv.selectEntity(cb -> cb.query().setProductId_Equal(productId));
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private PriceBean mappingToBean(Integer price) {
        PriceBean bean = new PriceBean();
        bean.price = price;
        return bean;
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private Integer calculatePrice(Integer unitPrice, Integer count) {
        BigDecimal unitPriceDecimal = new BigDecimal(unitPrice);
        BigDecimal countDecimal = new BigDecimal(count);
        return unitPriceDecimal.multiply(countDecimal).intValue();
    }
}
