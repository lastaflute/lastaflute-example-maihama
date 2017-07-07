package org.docksidestage.app.web.member;

import javax.annotation.Resource;

import org.dbflute.helper.HandyDate;
import org.dbflute.optional.OptionalEntity;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.docksidestage.dbflute.allcommon.CDef;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.unit.UnitHangarTestCase;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author black-trooper
 */
public class MemberEditActionTest extends UnitHangarTestCase {

    @Resource
    MemberBhv memberBhv;

    // ===================================================================================
    //                                                                             index()
    //                                                                             =======
    public void test_index() {
        // ## Arrange ##
        MemberEditAction action = new MemberEditAction();
        inject(action);
        Integer memberId = 1;

        // ## Act ##
        JsonResponse<MemberEditBody> response = action.index(memberId);

        // ## Assert ##
        showJson(response);
        TestingJsonData<MemberEditBody> data = validateJsonData(response);
        assertEquals(data.getJsonResult().memberId, memberId);
    }

    // ===================================================================================
    //                                                                            update()
    //                                                                            ========
    public void test_update() {
        // ## Arrange ##
        MemberEditAction action = new MemberEditAction();
        inject(action);
        MemberEditBody body = prepareEditBody();

        // ## Act ##
        action.update(body);

        // ## Assert ##
        OptionalEntity<Member> optionalEntity = memberBhv.selectByPK(1);
        assertTrue(optionalEntity.isPresent());
        Member member = optionalEntity.get();
        assertEquals(member.getMemberName(), body.memberName);
        assertEquals(member.getMemberAccount(), body.memberAccount);
        assertEquals(member.getBirthdate(), body.birthdate);
        assertEquals(member.getMemberStatusCodeAsMemberStatus(), body.memberStatus);
    }

    private MemberEditBody prepareEditBody() {
        Member member = memberBhv.selectByPK(1).get();

        MemberEditBody body = new MemberEditBody();
        body.memberId = member.getMemberId();
        body.versionNo = member.getVersionNo();

        body.memberName = "sea";
        body.memberAccount = "land";
        body.birthdate = new HandyDate("2017-07-05").getLocalDate();
        body.memberStatus = CDef.MemberStatus.Provisional;
        return body;
    }
}
