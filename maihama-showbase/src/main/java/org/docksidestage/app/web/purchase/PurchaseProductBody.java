package org.docksidestage.app.web.purchase;

import javax.validation.constraints.Min;

import org.lastaflute.web.validation.Required;

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
