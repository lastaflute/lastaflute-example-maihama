/*
 * Copyright 2015-2024 the original author or authors.
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
package org.docksidestage.app.web.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.lastaflute.web.validation.Required;

/**
 * @author iwamatsu0430
 * @author jflute
 */
public class MemberSearchRowResult {

    @Required
    public Integer memberId;
    @Required
    public String memberName;
    @Required
    public String memberStatusName;

    public LocalDate formalizedDate;
    @Required
    public LocalDateTime updateDatetime;
    @Required
    public Boolean withdrawalMember;
    @Required
    public Integer purchaseCount;
}
