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
package org.docksidestage.app.job;

import javax.annotation.Resource;

import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.lastaflute.db.jta.stage.TransactionStage;
import org.lastaflute.job.LaJob;
import org.lastaflute.job.LaJobRuntime;

/**
 * @author jflute
 */
public class SeaJob implements LaJob {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private TransactionStage stage;
    @Resource
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                             Job Run
    //                                                                             =======
    @Override
    public void run(LaJobRuntime runtime) {
        int memberId = 3; // #simple_for_example
        stage.requiresNew(tx -> {
            Member before = memberBhv.selectByPK(memberId).get();
            updateMember(before.getMemberId());
            restoreMember(before.getMemberId(), before.getMemberName()); // for test
        });
        runtime.showEndTitleRoll(data -> {
            data.register("targetMember", memberId);
        });
    }

    private void updateMember(Integer memberId) {
        Member member = new Member();
        member.setMemberId(memberId);
        member.setMemberName("byJob");
        memberBhv.updateNonstrict(member);
    }

    private void restoreMember(Integer memberId, String memberName) {
        Member member = new Member();
        member.setMemberId(memberId);
        member.setMemberName(memberName);
        memberBhv.updateNonstrict(member);
    }
}
