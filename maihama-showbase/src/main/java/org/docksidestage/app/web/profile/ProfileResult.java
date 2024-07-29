package org.docksidestage.app.web.profile;

import java.time.LocalDateTime;
import java.util.List;

import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.dbflute.exentity.MemberService;
import org.docksidestage.dbflute.exentity.Purchase;
import org.lastaflute.core.util.Lato;
import org.lastaflute.web.validation.Required;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * @author deco
 * @author black-trooper
 */
public class ProfileResult {

    @Required
    public final Integer memberId;
    @Required
    public final String memberName;
    @Required
    public final String memberStatusName;
    @Required
    public final Integer servicePointCount;
    @Required
    public final String serviceRankName;
    @NotNull
    @Valid
    public List<PurchasedProductPart> purchaseList;

    public ProfileResult(Member member) {
        this.memberId = member.getMemberId();
        this.memberName = member.getMemberName();
        this.memberStatusName = member.getMemberStatus().get().getMemberStatusName();
        MemberService service = member.getMemberServiceAsOne().get();
        this.servicePointCount = service.getServicePointCount();
        this.serviceRankName = service.getServiceRank().get().getServiceRankName();
    }

    public static class PurchasedProductPart {

        @Required
        public final String productName;
        @Required
        public final Integer regularPrice;
        @Required
        public final LocalDateTime purchaseDateTime;

        public PurchasedProductPart(Purchase purchase) {
            this.productName = purchase.getProduct().get().getProductName();
            this.regularPrice = purchase.getProduct().get().getRegularPrice();
            this.purchaseDateTime = purchase.getPurchaseDatetime();
        }
    }

    @Override
    public String toString() {
        return Lato.string(this);
    }
}
