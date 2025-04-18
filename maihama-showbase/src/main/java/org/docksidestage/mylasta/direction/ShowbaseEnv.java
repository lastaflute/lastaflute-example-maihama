/*
 * Copyright 2015-2024 the original author or authors.
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
package org.docksidestage.mylasta.direction;

import org.lastaflute.core.direction.exception.ConfigPropertyNotFoundException;

/**
 * @author FreeGen
 */
public interface ShowbaseEnv extends MaihamaConfig {

    /** The key of the configuration. e.g. showbase-support@annie.example.com */
    String MAIL_ADDRESS_SUPPORT = "mail.address.support";

    /** The key of the configuration. e.g. localhost:8091 */
    String SERVER_DOMAIN = "server.domain";

    /** The key of the configuration. e.g. true */
    String SWAGGER_ENABLED = "swagger.enabled";

    /**
     * Get the value of property as {@link String}.
     * @param propertyKey The key of the property. (NotNull)
     * @return The value of found property. (NotNull: if not found, exception)
     * @throws ConfigPropertyNotFoundException When the property is not found.
     */
    String get(String propertyKey);

    /**
     * Is the property true?
     * @param propertyKey The key of the property which is boolean type. (NotNull)
     * @return The determination, true or false. (if not found, exception)
     * @throws ConfigPropertyNotFoundException When the property is not found.
     */
    boolean is(String propertyKey);

    /**
     * Get the value for the key 'mail.address.support'. <br>
     * The value is, e.g. showbase-support@annie.example.com <br>
     * comment: Mail Address for Showbase Support
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getMailAddressSupport();

    /**
     * Get the value for the key 'server.domain'. <br>
     * The value is, e.g. localhost:8091 <br>
     * comment: domain to access this application from internet, e.g. for registration mail
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getServerDomain();

    /**
     * Get the value for the key 'swagger.enabled'. <br>
     * The value is, e.g. true <br>
     * comment: is it use swagger in this environment?
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getSwaggerEnabled();

    /**
     * Is the property for the key 'swagger.enabled' true? <br>
     * The value is, e.g. true <br>
     * comment: is it use swagger in this environment?
     * @return The determination, true or false. (if not found, exception but basically no way)
     */
    boolean isSwaggerEnabled();

    /**
     * The simple implementation for configuration.
     * @author FreeGen
     */
    public static class SimpleImpl extends MaihamaConfig.SimpleImpl implements ShowbaseEnv {

        /** The serial version UID for object serialization. (Default) */
        private static final long serialVersionUID = 1L;

        public String getMailAddressSupport() {
            return get(ShowbaseEnv.MAIL_ADDRESS_SUPPORT);
        }

        public String getServerDomain() {
            return get(ShowbaseEnv.SERVER_DOMAIN);
        }

        public String getSwaggerEnabled() {
            return get(ShowbaseEnv.SWAGGER_ENABLED);
        }

        public boolean isSwaggerEnabled() {
            return is(ShowbaseEnv.SWAGGER_ENABLED);
        }
    }
}
