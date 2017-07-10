package org.docksidestage.app.web.profile;

import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.docksidestage.unit.UnitHangarTestCase;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author black-trooper
 */
public class ProfileActionTest extends UnitHangarTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mockLogin();
    }

    public void test_index() {
        // ## Arrange ##
        ProfileAction action = new ProfileAction();
        inject(action);

        // ## Act ##
        JsonResponse<ProfileResult> response = action.index();

        // ## Assert ##
        TestingJsonData<ProfileResult> data = validateJsonData(response);
        ProfileResult bean = data.getJsonResult();
        assertEquals(getMockLoginUserId(), bean.memberId);
    }
}
