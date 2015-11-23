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
package org.docksidestage.app.web.base;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.logic.i18n.I18nDateLogic;
import org.docksidestage.app.web.base.login.OrleansLoginAssist;
import org.lastaflute.mixer2.view.TypicalMixView;
import org.lastaflute.mixer2.view.resolver.TypicalMixLayoutResolver;
import org.lastaflute.web.servlet.request.RequestManager;

/**
 * @author jflute
 */
public abstract class OrleansBaseView extends TypicalMixView {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // by everywhere injector
    @Resource
    private RequestManager requestManager;
    @Resource
    private I18nDateLogic i18nDateLogic;
    @Resource
    private OrleansLoginAssist orleansLoginAssist;

    // ===================================================================================
    //                                                                              Layout
    //                                                                              ======
    @Override
    protected void customizeLayout(TypicalMixLayoutResolver resolver) {
        resolver.resolveHeader((header, supporter) -> {
            orleansLoginAssist.getSessionUserBean().ifPresent(bean -> {
                // #pending can by class?
                header.replaceById("nav-user-name", bean.getMemberName());
            });
        });
    }

    // ===================================================================================
    //                                                                   Conversion Helper
    //                                                                   =================
    // #app_customize you can customize the conversion logic
    // -----------------------------------------------------
    //                                         to Local Date
    //                                         -------------
    protected OptionalThing<LocalDate> toDate(Object exp) { // application may call
        return i18nDateLogic.toDate(exp, myConvZone());
    }

    protected OptionalThing<LocalDateTime> toDateTime(Object exp) { // application may call
        return i18nDateLogic.toDateTime(exp, myConvZone());
    }

    // -----------------------------------------------------
    //                                        to String Date
    //                                        --------------
    protected OptionalThing<String> toStringDate(LocalDate date) { // application may call
        return i18nDateLogic.toStringDate(date, myConvZone());
    }

    protected OptionalThing<String> toStringDate(LocalDateTime dateTime) { // application may call
        return i18nDateLogic.toStringDate(dateTime, myConvZone());
    }

    protected OptionalThing<String> toStringDateTime(LocalDateTime dateTime) { // application may call
        return i18nDateLogic.toStringDateTime(dateTime, myConvZone());
    }

    // -----------------------------------------------------
    //                                   Conversion Resource
    //                                   -------------------
    protected TimeZone myConvZone() {
        return requestManager.getUserTimeZone();
    }
}
