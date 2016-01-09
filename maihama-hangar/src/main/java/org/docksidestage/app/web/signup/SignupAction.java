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
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.servlet.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author iwamatsu0430
 * @author jflute
 */
@AllowAnyoneAccess
public class SignupAction extends HangarBaseAction {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final Logger logger = LoggerFactory.getLogger(SignupAction.class);

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private PrimaryCipher primaryCipher;
    @Resource
    private SessionManager sessionManager;
    @Resource
    private Postbox postbox;
    @Resource
    private HangarConfig hangarConfig;
    @Resource
    private HangarLoginAssist docksideLoginAssist;
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
        validate(body, messages -> {
            int count = memberBhv.selectCount(cb -> {
                cb.query().setMemberAccount_Equal(body.memberAccount);
            });
            if (count > 0) {
                messages.addErrorsMemberAddAlreadyExist("memberAccount", "account");
            }
        });
        Integer memberId = newMember(body);
        docksideLoginAssist.identityLogin(memberId.longValue(), op -> op.rememberMe(true));
        WelcomeMemberPostcard.droppedInto(postbox, postcard -> {
            postcard.setFrom(hangarConfig.getMailAddressSupport(), HangarMessages.LABELS_MAIL_SUPPORT_PERSONAL);
            postcard.addTo(deriveMemberMailAddress(body));
            postcard.setDomain(hangarConfig.getServerDomain());
            postcard.setMemberName(body.memberName);
            postcard.setAccount(body.memberAccount);
            postcard.setToken(generateToken());
        });
        sessionManager.setAttribute("signupToken", "lasta-flute"); // #simple_for_example
        return JsonResponse.asEmptyBody();
    }

    @Execute
    public JsonResponse<Void> confirm() { // #simple_for_example
        sessionManager.getAttribute("signupToken", String.class).ifPresent(token -> {
            logger.debug("#confirm OK!");
        }).orElse(() -> {
            logger.debug("#confirm NG!");
        });
        return JsonResponse.asEmptyBody();
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private Integer newMember(SignupBody body) {
        Member member = new Member();
        member.setMemberAccount(body.memberAccount);
        member.setMemberName(body.memberName);
        member.setMemberStatusCode_Provisional();
        memberBhv.insert(member);

        MemberSecurity security = new MemberSecurity();
        security.setMemberId(member.getMemberId());
        security.setLoginPassword(docksideLoginAssist.encryptPassword(body.password));
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

    private String deriveMemberMailAddress(SignupBody body) {
        return body.memberAccount + "@docksidestage.org"; // #simple_for_example
    }

    private String generateToken() {
        return primaryCipher.encrypt(String.valueOf(new Random().nextInt())); // #simple_for_example
    }
}
