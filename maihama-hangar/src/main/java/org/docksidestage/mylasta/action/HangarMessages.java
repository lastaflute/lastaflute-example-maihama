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
package org.docksidestage.mylasta.action;

import org.docksidestage.mylasta.action.HangarLabels;
import org.lastaflute.core.message.UserMessage;

/**
 * The keys for message.
 * @author FreeGen
 */
public class HangarMessages extends HangarLabels {

    /** The serial version UID for object serialization. (Default) */
    private static final long serialVersionUID = 1L;

    /** The key of the message: LOGIN_REQUIRED */
    public static final String ERRORS_LOGIN_REQUIRED = "{errors.login.required}";

    /** The key of the message: UNKNOWN_BUSINESS_ERROR */
    public static final String ERRORS_UNKNOWN_BUSINESS_ERROR = "{errors.unknown.business.error}";

    /** The key of the message: SIGNUP_ACCOUNT_ALREADY_EXISTS */
    public static final String ERRORS_SIGNUP_ACCOUNT_ALREADY_EXISTS = "{errors.signup.account.already.exists}";

    /**
     * Add the created action message for the key 'constraints.AssertFalse.message' with parameters.
     * <pre>
     * message: ASSERT_FALSE
     * comment: @Override ---------------
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsAssertFalseMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_AssertFalse_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.AssertTrue.message' with parameters.
     * <pre>
     * message: ASSERT_TRUE
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsAssertTrueMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_AssertTrue_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.DecimalMax.message' with parameters.
     * <pre>
     * message: DECIMAL_MAX | inclusive:${inclusive == true ? 'true' : 'false'}, max:{value}
     * comment: @Override inclusive statement to avoid parameter mismatch with super properties
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsDecimalMaxMessage(String property, String value) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_DecimalMax_MESSAGE, value));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.DecimalMin.message' with parameters.
     * <pre>
     * message: DECIMAL_MIN | inclusive:${inclusive == true ? 'true' : 'false'}, min:{value}
     * comment: @Override me too
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsDecimalMinMessage(String property, String value) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_DecimalMin_MESSAGE, value));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Digits.message' with parameters.
     * <pre>
     * message: DIGITS | integer:{integer}, fraction:{fraction}
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param integer The parameter integer for message. (NotNull)
     * @param fraction The parameter fraction for message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsDigitsMessage(String property, String integer, String fraction) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_Digits_MESSAGE, integer, fraction));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Future.message' with parameters.
     * <pre>
     * message: FUTURE
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsFutureMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_Future_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Max.message' with parameters.
     * <pre>
     * message: MAX | max:{value}
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsMaxMessage(String property, String value) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_Max_MESSAGE, value));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Min.message' with parameters.
     * <pre>
     * message: MIN | min:{value}
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsMinMessage(String property, String value) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_Min_MESSAGE, value));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.NotNull.message' with parameters.
     * <pre>
     * message: NOT_NULL
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsNotNullMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_NotNull_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Null.message' with parameters.
     * <pre>
     * message: NULL
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsNullMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_Null_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Past.message' with parameters.
     * <pre>
     * message: PAST
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsPastMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_Past_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Pattern.message' with parameters.
     * <pre>
     * message: PATTERN
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsPatternMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_Pattern_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Size.message' with parameters.
     * <pre>
     * message: SIZE | min:{min}, max:{max}
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param min The parameter min for message. (NotNull)
     * @param max The parameter max for message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsSizeMessage(String property, String min, String max) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_Size_MESSAGE, min, max));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.CreditCardNumber.message' with parameters.
     * <pre>
     * message: CREDIT_CARD_NUMBER
     * comment: @Override -------------------
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsCreditCardNumberMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_CreditCardNumber_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.EAN.message' with parameters.
     * <pre>
     * message: EAN | type:{type}
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param type The parameter type for message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsEanMessage(String property, String type) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_EAN_MESSAGE, type));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Email.message' with parameters.
     * <pre>
     * message: EMAIL
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsEmailMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_Email_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Length.message' with parameters.
     * <pre>
     * message: LENGTH | min:{min}, max:{max}
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param min The parameter min for message. (NotNull)
     * @param max The parameter max for message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsLengthMessage(String property, String min, String max) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_Length_MESSAGE, min, max));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.LuhnCheck.message' with parameters.
     * <pre>
     * message: LuhnCheck | unused for now so parse failure, ${value} for freegen
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsLuhnCheckMessage(String property, String value) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_LuhnCheck_MESSAGE, value));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Mod10Check.message' with parameters.
     * <pre>
     * message: Mod10Check | unused for now so parse failure, ${value} for freegen
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsMod10CheckMessage(String property, String value) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_Mod10Check_MESSAGE, value));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Mod11Check.message' with parameters.
     * <pre>
     * message: Mod11Check | unused for now so parse failure, ${value} for freegen
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsMod11CheckMessage(String property, String value) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_Mod11Check_MESSAGE, value));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.ModCheck.message' with parameters.
     * <pre>
     * message: ModCheck | unused for now so parse failure, ${value} ${modType} for freegen
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @param modType The parameter modType for message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsModCheckMessage(String property, String value, String modType) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_ModCheck_MESSAGE, value, modType));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.NotBlank.message' with parameters.
     * <pre>
     * message: NOT_BLANK
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsNotBlankMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_NotBlank_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.NotEmpty.message' with parameters.
     * <pre>
     * message: NOT_EMPTY
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsNotEmptyMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_NotEmpty_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.ParametersScriptAssert.message' with parameters.
     * <pre>
     * message: PARAMETERS_SCRIPT_ASSERT | script:"{script}"
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param script The parameter script for message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsParametersScriptAssertMessage(String property, String script) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_ParametersScriptAssert_MESSAGE, script));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Range.message' with parameters.
     * <pre>
     * message: RANGE | min:{min}, max:{max}
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param min The parameter min for message. (NotNull)
     * @param max The parameter max for message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsRangeMessage(String property, String min, String max) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_Range_MESSAGE, min, max));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.SafeHtml.message' with parameters.
     * <pre>
     * message: SAFE_HTML
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsSafeHtmlMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_SafeHtml_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.ScriptAssert.message' with parameters.
     * <pre>
     * message: SCRIPT_ASSERT | script:"{script}"
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param script The parameter script for message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsScriptAssertMessage(String property, String script) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_ScriptAssert_MESSAGE, script));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.URL.message' with parameters.
     * <pre>
     * message: URL
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsUrlMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_URL_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Required.message' with parameters.
     * <pre>
     * message: REQUIRED
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsRequiredMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_Required_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.TypeAny.message' with parameters.
     * <pre>
     * message: TYPE_ANY | should be {propertyType}
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param propertyType The parameter propertyType for message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsTypeAnyMessage(String property, String propertyType) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_TypeAny_MESSAGE, propertyType));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.TypeInteger.message' with parameters.
     * <pre>
     * message: TYPE_NUMBER
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsTypeIntegerMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_TypeInteger_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.TypeLong.message' with parameters.
     * <pre>
     * message: TYPE_NUMBER
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsTypeLongMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_TypeLong_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.TypeLocalDate.message' with parameters.
     * <pre>
     * message: TYPE_DATE
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsTypeLocalDateMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_TypeLocalDate_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.TypeLocalDateTime.message' with parameters.
     * <pre>
     * message: TYPE_DATETIME
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsTypeLocalDateTimeMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_TypeLocalDateTime_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.TypeBoolean.message' with parameters.
     * <pre>
     * message: TYPE_BOOLEAN
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addConstraintsTypeBooleanMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(CONSTRAINTS_TypeBoolean_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.login.failure' with parameters.
     * <pre>
     * message: LOGIN_FAILURE
     * comment: @Override - - - - - - - - - -/
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addErrorsLoginFailure(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(ERRORS_LOGIN_FAILURE));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.app.illegal.transition' with parameters.
     * <pre>
     * message: ILLEGAL_TRANSITION
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addErrorsAppIllegalTransition(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(ERRORS_APP_ILLEGAL_TRANSITION));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.app.db.already.deleted' with parameters.
     * <pre>
     * message: ALREADY_DELETED
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addErrorsAppDbAlreadyDeleted(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(ERRORS_APP_DB_ALREADY_DELETED));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.app.db.already.updated' with parameters.
     * <pre>
     * message: ALREADY_UPDATED
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addErrorsAppDbAlreadyUpdated(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(ERRORS_APP_DB_ALREADY_UPDATED));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.app.db.already.exists' with parameters.
     * <pre>
     * message: ALREADY_EXISTS
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addErrorsAppDbAlreadyExists(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(ERRORS_APP_DB_ALREADY_EXISTS));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.app.double.submit.request' with parameters.
     * <pre>
     * message: DOUBLE_SUBMIT
     * comment: @Override
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    @Override
    public HangarMessages addErrorsAppDoubleSubmitRequest(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(ERRORS_APP_DOUBLE_SUBMIT_REQUEST));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.login.required' with parameters.
     * <pre>
     * message: LOGIN_REQUIRED
     * comment: framework does not have own message so define here, used in your ApiFailureHook
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public HangarMessages addErrorsLoginRequired(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(ERRORS_LOGIN_REQUIRED));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.unknown.business.error' with parameters.
     * <pre>
     * message: UNKNOWN_BUSINESS_ERROR
     * comment: for no-message application exception, basically should not be used
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public HangarMessages addErrorsUnknownBusinessError(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(ERRORS_UNKNOWN_BUSINESS_ERROR));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.signup.account.already.exists' with parameters.
     * <pre>
     * message: SIGNUP_ACCOUNT_ALREADY_EXISTS
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public HangarMessages addErrorsSignupAccountAlreadyExists(String property) {
        assertPropertyNotNull(property);
        add(property, new UserMessage(ERRORS_SIGNUP_ACCOUNT_ALREADY_EXISTS));
        return this;
    }
}
