/*
 * Copyright 2015-2022 the original author or authors.
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
package org.docksidestage;

import org.dbflute.tomcat.TomcatBoot;

/**
 * @author jflute
 */
public class ShowbaseBoot { // #change_it_first

    public static final String CONTEXT = "/showbase";

    public static void main(String[] args) { // e.g. java -Dlasta.env=production -jar maihama-showbase.war
        TomcatBoot boot = new TomcatBoot(8098, CONTEXT).asDevelopment(isDevelopment());
        boot.useMetaInfoResourceDetect().useWebFragmentsDetect(jarName -> { // both for swagger
            return jarName.contains("swagger-ui"); // meanwhile, restricted by [app]_env.properties
        });
        boot.configure("showbase_config.properties", "showbase_env.properties", "maihama_config.properties", "maihama_env.properties");
        boot.bootAwait();
    }

    private static boolean isDevelopment() {
        return System.getProperty("lasta.env") == null;
    }
}
