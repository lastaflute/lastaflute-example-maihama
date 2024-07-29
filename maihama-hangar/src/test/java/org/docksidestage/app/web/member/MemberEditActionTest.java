package org.docksidestage.app.web.member;

import org.apache.commons.lang3.StringUtils;
import org.dbflute.helper.HandyDate;
import org.dbflute.optional.OptionalEntity;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.docksidestage.dbflute.allcommon.CDef;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.unit.UnitHangarTestCase;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.validation.exception.ValidationErrorException;

import jakarta.annotation.Resource;

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

    public void test_update_validate_request_required() {
        // ## Arrange ##
        MemberEditAction action = new MemberEditAction();
        inject(action);

        // ## Act ##
        {
            MemberEditBody body = prepareEditBody();
            body.birthdate = null;
            action.update(body);
        }

        // ## Assert ##
        assertException(ValidationErrorException.class, () -> {
            MemberEditBody body = prepareEditBody();
            body.memberId = null;
            action.update(body);
        });

        assertException(ValidationErrorException.class, () -> {
            MemberEditBody body = prepareEditBody();
            body.memberName = null;
            action.update(body);
        });

        assertException(ValidationErrorException.class, () -> {
            MemberEditBody body = prepareEditBody();
            body.memberAccount = null;
            action.update(body);
        });

        assertException(ValidationErrorException.class, () -> {
            MemberEditBody body = prepareEditBody();
            body.memberStatus = null;
            action.update(body);
        });

        assertException(ValidationErrorException.class, () -> {
            MemberEditBody body = prepareEditBody();
            body.versionNo = null;
            action.update(body);
        });
    }

    public void test_update_validate_request_maxlength() {
        // ## Arrange ##
        MemberEditAction action = new MemberEditAction();
        inject(action);

        // ## Act ##
        {
            MemberEditBody body = prepareEditBody();
            body.memberName = StringUtils.repeat("＊", 100);
            body.memberAccount = StringUtils.repeat("*", 50);
            action.update(body);
        }

        // ## Assert ##
        assertException(ValidationErrorException.class, () -> {
            MemberEditBody body = prepareEditBody();
            body.memberName = StringUtils.repeat("*", 100 + 1);
            action.update(body);
        });

        assertException(ValidationErrorException.class, () -> {
            MemberEditBody body = prepareEditBody();
            body.memberAccount = StringUtils.repeat("*", 50 + 1);
            action.update(body);
        });
    }

    // ===================================================================================
    //                                                                               logic
    //                                                                               =====
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
