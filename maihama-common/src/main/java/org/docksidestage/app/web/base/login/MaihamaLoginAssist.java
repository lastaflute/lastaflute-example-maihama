/*
 * Copyright 2015-2022 the original author or authors.
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
package org.docksidestage.app.web.base.login;

import org.docksidestage.mylasta.action.MaihamaUserBean;
import org.lastaflute.web.login.PrimaryLoginManager;
import org.lastaflute.web.login.TypicalLoginAssist;

/**
 * @param <USER_BEAN> The type of user bean.
 * @param <USER_ENTITY> The type of user entity or model.
 * @author jflute
 */
public abstract class MaihamaLoginAssist<USER_BEAN extends MaihamaUserBean, USER_ENTITY> // project common
        extends TypicalLoginAssist<Integer, USER_BEAN, USER_ENTITY> // #change_it also UserBean
        implements PrimaryLoginManager { // interface for framework control

    @Override
    protected Integer toTypedUserId(String userKey) {
        return Integer.valueOf(userKey);
    }
}
