package org.docksidestage.app.web.purchase;


/**
 * @author toshiaki.arai
 */
public class PurchaseBean {

    public Long purchaseId;
    public Integer productId;
    public String productName;
    public String purchaseDatetime;
    public Integer purchaseCount;
    public Integer purchasePrice;
    // TODO toshiaki.arai 以下のスキーマ情報はあとで消す (2015/07/05)
    //    URCHASE_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '購入ID :  連番',
    //    `MEMBER_ID` int(11) NOT NULL COMMENT '会員ID : 会員を参照するID。\n購入を識別する自然キー(複合ユニーク制約)の筆頭要素。',
    //    `PRODUCT_ID` int(11) NOT NULL COMMENT '商品ID : あなたは何を買ったのか？',
    //    `PURCHASE_DATETIME` datetime NOT NULL COMMENT '購入日時 : 購入した瞬間の日時。',
    //    `PURCHASE_COUNT` int(11) NOT NULL COMMENT '購入数量 : 購入した商品の一回の購入における数量。',
    //    `PURCHASE_PRICE` int(11) NOT NULL COMMENT '購入価格 : 購入によって実際に会員が支払った（支払う予定の）価格。\n基本は商品の定価に購入数量を掛けたものになるが、ポイント利用や割引があったりと必ずしもそうはならない。',
    //    `PAYMENT_COMPLETE_FLG` int(11) NOT NULL COMMENT '支払完了フラグ : この購入に関しての支払いが完了しているか否か。',
}
