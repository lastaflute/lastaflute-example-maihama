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
package org.docksidestage.app.web.wx.remogen.bean.selfref;

import javax.validation.Valid;

import org.lastaflute.web.validation.Required;

/**
 * @author jflute
 */
public class SelfReferenceResult {

    // ===================================================================================
    //                                                                        Resort Basic
    //                                                                        ============
    @Required
    public final String resortName;

    // -----------------------------------------------------
    //                                 Direct Self Reference
    //                                 ---------------------
    @Valid
    public ResortPark resortPark;

    public static class ResortPark { // direct self reference

        @Required
        public String parkName;

        @Valid
        public ResortPark parentPark;
    }

    // -----------------------------------------------------
    //                               One-Step Self Reference
    //                               -----------------------
    @Valid
    public ExtendedArea extendedArea;

    public static class ExtendedArea { // one-step self reference

        @Required
        public String areaName;

        @Valid
        public ParkingArea parkingArea;

        public static class ParkingArea {

            @Required
            public String areaName;

            @Valid
            public ExtendedArea internalArea;
        }
    }

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public SelfReferenceResult(String resortName) {
        this.resortName = resortName;
    }
}
