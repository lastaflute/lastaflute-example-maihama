/*
 * Copyright 2015-2024 the original author or authors.
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
package org.docksidestage.app.web.base.paging;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dbflute.Entity;
import org.dbflute.cbean.result.PagingResultBean;
import org.lastaflute.web.validation.Required;

/**
 * @param <BEAN> The type of Search List Row bean
 * @author iwamatsu0430
 * @author jflute
 */
public class SearchPagingResult<BEAN> { // #app_customize

    /** row count per one page */
    @Required
    public final Integer pageSize;

    /** number of current page */
    @Required
    public final Integer currentPageNumber;

    /** count of all records */
    @Required
    public final Integer allRecordCount;

    /** count of all pages */
    @Required
    public final Integer allPageCount;

    /** paging data for current page */
    @NotNull
    @Valid
    public final List<BEAN> rows;

    public SearchPagingResult(PagingResultBean<? extends Entity> page, List<BEAN> rows) {
        this.pageSize = page.getPageSize();
        this.currentPageNumber = page.getCurrentPageNumber();
        this.allRecordCount = page.getAllRecordCount();
        this.allPageCount = page.getAllPageCount();
        this.rows = rows;
    }
}
