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
package org.docksidestage.app.job;

import javax.annotation.Resource;

import org.dbflute.optional.OptionalThing;
import org.dbflute.util.DfCollectionUtil;
import org.docksidestage.app.job.challenge.BonvoJob;
import org.docksidestage.app.job.challenge.DstoreJob;
import org.docksidestage.app.job.challenge.PiariJob;
import org.docksidestage.app.logic.context.AccessContextLogic;
import org.lastaflute.job.LaCron;
import org.lastaflute.job.LaJobRunner;
import org.lastaflute.job.LaJobScheduler;

/**
 * @author jflute
 */
public class AllJobScheduler implements LaJobScheduler {

    protected static final String APP_TYPE = "JOB";

    @Resource
    private AccessContextLogic accessContextLogic;

    @Override
    public void schedule(LaCron cron) {
        // basic example
        cron.register("* * * * *", SeaJob.class, waitIfConcurrent(), op -> {});
        cron.register("*/1 * * * *", LandJob.class, quitIfConcurrent(), op -> {});
        cron.register("*/2 * * * *", LandJob.class, quitIfConcurrent(), op -> {});

        // various challenge
        cron.registerNonCron(PiariJob.class, quitIfConcurrent(), op -> op.uniqueBy("piari").params(() -> {
            return DfCollectionUtil.newHashMap("celebration", "plaza");
        }));
        cron.registerNonCron(BonvoJob.class, errorIfConcurrent(), op -> op.uniqueBy("bonvo"));
        cron.registerNonCron(DstoreJob.class, errorIfConcurrent(), op -> op.uniqueBy("dstore"));
    }

    @Override
    public LaJobRunner createRunner() {
        return new LaJobRunner().useAccessContext(resource -> {
            return accessContextLogic.create(resource, () -> optEmpty(), () -> optEmpty(), () -> APP_TYPE, () -> optEmpty());
        });
    }

    private <EMPTY> OptionalThing<EMPTY> optEmpty() {
        return OptionalThing.empty();
    }
}
