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

import org.lastaflute.mixer2.view.Mixer2Supporter;
import org.lastaflute.mixer2.view.TypicalMixView;
import org.mixer2.jaxb.xhtml.Footer;
import org.mixer2.jaxb.xhtml.Header;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.xhtml.PathAdjuster;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;

/**
 * @author jflute
 */
public abstract class OrleansBaseView extends TypicalMixView {

    @Override
    public void beDynamic(Html html, Mixer2Supporter supporter) throws TagTypeUnmatchException {
        // #pending jflute embedded? (2015/11/16)
        supporter.loadById("/common/mix_layout.html", "header", Header.class).alwaysPresent(header -> {
            try {
                PathAdjuster.replacePath(header, Pattern.compile("@\\{/"), supporter.getRequestManager().getContextPath() + "/");
                PathAdjuster.replacePath(header, Pattern.compile("}$"), "");
                html.replaceById("header", header);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to replace.", e); // #pending rich message
            }
        });
        supporter.loadById("/common/mix_layout.html", "footer", Footer.class).alwaysPresent(footer -> {
            try {
                PathAdjuster.replacePath(footer, Pattern.compile("@\\{/"), supporter.getRequestManager().getContextPath() + "/");
                PathAdjuster.replacePath(footer, Pattern.compile("}$"), "");
                html.replaceById("footer", footer);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to replace.", e); // #pending rich message
            }
        });
        super.beDynamic(html, supporter);
    }
}
