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

import org.lastaflute.job.LaJob;
import org.lastaflute.job.LaJobRuntime;

/**
 * @author jflute
 */
public class BonvoJob implements LaJob {

    public static final String BOOOOOOOON = "Boooooooon!";

    // ===================================================================================
    //                                                                             Job Run
    //                                                                             =======
    @Override
    public void run(LaJobRuntime runtime) { // exception job!
        throw new IllegalStateException(BOOOOOOOON);
    }
}
