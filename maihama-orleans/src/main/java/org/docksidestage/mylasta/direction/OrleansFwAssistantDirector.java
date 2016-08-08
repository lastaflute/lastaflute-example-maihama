/*
 * Copyright 2015-2016 the original author or authors.
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

import org.docksidestage.mylasta.direction.sponsor.MaihamaCurtainBeforeHook;
import org.docksidestage.mylasta.direction.sponsor.OrleansListedClassificationProvider;
import org.docksidestage.mylasta.direction.sponsor.OrleansMultipartRequestHandler;
import org.lastaflute.core.direction.FwAssistantDirector;
import org.lastaflute.db.dbflute.classification.ListedClassificationProvider;
import org.lastaflute.mixer2.Mixer2RenderingProvider;
import org.lastaflute.thymeleaf.ThymeleafRenderingProvider;
import org.lastaflute.web.direction.FwWebDirection;
import org.lastaflute.web.response.HtmlResponse;
import org.lastaflute.web.ruts.NextJourney;
import org.lastaflute.web.ruts.multipart.MultipartResourceProvider;
import org.lastaflute.web.ruts.process.ActionRuntime;
import org.lastaflute.web.ruts.renderer.HtmlRenderer;
import org.lastaflute.web.ruts.renderer.HtmlRenderingProvider;

/**
 * @author jflute
 */
public class OrleansFwAssistantDirector extends MaihamaFwAssistantDirector {

    @Resource
    private OrleansConfig orleansConfig;

    @Override
    protected void setupAppConfig(List<String> nameList) {
        nameList.add("orleans_config.properties"); // base point
        nameList.add("orleans_env.properties");
    }

    @Override
    protected void setupAppMessage(List<String> nameList) {
        nameList.add("orleans_message"); // base point
        nameList.add("orleans_label");
    }

    @Override
    protected MaihamaCurtainBeforeHook createCurtainBeforeHook() {
        return new MaihamaCurtainBeforeHook() {
            @Override
            public void hook(FwAssistantDirector assistantDirector) {
                super.hook(assistantDirector);
            }
        };
    }

    @Override
    protected ListedClassificationProvider createListedClassificationProvider() {
        return new OrleansListedClassificationProvider();
    }

    @Override
    protected void prepareWebDirection(FwWebDirection direction) {
        super.prepareWebDirection(direction);
        direction.directHtmlRendering(createHtmlRenderingProvider());
        direction.directMultipart(createMultipartResourceProvider());
    }

    protected HtmlRenderingProvider createHtmlRenderingProvider() {
        final ThymeleafRenderingProvider thymeleaf = createThymeleafRenderingProvider();
        final Mixer2RenderingProvider mixer2 = createMixer2RenderingProvider();
        return new HtmlRenderingProvider() {

            @Override
            public HtmlRenderer provideRenderer(ActionRuntime runtime, NextJourney journey) {
                return chooseProvider(runtime, journey).provideRenderer(runtime, journey);
            }

            @Override
            public HtmlResponse provideShowErrorsResponse(ActionRuntime runtime) {
                return mixer2.provideShowErrorsResponse(runtime);
            }

            private HtmlRenderingProvider chooseProvider(ActionRuntime runtime, NextJourney journey) {
                return journey.getViewObject().isPresent() ? mixer2 : thymeleaf;
            }
        };
    }

    protected ThymeleafRenderingProvider createThymeleafRenderingProvider() { // will be deleted
        return new ThymeleafRenderingProvider().asDevelopment(orleansConfig.isDevelopmentHere());
    }

    protected Mixer2RenderingProvider createMixer2RenderingProvider() {
        return new Mixer2RenderingProvider().asDevelopment(orleansConfig.isDevelopmentHere());
    }

    protected MultipartResourceProvider createMultipartResourceProvider() {
        return () -> new OrleansMultipartRequestHandler();
    }
}
