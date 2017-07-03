package org.docksidestage.app.web.product;

import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.docksidestage.unit.UnitHangarTestCase;
import org.lastaflute.web.response.JsonResponse;

public class ProductDetailActionTest extends UnitHangarTestCase {

    public void test_index() {
        // ## Arrange ##
        ProductDetailAction action = new ProductDetailAction();
        inject(action);
        Integer productId = 1;

        // ## Act ##
        JsonResponse<ProductDetailResult> response = action.index(productId);

        // ## Assert ##
        showJson(response);
        TestingJsonData<ProductDetailResult> data = validateJsonData(response);
        assertEquals(data.getJsonResult().productId, productId);
    }

}
