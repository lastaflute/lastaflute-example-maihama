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
package org.docksidestage.app.web.wx.remogen.bean.suffix;

import java.util.List;

import javax.validation.Valid;

import org.lastaflute.web.validation.Required;

/**
 * @author jflute
 */
public class TonOnlySuffixResult {

    // ===================================================================================
    //                                                                        Resort Basic
    //                                                                        ============
    @Required
    public final String resortName;

    @Valid
    public ResortPark resortPark;

    public static class ResortPark {

        @Required
        public String parkName;

        @Valid
        public List<ShowStage> showStages;

        public static class ShowStage {

            @Required
            public String stageName;
        }
    }

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public TonOnlySuffixResult(String resortName) {
        this.resortName = resortName;
    }
}
