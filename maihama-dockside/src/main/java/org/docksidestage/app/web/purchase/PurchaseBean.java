package org.docksidestage.app.web.purchase;

import java.time.LocalDateTime;

import org.lastaflute.web.validation.Required;

/**
 * @author toshiaki.arai
 * @author jflute
 */
public class PurchaseBean {

    @Required
    public Long purchaseId;
    @Required
    public Integer productId;
    @Required
    public String productName;
    @Required
    public LocalDateTime purchaseDatetime;
    @Required
    public Integer purchaseCount;
    @Required
    public Integer purchasePrice;
}
