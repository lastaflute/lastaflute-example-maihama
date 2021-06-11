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
package org.docksidestage.app.web.products.assist;

import java.util.List;
import java.util.stream.Collectors;

import org.docksidestage.app.web.products.ProductsResult;
import org.docksidestage.app.web.products.ProductsRowResult;
import org.docksidestage.dbflute.exentity.Product;

/**
 * @author jflute
 */
public class ProductsMappingAssist {

    // ===================================================================================
    //                                                                         List Result
    //                                                                         ===========
    public List<ProductsRowResult> mappingToListResult(List<Product> productList) {
        return productList.stream().map(product -> {
            ProductsRowResult result = new ProductsRowResult();
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
        }).collect(Collectors.toList());
    }

    // ===================================================================================
    //                                                                       Single Result
    //                                                                       =============
    public ProductsResult mappingToSingleResult(Product product) {
        ProductsResult result = new ProductsResult();
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
