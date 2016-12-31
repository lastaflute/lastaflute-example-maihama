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
package org.docksidestage.mylasta.direction;

import java.util.List;

import javax.annotation.Resource;

import org.docksidestage.mylasta.direction.sponsor.MaihamaActionAdjustmentProvider;
import org.docksidestage.mylasta.direction.sponsor.MaihamaApiFailureHook;
import org.docksidestage.mylasta.direction.sponsor.MaihamaCookieResourceProvider;
import org.docksidestage.mylasta.direction.sponsor.MaihamaCurtainBeforeHook;
import org.docksidestage.mylasta.direction.sponsor.MaihamaJsonResourceProvider;
import org.docksidestage.mylasta.direction.sponsor.MaihamaMailDeliveryDepartmentCreator;
import org.docksidestage.mylasta.direction.sponsor.MaihamaSecurityResourceProvider;
import org.docksidestage.mylasta.direction.sponsor.MaihamaTimeResourceProvider;
import org.docksidestage.mylasta.direction.sponsor.MaihamaUserLocaleProcessProvider;
import org.docksidestage.mylasta.direction.sponsor.MaihamaUserTimeZoneProcessProvider;
import org.lastaflute.core.direction.CachedFwAssistantDirector;
import org.lastaflute.core.direction.FwAssistDirection;
import org.lastaflute.core.direction.FwCoreDirection;
import org.lastaflute.core.security.InvertibleCryptographer;
import org.lastaflute.core.security.OneWayCryptographer;
import org.lastaflute.db.dbflute.classification.ListedClassificationProvider;
import org.lastaflute.db.direction.FwDbDirection;
import org.lastaflute.web.direction.FwWebDirection;

/**
 * @author jflute
 */
public abstract class MaihamaFwAssistantDirector extends CachedFwAssistantDirector {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private MaihamaConfig config;

    // ===================================================================================
    //                                                                              Assist
    //                                                                              ======
    @Override
    protected void prepareAssistDirection(FwAssistDirection direction) {
        direction.directConfig(nameList -> setupAppConfig(nameList), "maihama_config.properties", "maihama_env.properties");
    }

    protected abstract void setupAppConfig(List<String> nameList);

    // ===================================================================================
    //                                                                               Core
    //                                                                              ======
    @Override
    protected void prepareCoreDirection(FwCoreDirection direction) {
        // this configuration is on maihama_env.properties because this is true only when development
        direction.directDevelopmentHere(config.isDevelopmentHere());

        // titles of the application for logging are from configurations
        direction.directLoggingTitle(config.getDomainTitle(), config.getEnvironmentTitle());

        // this configuration is on sea_env.properties because it has no influence to production
        // even if you set true manually and forget to set false back
        direction.directFrameworkDebug(config.isFrameworkDebug()); // basically false

        // you can add your own process when your application's curtain before
        direction.directCurtainBefore(createCurtainBeforeHook());

        direction.directSecurity(createSecurityResourceProvider());
        direction.directTime(createTimeResourceProvider());
        direction.directJson(createJsonResourceProvider());
        direction.directMail(createMailDeliveryDepartmentCreator().create());
    }

    protected MaihamaCurtainBeforeHook createCurtainBeforeHook() {
        return new MaihamaCurtainBeforeHook();
    }

    protected MaihamaSecurityResourceProvider createSecurityResourceProvider() { // #change_it_first
        final InvertibleCryptographer inver = InvertibleCryptographer.createAesCipher("maihama:dockside");
        final OneWayCryptographer oneWay = OneWayCryptographer.createSha256Cryptographer();
        return new MaihamaSecurityResourceProvider(inver, oneWay);
    }

    protected MaihamaTimeResourceProvider createTimeResourceProvider() {
        return new MaihamaTimeResourceProvider(config);
    }

    protected MaihamaJsonResourceProvider createJsonResourceProvider() {
        return new MaihamaJsonResourceProvider();
    }

    protected MaihamaMailDeliveryDepartmentCreator createMailDeliveryDepartmentCreator() {
        return new MaihamaMailDeliveryDepartmentCreator(config);
    }

    // ===================================================================================
    //                                                                                 DB
    //                                                                                ====
    @Override
    protected void prepareDbDirection(FwDbDirection direction) {
        direction.directClassification(createListedClassificationProvider());
    }

    protected abstract ListedClassificationProvider createListedClassificationProvider();

    // ===================================================================================
    //                                                                                Web
    //                                                                               =====
    @Override
    protected void prepareWebDirection(FwWebDirection direction) {
        direction.directRequest(createUserLocaleProcessProvider(), createUserTimeZoneProcessProvider());
        direction.directCookie(createCookieResourceProvider());
        direction.directAdjustment(createActionAdjustmentProvider());
        direction.directMessage(nameList -> setupAppMessage(nameList), "maihama_message", "maihama_label");
        direction.directApiCall(createApiFailureHook());
    }

    protected MaihamaUserLocaleProcessProvider createUserLocaleProcessProvider() {
        return new MaihamaUserLocaleProcessProvider();
    }

    protected MaihamaUserTimeZoneProcessProvider createUserTimeZoneProcessProvider() {
        return new MaihamaUserTimeZoneProcessProvider();
    }

    protected MaihamaCookieResourceProvider createCookieResourceProvider() { // #change_it_first
        final InvertibleCryptographer cr = InvertibleCryptographer.createAesCipher("dockside:maihama");
        return new MaihamaCookieResourceProvider(config, cr);
    }

    protected MaihamaActionAdjustmentProvider createActionAdjustmentProvider() {
        return new MaihamaActionAdjustmentProvider();
    }

    protected abstract void setupAppMessage(List<String> nameList);

    protected MaihamaApiFailureHook createApiFailureHook() {
        return new MaihamaApiFailureHook();
    }
}
