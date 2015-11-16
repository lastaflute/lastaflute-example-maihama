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
package org.docksidestage.app.web.signin;

import org.docksidestage.app.web.base.OrleansBaseView;
import org.lastaflute.web.servlet.request.RequestManager;
import org.mixer2.jaxb.xhtml.Body;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Input;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;

/**
 * @author jflute
 */
public class SigninView extends OrleansBaseView {

    private final SigninForm form;

    public SigninView(SigninForm form) {
        this.form = form;
    }

    @Override
    protected void render(Html html, RequestManager requestManager) throws TagTypeUnmatchException {
        Body body = html.getBody();
        if (isNotEmpty(form.account)) {
            // #thinking registerInputValue("account", form.account);
            body.getById("account", Input.class).setValue(form.account);
        }
        body.getById("rememberMe", Input.class).setChecked(form.rememberMe ? "on" : null);
    }
}
