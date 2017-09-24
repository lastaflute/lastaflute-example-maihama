package org.docksidestage.app.web.purchase;

import org.lastaflute.web.validation.Required;

/**
 * @author iwamatsu0430
 * @author jflute
 */
public class PurchasePriceResult {

    @Required
    public final Integer price;

    public PurchasePriceResult(Integer price) {
        this.price = price;
    }
}
