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

import java.util.regex.Pattern;

import org.lastaflute.core.util.ContainerUtil;
import org.lastaflute.mixer2.view.Mixer2View;
import org.lastaflute.web.servlet.request.RequestManager;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.xhtml.PathAdjuster;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;

/**
 * @author jflute
 */
public abstract class OrleansBaseView implements Mixer2View {

    @Override
    public Html toDynamicHtml(Html html) {
        RequestManager requestManager = ContainerUtil.getComponent(RequestManager.class); // #pending DI
        try {
            adjustCssPath(html, requestManager);
            render(html, requestManager);
            return html; // #pending return unneeded?
        } catch (TagTypeUnmatchException e) { // #pending needs to be embedded?
            throw new IllegalStateException("Failed to render the HTML: staticHtml=" + html, e);
        }
    }

    protected void adjustCssPath(Html html, RequestManager requestManager) {
        final Pattern pattern = Pattern.compile("^\\.+/.*css/(.*)$");
        PathAdjuster.replacePath(html, pattern, requestManager.getContextPath() + "/css/$1");
    }

    protected abstract void render(Html html, RequestManager requestManager) throws TagTypeUnmatchException;
}
