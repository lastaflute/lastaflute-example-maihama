package org.docksidestage.app.web.withdrawal;

import org.docksidestage.dbflute.allcommon.CDef;
import org.hibernate.validator.constraints.Length;

/**
 * @author black-trooper
 */
public class WithdrawalBody {

    public CDef.WithdrawalReason selectedReason;

    @Length(max = 30)
    public String reasonInput;
}
