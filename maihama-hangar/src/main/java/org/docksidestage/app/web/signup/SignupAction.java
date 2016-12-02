/*
 * Copyright 2015-2016 the original author or authors.
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

import java.util.Random;

import javax.annotation.Resource;

import org.docksidestage.app.web.base.HangarBaseAction;
import org.docksidestage.app.web.base.login.HangarLoginAssist;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exbhv.MemberSecurityBhv;
import org.docksidestage.dbflute.exbhv.MemberServiceBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.dbflute.exentity.MemberSecurity;
import org.docksidestage.dbflute.exentity.MemberService;
import org.docksidestage.mylasta.action.HangarMessages;
import org.docksidestage.mylasta.direction.HangarConfig;
import org.docksidestage.mylasta.mail.member.WelcomeMemberPostcard;
import org.lastaflute.core.mail.Postbox;
import org.lastaflute.core.security.PrimaryCipher;
import org.lastaflute.core.util.LaStringUtil;
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.servlet.request.ResponseManager;
import org.lastaflute.web.servlet.session.SessionManager;

/**
 * @author iwamatsu0430
 * @author jflute
 */
@AllowAnyoneAccess
public class SignupAction extends HangarBaseAction {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String SIGNUP_TOKEN_KEY = "signupToken";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private PrimaryCipher primaryCipher;
    @Resource
    private Postbox postbox;
    @Resource
    private ResponseManager responseManager;
    @Resource
    private SessionManager sessionManager;
    @Resource
    private HangarConfig config;
    @Resource
    private HangarLoginAssist loginAssist;
    @Resource
    private MemberBhv memberBhv;
    @Resource
    private MemberSecurityBhv memberSecurityBhv;
    @Resource
    private MemberServiceBhv memberServiceBhv;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<Void> index(SignupBody body) {
        validate(body, messages -> moreValidation(body, messages));
        Integer memberId = insertProvisionalMember(body);
        loginAssist.identityLogin(memberId.longValue(), op -> op.rememberMe(true)); // fixedly remember-me here

        String signupToken = saveSignupToken();
        sendSignupMail(body, signupToken);
        return JsonResponse.asEmptyBody();
    }

    private void moreValidation(SignupBody body, HangarMessages messages) {
        if (LaStringUtil.isNotEmpty(body.memberAccount)) {
            int count = memberBhv.selectCount(cb -> {
                cb.query().setMemberAccount_Equal(body.memberAccount);
            });
            if (count > 0) {
                messages.addErrorsMemberAddAlreadyExist("memberAccount", "account");
            }
        }
    }

    private String saveSignupToken() {
        String token = primaryCipher.encrypt(String.valueOf(new Random().nextInt())); // #simple_for_example
        sessionManager.setAttribute(SIGNUP_TOKEN_KEY, token);
        return token;
    }

    private void sendSignupMail(SignupBody body, String signupToken) {
        WelcomeMemberPostcard.droppedInto(postbox, postcard -> {
            postcard.setFrom(config.getMailAddressSupport(), "Hangar Support"); // #simple_for_example
            postcard.addTo(body.memberAccount + "@docksidestage.org"); // #simple_for_example
            postcard.setDomain(config.getServerDomain());
            postcard.setMemberName(body.memberName);
            postcard.setAccount(body.memberAccount);
            postcard.setToken(signupToken);
            postcard.async();
            postcard.retry(3, 1000L);
        });
    }

    @Execute
    public JsonResponse<Void> formalize(String account, String token) { // from mail link
        verifySignupTokenMatched(account, token);
        updateMemberAsFormalized(account);
        return JsonResponse.asEmptyBody();
    }

    private void verifySignupTokenMatched(String account, String token) {
        String saved = sessionManager.getAttribute(SIGNUP_TOKEN_KEY, String.class).orElseTranslatingThrow(cause -> {
            return responseManager.new404("Not found the signupToken in session: " + account, op -> op.cause(cause));
        });
        if (!saved.equals(token)) {
            throw responseManager.new404("Unmatched signupToken in session: saved=" + saved + ", requested=" + token);
        }
    }

    // ===================================================================================
    //                                                                              Update
    //                                                                              ======
    private Integer insertProvisionalMember(SignupBody body) {
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
        return member.getMemberId();
    }

    private void updateMemberAsFormalized(String account) {
        Member member = new Member();
        member.uniqueBy(account);
        member.setMemberStatusCode_Formalized();
        memberBhv.updateNonstrict(member);
    }
}
