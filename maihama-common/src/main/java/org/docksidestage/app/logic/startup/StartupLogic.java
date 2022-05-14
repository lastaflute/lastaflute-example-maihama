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
package org.docksidestage.app.logic.startup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.dbflute.util.Srl;

/**
 * @author jflute
 */
public class StartupLogic {

    // ===================================================================================
    //                                                                            Dockside
    //                                                                            ========
    public void fromDockside(File repositoryDir, String domain, String serviceName, String appName) {
        newProjectFromDockside(repositoryDir, domain, serviceName, appName, false);
    }

    public void fromDocksideAppOnly(File repositoryDir, String domain, String serviceName, String appName) {
        newProjectFromDockside(repositoryDir, domain, serviceName, appName, true);
    }

    protected void newProjectFromDockside(File repositoryDir, String domain, String serviceName, String appName, boolean appOnly) {
        new NewProjectCreator("dockside", repositoryDir, "maihama-dockside", original -> {
            String filtered = original;
            filtered = filterCommonItem(repositoryDir, domain, serviceName, filtered);
            filtered = replace(filtered, "Dockside", Srl.initCap(appName));
            filtered = replace(filtered, "dockside", Srl.initUncap(appName));
            filtered = replace(filtered, "new JettyBoot(8091, ", "new JettyBoot(9001, ");
            filtered = replace(filtered, "new TomcatBoot(8091, ", "new TomcatBoot(9001, ");
            return filtered;
        }, appOnly).newProject();
    }

    // ===================================================================================
    //                                                                              Hangar
    //                                                                              ======
    public void fromHangar(File repositoryDir, String domain, String serviceName, String appName) {
        newProjectFromHangar(repositoryDir, domain, serviceName, appName, false);
    }

    public void fromHangarAppOnly(File repositoryDir, String domain, String serviceName, String appName) {
        newProjectFromHangar(repositoryDir, domain, serviceName, appName, true);
    }

    protected void newProjectFromHangar(File repositoryDir, String domain, String serviceName, String appName, boolean appOnly) {
        new NewProjectCreator("hangar", repositoryDir, "maihama-hangar", original -> {
            String filtered = original;
            filtered = filterCommonItem(repositoryDir, domain, serviceName, filtered);
            filtered = replace(filtered, "Hangar", Srl.initCap(appName));
            filtered = replace(filtered, "hangar", Srl.initUncap(appName));
            filtered = replace(filtered, "new JettyBoot(8092, ", "new JettyBoot(9001, "); // as main
            filtered = replace(filtered, "new TomcatBoot(8092, ", "new TomcatBoot(9001, "); // as main
            return filtered;
        }, appOnly).newProject();
    }

    // ===================================================================================
    //                                                                            Showbase
    //                                                                            ========
    public void fromShowbase(File repositoryDir, String domain, String serviceName, String appName) {
        newProjectFromShowbase(repositoryDir, domain, serviceName, appName, false);
    }

    public void fromShowbaseAppOnly(File repositoryDir, String domain, String serviceName, String appName) {
        newProjectFromShowbase(repositoryDir, domain, serviceName, appName, true);
    }

    protected void newProjectFromShowbase(File repositoryDir, String domain, String serviceName, String appName, boolean appOnly) {
        new NewProjectCreator("showbase", repositoryDir, "maihama-showbase", original -> {
            String filtered = original;
            filtered = filterCommonItem(repositoryDir, domain, serviceName, filtered);
            filtered = replace(filtered, "Showbase", Srl.initCap(appName));
            filtered = replace(filtered, "showbase", Srl.initUncap(appName));
            filtered = replace(filtered, "new JettyBoot(8098, ", "new JettyBoot(9001, "); // as main
            filtered = replace(filtered, "new TomcatBoot(8098, ", "new TomcatBoot(9001, "); // as main
            return filtered;
        }, appOnly).newProject();
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    protected String filterCommonItem(File repositoryDir, String domain, String serviceName, String filtered) {
        String packageName = buildPackageName(domain);
        filtered = replace(filtered, buildProjectDirPureName(repositoryDir), Srl.initUncap(serviceName)); // e.g. lastaflute-example-harbor
        filtered = replace(filtered, "lastaflute-example-maihama", Srl.initUncap(serviceName)); // just in case
        filtered = replace(filtered, "maihamadb", Srl.initUncap(serviceName) + (!serviceName.endsWith("db") ? "db" : ""));
        filtered = replace(filtered, "org/docksidestage", replace(packageName, ".", "/")); // for file path
        filtered = replace(filtered, "docksidestage.org", domain);
        filtered = replace(filtered, "org.docksidestage", packageName);
        filtered = replace(filtered, "Maihama", Srl.initCap(serviceName));
        filtered = replace(filtered, "maihama", Srl.initUncap(serviceName));
        return filtered;
    }

    protected String buildPackageName(String domain) { // e.g. docksidestage.org to org.docksidestage
        List<String> elementList = new ArrayList<String>(Arrays.asList(domain.split("\\.")));
        Collections.reverse(elementList);
        String pkgName = elementList.stream().reduce((left, right) -> left + "." + right).get();
        return pkgName.replace("-", ""); // e.g. org.dockside-stage to org.docksidestage
    }

    protected String buildProjectDirPureName(File projectDir) { // e.g. /sea/mystic => mystic
        try {
            return Srl.substringLastRear(projectDir.getCanonicalPath(), "/"); // thanks oreilly
        } catch (IOException e) {
            throw new IllegalStateException("Failed to get canonical path: " + projectDir);
        }
    }

    protected String replace(String str, String fromStr, String toStr) {
        return Srl.replace(str, fromStr, toStr);
    }
}
