package org.docksidestage.app.web.signup;

import java.util.Random;

import javax.annotation.Resource;

import org.docksidestage.app.web.base.DocksideBaseAction;
import org.docksidestage.app.web.base.login.DocksideLoginAssist;
import org.docksidestage.app.web.mypage.MypageAction;
import org.docksidestage.app.web.signin.SigninAction;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exbhv.MemberSecurityBhv;
import org.docksidestage.dbflute.exbhv.MemberServiceBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.dbflute.exentity.MemberSecurity;
import org.docksidestage.dbflute.exentity.MemberService;
import org.docksidestage.mylasta.direction.DocksideConfig;
import org.docksidestage.mylasta.mail.member.WelcomeMemberPostcard;
import org.lastaflute.core.mail.Postbox;
import org.lastaflute.core.security.PrimaryCipher;
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.HtmlResponse;

/**
 * @author annie_pocket
 * @author jflute
 */
@AllowAnyoneAccess
public class SignupAction extends DocksideBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                          DI Component
    //                                          ------------
    @Resource
    private MemberBhv memberBhv;
    @Resource
    private MemberSecurityBhv memberSecurityBhv;
    @Resource
    private MemberServiceBhv memberServiceBhv;
    @Resource
    private DocksideLoginAssist docksideLoginAssist;
    @Resource
    private Postbox postbox;
    @Resource
    private DocksideConfig docksideConfig;
    @Resource
    private PrimaryCipher primaryCipher;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public HtmlResponse index() {
        return asHtml(path_Signup_SignupJsp).useForm(SignupForm.class);
    }

    @Execute
    public HtmlResponse doSignup(SignupForm form) {
        validate(form, messages -> {
            int count = memberBhv.selectCount(cb -> {
                cb.query().setMemberAccount_Equal(form.account);
            });
            if (count > 0) {
                messages.addErrorsSignupAccountAlreadyExists("account");
            }
        } , () -> {
            return asHtml(path_Signup_SignupJsp);
        });
        Integer memberId = newMember(form);
        docksideLoginAssist.identityLogin(memberId.longValue(), op -> {}); // no remember-me here

        WelcomeMemberPostcard.droppedInto(postbox, postcard -> {
            postcard.setFrom(docksideConfig.getMailAddressSupport(), "Dockside Support");
            postcard.addTo(deriveMemberMailAddress(form));
            postcard.setDomain(docksideConfig.getServerDomain());
            postcard.setMemberName(form.name);
            postcard.setAccount(form.account);
            postcard.setToken(generateToken());
        });
        return redirect(MypageAction.class);
    }

    @Execute
    public HtmlResponse register(SignupForm form) {
        Member member = new Member();
        member.setMemberAccount(form.account);
        member.setMemberStatusCode_Formalized();
        memberBhv.update(member);

        return redirect(SigninAction.class);
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private Integer newMember(SignupForm form) {
        Member member = new Member();
        member.setMemberAccount(form.account);
        member.setMemberName(form.name);
        member.setMemberStatusCode_Provisional();
        memberBhv.insert(member);

        MemberSecurity security = new MemberSecurity();
        security.setMemberId(member.getMemberId());
        security.setLoginPassword(docksideLoginAssist.encryptPassword(form.password));
        security.setReminderQuestion(form.reminderQuestion);
        security.setReminderAnswer(form.reminderAnswer);
        security.setReminderUseCount(0);
        memberSecurityBhv.insert(security);

        MemberService service = new MemberService();
        service.setMemberId(member.getMemberId());
        service.setServicePointCount(0);
        service.setServiceRankCode_Plastic();
        memberServiceBhv.insert(service);
        return member.getMemberId();
    }

    private String deriveMemberMailAddress(SignupForm form) {
        return form.account + "@docksidestage.org"; // simple for example
    }

    private String generateToken() {
        return primaryCipher.encrypt(String.valueOf(new Random().nextInt())); // simple for example
    }
}
