/*
 * Copyright 2015-2024 the original author or authors.
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

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.cbean.result.PagingResultBean;
import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.app.web.base.paging.PagingAssist;
import org.docksidestage.app.web.base.paging.SearchPagingResult;
import org.docksidestage.dbflute.allcommon.CDef.ProductStatus;
import org.docksidestage.dbflute.exbhv.ProductBhv;
import org.docksidestage.dbflute.exbhv.ProductStatusBhv;
import org.docksidestage.dbflute.exentity.Product;
import org.lastaflute.core.util.LaStringUtil;
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author iwamatsu0430
 * @author jflute
 * @author black-trooper
 */
@AllowAnyoneAccess
public class ProductListAction extends HangarBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ProductBhv productBhv;
    @Resource
    private ProductStatusBhv productStatusBhv;
    @Resource
    private PagingAssist pagingAssist;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<SearchPagingResult<ProductRowResult>> search(OptionalThing<Integer> pageNumber, ProductSearchBody body) {
        validate(body, messages -> {});

        PagingResultBean<Product> page = selectProductPage(pageNumber.orElse(1), body);
        List<ProductRowResult> rows = page.stream().map(product -> {
            return mappingToResult(product);
        }).collect(Collectors.toList());

        SearchPagingResult<ProductRowResult> result = pagingAssist.createPagingResult(page, rows);
        return asJson(result);
    }

    @Execute
    public JsonResponse<List<SimpleEntry<String, String>>> status() {
        List<SimpleEntry<String, String>> productStatusList = ProductStatus.listAll().stream().map(m -> {
            return new SimpleEntry<>(m.code(), m.alias());
        }).collect(Collectors.toList());
        return asJson(productStatusList);
    }

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    private PagingResultBean<Product> selectProductPage(int pageNumber, ProductSearchBody body) {
        verifyOrIllegalTransition("The pageNumber should be positive number: " + pageNumber, pageNumber > 0);
        return productBhv.selectPage(cb -> {
            cb.setupSelect_ProductStatus();
            cb.setupSelect_ProductCategory();
            cb.specify().derivedPurchase().max(purchaseCB -> {
                purchaseCB.specify().columnPurchaseDatetime();
            }, Product.ALIAS_latestPurchaseDate);
            if (LaStringUtil.isNotEmpty(body.productName)) {
                cb.query().setProductName_LikeSearch(body.productName, op -> op.likeContain());
            }
            if (LaStringUtil.isNotEmpty(body.purchaseMemberName)) {
                cb.query().existsPurchase(purchaseCB -> {
                    purchaseCB.query().queryMember().setMemberName_LikeSearch(body.purchaseMemberName, op -> op.likeContain());
                });
            }
            if (body.productStatus != null) {
                cb.query().setProductStatusCode_Equal_AsProductStatus(body.productStatus);
            }
            cb.query().addOrderBy_ProductName_Asc();
            cb.query().addOrderBy_ProductId_Asc();
            cb.paging(4, pageNumber);
        });
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private ProductRowResult mappingToResult(Product product) {
        ProductRowResult result = new ProductRowResult();
        result.productId = product.getProductId();
        result.productName = product.getProductName();
        product.getProductStatus().alwaysPresent(status -> {
            result.productStatus = status.getProductStatusName();
        });
        product.getProductCategory().alwaysPresent(category -> {
            result.productCategory = category.getProductCategoryName();
        });
        result.regularPrice = product.getRegularPrice();
        result.latestPurchaseDate = product.getLatestPurchaseDate();
        return result;
    }
}
