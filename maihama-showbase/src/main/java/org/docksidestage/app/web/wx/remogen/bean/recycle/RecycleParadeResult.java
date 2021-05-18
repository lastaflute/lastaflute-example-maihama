/*
 * Copyright 2015-2021 the original author or authors.
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
package org.docksidestage.app.web.wx.remogen.bean.recycle;

import java.util.List;

import javax.validation.Valid;

import org.docksidestage.app.web.wx.remogen.bean.recycle.RecycleParadeResult.DepartmentStorePart.OfficialShopPart;
import org.lastaflute.web.validation.Required;

/**
 * @author jflute
 */
public class RecycleParadeResult {

    // ===================================================================================
    //                                                                        Resort Basic
    //                                                                        ============
    @Required
    public final String resortName;

    @Valid
    public ResortParkPart firstPark;

    public static class ResortParkPart {

        @Required
        public String parkName;

        @Valid
        public List<ShowStagePart> showStages;

        @Valid
        public ThemeColorResult themeColor;
    }

    public static class ShowStagePart {

        @Required
        public String stageName;
    }

    // ===================================================================================
    //                                                                       Extended Area
    //                                                                       =============
    @Valid
    public List<ExtendedAreaPart> extendedAreas;

    public static class ExtendedAreaPart {

        @Required
        public String direction;

        @Valid
        public ResortParkPart nextPark;
    }

    // ===================================================================================
    //                                                                    Department Store
    //                                                                    ================
    @Valid
    public DepartmentStorePart departmentStore;

    public static class DepartmentStorePart {

        @Required
        public String storeName;

        public Integer shopCount;

        @Valid
        public OfficialShopPart officialShop;

        public static class OfficialShopPart {

            @Required
            public String shopName;
        }

        @Valid
        public ShowStagePart showStage;
    }

    // ===================================================================================
    //                                                                         Theme Hotel
    //                                                                         ===========
    @Valid
    public List<ThemeHotelPart> bigHotels;

    public static class ThemeHotelPart {

        @Required
        public String hotelName;

        @Valid
        public ResortParkPart correspondingPark;

        @Valid
        public OfficialShopPart officialShop;

        @Valid
        public ShowStagePart showStage;

        @Valid
        public ThemeColorResult themeColor;
    }

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public RecycleParadeResult(String resortName) {
        this.resortName = resortName;
    }
}
