package org.docksidestage.app.web.member;

import org.lastaflute.web.validation.Required;

/**
 * @author iwamatsu0430
 * @author jflute
 */
public class MemberInfoResult {

    public Integer memberId;

    @Required
    public String memberName;

    public String memberStatusName;
}
