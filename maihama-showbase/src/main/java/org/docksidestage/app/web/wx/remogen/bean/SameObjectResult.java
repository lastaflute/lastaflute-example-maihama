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
package org.docksidestage.app.web.wx.remogen.bean;

import java.util.List;

import javax.validation.Valid;

import org.docksidestage.app.web.wx.remogen.bean.SameObjectResult.DepartmentStore.OfficialShop;
import org.lastaflute.web.validation.Required;

/**
 * @author jflute
 */
public class SameObjectResult {

    @Required
    public final String resortName;

    @Valid
    public ResortPark firstPark;

    public static class ResortPark {

        @Required
        public String parkName;

        @Required
        public Integer stageCount;
    }

    @Valid
    public List<ExtendedArea> extendedAreas;

    public static class ExtendedArea {

        @Required
        public String direction;

        @Valid
        public ResortPark nextPark;
    }

    @Valid
    public DepartmentStore departmentStore;

    public static class DepartmentStore {

        @Required
        public String storeName;

        public Integer shopCount;

        @Valid
        public OfficialShop officialShop;

        public static class OfficialShop {

            @Required
            public String shopName;
        }
    }

    @Valid
    public List<ThemeHotel> bigHotels;

    public static class ThemeHotel {

        @Required
        public String hotelName;

        @Valid
        public ResortPark correspondingPark;

        @Valid
        public OfficialShop officialShop;
    }

    public SameObjectResult(String resortName) {
        this.resortName = resortName;
    }
}
