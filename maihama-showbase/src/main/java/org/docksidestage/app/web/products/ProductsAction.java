/*
 * Copyright 2015-2018 the original author or authors.
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
package org.docksidestage.app.web.products;

import java.util.List;

import javax.annotation.Resource;

import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.app.web.products.assist.ProductsCrudAssist;
import org.docksidestage.app.web.products.assist.ProductsMappingAssist;
import org.docksidestage.dbflute.exentity.Product;
import org.lastaflute.web.Execute;
import org.lastaflute.web.RestfulAction;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 */
@AllowAnyoneAccess // is NOT related to RESTful
@RestfulAction // is required for RESTful API
public class ProductsAction extends ShowbaseBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ProductsCrudAssist productsCrudAssist;
    @Resource
    private ProductsMappingAssist productsMappingAssist;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<List<ProductsRowResult>> get$index(ProductsSearchForm form) {
        validate(form, messages -> {});
        List<Product> productList = productsCrudAssist.selectProductList(form);
        List<ProductsRowResult> listResult = productsMappingAssist.mappingToListResult(productList);
        return asJson(listResult);
    }

    @Execute
    public JsonResponse<ProductsResult> get$index(Integer productId) {
        Product product = productsCrudAssist.selectProductById(productId);
        ProductsResult singleResult = productsMappingAssist.mappingToSingleResult(product);
        return asJson(singleResult);
    }

    @Execute
    public JsonResponse<Void> post$index(ProductsPostBody body) {
        validate(body, messages -> {});
        return JsonResponse.asEmptyBody(); // dummy implementation
    }

    @Execute
    public JsonResponse<Void> put$index(Integer productId, ProductsPutBody body) {
        validate(body, messages -> {});
        return JsonResponse.asEmptyBody(); // dummy implementation
    }

    @Execute
    public JsonResponse<Void> delete$index(Integer productId) {
        return JsonResponse.asEmptyBody(); // dummy implementation
    }
}
