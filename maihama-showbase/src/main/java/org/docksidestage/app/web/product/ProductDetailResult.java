package org.docksidestage.app.web.product;

import org.lastaflute.web.validation.Required;

/**
 * @author iwamatsu0430
 * @author jflute
 */
public class ProductDetailResult {

    @Required
    public Integer productId;
    @Required
    public String productName;
    @Required
    public String categoryName;
    @Required
    public Integer regularPrice;
    @Required
    public String productHandleCode;
}
