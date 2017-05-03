/*
 * Copyright 2015-2017 the original author or authors.
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

import javax.annotation.Resource;

import org.docksidestage.app.web.base.OrleansBaseAction;
import org.lastaflute.job.JobManager;
import org.lastaflute.job.LaJobHistory;
import org.lastaflute.job.LaScheduledJob;
import org.lastaflute.job.key.LaJobUnique;
import org.lastaflute.job.subsidiary.LaunchNowOption;
import org.lastaflute.job.subsidiary.LaunchedProcess;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.servlet.request.ResponseManager;

/**
 * @author jflute
 */
public class JobExecuteAction extends OrleansBaseAction {

    @Resource
    private JobManager jobManager;
    @Resource
    private ResponseManager responseManager;

    @Execute
    public JsonResponse<JobExecuteResult> index(JobExecuteBody body) {
        validateApi(body, messages -> {});
        LaScheduledJob job = findJob(body);

        LaunchedProcess process = job.launchNow(op -> mappingToParams(body, op));
        LaJobHistory history = process.waitForEnding().get();

        JobExecuteResult result = new JobExecuteResult(history);
        return asJson(result);
    }

    private LaScheduledJob findJob(JobExecuteBody body) {
        return jobManager.findJobByUniqueOf(LaJobUnique.of(body.jobCode)).orElseTranslatingThrow(cause -> {
            return responseManager.new400("Not found the job: " + body.jobCode, op -> op.cause(cause));
        });
    }

    private void mappingToParams(JobExecuteBody body, LaunchNowOption op) {
        op.param("execTime", body.execTime);
        if (body.varyingParameter != null) {
            body.varyingParameter.forEach((key, value) -> op.param(key, value));
        }
    }
}
