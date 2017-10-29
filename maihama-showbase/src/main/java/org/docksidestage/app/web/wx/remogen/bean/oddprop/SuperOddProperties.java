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
package org.docksidestage.app.web.wx.remogen.bean.oddprop;

import javax.validation.Valid;

import org.lastaflute.web.validation.Required;

/**
 * @author jflute
 */
public class SuperOddProperties {

    @Required
    public String seaResult;

    public Integer landBeanCount;

    @Valid
    public PlainNormalEntry normalEntry;

    public static class PlainNormalEntry {

        @Required
        public String piariFormStyle;
    }

    @Valid
    public ResultBeanEntry beanEntry;

    public static class ResultBeanEntry {

        @Required
        public String piariFormStyle;
    }

    @Valid
    public PlainGenericEntry<PlainGenericParameter> plainGenericEntry;

    public static class PlainGenericEntry<BEAN> {

        @Required
        public String bonvoName;

        @Valid
        public BEAN internalBean;
    }

    public static class PlainGenericParameter {

        @Required
        public String dstoreName;
    }

    @Valid
    public ResultGenericEntry<ResultGenericParameter> genericEntry;

    public static class ResultGenericEntry<BEAN> {

        @Required
        public String bonvoName;

        @Valid
        public BEAN internalBean;
    }

    public static class ResultGenericParameter {

        @Required
        public String dstoreName;
    }

    public SuperOddProperties(String seaResult) {
        this.seaResult = seaResult;
    }
}
