/*
 * Copyright 2015-2016 the original author or authors.
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
package org.docksidestage.app.job;

import javax.annotation.Resource;

import org.docksidestage.mylasta.direction.OrleansConfig;
import org.lastaflute.job.LaCron;
import org.lastaflute.job.LaScheduler;

/**
 * @author jflute
 */
public class AllJobScheduler implements LaScheduler {

    @Resource
    private OrleansConfig orleansConfig;

    @Override
    public void schedule(LaCron cron) {
        System.out.println("@@@: " + this + " :: " + getClass().getClassLoader() + " :: " + orleansConfig.getDomainName());
        cron.register("* * * * *", () -> SeaJob.class);
    }
}
