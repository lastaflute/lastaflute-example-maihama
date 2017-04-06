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

import java.time.LocalDateTime;

import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.docksidestage.app.job.challenge.BonvoJob;
import org.docksidestage.app.job.challenge.PiariJob;
import org.docksidestage.app.web.job.JobExecuteResult.ExecResultTypePart;
import org.docksidestage.unit.UnitOrleansTestCase;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 */
public class JobExecuteActionTest extends UnitOrleansTestCase {

    public void test_index_success() {
        // ## Arrange ##
        JobExecuteAction action = new JobExecuteAction();
        inject(action);
        LocalDateTime before = currentLocalDateTime();

        // ## Act ##
        JsonResponse<JobExecuteResult> response = action.index("piari");

        // ## Assert ##
        showJson(response);
        TestingJsonData<JobExecuteResult> jsonData = validateJsonData(response);
        JobExecuteResult result = jsonData.getJsonResult();
        assertJobIdentity("piari", PiariJob.class, result);
        assertTimeBeforeAfter(result, before);
        assertEquals("true", result.endTitleRoll.get("empty"));
        assertEquals(ExecResultTypePart.SUCCESS, result.execResultType);
        assertFalse(result.errorEnding);
        assertNull(result.errorMessage);
    }

    public void test_index_causedByApplication() {
        // ## Arrange ##
        JobExecuteAction action = new JobExecuteAction();
        inject(action);
        LocalDateTime before = currentLocalDateTime();

        // ## Act ##
        JsonResponse<JobExecuteResult> response = action.index("bonvo");

        // ## Assert ##
        showJson(response);
        TestingJsonData<JobExecuteResult> jsonData = validateJsonData(response);
        JobExecuteResult result = jsonData.getJsonResult();
        assertJobIdentity("bonvo", BonvoJob.class, result);
        assertTimeBeforeAfter(result, before);
        assertEquals(ExecResultTypePart.CAUSED_BY_APPLICATION, result.execResultType);
        assertTrue(result.errorEnding);
        assertNotNull(result.errorMessage);
        assertContains(result.errorMessage, BonvoJob.BOOOOOOOON);
    }

    private void assertJobIdentity(String jobUnique, Class<?> jobType, JobExecuteResult result) {
        assertEquals(jobUnique, result.jobUnique);
        assertEquals(jobType.getName(), result.jobTypeFqcn);
    }

    private void assertTimeBeforeAfter(JobExecuteResult result, LocalDateTime before) {
        assertTrue(result.activationTime.isAfter(before));
        assertNotNull(result.beginTime);
        assertTrue(result.beginTime.isAfter(result.activationTime));
        assertNotNull(result.endTime);
        assertTrue(result.endTime.isAfter(result.beginTime));
    }
}
