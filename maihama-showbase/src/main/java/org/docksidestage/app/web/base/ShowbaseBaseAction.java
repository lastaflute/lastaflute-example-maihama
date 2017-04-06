/*
 * Copyright 2015-2017 the original author or authors.
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

import javax.annotation.Resource;

import org.dbflute.optional.OptionalThing;
import org.docksidestage.mylasta.action.ShowbaseMessages;
import org.docksidestage.mylasta.action.ShowbaseUserBean;
import org.docksidestage.mylasta.direction.ShowbaseConfig;
import org.lastaflute.web.login.LoginManager;
import org.lastaflute.web.ruts.process.ActionRuntime;
import org.lastaflute.web.validation.ActionValidator;
import org.lastaflute.web.validation.LaValidatableApi;

/**
 * @author jflute
 */
public abstract class ShowbaseBaseAction extends MaihamaBaseAction // has several interfaces for direct use
        implements LaValidatableApi<ShowbaseMessages> {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** The application type for SHowBase, e.g. used by access context. */
    protected static final String APP_TYPE = "SHB"; // #change_it_first

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ShowbaseConfig config;

    // ===================================================================================
    //                                                                               Hook
    //                                                                              ======
    // #app_customize you can customize the action hook
    @Override
    public void hookFinally(ActionRuntime runtime) {
        super.hookFinally(runtime);
    }

    // ===================================================================================
    //                                                                           User Info
    //                                                                           =========
    // -----------------------------------------------------
    //                                      Application Info
    //                                      ----------------
    @Override
    protected String myAppType() { // for framework
        return APP_TYPE;
    }

    // -----------------------------------------------------
    //                                            Login Info
    //                                            ----------
    // #app_customize return empty if login is unused
    @Override
    protected OptionalThing<ShowbaseUserBean> getUserBean() { // application may call, overriding for co-variant
        return OptionalThing.empty();
    }

    @Override
    protected OptionalThing<String> myUserType() { // for framework
        return OptionalThing.empty();
    }

    @Override
    protected OptionalThing<LoginManager> myLoginManager() { // for framework
        return OptionalThing.empty();
    }

    // ===================================================================================
    //                                                                          Validation
    //                                                                          ==========
    @SuppressWarnings("unchecked")
    @Override
    public ActionValidator<ShowbaseMessages> createValidator() { // for co-variant
        return super.createValidator();
    }

    @Override
    public ShowbaseMessages createMessages() { // application may call
        return new ShowbaseMessages(); // overriding to change return type to concrete-class
    }
}
