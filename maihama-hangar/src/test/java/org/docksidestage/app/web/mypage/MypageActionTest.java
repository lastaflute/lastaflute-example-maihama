package org.docksidestage.app.web.mypage;

import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.docksidestage.unit.UnitHangarTestCase;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 */
public class MypageActionTest extends UnitHangarTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mockLogin();
    }

    public void test_index() {
        // ## Arrange ##
        MypageAction action = new MypageAction();
        inject(action);

        // ## Act ##
        JsonResponse<MypageBean> response = action.index();

        // ## Assert ##
        showJson(response);
        TestingJsonData<MypageBean> data = validateJsonData(response);
        MypageBean bean = data.getJsonBean();
        assertEquals(getMockLoginUserId(), bean.memberId);
    }
}
