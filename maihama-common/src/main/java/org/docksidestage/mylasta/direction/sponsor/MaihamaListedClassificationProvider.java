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
package org.docksidestage.mylasta.direction.sponsor;

import java.util.function.Function;

import org.dbflute.jdbc.ClassificationMeta;
import org.dbflute.optional.OptionalThing;
import org.docksidestage.dbflute.allcommon.CDef;
import org.docksidestage.dbflute.allcommon.DBCurrent;
import org.lastaflute.db.dbflute.classification.TypicalListedClassificationProvider;
import org.lastaflute.db.dbflute.exception.ProvidedClassificationNotFoundException;

/**
 * @author jflute
 */
public abstract class MaihamaListedClassificationProvider extends TypicalListedClassificationProvider {

    @Override
    protected Function<String, ClassificationMeta> chooseClassificationFinder(String projectName)
            throws ProvidedClassificationNotFoundException {
        if (DBCurrent.getInstance().projectName().equals(projectName)) {
            return clsName -> onMainSchema(clsName).orElse(null); // null means not found
        } else {
            throw new ProvidedClassificationNotFoundException("Unknown DBFlute project name: " + projectName);
        }
    }

    @Override
    protected Function<String, ClassificationMeta> getDefaultClassificationFinder() {
        return clsName -> {
            return onMainSchema(clsName).orElseGet(() -> {
                return onAppCls(clsName).orElse(null); // null means not found
            });
        };
    }

    protected OptionalThing<ClassificationMeta> onMainSchema(String clsName) {
        return findMeta(CDef.DefMeta.class, clsName);
    }

    protected abstract OptionalThing<ClassificationMeta> onAppCls(String clsName);
}