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
import org.mixer2.xhtml.exception.TagTypeUnmatchException;

/**
 * @author jflute
 */
public abstract class OrleansBaseView extends TypicalMixView {

    // #pending embed this to supporter
    protected <ENTITY> void reflectDataToTBody(Html html, List<ENTITY> entityList, String tbodyId,
            Consumer<TableDataResource<ENTITY>> oneArgLambda) throws TagTypeUnmatchException {
        final Body body = html.getBody();
        final Tbody tbody = body.getById(tbodyId, Tbody.class);
        final Tr baseTr = tbody.getTr().get(0).copy(Tr.class);
        tbody.unsetTr();
        for (ENTITY product : entityList) {
            final Tr tr = baseTr.copy(Tr.class);
            final List<Td> tdList = tr.getThOrTd().stream().map(flow -> (Td) flow).collect(Collectors.toList());
            oneArgLambda.accept(new TableDataResource<ENTITY>(tr, tdList, product));
            tbody.getTr().add(tr);
        }
    }

    public static class TableDataResource<ENTITY> {

        protected final Tr row;
        protected final List<Td> dataList;
        protected final ENTITY entity;
        protected int index;

        public TableDataResource(Tr currentRow, List<Td> dataList, ENTITY entity) {
            this.row = currentRow;
            this.dataList = dataList;
            this.entity = entity;
        }

        public void reflectTag(AbstractJaxb tag) {
            dataList.get(index).replaceInner(tag);
            ++index;
        }

        public void reflectText(Object text) {
            dataList.get(index).replaceInner(text.toString());
            ++index;
        }

        public Tr getRow() {
            return row;
        }

        public List<Td> getDataList() {
            return dataList;
        }

        public ENTITY getEntity() {
            return entity;
        }

        public int getIndex() {
            return index;
        }
    }
}
