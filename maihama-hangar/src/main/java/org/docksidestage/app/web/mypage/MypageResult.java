package org.docksidestage.app.web.mypage;

import java.util.List;

import javax.validation.Valid;

import org.docksidestage.dbflute.exentity.Product;
import org.lastaflute.web.validation.Required;

/**
 * @author shunsuke.tadokoro
 * @author jflute
 * @author black-trooper
 */
public class MypageResult {

    @Required
    @Valid
    public List<ProductPart> recentProducts;
    @Required
    @Valid
    public List<ProductPart> highPriceProducts;

    static public class ProductPart {

        @Required
        public final String productName;
        @Required
        public final Integer regularPrice;

        public ProductPart(Product product) {
            this.productName = product.getProductName();
            this.regularPrice = product.getRegularPrice();
        }

        @Override
        public String toString() {
            return "{" + productName + ", " + regularPrice + "}";
        }
    }
}
