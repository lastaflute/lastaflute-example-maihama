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
import org.lastaflute.job.subsidiary.LaunchedProcess;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 */
public class JobExecuteAction extends OrleansBaseAction {

    @Resource
    private JobManager jobManager;

    @Execute
    public JsonResponse<JobExecuteResult> index(String jobUnique) {
        LaScheduledJob job = jobManager.findJobByUniqueOf(LaJobUnique.of(jobUnique)).get();

        LaunchedProcess process = job.launchNow();
        LaJobHistory history = process.waitForEnding().get();

        JobExecuteResult result = new JobExecuteResult(history);
        return asJson(result);
    }
}
