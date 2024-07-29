package org.docksidestage.app.web.purchase;

import org.lastaflute.web.validation.Required;

import jakarta.validation.constraints.Min;

/**
 * @author iwamatsu0430
 * @author jflute
 */
public class PurchaseProductBody {

    @Required
    public Integer productId;

    @Required
    @Min(1)
    public Integer purchaseCount;
}
