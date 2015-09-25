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

import javax.annotation.Resource;

import org.dbflute.Entity;
import org.dbflute.cbean.result.PagingResultBean;
import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.web.base.login.HangarLoginAssist;
import org.docksidestage.app.web.base.paging.SearchPagingBean;
import org.docksidestage.mylasta.action.HangarMessages;
import org.docksidestage.mylasta.action.HangarUserBean;
import org.docksidestage.mylasta.direction.HangarConfig;
import org.lastaflute.web.callback.ActionRuntime;
import org.lastaflute.web.login.LoginManager;
import org.lastaflute.web.servlet.request.RequestManager;
import org.lastaflute.web.validation.ActionValidator;
import org.lastaflute.web.validation.LaValidatableApi;

/**
 * @author jflute
 * @author iwamatsu0430
 */
public abstract class HangarBaseAction extends MaihamaBaseAction implements LaValidatableApi<HangarMessages> {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** The application type for Hangar, e.g. used by access context. */
    protected static final String APP_TYPE = "HGR";

    /** The user type for Member, e.g. used by access context. */
    protected static final String USER_TYPE = "M";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private RequestManager requestManager;
    @Resource
    private HangarConfig hangarConfig;
    @Resource
    private HangarLoginAssist hangarLoginAssist;

    // ===================================================================================
    //                                                                               Hook
    //                                                                              ======
    // #app_customize you can customize the action hook
    @Override
    public void hookFinally(ActionRuntime runtimeMeta) {
        super.hookFinally(runtimeMeta);
    }

    // ===================================================================================
    //                                                                           User Info
    //                                                                           =========
    @Override
    protected OptionalThing<HangarUserBean> getUserBean() { // application may call
        return hangarLoginAssist.getSessionUserBean(); // #app_customize return empty if login is unused
    }

    @Override
    protected String myAppType() { // for framework
        return APP_TYPE;
    }

    @Override
    protected OptionalThing<String> myUserType() { // for framework
        return OptionalThing.of(USER_TYPE);
    }

    @Override
    protected OptionalThing<LoginManager> myLoginManager() { // for framework
        return OptionalThing.of(hangarLoginAssist);
    }

    // ===================================================================================
    //                                                                          Validation
    //                                                                          ==========
    @SuppressWarnings("unchecked")
    @Override
    public ActionValidator<HangarMessages> createValidator() { // for co-variant
        return super.createValidator();
    }

    @Override
    public HangarMessages createMessages() { // application may call
        return new HangarMessages(); // overriding to change return type to concrete-class
    }

    // ===================================================================================
    //                                                                              Paging
    //                                                                              ======
    /**
     * Create paging bean for JSON.
     * @param page The selected result bean of paging. (NotNull)
     * @return The new-created bean of paging. (NotNull)
     */
    protected <ENTITY extends Entity, BEAN> SearchPagingBean<BEAN> createPagingBean(PagingResultBean<ENTITY> page) {
        return new SearchPagingBean<BEAN>(page);
    }

    /**
     * Get page size (record count of one page) for paging.
     * @return The integer as page size. (NotZero, NotMinus)
     */
    protected int getPagingPageSize() { // application may call
        return hangarConfig.getPagingPageSizeAsInteger();
    }
}
