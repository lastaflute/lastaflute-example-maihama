/*
 * Copyright 2015-2019 the original author or authors.
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
package org.docksidestage.app.web.job;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.lastaflute.core.util.Lato;
import org.lastaflute.job.LaJobHistory;
import org.lastaflute.job.subsidiary.ExecResultType;
import org.lastaflute.web.validation.Required;

/**
 * @author jflute
 */
public class JobExecuteResult {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Required
    public final String jobUnique;
    @Required
    public final String jobTypeFqcn;
    @Required
    public final LocalDateTime activationTime;
    public final LocalDateTime beginTime; // null if e.g. duplicate execution
    public final LocalDateTime endTime; // me too
    @NotNull
    public Map<String, String> endTitleRoll;
    @Required
    public final ExecResultTypePart execResultType;

    public enum ExecResultTypePart { // mapping class not to depend on framework

        SUCCESS // no cause
        , QUIT_BY_CONCURRENT // no execution as quit
        , ERROR_BY_CONCURRENT // no execution as error
        , CAUSED_BY_APPLICATION // exception thrown by application
        , CAUSED_BY_FRAMEWORK // exception thrown by framework
        , UNKNOWN; // no way! but just in case

        public static ExecResultTypePart of(ExecResultType nativeType) {
            if (ExecResultType.SUCCESS.equals(nativeType)) {
                return SUCCESS;
            } else if (ExecResultType.QUIT_BY_CONCURRENT.equals(nativeType)) {
                return QUIT_BY_CONCURRENT;
            } else if (ExecResultType.ERROR_BY_CONCURRENT.equals(nativeType)) {
                return ERROR_BY_CONCURRENT;
            } else if (ExecResultType.CAUSED_BY_APPLICATION.equals(nativeType)) {
                return CAUSED_BY_APPLICATION;
            } else if (ExecResultType.CAUSED_BY_FRAMEWORK.equals(nativeType)) {
                return CAUSED_BY_FRAMEWORK;
            } else { // no way!
                return UNKNOWN;
            }
        }
    }

    @Required
    public final Boolean errorEnding;
    public final String errorMessage; // null if no error e.g. quit, success

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public JobExecuteResult(LaJobHistory history) {
        this.jobUnique = history.getJobUnique().get().value(); // always present in this action
        this.jobTypeFqcn = history.getJobTypeFqcn();
        this.activationTime = history.getActivationTime();
        this.beginTime = history.getBeginTime().orElse(null);
        this.endTime = history.getEndTime().orElse(null);
        this.endTitleRoll = history.getEndTitleRollSnapshotMap();
        this.execResultType = ExecResultTypePart.of(history.getExecResultType());
        this.errorEnding = history.getExecResultType().isErrorResult(); // facade
        this.errorMessage = history.getCause().map(cause -> buildExceptionStackTrace(cause)).orElse(null);
    }

    private String buildExceptionStackTrace(Throwable cause) {
        final StringBuilder sb = new StringBuilder();
        final ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        PrintStream ps = null;
        try {
            ps = new PrintStream(out);
            cause.printStackTrace(ps);
            final String encoding = "UTF-8";
            try {
                sb.append(out.toString(encoding));
            } catch (UnsupportedEncodingException continued) {
                sb.append(out.toString()); // retry without encoding
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return sb.toString();
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    public String toString() {
        return Lato.string(this);
    }
}
