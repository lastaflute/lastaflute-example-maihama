/*
 * Copyright 2014-2015 the original author or authors.
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
package org.docksidestage.app.web.base;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.dbflute.jdbc.Classification;
import org.dbflute.optional.OptionalThing;
import org.docksidestage.app.logic.i18n.I18nDateLogic;
import org.docksidestage.app.web.base.login.OrleansLoginAssist;
import org.lastaflute.di.helper.beans.BeanDesc;
import org.lastaflute.di.helper.beans.PropertyDesc;
import org.lastaflute.di.helper.beans.factory.BeanDescFactory;
import org.lastaflute.mixer2.view.Mixer2Supporter;
import org.lastaflute.mixer2.view.TypicalMixView;
import org.lastaflute.mixer2.view.resolver.TypicalMixLayoutResolver;
import org.lastaflute.mixer2.view.resolver.TypicalMixStyleResolver;
import org.lastaflute.web.servlet.request.RequestManager;
import org.mixer2.jaxb.xhtml.Form;
import org.mixer2.jaxb.xhtml.Input;
import org.mixer2.jaxb.xhtml.Option;
import org.mixer2.jaxb.xhtml.Select;
import org.mixer2.jaxb.xhtml.Textarea;
import org.mixer2.util.M2StringUtils;

/**
 * @author jflute
 */
public abstract class OrleansBaseView extends TypicalMixView {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // by everywhere injector
    @Resource
    private RequestManager requestManager;
    @Resource
    private I18nDateLogic i18nDateLogic;
    @Resource
    private OrleansLoginAssist orleansLoginAssist;

    // ===================================================================================
    //                                                                              Layout
    //                                                                              ======
    @Override
    protected void customizeLayout(TypicalMixLayoutResolver resolver) {
        resolver.resolveHeader((header, supporter) -> {
            orleansLoginAssist.getSessionUserBean().ifPresent(bean -> {
                // #pending can by class?
                header.replaceById("nav-user-name", bean.getMemberName());
            });
        });
    }

    // ===================================================================================
    //                                                                               Style
    //                                                                               =====
    @Override
    protected TypicalMixStyleResolver createTypicalMixStyleResolver() {
        return super.createTypicalMixStyleResolver().useVersionQuery();
    }

    // ===================================================================================
    //                                                                               Try
    //                                                                            ========
    protected void populateForm(Form form, Object bean, Mixer2Supporter supporter)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        // TODO jflute xxxxxxxx (2015/12/07)
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(bean.getClass());
        final int propertySize = beanDesc.getPropertyDescSize();
        for (int i = 0; i < propertySize; i++) {
            final PropertyDesc pd = beanDesc.getPropertyDesc(i);
            final String propertyName = pd.getPropertyName();
            final Object propertyValue = pd.getValue(bean);
            if (propertyValue == null) { // #thinking or set null?
                continue;
            }
            supporter.findInput(form, propertyName).alwaysPresent(input -> {
                switch (input.getType()) {
                case RADIO:
                    if (input.getValue() != null && input.getValue().equals(convertToInputValue(propertyValue))) {
                        input.setChecked("checked");
                    } else {
                        input.setChecked(null);
                    }
                    break;
                case CHECKBOX:
                    String[] beanValues = null;
                    try {
                        beanValues = BeanUtils.getArrayProperty(bean, input.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    boolean matchBean = false;
                    for (String value : beanValues) {
                        if (value.equals(input.getValue())) {
                            input.setChecked("checked");
                            matchBean = true;
                            continue;
                        }
                    }
                    if (!matchBean) {
                        input.setChecked(null);
                    }
                    break;
                default:
                    input.setValue(convertToInputValue(propertyValue));
                }
            });
        }
        for (Input input : form.getDescendants(Input.class)) {
            // name属性が指定されていない場合はスルーする。
            if (M2StringUtils.isBlank(input.getName())) {
                continue;
            }

            switch (input.getType()) {
            case RADIO:
                // radioは同じnameで複数個ありうるが、ひとつしか選択できない
                String beanValue = BeanUtils.getProperty(bean, input.getName());
                if (input.getValue() != null && input.getValue().equals(beanValue)) {
                    input.setChecked("checked");
                } else {
                    input.setChecked(null);
                }
                break;
            case CHECKBOX:
                // checkboxは同じnameで複数個ありうるし、複数選択できる
                String[] beanValues = BeanUtils.getArrayProperty(bean, input.getName());
                boolean matchBean = false;
                for (String value : beanValues) {
                    if (value.equals(input.getValue())) {
                        input.setChecked("checked");
                        matchBean = true;
                        continue;
                    }
                }
                // inputタグのvalueが、beanの配列の値のどれにもマッチしてない場合には
                // チェックをはずす
                if (!matchBean) {
                    input.setChecked(null);
                }
                break;
            default:
                // radio,checkbox以外
                input.setValue(BeanUtils.getProperty(bean, input.getName()));
            }
        }

        // textarea
        for (Textarea textarea : form.getDescendants(Textarea.class)) {
            // name属性が指定されていない場合はスルーする。
            if (M2StringUtils.isBlank(textarea.getName())) {
                continue;
            } else {
                textarea.setContent(BeanUtils.getProperty(bean, textarea.getName()));
            }
        }

        // select
        for (Select select : form.getDescendants(Select.class)) {
            // name属性が指定されていない場合はスルーする。
            if (M2StringUtils.isBlank(select.getName())) {
                continue;
            } else {
                for (Option option : select.getDescendants(Option.class)) {
                    boolean matchBean = false;
                    for (String value : BeanUtils.getArrayProperty(bean, select.getName())) {
                        if (value.equals(option.getValue())) {
                            option.setSelected("selected");
                            matchBean = true;
                        }
                    }
                    // optionタグのvalueが、beanの配列の値のどれにも
                    // マッチしてない場合にはチェックをはずす
                    if (!matchBean) {
                        option.setSelected(null);
                    }
                }
            }
        }
    }

    protected String convertToInputValue(Object propertyValue) {
        if (propertyValue instanceof Classification) {
            return ((Classification) propertyValue).code();
        } else {
            return propertyValue.toString();
        }
    }

    // ===================================================================================
    //                                                                   Conversion Helper
    //                                                                   =================
    // #app_customize you can customize the conversion logic
    // -----------------------------------------------------
    //                                         to Local Date
    //                                         -------------
    protected OptionalThing<LocalDate> toDate(Object exp) { // application may call
        return i18nDateLogic.toDate(exp, myConvZone());
    }

    protected OptionalThing<LocalDateTime> toDateTime(Object exp) { // application may call
        return i18nDateLogic.toDateTime(exp, myConvZone());
    }

    // -----------------------------------------------------
    //                                        to String Date
    //                                        --------------
    protected OptionalThing<String> toStringDate(LocalDate date) { // application may call
        return i18nDateLogic.toStringDate(date, myConvZone());
    }

    protected OptionalThing<String> toStringDate(LocalDateTime dateTime) { // application may call
        return i18nDateLogic.toStringDate(dateTime, myConvZone());
    }

    protected OptionalThing<String> toStringDateTime(LocalDateTime dateTime) { // application may call
        return i18nDateLogic.toStringDateTime(dateTime, myConvZone());
    }

    // -----------------------------------------------------
    //                                   Conversion Resource
    //                                   -------------------
    protected TimeZone myConvZone() {
        return requestManager.getUserTimeZone();
    }
}
