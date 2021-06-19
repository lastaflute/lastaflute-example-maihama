/*
 * Copyright 2015-2018 the original author or authors.
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
package org.docksidestage.app.web.ballet.dancers;

import org.docksidestage.dbflute.allcommon.CDef;
import org.hibernate.validator.constraints.Length;
import org.lastaflute.core.util.Lato;

/**
 * @author jflute
 */
public class BalletDancersSearchForm {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    /** prefix for name of product e.g. R */
    @Length(max = 10)
    public String productName;

    /** status of product e.g. ONS */
    public CDef.ProductStatus productStatus;

    /** prefix for member name who purchases the product e.g. S */
    @Length(max = 5)
    public String purchaseMemberName;

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    public String toString() {
        return Lato.string(this);
    }
}
