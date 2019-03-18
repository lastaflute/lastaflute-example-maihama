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
package org.docksidestage.app.job.challenge;

import java.time.LocalDateTime;

import org.lastaflute.job.LaJob;
import org.lastaflute.job.LaJobRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jflute
 */
public class PiariJob implements LaJob {

    private static final Logger logger = LoggerFactory.getLogger(PiariJob.class);

    // ===================================================================================
    //                                                                             Job Run
    //                                                                             =======
    @Override
    public void run(LaJobRuntime runtime) { // empty job!
        LocalDateTime executionDateTime = (LocalDateTime) runtime.getParameterMap().get("executionDateTime");
        if (executionDateTime != null) { // means from launch-now
            logger.debug("...Accepting executionDateTime: {}", executionDateTime);
        }
        int count = 0;
        while (true) {
            if (count > 30) {
                break;
            }
            try {
                logger.debug("#job ...Sleeping piari: {}", count);
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runtime.stopIfNeeds();
            ++count;
        }
        runtime.showEndTitleRoll(data -> {
            data.register("empty", true);
            if (executionDateTime != null) {
                data.register("executionDateTime", executionDateTime);
            }
        });
    }
}
