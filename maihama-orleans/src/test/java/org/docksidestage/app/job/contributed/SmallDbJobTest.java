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
package org.docksidestage.app.job.contributed;

import java.util.Map;

import org.docksidestage.bizfw.job.contributed.NxBatchRecorder;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.unit.UnitOrleansJobTestCase;
import org.lastaflute.db.jta.stage.BegunTx;
import org.lastaflute.db.jta.stage.BegunTxContext;
import org.lastaflute.job.mock.MockJobRuntime;

import jakarta.annotation.Resource;

/**
 * @author jflute
 * @author contributed by U-NEXT
 */
public class SmallDbJobTest extends UnitOrleansJobTestCase {

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // 【コントリビュート取り込みメモ】by jflute (2019/04/01)
    // もともとは AssertJ でアサートされていたが、maihamaプロジェクトでは使っていないので普通のassertで書き直している
    // _/_/_/_/_/_/_/_/_/_/
    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private MemberBhv memberBhv;

    // ===================================================================================
    //                                                                              正常系
    //                                                                             =======
    public void test_run_basic() {
        // ## Arrange ##
        // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
        // changeRequiresNewToRequired():
        // Job内のrequiresNewのTransactionでコミットされてしまってテストしづらいので、
        // requiredに強制変換することでテスト用のTransactionを継承させるようにしてロールバックできるように。
        // requiresNewを設定していること自体のテストは、test_run_transactionUsed()にて行う。
        //
        // suppressCursorSelectFetchSize():
        // 一方で、MySQLだと、Requiredに変換することで今度はStreaming result setエラーが発生してしまう。
        // (DBFluteのlittleAdjustmentMap.dfpropでcursorSelectFetchSizeがInteger.MIN_VALUEに設定されていれば)
        // 本番においてはInteger.MIN_VALUEは重要だがUnitTestでは重要ではないので、一時的にsuppressさせている。
        // ゆえに、MySQLのときはchangeRequiresNewToRequired()とsuppressCursorSelectFetchSize()がセットとなる。
        // _/_/_/_/_/_/_/_/_/_/
        changeRequiresNewToRequired(); // Job内のTransactionをテスト用のTransactionに
        suppressCursorSelectFetchSize(); // したときのMySQLのStreaming result setエラー回避

        SmallDbJob job = new SmallDbJob();
        inject(job);
        MockJobRuntime runtime = mockRuntime(job);

        // ## Act ##
        job.run(runtime);

        // ## Assert ##
        showEndTitleRoll(runtime); // end-title-rollの中身を目視するため
        assertPlannedSuccess(runtime); // 計画通りの成功であることをアサート

        // ここで、DBの中身が変わったことをしましょう！
        memberBhv.selectList(cb -> {
            cb.setupSelect_MemberServiceAsOne();
            cb.query().setMemberStatusCode_Equal_Formalized();
        }).forEach(member -> {
            assertEquals(99999, member.getMemberServiceAsOne().get().getServicePointCount());
        });

        // ここで、業務で追加した情報をアサートしましょう！
        // (recorderからスキップされた例外情報などもアサート可能)
        //Map<String, Object> rollMap = runtime.getEndTitleRollMap();
        //NxBatchRecorder recorder = (NxBatchRecorder) rollMap.get("recorder");
        //List<BatchBusinessSkipPart> businessSkips = recorder.getBusinessSkips();
    }

    // ===================================================================================
    //                                                                         Transaction
    //                                                                         ===========
    public void test_run_transactionUsed() { // ちゃんとTransactionが使われていることをテスト
        // ## Arrange ##
        // もし、デフォルトで changeRequiresNewToRequired() しているのであれば、ここでrestoreが必要
        //restoreRequiresNewToRequired();
        SmallDbJob job = new SmallDbJob() {
            @Override
            protected void updateMember(Member member) { // Transaction確認のため、そして、何も処理しない
                assertTranasctionRequiresNew(); // ここはrequiresNew()の中だよね
                markHere("called");
            }
        };
        inject(job);
        MockJobRuntime runtime = mockRuntime(job);

        // ## Act ##
        job.run(runtime);

        // ## Assert ##
        assertMarked("called");
    }

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // 【コントリビュート取り込みメモ】by jflute (2019/04/01)
    // ここからのメソッドは、もともとJobテスト用のスーパークラスに定義されていたもの。
    // いったんここにべたっと。
    // _/_/_/_/_/_/_/_/_/_/
    // ===================================================================================
    //                                                                         Show Helper
    //                                                                         ===========
    protected void showEndTitleRoll(MockJobRuntime runtime) { // developer can use
        if (runtime == null) {
            throw new IllegalArgumentException("The argument 'runtime' should not be null.");
        }
        Map<String, Object> rollMap = runtime.getEndTitleRollMap();
        StringBuilder sb = new StringBuilder();
        sb.append(ln()).append("[EndTitleRoll]");
        rollMap.forEach((key, value) -> {
            sb.append(ln()).append(" ").append(key).append(" = ").append(value);
        });
        log(sb.toString());
    }

    // ===================================================================================
    //                                                                       Assert Helper
    //                                                                       =============
    // -----------------------------------------------------
    //                                               Success
    //                                               -------
    /**
     * 計画通りの成功であることをアサート。<br>
     * 計画件数が0ではなく、処理件数や成功件数が計画件数と同じであることをチェック。
     * <pre>
     * // ## Assert ##
     * showEndTitleRoll(runtime); // end-title-rollの中身を目視するため
     * assertPlannedSuccess(runtime); // 計画通りの成功であることをアサート
     * </pre>
     * @param runtime MockのJobの実行オブジェクト (NotNull)
     */
    protected void assertPlannedSuccess(MockJobRuntime runtime) { // developer can use
        if (runtime == null) {
            throw new IllegalArgumentException("The argument 'runtime' should not be null.");
        }
        Map<String, Object> rollMap = runtime.getEndTitleRollMap();
        NxBatchRecorder recorder = (NxBatchRecorder) rollMap.get("recorder");
        if (recorder == null) {
            throw new IllegalStateException("Not found the recorder in the end-title-roll map: " + rollMap.keySet());
        }
        recorder.getPlannedCount().alwaysPresent(plannedCount -> {
            assertNotSame("計画件数は0ではない", 0, plannedCount);
            assertEquals("処理件数は計画件数と同じ", plannedCount.intValue(), recorder.getProcessedCount());
            assertEquals("成功件数は計画件数と同じ", plannedCount.intValue(), recorder.getSuccessCount());
        });
        assertTrue("エラーはない", recorder.getErrors().isEmpty());
    }

    // -----------------------------------------------------
    //                                           Transaction
    //                                           -----------
    /**
     * いま現在、Transaction が requiresNew() で開始されていることをアサート。
     * 
     * <p>↓もし、デフォルトで changechangeRequiresNewToRequired() している場合の話: <br>
     * デフォルトでは、そもそも requiresNew() が requires() に変換されているので、
     * restoreChangingRequiresNewToRequired()を組み合わせて使うこと。</p>
     * 
     * <p>TestCase の Transaction は、BegunTxContext には入らないので紛れることはない。</p>
     * <pre>
     * // ## Arrange ##
     * // もし、デフォルトで changeRequiresNewToRequired() しているのであれば、ここでrestoreが必要
     * //restoreChangingRequiresNewToRequired(); // requiresNew()を復活させて...
     * SmallDbJob job = new SmallDbJob() {
     *     protected void updateMember(Member member, String detarame) { // Transaction確認のため、そして、何も処理しない
     *         assertTranasctionRequiresNew(); // ここはrequiresNew()の中だよね
     *         markHere("updateMember");
     *     }
     * }
     * inject(job);
     * MockJobRuntime runtime = mockRuntime(job);
     *
     * // ## Act ##
     * job.run(runtime);
     *
     * // ## Assert ##
     * assertMarked("updateMember");
     * </pre>
     */
    protected void assertTranasctionRequiresNew() { // developer can use
        BegunTx<?> tx = BegunTxContext.getBegunTxOnThread();
        assertNotNull(tx);
        assertTrue(tx.isRequiresNew());
        assertFalse(tx.isRollbackOnly());
    }
}
