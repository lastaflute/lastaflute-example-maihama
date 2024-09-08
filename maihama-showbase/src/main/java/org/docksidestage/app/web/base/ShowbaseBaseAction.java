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
package org.docksidestage.app.web.base;

import java.util.stream.Stream;

import javax.annotation.Resource;

import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.web.base.login.ShowbaseLoginAssist;
import org.docksidestage.mylasta.action.ShowbaseMessages;
import org.docksidestage.mylasta.action.ShowbaseUserBean;
import org.docksidestage.mylasta.direction.ShowbaseConfig;
import org.lastaflute.core.exception.LaApplicationMessage;
import org.lastaflute.core.json.JsonManager;
import org.lastaflute.web.login.LoginManager;
import org.lastaflute.web.response.ActionResponse;
import org.lastaflute.web.ruts.config.restful.httpstatus.TypicalStructuredSuccessHttpStatusHandler;
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

    /** The user type for Member, e.g. used by access context. */
    protected static final String USER_TYPE = "M"; // #change_it_first (can delete if no login)

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private JsonManager jsonManager;
    @Resource
    private ShowbaseConfig config;
    @Resource
    private ShowbaseLoginAssist loginAssist;
    //@Resource // if you need to auth your API, you can use this assist
    //private AccessTokenAuthAssist accessTokenAuthAssist;

    // ===================================================================================
    //                                                                               Hook
    //                                                                              ======
    // -----------------------------------------------------
    //                                                Before
    //                                                ------
    // #app_customize you can customize the action hook
    @Override
    public ActionResponse hookBefore(ActionRuntime runtime) {
        final ActionResponse superResponse = super.hookBefore(runtime);

        // if you need to auth your API, you can implement here
        //accessTokenAuthAssist.determineAuth(runtime);

        return superResponse;
    }

    // -----------------------------------------------------
    //                                            on Failure
    //                                            ----------
    @Override
    protected Object[] filterApplicationExceptionMessageValues(ActionRuntime runtime, LaApplicationMessage msg) {
        return Stream.of(msg.getValues()).map(vl -> jsonManager.toJson(vl)).toArray(); // for client-managed message
    }

    // -----------------------------------------------------
    //                                               Finally
    //                                               -------
    @Override
    public void hookFinally(ActionRuntime runtime) {
        super.hookFinally(runtime);

        // for RESTful HTTP status as conventional way e.g. POST:201, DELETE(no return):204
        new TypicalStructuredSuccessHttpStatusHandler().reflectHttpStatusIfNeeds(runtime);
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
        return loginAssist.getSavedUserBean();
    }

    @Override
    protected OptionalThing<String> myUserType() { // for framework
        return OptionalThing.of(USER_TYPE);
    }

    @Override
    protected OptionalThing<LoginManager> myLoginManager() { // for framework
        return OptionalThing.of(loginAssist);
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
