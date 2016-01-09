/*
 * Copyright 2015-2016 the original author or authors.
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

import org.docksidestage.mylasta.action.HangarLabels;
import org.lastaflute.web.ruts.message.ActionMessage;

/**
 * The keys for message.
 * @author FreeGen
 */
public class HangarMessages extends HangarLabels {

    /** The serial version UID for object serialization. (Default) */
    private static final long serialVersionUID = 1L;

    /** The key of the message: the member {0} already exists so input others */
    public static final String ERRORS_MEMBER_ADD_ALREADY_EXIST = "{errors.member.add.already.exist}";

    /**
     * Add the created action message for the key 'errors.member.add.already.exist' with parameters.
     * <pre>
     * message: the member {0} already exists so input others
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter arg0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public HangarMessages addErrorsMemberAddAlreadyExist(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_MEMBER_ADD_ALREADY_EXIST, arg0));
        return this;
    }
}
