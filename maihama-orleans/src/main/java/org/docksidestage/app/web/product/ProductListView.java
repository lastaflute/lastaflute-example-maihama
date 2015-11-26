/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.docksidestage.app.web.product;

import java.util.List;

import org.dbflute.cbean.result.PagingResultBean;
import org.docksidestage.app.web.base.OrleansBaseView;
import org.docksidestage.dbflute.exentity.Product;
import org.lastaflute.mixer2.view.Mixer2Supporter;
import org.mixer2.jaxb.xhtml.Body;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Option;
import org.mixer2.xhtml.AbstractJaxb;

/**
 * @author jflute
 */
public class ProductListView extends OrleansBaseView {

    private final ProductSearchForm form;
    private final PagingResultBean<Product> page;

    public ProductListView(ProductSearchForm form, PagingResultBean<Product> page) {
        this.form = form;
        this.page = page;
    }

    @Override
    protected void render(Html html, Mixer2Supporter supporter) {
        Body body = html.getBody();
        if (form.productStatus != null) {
            String statusCode = form.productStatus.code();
            supporter.findSelect(body, "productStatus").alwaysPresent(select -> { // #pending to easy selected
                List<AbstractJaxb> groupOrOptList = select.getOptgroupOrOption();
                for (AbstractJaxb groupOrOpt : groupOrOptList) {
                    Option option = (Option) groupOrOpt;
                    if (statusCode.equals(option.getValue())) {
                        option.setSelected("selected");
                    }
                }
            });
        }
        supporter.reflectListToTBody(body, page, "products", res -> {
            Product product = res.getEntity();
            res.reflectText(product.getProductId());
            res.reflectText(product.getProductName());
            res.reflectText(product.getProductStatus().get().getProductStatusName());
            res.reflectText(product.getProductCategory().get().getProductCategoryName());
            res.reflectText(product.getRegularPrice());
            res.reflectText(toDate(product.getLatestPurchaseDate()).orElse(null));
        });
    }
}
