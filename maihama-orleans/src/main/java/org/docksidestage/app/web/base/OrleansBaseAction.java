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
import org.dbflute.optional.OptionalObject;
import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.web.base.login.OrleansLoginAssist;
import org.docksidestage.app.web.base.paging.PagingNavi;
import org.docksidestage.mylasta.action.OrleansHtmlPath;
import org.docksidestage.mylasta.action.OrleansMessages;
import org.docksidestage.mylasta.action.OrleansUserBean;
import org.docksidestage.mylasta.direction.OrleansConfig;
import org.lastaflute.web.login.LoginManager;
import org.lastaflute.web.response.render.RenderData;
import org.lastaflute.web.ruts.process.ActionRuntime;
import org.lastaflute.web.validation.ActionValidator;
import org.lastaflute.web.validation.LaValidatable;

/**
 * @author jflute
 */
public abstract class OrleansBaseAction extends MaihamaBaseAction // has several interfaces for direct use
        implements LaValidatable<OrleansMessages>, OrleansHtmlPath {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** The application type for DoCKside, e.g. used by access context. */
    protected static final String APP_TYPE = "DCK"; // #change_it_first

    /** The user type for Member, e.g. used by access context. */
    protected static final String USER_TYPE = "M"; // #change_it_first

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private OrleansConfig orleansConfig;
    @Resource
    private OrleansLoginAssist orleansLoginAssist;

    // ===================================================================================
    //                                                                               Hook
    //                                                                              ======
    // #app_customize you can customize the action hook
    @Override
    public void hookFinally(ActionRuntime runtime) {
        if (runtime.isForwardToHtml()) {
            runtime.registerData("headerBean", getUserBean().map(userBean -> {
                return new OrleansHeaderBean(userBean);
            }).orElse(OrleansHeaderBean.empty()));
        }
        super.hookFinally(runtime);
    }

    // ===================================================================================
    //                                                                           User Info
    //                                                                           =========
    @Override
    protected OptionalThing<OrleansUserBean> getUserBean() { // to return as concrete class
        return orleansLoginAssist.getSessionUserBean(); // #app_customize return empty if login is unused
    }

    @Override
    protected String myAppType() { // for framework
        return APP_TYPE;
    }

    @Override
    protected OptionalThing<String> myUserType() { // for framework
        return OptionalObject.of(USER_TYPE); // #app_customize return empty if login is unused
    }

    @Override
    protected OptionalThing<LoginManager> myLoginManager() {
        return OptionalThing.of(orleansLoginAssist);
    }

    // ===================================================================================
    //                                                                          Validation
    //                                                                          ==========
    @SuppressWarnings("unchecked")
    @Override
    public ActionValidator<OrleansMessages> createValidator() { // for co-variant
        return super.createValidator();
    }

    @Override
    public OrleansMessages createMessages() { // application may call
        return new OrleansMessages(); // overriding to change return type to concrete-class
    }

    // ===================================================================================
    //                                                                              Paging
    //                                                                              ======
    // #app_customize you can customize the paging navigation logic
    /**
     * Register the paging navigation as page-range.
     * @param data The data object to render the HTML. (NotNull)
     * @param page The selected page as bean of paging result. (NotNull)
     * @param form The form for query string added to link. (NotNull)
     */
    protected void registerPagingNavi(RenderData data, PagingResultBean<? extends Entity> page, Object form) { // application may call
        data.register("pagingNavi", createPagingNavi(page, form));
    }

    protected PagingNavi createPagingNavi(PagingResultBean<? extends Entity> page, Object form) { // application may override
        return new PagingNavi(page, op -> {
            op.rangeSize(orleansConfig.getPagingPageRangeSizeAsInteger());
            if (orleansConfig.isPagingPageRangeFillLimit()) {
                op.fillLimit();
            }
        } , form);
    }

    /**
     * Get page size (record count of one page) for paging.
     * @return The integer as page size. (NotZero, NotMinus)
     */
    protected int getPagingPageSize() { // application may call
        return orleansConfig.getPagingPageSizeAsInteger();
    }

    // ===================================================================================
    //                                                                            Document
    //                                                                            ========
    /**
     * {@inheritDoc} <br>
     * <pre>
     * <span style="font-size: 130%; color: #553000">[Paging]</span>
     * o registerPagingNavi() <span style="color: #3F7E5E">// register paging navigation to HTML</span>
     * o getPagingPageSize() <span style="color: #3F7E5E">// get page size: record count per one page</span>
     * </pre>
     */
    @Override
    public void document1_CallableSuperMethod() {
        super.document1_CallableSuperMethod();
    }
}
