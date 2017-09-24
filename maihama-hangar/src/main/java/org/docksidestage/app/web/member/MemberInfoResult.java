package org.docksidestage.app.web.member;

import org.lastaflute.web.validation.Required;

/**
 * @author iwamatsu0430
 * @author jflute
 */
public class MemberInfoResult {

    public Integer memberId; // null allowed when no login

    @Required
    public String memberName; // can be guest

    public String memberStatusName; // null allowed when no login
}
