package org.docksidestage.app.web.product;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dbflute.optional.OptionalThing;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.docksidestage.app.web.base.paging.SearchPagingResult;
import org.docksidestage.dbflute.allcommon.CDef;
import org.docksidestage.dbflute.allcommon.CDef.ProductStatus;
import org.docksidestage.unit.UnitHangarTestCase;
import org.lastaflute.web.exception.RequestIllegalTransitionException;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.validation.exception.ValidationErrorException;

/**
 * @author jflute
 * @author black-trooper
 */
public class ProductListActionTest extends UnitHangarTestCase {

    public void test_search() {
        // ## Arrange ##
        ProductListAction action = new ProductListAction();
        inject(action);
        ProductSearchBody body = new ProductSearchBody();
        body.productName = "P";
        body.productStatus = CDef.ProductStatus.OnSaleProduction;
        body.purchaseMemberName = "a";

        // ## Act ##
        JsonResponse<SearchPagingResult<ProductRowResult>> response = action.search(OptionalThing.of(1), body);

        // ## Assert ##
        showJson(response);
        TestingJsonData<SearchPagingResult<ProductRowResult>> data = validateJsonData(response);
        assertHasAnyElement(data.getJsonResult().rows);
        data.getJsonResult().rows.forEach(bean -> {
            log(bean);
            assertContainsIgnoreCase(bean.productName, body.productName);
            assertEquals(bean.productStatus, body.productStatus.alias());
        });
    }

    public void test_search_notFound() {
        // ## Arrange ##
        ProductListAction action = new ProductListAction();
        inject(action);
        ProductSearchBody body = new ProductSearchBody();
        body.productName = "not found";

        // ## Act ##
        JsonResponse<SearchPagingResult<ProductRowResult>> response = action.search(OptionalThing.of(1), body);

        // ## Assert ##
        showJson(response);
        TestingJsonData<SearchPagingResult<ProductRowResult>> data = validateJsonData(response);
        assertHasZeroElement(data.getJsonResult().rows);
    }

    public void test_search_validate_request_maxlength() {
        // ## Arrange ##
        ProductListAction action = new ProductListAction();
        inject(action);

        // ## Act ##
        {
            ProductSearchBody body = new ProductSearchBody();
            body.productName = StringUtils.repeat("*", 10);
            body.purchaseMemberName = StringUtils.repeat("*", 5);
            action.search(OptionalThing.of(1), body);
        }

        // ## Assert ##
        assertException(ValidationErrorException.class, () -> {
            ProductSearchBody body = new ProductSearchBody();
            body.productName = StringUtils.repeat("*", 10 + 1);
            action.search(OptionalThing.of(1), body);
        });
        assertException(ValidationErrorException.class, () -> {
            ProductSearchBody body = new ProductSearchBody();
            body.purchaseMemberName = StringUtils.repeat("*", 5 + 1);
            action.search(OptionalThing.of(1), body);
        });
    }

    public void test_search_bad_request() {
        // ## Arrange ##
        ProductListAction action = new ProductListAction();
        inject(action);
        ProductSearchBody body = new ProductSearchBody();

        // ## Act ##
        // ## Assert ##
        assertException(RequestIllegalTransitionException.class, () -> {
            action.search(OptionalThing.of(0), body);
        });
    }

    public void test_status() {
        // ## Arrange ##
        ProductListAction action = new ProductListAction();
        inject(action);

        // ## Act ##
        JsonResponse<List<SimpleEntry<String, String>>> response = action.status();

        // ## Assert ##
        showJson(response);
        List<SimpleEntry<String, String>> jsonResult = response.getJsonResult();
        assertEquals(jsonResult.size(), ProductStatus.listAll().size());
        assertHasAnyElement(jsonResult);
        jsonResult.forEach(bean -> {
            assertTrue(ProductStatus.of(bean.getKey()).isPresent());
        });
    }
}
