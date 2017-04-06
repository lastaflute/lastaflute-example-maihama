package org.docksidestage.mylasta.direction.sponsor;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dbflute.helper.message.ExceptionMessageBuilder;
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.Srl;
import org.docksidestage.mylasta.direction.sponsor.ShowbaseApiFailureHook.ShowbaseUnifiedFailureResult.ShowbaseFailureErrorPart;
import org.lastaflute.core.json.JsonManager;
import org.lastaflute.web.api.ApiFailureHook;
import org.lastaflute.web.api.ApiFailureResource;
import org.lastaflute.web.response.ApiResponse;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.validation.Required;

/**
 * @author jflute
 */
public class ShowbaseApiFailureHook implements ApiFailureHook {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    protected static final int BUSINESS_FAILURE_STATUS = HttpServletResponse.SC_BAD_REQUEST;

    // ===================================================================================
    //                                                                    Business Failure
    //                                                                    ================
    @Override
    public ApiResponse handleValidationError(ApiFailureResource resource) {
        final ShowbaseUnifiedFailureResult result = createFailureResult(ShowbaseUnifiedFailureType.VALIDATION_ERROR, resource);
        return asJson(result).httpStatus(BUSINESS_FAILURE_STATUS);
    }

    @Override
    public ApiResponse handleApplicationException(ApiFailureResource resource, RuntimeException cause) {
        final ShowbaseUnifiedFailureResult result = createFailureResult(ShowbaseUnifiedFailureType.BUSINESS_ERROR, resource);
        return asJson(result).httpStatus(BUSINESS_FAILURE_STATUS);
    }

    // ===================================================================================
    //                                                                      System Failure
    //                                                                      ==============
    @Override
    public OptionalThing<ApiResponse> handleClientException(ApiFailureResource resource, RuntimeException cause) {
        final ShowbaseUnifiedFailureResult result = createFailureResult(ShowbaseUnifiedFailureType.CLIENT_ERROR, resource);
        return OptionalThing.of(asJson(result)); // HTTP status will be automatically sent as client error for the cause
    }

    @Override
    public OptionalThing<ApiResponse> handleServerException(ApiFailureResource resource, Throwable cause) {
        return OptionalThing.empty(); // means empty body, HTTP status will be automatically sent as server error
    }

    // ===================================================================================
    //                                                                      Failure Result
    //                                                                      ==============
    protected ShowbaseUnifiedFailureResult createFailureResult(ShowbaseUnifiedFailureType failureType, ApiFailureResource resource) {
        return new ShowbaseUnifiedFailureResult(failureType, toErrors(resource, resource.getPropertyMessageMap()));
    }

    protected List<ShowbaseFailureErrorPart> toErrors(ApiFailureResource resource, Map<String, List<String>> propertyMessageMap) {
        return propertyMessageMap.entrySet().stream().flatMap(entry -> {
            return toFailureErrorPart(resource, entry.getKey(), entry.getValue()).stream();
        }).collect(Collectors.toList());
    }

    protected List<ShowbaseFailureErrorPart> toFailureErrorPart(ApiFailureResource resource, String field, List<String> messageList) {
        final String delimiter = "|";
        return messageList.stream().map(message -> {
            if (message.contains(delimiter)) { // e.g. LENGTH | min:{min}, max:{max}
                return createJsonistaError(resource, field, message, delimiter);
            } else { // e.g. REQUIRED
                return createSimpleError(field, message);
            }
        }).collect(Collectors.toList());
    }

    // -----------------------------------------------------
    //                                        Jsonista Error
    //                                        --------------
    protected ShowbaseFailureErrorPart createJsonistaError(ApiFailureResource resource, String field, String message, String delimiter) {
        final String code = Srl.substringFirstFront(message, delimiter).trim(); // e.g. LENGTH
        final String json = "{" + Srl.substringFirstRear(message, delimiter).trim() + "}"; // e.g. {min:{min}, max:{max}}
        final Map<String, Object> data = parseJsonistaData(resource, field, code, json);
        return new ShowbaseFailureErrorPart(field, code, filterDataParserHeadache(data));
    }

    @SuppressWarnings("unchecked")
    protected Map<String, Object> parseJsonistaData(ApiFailureResource resource, String field, String code, String json) {
        try {
            final JsonManager jsonManager = resource.getRequestManager().getJsonManager();
            return jsonManager.fromJson(json, Map.class);
        } catch (RuntimeException e) {
            final ExceptionMessageBuilder br = new ExceptionMessageBuilder();
            br.addNotice("Failed to parse client-managed message data.");
            br.addItem("Advice");
            br.addElement("Arrange your [app]_message.properties");
            br.addElement("for client-managed message way like this:");
            br.addElement("  constraints.Length.message = LENGTH | min:{min}, max:{max}");
            br.addElement("  constraints.Required.message = REQUIRED");
            br.addElement("  ...");
            br.addItem("Message List");
            br.addElement(resource.getMessageList());
            br.addItem("Target Field");
            br.addElement(field);
            br.addItem("Error Code");
            br.addElement(code);
            br.addItem("Data as JSON");
            br.addElement(json);
            final String msg = br.buildExceptionMessage();
            throw new IllegalStateException(msg, e);
        }
    }

    protected Map<String, Object> filterDataParserHeadache(Map<String, Object> data) {
        if (data.isEmpty()) {
            return data;
        }
        final Map<String, Object> filteredMap = new LinkedHashMap<String, Object>(data.size());
        data.entrySet().stream().forEach(entry -> {
            Object value = entry.getValue();
            if (value instanceof Double) { // Gson already parses number as double in map
                final Double dble = (Double) value;
                if (Srl.rtrim(dble.toString(), "0").endsWith(".")) { // might be not decimal
                    value = dble.intValue();
                }
            }
            filteredMap.put(entry.getKey(), value);
        });
        return filteredMap;
    }

    // -----------------------------------------------------
    //                                          Simple Error
    //                                          ------------
    protected ShowbaseFailureErrorPart createSimpleError(String field, String message) {
        return new ShowbaseFailureErrorPart(field, message, Collections.emptyMap());
    }

    // ===================================================================================
    //                                                                      Failure Result
    //                                                                      ==============
    public static class ShowbaseUnifiedFailureResult {

        @Required
        public final ShowbaseUnifiedFailureType cause;

        @NotNull
        @Valid
        public final List<ShowbaseFailureErrorPart> errors;

        public static class ShowbaseFailureErrorPart {

            @Required
            public final String field;

            @Required
            public final String code;

            @NotNull
            public final Map<String, Object> data;

            public ShowbaseFailureErrorPart(String field, String code, Map<String, Object> data) {
                this.field = field;
                this.code = code;
                this.data = data;
            }
        }

        public ShowbaseUnifiedFailureResult(ShowbaseUnifiedFailureType cause, List<ShowbaseFailureErrorPart> errors) {
            this.cause = cause;
            this.errors = errors;
        }
    }

    public static enum ShowbaseUnifiedFailureType {
        VALIDATION_ERROR, BUSINESS_ERROR, CLIENT_ERROR
        // SERVER_ERROR is implemented by 500.json
    }

    // ===================================================================================
    //                                                                       JSON Response
    //                                                                       =============
    protected <RESULT> JsonResponse<RESULT> asJson(RESULT result) {
        return new JsonResponse<RESULT>(result);
    }
}
