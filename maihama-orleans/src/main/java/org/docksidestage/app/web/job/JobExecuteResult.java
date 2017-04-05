package org.docksidestage.app.web.job;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.lastaflute.job.LaJobHistory;
import org.lastaflute.web.validation.Required;

/**
 * @author jflute
 */
public class JobExecuteResult {

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
    public final Boolean errorEnding;
    public final String errorMessage; // null if no error e.g. quit, success

    public JobExecuteResult(LaJobHistory history) {
        this.jobUnique = history.getJobUnique().get().value(); // always present in this action
        this.jobTypeFqcn = history.getJobTypeFqcn();
        this.activationTime = history.getActivationTime();
        this.beginTime = history.getBeginTime().orElse(null);
        this.endTime = history.getEndTime().orElse(null);
        this.endTitleRoll = history.getEndTitleRollSnapshotMap();
        this.errorEnding = history.getExecResultType().isErrorResult(); // want to get detail info?
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
}
