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
package org.docksidestage.app.web;

import org.docksidestage.app.web.base.ShowbaseBaseAction;
import org.lastaflute.core.message.UserMessages;
import org.lastaflute.web.Execute;
import org.lastaflute.web.exception.Forced404NotFoundException;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.HtmlResponse;

/**
 * @author jflute
 */
@AllowAnyoneAccess
public class RootAction extends ShowbaseBaseAction {

    @Execute
    public HtmlResponse index() {
        throw new Forced404NotFoundException("no implementation yet", UserMessages.empty());
    }

    @Execute
    public HtmlResponse roneman() {
        throw new Forced404NotFoundException("delete this example method", UserMessages.empty());
    }

    @Execute
    public HtmlResponse rshining() {
        throw new Forced404NotFoundException("delete this example method", UserMessages.empty());
    }
}
