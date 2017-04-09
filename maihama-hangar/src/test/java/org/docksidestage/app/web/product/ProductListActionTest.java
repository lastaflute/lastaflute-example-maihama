package org.docksidestage.app.web.product;

import org.dbflute.optional.OptionalThing;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.docksidestage.app.web.base.paging.SearchPagingResult;
import org.docksidestage.unit.UnitHangarTestCase;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 */
public class ProductListActionTest extends UnitHangarTestCase {

    public void test_index_searchByName() {
        // ## Arrange ##
        ProductListAction action = new ProductListAction();
        inject(action);
        ProductSearchBody body = new ProductSearchBody();
        body.productName = "P";

        // ## Act ##
        JsonResponse<SearchPagingResult<ProductRowResult>> response = action.index(OptionalThing.of(1), body);

        // ## Assert ##
        showJson(response);
        TestingJsonData<SearchPagingResult<ProductRowResult>> data = validateJsonData(response);
        data.getJsonResult().rows.forEach(bean -> {
            log(bean);
            assertContainsIgnoreCase(bean.productName, body.productName);
        });
    }
}
