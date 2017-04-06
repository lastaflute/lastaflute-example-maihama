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
