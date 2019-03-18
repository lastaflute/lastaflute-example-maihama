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

import java.util.Map;

import org.docksidestage.unit.UnitOrleansJobTestCase;
import org.lastaflute.job.mock.MockJobRuntime;

/**
 * @author jflute
 */
public class SeaJobTest extends UnitOrleansJobTestCase {

    public void test_run_basic() {
        // ## Arrange ##
        changeRequiresNewToRequired();
        SeaJob job = new SeaJob();
        inject(job);
        MockJobRuntime runtime = mockRuntime(job);

        // ## Act ##
        job.run(runtime);

        // ## Assert ##
        Map<String, Object> rollMap = runtime.getEndTitleRollMap();
        log(rollMap);
        assertEquals(3, rollMap.get("targetMember")); // #simple_for_example
    }
}
