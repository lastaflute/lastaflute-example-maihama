package org.docksidestage.app.web.signup;

import java.util.Random;

import javax.annotation.Resource;

import org.docksidestage.app.web.base.OrleansBaseAction;
import org.docksidestage.app.web.base.login.OrleansLoginAssist;
import org.docksidestage.app.web.mypage.MypageAction;
import org.docksidestage.app.web.signin.SigninAction;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exbhv.MemberSecurityBhv;
import org.docksidestage.dbflute.exbhv.MemberServiceBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.dbflute.exentity.MemberSecurity;
import org.docksidestage.dbflute.exentity.MemberService;
import org.docksidestage.mylasta.action.OrleansMessages;
import org.docksidestage.mylasta.direction.OrleansConfig;
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
public class SignupAction extends OrleansBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private MemberBhv memberBhv;
    @Resource
    private MemberSecurityBhv memberSecurityBhv;
    @Resource
    private MemberServiceBhv memberServiceBhv;
    @Resource
    private OrleansLoginAssist orleansLoginAssist;
    @Resource
    private Postbox postbox;
    @Resource
    private OrleansConfig orleansConfig;
    @Resource
    private PrimaryCipher primaryCipher;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public HtmlResponse index() {
        return asHtml(path_Signup_SignupHtml).useForm(SignupForm.class);
    }

    @Execute
    public HtmlResponse signup(SignupForm form) {
        validate(form, messages -> {
            moreValidate(form, messages);
        } , () -> {
            return asHtml(path_Signup_SignupHtml);
        });
        Integer memberId = newMember(form);
        orleansLoginAssist.identityLogin(memberId, op -> {}); // no remember-me here

        WelcomeMemberPostcard.droppedInto(postbox, postcard -> {
            postcard.setFrom(orleansConfig.getMailAddressSupport(), "Orleans Support");
            postcard.addTo(deriveMemberMailAddress(form));
            postcard.setDomain(orleansConfig.getServerDomain());
            postcard.setMemberName(form.memberName);
            postcard.setAccount(form.memberAccount);
            postcard.setToken(generateToken());
        });
        return redirect(MypageAction.class);
    }

    private void moreValidate(SignupForm form, OrleansMessages messages) {
        if (isNotEmpty(form.memberAccount)) {
            int count = memberBhv.selectCount(cb -> {
                cb.query().setMemberAccount_Equal(form.memberAccount);
            });
            if (count > 0) {
                messages.addErrorsSignupAccountAlreadyExists("memberAccount");
            }
        }
    }

    @Execute
    public HtmlResponse register(String account, String token) { // from mail link
        Member member = new Member();
        member.setMemberAccount(account);
        member.setMemberStatusCode_Formalized();
        memberBhv.update(member);
        return redirect(SigninAction.class);
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private Integer newMember(SignupForm form) {
        Member member = new Member();
        member.setMemberName(form.memberName);
        member.setMemberAccount(form.memberAccount);
        member.setMemberStatusCode_Provisional();
        memberBhv.insert(member);

        MemberSecurity security = new MemberSecurity();
        security.setMemberId(member.getMemberId());
        security.setLoginPassword(orleansLoginAssist.encryptPassword(form.password));
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
        return form.memberAccount + "@docksidestage.org"; // #simple_for_example
    }

    private String generateToken() {
        return primaryCipher.encrypt(String.valueOf(new Random().nextInt())); // simple for example
    }
}
