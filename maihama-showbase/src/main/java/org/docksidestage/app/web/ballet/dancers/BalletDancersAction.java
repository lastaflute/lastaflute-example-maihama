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
package org.docksidestage.app.web.ballet.dancers;

import java.util.List;

import org.docksidestage.app.web.ballet.dancers.assist.BalletDancersCrudAssist;
import org.docksidestage.app.web.ballet.dancers.assist.BalletDancersMappingAssist;
import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.dbflute.exentity.Product;
import org.lastaflute.web.Execute;
import org.lastaflute.web.RestfulAction;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.JsonResponse;

import jakarta.annotation.Resource;

/**
 * @author jflute
 */
@AllowAnyoneAccess // is NOT related to RESTful
@RestfulAction(hyphenate = "ballet-dancers")
public class BalletDancersAction extends ShowbaseBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private BalletDancersCrudAssist balletDancersCrudAssist;
    @Resource
    private BalletDancersMappingAssist balletDancersMappingAssist;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // /ballet-dancers/
    //
    // http://localhost:8098/showbase/ballet-dancers/?productName=S
    // _/_/_/_/_/_/_/_/_/_/
    @Execute
    public JsonResponse<List<BalletDancersRowResult>> get$index(BalletDancersSearchForm form) {
        validate(form, messages -> {});
        List<Product> productList = balletDancersCrudAssist.selectProductList(form);
        List<BalletDancersRowResult> listResult = balletDancersMappingAssist.mappingToListResult(productList);
        return asJson(listResult);
    }

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // /ballet-dancers/1/
    //
    // http://localhost:8098/showbase/ballet-dancers/1/
    // _/_/_/_/_/_/_/_/_/_/
    @Execute
    public JsonResponse<BalletDancersResult> get$index(Integer productId) {
        Product product = balletDancersCrudAssist.selectProductById(productId);
        BalletDancersResult singleResult = balletDancersMappingAssist.mappingToSingleResult(product);
        return asJson(singleResult);
    }

    @Execute
    public JsonResponse<Void> post$index(BalletDancersPostBody body) {
        validate(body, messages -> {});
        return JsonResponse.asEmptyBody(); // dummy implementation
    }

    @Execute
    public JsonResponse<Void> put$index(Integer productId, BalletDancersPutBody body) {
        validate(body, messages -> {});
        return JsonResponse.asEmptyBody(); // dummy implementation
    }

    @Execute
    public JsonResponse<Void> delete$index(Integer productId) {
        return JsonResponse.asEmptyBody(); // dummy implementation
    }
}
