/*
 * Copyright 2015-2017 the original author or authors.
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
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.cbean.result.PagingResultBean;
import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.app.web.base.paging.PagingAssist;
import org.docksidestage.app.web.base.paging.SearchPagingResult;
import org.docksidestage.dbflute.exbhv.ProductBhv;
import org.docksidestage.dbflute.exbhv.ProductStatusBhv;
import org.docksidestage.dbflute.exentity.Product;
import org.lastaflute.core.util.LaStringUtil;
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.JsonResponse;

// #thinking restful example...but
/**
 * @author jflute
 */
@AllowAnyoneAccess
public class ProductsAction extends ShowbaseBaseAction {

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
    @Execute(suppressValidatorCallCheck = true) // #hope no option by jflute
    public JsonResponse<? extends Object> post$index(OptionalThing<Integer> productId, ProductsSearchBody body) {
        if (productId.isPresent()) {
            return doDetail(productId.get());
        } else {
            return doList(body);
        }
    }

    private JsonResponse<ProductDetailResult> doDetail(Integer productId) {
        Product product = selectProduct(productId);
        return asJson(mappingToDetailResult(product));
    }

    private JsonResponse<SearchPagingResult<ProductsRowResult>> doList(ProductsSearchBody body) {
        validate(body, messages -> {});

        PagingResultBean<Product> page = selectProductPage(body);
        List<ProductsRowResult> rows = page.stream().map(product -> {
            return mappingToRowResult(product);
        }).collect(Collectors.toList());

        SearchPagingResult<ProductsRowResult> result = pagingAssist.createPagingResult(page, rows);
        return asJson(result);
    }

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    private Product selectProduct(int productId) {
        return productBhv.selectEntity(cb -> {
            cb.setupSelect_ProductCategory();
            cb.query().setProductId_Equal(productId);
        }).get();
    }

    private PagingResultBean<Product> selectProductPage(ProductsSearchBody form) {
        final Integer pageNumber = form.pageNumber != null ? form.pageNumber : 1;
        verifyOrClientError("The pageNumber should be positive number: " + pageNumber, pageNumber > 0);
        return productBhv.selectPage(cb -> {
            cb.setupSelect_ProductStatus();
            cb.setupSelect_ProductCategory();
            cb.specify().derivedPurchase().count(purchaseCB -> {
                purchaseCB.specify().columnPurchaseId();
            }, Product.ALIAS_purchaseCount);
            if (LaStringUtil.isNotEmpty(form.productName)) {
                cb.query().setProductName_LikeSearch(form.productName, op -> op.likeContain());
            }
            if (LaStringUtil.isNotEmpty(form.purchaseMemberName)) {
                cb.query().existsPurchase(purchaseCB -> {
                    purchaseCB.query().queryMember().setMemberName_LikeSearch(form.purchaseMemberName, op -> op.likeContain());
                });
            }
            if (form.productStatus != null) {
                cb.query().setProductStatusCode_Equal_AsProductStatus(form.productStatus);
            }
            cb.query().addOrderBy_ProductName_Asc();
            cb.query().addOrderBy_ProductId_Asc();
            cb.paging(Integer.MAX_VALUE, pageNumber); // #later: waiting for client side implementation by jflute
        });
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private ProductDetailResult mappingToDetailResult(Product product) {
        ProductDetailResult result = new ProductDetailResult();
        result.productId = product.getProductId();
        result.productName = product.getProductName();
        result.regularPrice = product.getRegularPrice();
        result.productHandleCode = product.getProductHandleCode();
        product.getProductCategory().alwaysPresent(category -> {
            result.categoryName = category.getProductCategoryName();
        });
        return result;
    }

    private ProductsRowResult mappingToRowResult(Product product) {
        ProductsRowResult result = new ProductsRowResult();
        result.productId = product.getProductId();
        result.productName = product.getProductName();
        product.getProductStatus().alwaysPresent(status -> {
            result.productStatusName = status.getProductStatusName();
        });
        result.regularPrice = product.getRegularPrice();
        return result;
    }
}
