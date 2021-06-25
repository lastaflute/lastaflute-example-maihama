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
package org.docksidestage.app.web.ballet.dancers.greatest.favorite.studios;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.lastaflute.core.util.Lato;
import org.lastaflute.web.validation.Required;

/**
 * @author jflute
 */
public class GreatestFavoriteStudiosListResult {

    @Valid
    @NotNull
    public List<PurchasesRowPart> rows;

    public GreatestFavoriteStudiosListResult(List<PurchasesRowPart> rows) {
        this.rows = rows;
    }

    public static class PurchasesRowPart {

        @Required
        public Long purchaseId;
        @Required
        public String memberName;
        @Required
        public String productName;
    }

    @Override
    public String toString() {
        return Lato.string(this);
    }
}