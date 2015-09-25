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
package org.docksidestage.mylasta.template.bean.ooo;

import org.lastaflute.core.template.TemplateManager;
import org.lastaflute.core.template.TemplatePmb;
import org.lastaflute.core.template.TPCall;

/**
 * The Parameter-comMent style template (called PM template) on LastaFlute.
 * @author FreeGen
 */
public class NnnNestedBean implements TemplatePmb {

    public static final String PATH = "bean/ooo/NnnNested.dfpm";

    public static String parsedBy(TemplateManager templateManager, TPCall<NnnNestedBean> oneArgLambda) {
        NnnNestedBean pmb = new NnnNestedBean();
        oneArgLambda.setup(pmb);
        return templateManager.parse(pmb);
    }

    protected String sea;
    protected String land;

    @Override
    public String getTemplatePath() {
        return PATH;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NnnNestedBean:{");
        sb.append(sea);
        sb.append(", ").append(land);
        sb.append("}");
        return sb.toString();
    }

    /**
     * Get the value of sea, called by parameter comment.
     * @return The parameter value of sea. (NullAllowed: e.g. when no setting)
     */
    public String getSea() {
        return sea;
    }

    /**
     * Set the value of sea, used in parameter comment. <br>
     * Even if empty string, treated as empty plainly. So "IF pmb != null" is false if empty.
     * @param sea The parameter value for parameter comment. (NullAllowed)
     */
    public void setSea(String sea) {
        this.sea = sea;
    }

    /**
     * Get the value of land, called by parameter comment.
     * @return The parameter value of land. (NullAllowed: e.g. when no setting)
     */
    public String getLand() {
        return land;
    }

    /**
     * Set the value of land, used in parameter comment. <br>
     * Even if empty string, treated as empty plainly. So "IF pmb != null" is false if empty.
     * @param land The parameter value for parameter comment. (NullAllowed)
     */
    public void setLand(String land) {
        this.land = land;
    }
}
