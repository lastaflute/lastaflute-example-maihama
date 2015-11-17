/*
 * Copyright 2014-2015 the original author or authors.
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
package org.docksidestage.app.web.base;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.lastaflute.mixer2.view.TypicalMixView;
import org.mixer2.jaxb.xhtml.Body;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.Tbody;
import org.mixer2.jaxb.xhtml.Td;
import org.mixer2.jaxb.xhtml.Tr;
import org.mixer2.xhtml.AbstractJaxb;

/**
 * @author jflute
 */
public abstract class OrleansBaseView extends TypicalMixView {

    // ===================================================================================
    //                                                                             Reflect
    //                                                                             =======
    // #pending embed this to supporter
    protected <ENTITY> void reflectDataToTBody(Html html, List<ENTITY> entityList, String tbodyId,
            Consumer<TableDataResource<ENTITY>> oneArgLambda) {
        final Body body = html.getBody();
        final Tbody tbody = body.getById(tbodyId, Tbody.class);
        final Tr baseTr = tbody.getTr().get(0).copy(Tr.class); // #pending check out of bounds
        tbody.unsetTr();
        entityList.forEach(entity -> {
            final Tr tr = baseTr.copy(Tr.class);
            final List<Td> tdList = tr.getThOrTd().stream().map(flow -> {
                return (Td) flow; // #pending check class cast
            }).collect(Collectors.toList());
            oneArgLambda.accept(new TableDataResource<ENTITY>(tbody, tr, tdList, entity));
            tbody.getTr().add(tr);
        });
    }

    public static class TableDataResource<ENTITY> {

        protected final Tbody tbody;
        protected final Tr tr;
        protected final List<Td> tdList;
        protected final ENTITY entity;
        protected int index;

        public TableDataResource(Tbody tbody, Tr tr, List<Td> tdList, ENTITY entity) {
            this.tbody = tbody;
            this.tr = tr;
            this.tdList = tdList;
            this.entity = entity;
        }

        public void reflectTag(AbstractJaxb tag) {
            tdList.get(index).replaceInner(tag); // #pending check out of bounds
            ++index;
        }

        public void reflectText(Object text) {
            tdList.get(index).replaceInner(text.toString()); // #pending check out of bounds
            ++index;
        }

        public Tbody getTbody() {
            return tbody;
        }

        public Tr getTr() {
            return tr;
        }

        public Td getCurrentTd() {
            return tdList.get(index); // #pending check out of bounds
        }

        public List<Td> getTdList() {
            return tdList;
        }

        public ENTITY getEntity() {
            return entity;
        }
    }
}
