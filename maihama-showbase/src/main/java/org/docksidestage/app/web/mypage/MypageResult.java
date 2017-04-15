package org.docksidestage.app.web.mypage;

import org.lastaflute.web.validation.Required;

/**
 * @author jflute
 */
public class MypageResult {

    @Required
    public Integer memberId;
    @Required
    public String memberName;
    @Required
    public String memberStatus;
    @Required
    public String serviceRank;
    @Required
    public String cipheredPassword;

    public String memberAddress;
}
