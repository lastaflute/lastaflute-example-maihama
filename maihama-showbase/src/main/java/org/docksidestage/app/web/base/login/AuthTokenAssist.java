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
package org.docksidestage.app.web.base.login;

import java.io.IOException;
import java.util.Random;

import javax.annotation.Resource;

import org.dbflute.helper.filesystem.FileTextIO;
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.DfResourceUtil;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jflute
 */
public class AuthTokenAssist { // #change_it

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenAssist.class);

    @Resource
    private MemberBhv memberBhv;

    // #simple_for_example no no no no way, normally use database or KVS
    public String saveMemberToken(String memberAccount) {
        final String token = Integer.toHexString(new Random().nextInt());
        final String path = buildTokenFilePath(token);
        new FileTextIO().encodeAsUTF8().write(path, memberAccount);
        return token;
    }

    public OptionalThing<Member> findMemberByToken(String token) {
        final String path = buildTokenFilePath(token);
        final String account;
        try {
            account = new FileTextIO().encodeAsUTF8().read(path);
        } catch (RuntimeException continued) { // contains "file not found"
            logger.debug("Failed to read the file: {}, {}", path, continued.getMessage());
            return OptionalThing.empty();
        }
        return memberBhv.selectByUniqueOf(account);
    }

    private String buildTokenFilePath(String token) {
        try {
            return DfResourceUtil.getBuildDir(getClass()).getParentFile().getCanonicalPath() + "/auth-" + token + ".txt";
        } catch (IOException e) {
            throw new IllegalStateException("Failed to get canonical path for the token: " + token, e);
        }
    }
}
