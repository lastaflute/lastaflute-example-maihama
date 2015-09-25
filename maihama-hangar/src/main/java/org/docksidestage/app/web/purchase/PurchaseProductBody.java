package org.docksidestage.app.web.purchase;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author iwamatsu0430
 */
public class PurchaseProductBody {

    @NotNull
    public Integer productId;

    @NotNull
    @Min(1)
    public Integer purchaseCount;
}
