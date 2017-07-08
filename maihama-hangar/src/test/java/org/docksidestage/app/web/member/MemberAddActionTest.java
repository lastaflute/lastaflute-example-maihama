package org.docksidestage.app.web.member;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.dbflute.cbean.result.PagingResultBean;
import org.dbflute.helper.HandyDate;
import org.docksidestage.dbflute.allcommon.CDef;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.unit.UnitHangarTestCase;
import org.lastaflute.web.validation.exception.ValidationErrorException;

/**
 * @author black-trooper
 */
public class MemberAddActionTest extends UnitHangarTestCase {

    @Resource
    MemberBhv memberBhv;

    // ===================================================================================
    //                                                                          register()
    //                                                                          ==========
    public void test_register_formalized() {
        // ## Arrange ##
        MemberAddAction action = new MemberAddAction();
        inject(action);
        MemberAddBody body = prepareAddBody();
        body.memberStatus = CDef.MemberStatus.Formalized;

        // ## Act ##
        action.register(body);

        // ## Assert ##
        PagingResultBean<Member> page = memberBhv.selectPage(cb -> {
            cb.query().addOrderBy_RegisterDatetime_Desc();
            cb.paging(1, 1);
        });
        assertHasAnyElement(page);
        Member member = page.get(0);
        assertEquals(member.getMemberName(), body.memberName);
        assertEquals(member.getMemberAccount(), body.memberAccount);
        assertEquals(member.getBirthdate(), body.birthdate);
        assertEquals(member.getMemberStatusCodeAsMemberStatus(), body.memberStatus);
        assertNotNull(member.getFormalizedDatetime());
    }

    public void test_register_not_formalized() {
        // ## Arrange ##
        MemberAddAction action = new MemberAddAction();
        inject(action);
        MemberAddBody body = prepareAddBody();
        body.memberStatus = CDef.MemberStatus.Provisional;

        // ## Act ##
        action.register(body);

        // ## Assert ##
        PagingResultBean<Member> page = memberBhv.selectPage(cb -> {
            cb.query().addOrderBy_RegisterDatetime_Desc();
            cb.paging(1, 1);
        });
        assertHasAnyElement(page);
        Member member = page.get(0);
        assertEquals(member.getMemberName(), body.memberName);
        assertEquals(member.getMemberAccount(), body.memberAccount);
        assertEquals(member.getBirthdate(), body.birthdate);
        assertEquals(member.getMemberStatusCodeAsMemberStatus(), body.memberStatus);
        assertNull(member.getFormalizedDatetime());
    }

    public void test_register_validate_request_required() {
        // ## Arrange ##
        MemberAddAction action = new MemberAddAction();
        inject(action);

        // ## Act ##
        // ## Assert ##
        assertException(ValidationErrorException.class, () -> {
            MemberAddBody body = prepareAddBody();
            body.memberName = null;
            action.register(body);
        });

        assertException(ValidationErrorException.class, () -> {
            MemberAddBody body = prepareAddBody();
            body.memberAccount = null;
            action.register(body);
        });

        assertException(ValidationErrorException.class, () -> {
            MemberAddBody body = prepareAddBody();
            body.memberStatus = null;
            action.register(body);
        });
    }

    public void test_register_validate_request_maxlength() {
        // ## Arrange ##
        MemberAddAction action = new MemberAddAction();
        inject(action);

        // ## Act ##
        {
            MemberAddBody body = prepareAddBody();
            body.memberName = StringUtils.repeat("*", 100);
            body.memberAccount = StringUtils.repeat("*", 50);
            action.register(body);
        }

        // ## Assert ##
        assertException(ValidationErrorException.class, () -> {
            MemberAddBody body = prepareAddBody();
            body.memberName = StringUtils.repeat("*", 100 + 1);
            action.register(body);
        });

        assertException(ValidationErrorException.class, () -> {
            MemberAddBody body = prepareAddBody();
            body.memberAccount = StringUtils.repeat("*", 50 + 1);
            action.register(body);
        });
    }

    // ===================================================================================
    //                                                                               logic
    //                                                                               =====
    private MemberAddBody prepareAddBody() {
        MemberAddBody body = new MemberAddBody();
        body.memberName = "sea";
        body.memberAccount = "land";
        body.birthdate = new HandyDate("2017-07-05").getLocalDate();
        body.memberStatus = CDef.MemberStatus.Formalized;
        return body;
    }
}
