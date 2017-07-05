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

    public void test_update() {
        // ## Arrange ##
        MemberEditAction action = new MemberEditAction();
        inject(action);

        MemberEditBody editBody = memberBhv.selectByPK(1).map(entity -> {
            MemberEditBody body = new MemberEditBody();
            body.memberId = entity.getMemberId();
            body.versionNo = entity.getVersionNo();
            return body;
        }).get();
        editBody.memberName = "test user";
        editBody.memberAccount = "test_user0001";
        editBody.birthdate = new HandyDate("2017-07-05").getLocalDate();
        editBody.memberStatus = CDef.MemberStatus.Provisional;

        // ## Act ##
        action.update(editBody);

        // ## Assert ##
        OptionalEntity<Member> optionalEntity = memberBhv.selectByPK(1);
        assertTrue(optionalEntity.isPresent());
        Member member = optionalEntity.get();
        assertEquals(member.getMemberName(), editBody.memberName);
        assertEquals(member.getMemberAccount(), editBody.memberAccount);
        assertEquals(member.getBirthdate(), editBody.birthdate);
        assertEquals(member.getMemberStatusCodeAsMemberStatus(), editBody.memberStatus);
    }
}
