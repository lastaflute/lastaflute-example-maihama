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

import javax.annotation.Resource;

import org.dbflute.cbean.result.PagingResultBean;
import org.docksidestage.app.web.base.OrleansBaseView;
import org.docksidestage.dbflute.exentity.Product;
import org.lastaflute.mixer2.view.Mixer2Supporter;
import org.lastaflute.web.path.ActionPathResolver;
import org.mixer2.jaxb.xhtml.A;
import org.mixer2.jaxb.xhtml.Body;
import org.mixer2.jaxb.xhtml.Html;

/**
 * @author jflute
 */
public class ProductListView extends OrleansBaseView {

    private final ProductSearchForm form;
    private final PagingResultBean<Product> page;
    @Resource
    private ActionPathResolver pathResolver;

    public ProductListView(ProductSearchForm form, PagingResultBean<Product> page) {
        this.form = form;
        this.page = page;
    }

    @Override
    protected void render(Html html, Mixer2Supporter supporter) {
        Body body = html.getBody();
        if (form.productStatus != null) {
            supporter.reflectSelectSelected(body, "productStatus", form.productStatus.code());
        }
        supporter.reflectListToTBody(page, body, "products", res -> {
            Product product = res.getEntity();
            res.register(product.getProductId());
            res.registerWithInner(inner -> {
                inner.findFirst(A.class).alwaysPresent(atag -> {
                    String url = supporter.toLinkUrl(ProductDetailAction.class, moreUrl(product.getProductId()));
                    supporter.reflectLinkUrl(atag, url);
                    atag.replaceInner(product.getProductName());
                });
            });
            res.register(product.getProductStatus().get().getProductStatusName());
            res.register(product.getProductCategory().get().getProductCategoryName());
            res.register(product.getRegularPrice());
            res.register(toDate(product.getLatestPurchaseDate()).orElse(null));
        });
    }
}
