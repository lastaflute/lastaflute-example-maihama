/*
 * Copyright 2015-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.docksidestage.app.web.signup;

import javax.annotation.Resource;

import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.docksidestage.app.web.base.login.ShowbaseLoginAssist;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exbhv.MemberSecurityBhv;
import org.docksidestage.dbflute.exbhv.MemberServiceBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.dbflute.exentity.MemberSecurity;
import org.docksidestage.dbflute.exentity.MemberService;
import org.docksidestage.mylasta.action.ShowbaseMessages;
import org.docksidestage.mylasta.direction.ShowbaseConfig;
import org.docksidestage.mylasta.mail.member.WelcomeMemberPostcard;
import org.lastaflute.core.mail.Postbox;
import org.lastaflute.core.util.LaStringUtil;
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author iwamatsu0430
 * @author jflute
 */
@AllowAnyoneAccess
public class SignupAction extends ShowbaseBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private Postbox postbox;
    @Resource
    private ShowbaseConfig config;
    @Resource
    private ShowbaseLoginAssist loginAssist;
    @Resource
    private MemberBhv memberBhv;
    @Resource
    private MemberSecurityBhv memberSecurityBhv;
    @Resource
    private MemberServiceBhv memberServiceBhv;
    @Resource
    private SignupTokenAssist signupTokenAssist;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<Void> index(SignupBody body) {
        validate(body, messages -> moreValidation(body, messages));
        Member member = insertProvisionalMember(body);
        String signupToken = signupTokenAssist.saveSignupToken(member);
        sendSignupMail(body, signupToken);
        return JsonResponse.asEmptyBody(); // without automatic login here, client will call sign-in
    }

    private void moreValidation(SignupBody body, ShowbaseMessages messages) {
        if (LaStringUtil.isNotEmpty(body.memberAccount)) {
            int count = memberBhv.selectCount(cb -> {
                cb.query().setMemberAccount_Equal(body.memberAccount);
            });
            if (count > 0) {
                messages.addErrorsSignupAccountAlreadyExists("memberAccount");
            }
        }
    }

    private void sendSignupMail(SignupBody body, String token) {
        WelcomeMemberPostcard.droppedInto(postbox, postcard -> {
            postcard.setFrom(config.getMailAddressSupport(), "Showbase Support"); // #simple_for_example
            postcard.addTo(body.memberAccount + "@docksidestage.org"); // #simple_for_example
            postcard.setDomain(config.getServerDomain());
            postcard.setMemberName(body.memberName);
            postcard.setAccount(body.memberAccount);
            postcard.setToken(token);
            postcard.async();
            postcard.retry(3, 1000L);
        });
    }

    @Execute
    public JsonResponse<Void> register(String account, String token) { // from mail link
        signupTokenAssist.verifySignupTokenMatched(account, token);
        updateMemberAsFormalized(account);
        return JsonResponse.asEmptyBody();
    }

    // ===================================================================================
    //                                                                              Update
    //                                                                              ======
    private Member insertProvisionalMember(SignupBody body) {
        Member member = new Member();
        member.setMemberName(body.memberName);
        member.setMemberAccount(body.memberAccount);
        member.setMemberStatusCode_Provisional();
        memberBhv.insert(member);

        MemberSecurity security = new MemberSecurity();
        security.setMemberId(member.getMemberId());
        security.setLoginPassword(loginAssist.encryptPassword(body.password));
        security.setReminderQuestion(body.reminderQuestion);
        security.setReminderAnswer(body.reminderAnswer);
        security.setReminderUseCount(0);
        memberSecurityBhv.insert(security);

        MemberService service = new MemberService();
        service.setMemberId(member.getMemberId());
        service.setServicePointCount(0);
        service.setServiceRankCode_Plastic();
        memberServiceBhv.insert(service);
        return member;
    }

    private void updateMemberAsFormalized(String account) {
        Member member = new Member();
        member.uniqueBy(account);
        member.setMemberStatusCode_Formalized();
        memberBhv.updateNonstrict(member);
    }
}
