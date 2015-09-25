package org.docksidestage.app.web.mypage;

/**
 * @author shunsuke.tadokoro
 */
public class MypageBean {

    public Integer memberId;
    public String memberName;
    public String memberStatusCode;
    public String memberServiceName;
    public String memberPassword;
    public String memberAddress;

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberStatusCode(String memberStatusCode) {
        this.memberStatusCode = memberStatusCode;
    }

    public void setMemberServiceName(String memberServiceName) {
        this.memberServiceName = memberServiceName;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }
}
