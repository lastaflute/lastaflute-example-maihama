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
package org.docksidestage.app.logic.startup;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.dbflute.util.Srl;

/**
 * @author jflute
 */
public class StartupLogic {

    public void fromDockside(File repositoryDir, String domain, String serviceName, String appName) {
        new NewProjectCreator("dockside", repositoryDir, "maihama-dockside", original -> {
            String filtered = original;
            filtered = filterCommonItem(domain, serviceName, filtered);
            filtered = replace(filtered, "Dockside", Srl.initCap(appName));
            filtered = replace(filtered, "dockside", Srl.initUncap(appName));
            filtered = replace(filtered, "new JettyBoot(8091, ", "new JettyBoot(9001, ");
            filtered = replace(filtered, "new TomcatBoot(8091, ", "new TomcatBoot(9001, ");
            return filtered;
        }).newProject();
    }

    public void fromHangar(File repositoryDir, String domain, String serviceName, String appName) {
        new NewProjectCreator("hangar", repositoryDir, "maihama-hangar", original -> {
            String filtered = original;
            filtered = filterCommonItem(domain, serviceName, filtered);
            filtered = replace(filtered, "Hangar", Srl.initCap(appName));
            filtered = replace(filtered, "hangar", Srl.initUncap(appName));
            filtered = replace(filtered, "new JettyBoot(8092, ", "new JettyBoot(9001, "); // as main
            filtered = replace(filtered, "new TomcatBoot(8092, ", "new TomcatBoot(9001, "); // as main
            return filtered;
        }).newProject();
    }

    protected String filterCommonItem(String domain, String serviceName, String filtered) {
        String packageName = buildPackageName(domain);
        filtered = replace(filtered, "lastaflute-example-maihama", Srl.initUncap(serviceName));
        filtered = replace(filtered, "maihamadb", Srl.initUncap(serviceName) + (!serviceName.endsWith("db") ? "db" : ""));
        filtered = replace(filtered, "org/docksidestage", replace(packageName, ".", "/"));
        filtered = replace(filtered, "docksidestage.org", domain);
        filtered = replace(filtered, "org.docksidestage", packageName);
        filtered = replace(filtered, "Maihama", Srl.initCap(serviceName));
        filtered = replace(filtered, "maihama", Srl.initUncap(serviceName));
        return filtered;
    }

    protected String buildPackageName(String domain) {
        List<String> elementList = new ArrayList<String>(Arrays.asList(domain.split("\\.")));
        Collections.reverse(elementList);
        return elementList.stream().reduce((left, right) -> left + "." + right).get();
    }

    protected String replace(String str, String fromStr, String toStr) {
        return Srl.replace(str, fromStr, toStr);
    }
}
