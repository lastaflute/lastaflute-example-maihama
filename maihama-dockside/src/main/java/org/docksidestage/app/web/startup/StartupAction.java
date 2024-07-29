package org.docksidestage.app.web.startup;

import java.io.File;

import org.dbflute.util.DfResourceUtil;
import org.dbflute.util.Srl;
import org.docksidestage.app.logic.startup.StartupLogic;
import org.docksidestage.app.web.base.DocksideBaseAction;
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.HtmlResponse;

import jakarta.annotation.Resource;

/**
 * @author jflute
 */
@AllowAnyoneAccess
public class StartupAction extends DocksideBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private StartupLogic startupLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public HtmlResponse index() {
        return asHtml(path_Startup_StartupHtml);
    }

    @Execute
    public HtmlResponse create(StartupForm form) {
        validate(form, messages -> {}, () -> {
            return asHtml(path_Startup_StartupHtml);
        });

        File projectDir = getProjectDir();
        startupLogic.fromDockside(projectDir, form.domain, form.serviceName, form.appName);

        StartupBean bean = mappingToBean(form, projectDir);
        return asHtml(path_Startup_StartupHtml).renderWith(data -> {
            data.register("bean", bean);
        });
    }

    private File getProjectDir() { // e.g. [workspace]/[project]/target/classes
        return DfResourceUtil.getBuildDir(getClass()).getParentFile().getParentFile().getParentFile();
    }

    private StartupBean mappingToBean(StartupForm form, File projectDir) {
        String projectPath = Srl.replace(projectDir.getParentFile().getPath(), "\\", "/") + "/" + form.serviceName;
        return new StartupBean(projectPath);
    }
}
