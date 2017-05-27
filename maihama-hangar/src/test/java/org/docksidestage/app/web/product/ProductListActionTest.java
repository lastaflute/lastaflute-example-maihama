package org.docksidestage.app.web.product;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import org.dbflute.optional.OptionalThing;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.docksidestage.app.web.base.paging.SearchPagingResult;
import org.docksidestage.dbflute.allcommon.CDef.ProductStatus;
import org.docksidestage.unit.UnitHangarTestCase;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 */
public class ProductListActionTest extends UnitHangarTestCase {

    public void test_search_searchByName() {
        // ## Arrange ##
        ProductListAction action = new ProductListAction();
        inject(action);
        ProductSearchBody body = new ProductSearchBody();
        body.productName = "P";

        // ## Act ##
        JsonResponse<SearchPagingResult<ProductRowResult>> response = action.search(OptionalThing.of(1), body);

        // ## Assert ##
        showJson(response);
        TestingJsonData<SearchPagingResult<ProductRowResult>> data = validateJsonData(response);
        data.getJsonResult().rows.forEach(bean -> {
            log(bean);
            assertContainsIgnoreCase(bean.productName, body.productName);
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
