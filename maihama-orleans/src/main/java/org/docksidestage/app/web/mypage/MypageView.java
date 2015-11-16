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
package org.docksidestage.app.web.mypage;

import org.dbflute.cbean.result.ListResultBean;
import org.docksidestage.app.web.base.OrleansBaseView;
import org.docksidestage.dbflute.exentity.Product;
import org.lastaflute.mixer2.view.Mixer2Supporter;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;

/**
 * @author jflute
 */
public class MypageView extends OrleansBaseView {

    private final ListResultBean<Product> productList;

    public MypageView(ListResultBean<Product> productList) {
        this.productList = productList;
    }

    @Override
    protected void render(Html html, Mixer2Supporter supporter) throws TagTypeUnmatchException {
        // beta beta
        //Body body = html.getBody();
        //Tbody productTBody = body.getById("products", Tbody.class);
        //Tr baseTr = productTBody.getTr().get(0).copy(Tr.class);
        //productTBody.unsetTr();
        //for (Product product : productList) {
        //    Tr tr = baseTr.copy(Tr.class);
        //    List<Flow> tdList = tr.getThOrTd();
        //    tdList.get(0).replaceInner(product.getProductName());
        //    tdList.get(1).replaceInner(String.valueOf(product.getRegularPrice()));
        //    productTBody.getTr().add(tr);
        //}
        // #thinking use TableBuilder?
        reflectDataToTBody(html, productList, "products", res -> {
            Product product = res.getEntity();
            res.reflectText(product.getProductName());
            res.reflectText(product.getRegularPrice());
        });
    }
}
