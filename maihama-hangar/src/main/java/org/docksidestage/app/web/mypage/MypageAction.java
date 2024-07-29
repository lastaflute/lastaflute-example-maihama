/*
 * Copyright 2015-2022 the original author or authors.
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

import java.util.List;
import java.util.stream.Collectors;

import org.dbflute.cbean.result.ListResultBean;
import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.app.web.mypage.MypageResult.ProductPart;
import org.docksidestage.dbflute.exbhv.ProductBhv;
import org.docksidestage.dbflute.exentity.Product;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

import jakarta.annotation.Resource;

/**
 * @author shunsuke.tadokoro
 * @author jflute
 * @author black-trooper
 */
public class MypageAction extends HangarBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ProductBhv productBhv;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<MypageResult> index() {
        MypageResult result = new MypageResult();
        result.recentProducts = mappingToProducts(selectRecentProductList());
        result.highPriceProducts = mappingToProducts(selectHighPriceProductList());
        return asJson(result);
    }

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    private ListResultBean<Product> selectRecentProductList() {
        ListResultBean<Product> productList = productBhv.selectList(cb -> {
            cb.specify().derivedPurchase().max(purchaseCB -> {
                purchaseCB.specify().columnPurchaseDatetime();
            }, Product.ALIAS_latestPurchaseDate);
            cb.query().existsPurchase(purchaseCB -> {
                purchaseCB.query().setMemberId_Equal(getUserBean().get().getMemberId());
            });
            cb.query().addSpecifiedDerivedOrderBy_Desc(Product.ALIAS_latestPurchaseDate);
            cb.query().addOrderBy_ProductId_Asc();
            cb.fetchFirst(3);
        });
        return productList;
    }

    private ListResultBean<Product> selectHighPriceProductList() {
        ListResultBean<Product> productList = productBhv.selectList(cb -> {
            cb.query().existsPurchase(purchaseCB -> {
                purchaseCB.query().setMemberId_Equal(getUserBean().get().getMemberId());
            });
            cb.query().addOrderBy_RegularPrice_Desc();
            cb.fetchFirst(3);
        });
        return productList;
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private List<ProductPart> mappingToProducts(List<Product> productList) {
        return productList.stream().map(product -> new ProductPart(product)).collect(Collectors.toList());
    }
}
