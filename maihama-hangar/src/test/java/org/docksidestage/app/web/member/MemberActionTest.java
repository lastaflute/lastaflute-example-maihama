package org.docksidestage.app.web.member;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.docksidestage.app.web.signin.SigninAction;
import org.docksidestage.app.web.signin.SigninBody;
import org.docksidestage.dbflute.allcommon.CDef;
import org.docksidestage.unit.UnitHangarTestCase;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author black-trooper
 */
public class MemberActionTest extends UnitHangarTestCase {

    // ===================================================================================
    //                                                                              info()
    //                                                                              ======
    public void test_info_singed_in() {
        // ## Arrange ##
        MemberAction action = new MemberAction();
        inject(action);
        signin();

        // ## Act ##
        JsonResponse<MemberInfoResult> response = action.info();

        // ## Assert ##
        showJson(response);
        TestingJsonData<MemberInfoResult> data = validateJsonData(response);
        MemberInfoResult info = data.getJsonResult();
        assertNotNull(info.memberId);
        assertNotNull(info.memberName);
        assertNotNull(info.memberStatusName);
    }

    public void test_info_not_singed_in() {
        // ## Arrange ##
        MemberAction action = new MemberAction();
        inject(action);

        // ## Act ##
        JsonResponse<MemberInfoResult> response = action.info();

        // ## Assert ##
        showJson(response);
        TestingJsonData<MemberInfoResult> data = validateJsonData(response);
        MemberInfoResult info = data.getJsonResult();
        assertNull(info.memberId);
        assertEquals(info.memberName, "Guest");
        assertNull(info.memberStatusName);
    }

    // ===================================================================================
    //                                                                            status()
    //                                                                            ========
    public void test_status() {
        // ## Arrange ##
        MemberAction action = new MemberAction();
        inject(action);

        // ## Act ##
        JsonResponse<List<SimpleEntry<String, String>>> response = action.status();

        // ## Assert ##
        showJson(response);
        TestingJsonData<List<SimpleEntry<String, String>>> data = validateJsonData(response);
        List<SimpleEntry<String, String>> rows = data.getJsonResult();
        assertHasAnyElement(rows);
        assertEquals(rows.size(), CDef.MemberStatus.listAll().size());
        rows.forEach(entry -> {
            assertTrue(CDef.MemberStatus.of(entry.getKey()).isPresent());
        });
    }

    private void signin() {
        // ## Arrange ##
        SigninAction action = new SigninAction();
        inject(action);
        SigninBody body = new SigninBody();
        body.account = "Pixy";
        body.password = "sea";

        // ## Act ##
        action.index(body);
    }
}
