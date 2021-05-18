/*
 * Copyright 2015-2021 the original author or authors.
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

import org.docksidestage.dbflute.allcommon.CDef;
import org.hibernate.validator.constraints.Length;
import org.lastaflute.web.validation.Required;

/**
 * @author jflute
 * @author iwamatsu0430
 * @author black-trooper
 */
public class MemberEditBody {

    @Required
    public Integer memberId;

    @Required
    @Length(max = 100)
    public String memberName;

    @Required
    @Length(max = 50)
    public String memberAccount;

    @Required
    public CDef.MemberStatus memberStatus;

    public LocalDate birthdate;

    @Required
    public Long versionNo;
}