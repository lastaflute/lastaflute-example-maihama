package org.docksidestage.app.web.mypage;

import java.util.List;

import org.docksidestage.dbflute.exentity.Product;
import org.lastaflute.core.util.Lato;
import org.lastaflute.web.validation.Required;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * @author shunsuke.tadokoro
 * @author jflute
 * @author black-trooper
 */
public class MypageResult {

    @NotNull
    @Valid
    public List<ProductPart> recentProducts;
    @NotNull
    @Valid
    public List<ProductPart> highPriceProducts;

    public static class ProductPart {

        @Required
        public final String productName;
        @Required
        public final Integer regularPrice;

        public ProductPart(Product product) {
            this.productName = product.getProductName();
            this.regularPrice = product.getRegularPrice();
        }
    }

    @Override
    public String toString() {
        return Lato.string(this);
    }
}
