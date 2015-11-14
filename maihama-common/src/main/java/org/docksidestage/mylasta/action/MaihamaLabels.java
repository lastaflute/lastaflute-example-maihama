/*
 * Copyright 2014-2015 the original author or authors.
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
package org.docksidestage.mylasta.action;

import org.lastaflute.web.ruts.message.ActionMessages;

/**
 * The keys for message.
 * @author FreeGen
 */
public class MaihamaLabels extends ActionMessages {

    /** The serial version UID for object serialization. (Default) */
    private static final long serialVersionUID = 1L;

    /** The key of the message: ID */
    public static final String LABELS_ID = "{labels.id}";

    /** The key of the message: Name */
    public static final String LABELS_NAME = "{labels.name}";

    /** The key of the message: Count */
    public static final String LABELS_COUNT = "{labels.count}";

    /** The key of the message: Price */
    public static final String LABELS_PRICE = "{labels.price}";

    /** The key of the message: EMail */
    public static final String LABELS_EMAIL = "{labels.email}";

    /** The key of the message: Password */
    public static final String LABELS_PASSWORD = "{labels.password}";

    /** The key of the message: List */
    public static final String LABELS_LIST = "{labels.list}";

    /** The key of the message: Detail */
    public static final String LABELS_DETAIL = "{labels.detail}";

    /** The key of the message: Edit */
    public static final String LABELS_EDIT = "{labels.edit}";

    /** The key of the message: Add */
    public static final String LABELS_ADD = "{labels.add}";

    /** The key of the message: Search */
    public static final String LABELS_SEARCH = "{labels.search}";

    /** The key of the message: @[labels.search] Condition */
    public static final String LABELS_SEARCH_CONDITION = "{labels.search.condition}";

    /** The key of the message: @[labels.search] Result */
    public static final String LABELS_SEARCH_RESULT = "{labels.search.result}";

    /** The key of the message: Register */
    public static final String LABELS_REGISTER = "{labels.register}";

    /** The key of the message: Update */
    public static final String LABELS_UPDATE = "{labels.update}";

    /** The key of the message: Confirm */
    public static final String LABELS_CONFIRM = "{labels.confirm}";

    /** The key of the message: Finish */
    public static final String LABELS_FINISH = "{labels.finish}";

    /** The key of the message: Member */
    public static final String LABELS_MEMBER = "{labels.member}";

    /** The key of the message: Product */
    public static final String LABELS_PRODUCT = "{labels.product}";

    /** The key of the message: Purchase */
    public static final String LABELS_PURCHASE = "{labels.purchase}";

    /** The key of the message: Withdrawal */
    public static final String LABELS_WITHDRAWAL = "{labels.withdrawal}";

    /** The key of the message: Payment */
    public static final String LABELS_PAYMENT = "{labels.payment}";

    /** The key of the message: Status */
    public static final String LABELS_STATUS = "{labels.status}";

    /** The key of the message: Category */
    public static final String LABELS_CATEGORY = "{labels.category}";

    /** The key of the message: @[labels.member] @[labels.status] */
    public static final String LABELS_MEMBER_STATUS = "{labels.memberStatus}";

    /** The key of the message: @[labels.product] @[labels.status] */
    public static final String LABELS_PRODUCT_STATUS = "{labels.productStatus}";

    /** The key of the message: @[labels.product] @[labels.category] */
    public static final String LABELS_PRODUCT_CATEGORY = "{labels.productCategory}";

    /** The key of the message: @[labels.member] @[labels.id] */
    public static final String LABELS_MEMBER_ID = "{labels.memberId}";

    /** The key of the message: @[labels.member] @[labels.name] */
    public static final String LABELS_MEMBER_NAME = "{labels.memberName}";

    /** The key of the message: @[labels.member] Account */
    public static final String LABELS_MEMBER_ACCOUNT = "{labels.memberAccount}";

    /** The key of the message: @[labels.purchase] @[labels.price] */
    public static final String LABELS_PURCHASE_PRICE = "{labels.purchasePrice}";

    /** The key of the message: @[labels.purchase] @[labels.count] */
    public static final String LABELS_PURCHASE_COUNT = "{labels.purchaseCount}";

    /** The key of the message: @[labels.product] @[labels.name] */
    public static final String LABELS_PRODUCT_NAME = "{labels.productName}";

    /** The key of the message: Regular @[labels.price] */
    public static final String LABELS_REGULAR_PRICE = "{labels.regularPrice}";

    /** The key of the message: Version No */
    public static final String LABELS_VERSION_NO = "{labels.versionNo}";

    /** The key of the message: Register Datetime */
    public static final String LABELS_REGISTER_DATETIME = "{labels.registerDatetime}";

    /** The key of the message: Register Datetime */
    public static final String LABELS_UPDATE_DATETIME = "{labels.updateDatetime}";

    /** The key of the message: Notice */
    public static final String LABELS_ERROR_MESSAGE_TITLE = "{labels.error.message.title}";

    /** The key of the message: -- */
    public static final String LABELS_LISTBOX_CAPTION_SHORT = "{labels.listbox.caption.short}";

    /** The key of the message: ---- */
    public static final String LABELS_LISTBOX_CAPTION_LONG = "{labels.listbox.caption.long}";

    /** The key of the message: select */
    public static final String LABELS_LISTBOX_CAPTION_TELL = "{labels.listbox.caption.tell}";

    /**
     * Assert the property is not null.
     * @param property The value of the property. (NotNull)
     */
    protected void assertPropertyNotNull(String property) {
        if (property == null) {
            String msg = "The argument 'property' for message should not be null.";
            throw new IllegalArgumentException(msg);
        }
    }
}
