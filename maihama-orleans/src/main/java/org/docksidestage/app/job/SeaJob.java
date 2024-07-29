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

import org.docksidestage.app.logic.DanceSongLogic;
import org.docksidestage.app.logic.env.ComedyRhythmLogic;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.lastaflute.db.jta.stage.TransactionStage;
import org.lastaflute.job.LaJob;
import org.lastaflute.job.LaJobRuntime;

import jakarta.annotation.Resource;

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
    @Resource
    private DanceSongLogic danceSongLogic; // logic call example
    @Resource
    private ComedyRhythmLogic comedyRhythmLogic; // env-dispatched logic call example
    @Resource
    private ItsMyPartyAssist itsMyPartyAssist; // assist call example

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
        danceSongLogic.letsDance();
        comedyRhythmLogic.letsLaugh();
        itsMyPartyAssist.beHappy();
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
