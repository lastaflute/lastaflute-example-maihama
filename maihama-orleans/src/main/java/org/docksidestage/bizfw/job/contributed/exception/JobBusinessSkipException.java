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
package org.docksidestage.bizfw.job.contributed.exception;

/**
 * Jobで業務的スキップをしたときの例外クラス。<br>
 * この例外のメッセージは、asBusinessSkip() で使われることを想定。<br>
 * というか、asBusinessSkip() を呼び出し側で行うために投げる例外と言って過言ではない。
 * <pre>
 * try {
 *     processMember(member); // ここで、BusinessSkip例外とか予期せぬ例外があがるかも
 *     recorder.asSuccess(); // 成功したってマークだよん (こいつが例外なげることは基本ない)
 * } catch (JobBusinessSkipException continued) { // 単なる制御用の例外なので特に情報を引き継いだりしない
 *     recorder.asBusinessSkip(continued.getMessage()); // ログ出力も含む
 * } catch (RuntimeException continued) { // NullPointerもSQL例外もあり得る
 *     recorder.asError("memberPublicCode=" + member.getMemberPublicCode(), continued); // ログ出力や同じ例外チェックも含む
 * }
 * ...
 * int updatedCount = memberBhv.queryUpdate(member, cb -> { // ☆後述のtipsコメントを参照
 *     cb.query().setMemberId_Equal(member.getMemberId()); // ほらっ、PK忘れずに！
 *     arrangeJobTargetCB(cb); // 加えて、業務状態をチェックする条件
 * });
 * if (updatedCount == 0) { // ないじゃん！すれ違いで処理対象じゃなくなっちゃったー
 *     throw new JobBusinessSkipException("memberAccount=" + member.getMemberAccount()); // asBusinessSkip()想定
 * }
 * ...
 * </pre>
 * @author jflute
 * @author contributed by U-NEXT
 */
public class JobBusinessSkipException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JobBusinessSkipException(String msg) {
        super(msg);
    }

    // 一応、ネストした例外で使われることを想定していないので、causeを受け取るコンストラクタはない by jflute (2017/05/10)
    // 必要になったら作ってもOKだが、そのときは asBusinessSkip() も例外を受け取るようにしないと意味がないかも。
}
