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

import javax.annotation.Resource;

import org.docksidestage.bizfw.job.contributed.NxBatchRecorder;
import org.docksidestage.bizfw.job.contributed.exception.JobBusinessSkipException;
import org.docksidestage.dbflute.cbean.MemberCB;
import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exbhv.MemberServiceBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.dbflute.exentity.MemberService;
import org.lastaflute.db.jta.stage.TransactionStage;
import org.lastaflute.job.LaJob;
import org.lastaflute.job.LaJobRuntime;

/*
 * 【JOB設計の基本べき集】
 *
 * 1. 大量件数の検索でメモリ不足で落ちないように
 * 　=> かといって、JOB全体の実行時間を大きく損ねないように
 * 　=> 方式については、JOB設計の選択肢を参考に
 *
 * 2. JOBの終了後には、必ずバッチの結果をログに残す
 * 　=> 成功件数、スキップ件数、エラー件数、スキップやエラーのきっかけとなった例外情報など
 * 　=> スキップした対象レコードを特定できるIDなど (運用上のリカバリができるように)
 * 　=> JOBごとに独自の形式ではなく、できれば定型的に (見やすさ＆パースしやすさのために)
 *
 * 3. とあるデータのエラーで、関連のないデータの成功処理が破棄されないように
 * 　=> 成功するデータは、それはそれで確定して業務を進めたい
 * 　=> 細かいハンドリングについては、JOB設計の選択肢を参考に
 *
 * 4. JOBが失敗したときに、リトライ(再実行)でリカバリできるようにする
 * 　=> すでに処理済みのデータが二重で処理されて問題を起こさないように (検索条件などで工夫)
 * 　=> リトライできないなら、できないことを明示して運用事故を起こさないようにする
 * 　=> リモートAPI呼び出しを利用するケースについて、JOB設計の選択肢を参考に
 *
 *
 * 【JOB設計の選択肢】
 *
 * 1. 大量データ検索の方式
 *
 *  A. カーソル検索で一件ずつメモリ展開           // ☆基本これを想定
 *  B. リスト検索で最初の数件ずつまとめて処理      // ステータス制御があれば有効 (でないと無限ループ)
 *  C. ページング条件を入れたリスト検索           // ステータス制御がないときのBの代替 (あまりないかも)
 *  D. 対象PKリスト検索 + ループ内つどつど一件検索  // PKだけのリストならメモリはあまり気にしなくて良い
 *  E. 全件問答無用でリスト検索                  // メモリに自信があれば...でもまずあり得ない
 *
 * 2. データ間の関連性
 *
 *  A. データ間の関連性はなく、それぞれの一件処理は独立している ☆基本これを想定
 *  B. データ間の関連性があり、まとまって一つの処理と言える。このケースが発生したら相談。(現状は考慮外。)
 *
 *  ※次のトランザクション単位に大きく影響
 *
 *
 * 3. トランザクション単位
 *
 *  A. 1件      // データ間の関連性がないなら基本これ ☆基本これを想定
 *  B. 一定件数  // 関連性がある、もしくは、何かしらの物理的な都合があるケース
 *  C. 全部     // 全部まとめて関連性がある、もしくは、実装上の割り切りなど
 *
 *  ※基本的に、データ間の関連性次第
 *
 *
 * 4. とあるデータで業務例外になった場合の処理
 *    (基本的に業務例外ごとに振舞いが変わるはず)
 *
 *  A. 対象データをリトライ    // リトライできそうな例外であれば (何回リトライするか？)
 *  B. 対象データをスキップ    // 状況記録(ログ出力など)だけしてcontinue
 *  C. B+次回スキップ用にDB記録  // JOB自体のリトライでもう一度対象にならないようになど
 *
 *  ※例外の種類によって、様々なハイブリッドがあり得る
 *  ※システム例外と違い、業務例外でJOBを停止するケースはあまり考えられない
 *
 *
 * 5. とあるデータでシステム例外になった場合の処理
 *    (そんなに例外ごとに変わるものではない)
 *
 *  A. 対象データをリトライ  // リトライできそうな例外であれば (何回リトライするか？)
 *  B. 対象データをスキップ  // 状況記録(ログ出力など)だけしてcontinue
 *  C. JOBを停止            // そのままthrowしてエラーハンドリング(例外の種類)
 *  D. BとCのハイブリッド     // 基本はスキップ(A)で、同じ例外が続いたらバッチを停止(B)
 *
 *  ※例外の種類によって、様々なハイブリッドがあり得る
 *
 *
 * 6. JOB内のリモートAPI呼び出し成功後にJOBが失敗したときの業務的なJOBのリトライ
 *    (リモート先のデータはロールバックできないので、サーバー間で状態がズレてる可能性)
 *
 *  A. 可能        // そのままもう一度同じ処理を呼び出しても問題なし
 *  B. 条件付き可能 // キャンセル処理の後であれば同じ処理を呼び出せる
 *  C. 不可能      // 取り消しもできないのでリトライ不可、運用でリカバリ
 *
 *
 * 7. JOB内のリモートAPI呼び出し一回における処理対象レコード件数
 *    (1件ずつ呼び出しするか、複数件で呼び出しするか)
 *
 *  A. 1件    // 業務的に1件じゃないといけない、もしくは、パフォーマンス的に別に問題ないなど
 *  B. 複数件  // パフォーマンス的に一括で複数件を処理しないといけないなど
 *
 *
 * 8. すれ違い更新の可能性
 *
 *  A. 画面の更新は気にせず  // バッチ優先: バッチに影響が出る業務状態の変更がない or レアケース過ぎるので気にしない or あっても問題にならないと割り切り
 *  　=> 夜間バッチだからすれ違うことは業務的にないと言える、それで割り切ってもOKという判断のとき
 *  　=> 発生する確率が非常に低いと想定し、いざ発生しても、人間的な運用で賄えばOKという判断のとき
 *  　=> 一方で、バッチを優先しつつすれ違い更新を防ぎたいのであれば、画面を参照モードにするという選択肢もある
 *
 *  B. 画面からの更新を考慮  // 画面優先: バッチは一件処理のときの更新直前に業務状態をチェック (例えば、業務状態をチェックする絞り込み条件の検索をしてから更新)
 *  　=> そして、"直前の業務状態チェック" と "実際の更新" の間にすれ違いを防ぐための手段として幾つか選択肢がある。
 *  　=> B-1. for update: 直前の業務状態チェック を select for update で行う。一瞬だけ画面の更新を待たせて一貫性を保つ (その一瞬だけバッチ優先)
 *  　=> B-2. QueryUpdate: update文の絞り込み条件にPKに加えて業務状態チェックも入れて、一つのSQLで一緒にやってしまう (SQLの瞬間だけバッチ優先)
 *  　=> (B-3. バージョン番号楽観排他: バージョン番号で更新を検知してもバッチに影響のある更新とは限らないので業務状態チェックからリトライが必要なので微妙)
 *
 *  C. 画面でデータロック   // 画面最強: 画面を開いたら or ロックボタンを押したら、バッチではそのデータは処理しない (つまり完全に悲観)
 *  　=> 画面を開く or ロックボタンを押されたら、そのことを自体を知らせるステータスのようなDB上のカラムを更新 (ロック状態にする)
 *  　=> バッチは、そのカラムを必ず判定条件に入れて、ロック状態だったら処理をスキップさせる
 *  　=> そのままだと一生処理されなくなっちゃうから、何かのアクションで解除したり、一時間で自動解除したりなど工夫する
 *
 *  ※さらに、Jobごとに必要化どうか関係なく全体で統一的な対応をするか？Jobごとに A, B, C を決めて対応するか？の選択肢がある
 *  ※さらに、複数テーブルの更新だと、二個目のテーブルの制御を...基点テーブルの更新ロックに任せてシンプルにする？つどチェックするか？の選択肢がある
 *  ※ちなみに、B-3にもあるように、バッチではバージョン番号の楽観排他は意味をなさないので、更新は制御なしでOK (updateNonstrict()相当の更新)
 *  　=> 無論、画面側で楽観排他させるために、バージョン番号のインクリメントは必要 (そもそも排他制御有無に関わらずバージョン番号はインクリメントするもの)
 */

/**
 * DBを使ったJob のExample。<br>
 * 以下を想定した実装になっている。
 * <pre>
 * o 前後のデータに関連はないと想定して、一件ずつトランザクション
 * o 前後のデータに関連はないと想定して、一件処理で例外が発生しても続行
 * o 画面からの更新を考慮すると想定して、排他制御をしている (QueryUpdate方式)
 * </pre>
 * @author awane
 * @author jflute
 * @author contributed by U-NEXT
 */
public class SmallDbJob implements LaJob {

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // 【コントリビュート取り込みメモ】by jflute (2019/04/01)
    // コントリビュート取り込みに伴い、Exampleで使うテーブルはmaihamadbに合わせて微調整している。
    // (わかりやすさのため、とりあえず中心となるテーブルの会員(MEMBER)を利用)
    // また、会社的っぽい情報は削除している。それ以外はできるだけそのまま。
    // _/_/_/_/_/_/_/_/_/_/
    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private TransactionStage transactionStage;
    @Resource
    private MemberBhv memberBhv;
    @Resource
    private MemberServiceBhv memberServiceBhv;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Override
    public void run(LaJobRuntime runtime) {
        NxBatchRecorder recorder = new NxBatchRecorder();
        runtime.showEndTitleRoll(data -> {
            data.register("recorder", recorder); // 名前はなんでもいいけど、だいたい recorder を慣習に
        });
        recorder.planBatch(memberBhv.selectCount(cb -> arrangeJobTargetCB(cb)));

        memberBhv.selectCursor(cb -> {
            cb.specify().columnMemberAccount(); // このExampleではログのためにしか使ってないけど...
            arrangeJobTargetCB(cb);
            cb.addOrderBy_PK_Asc(); // 必須ではないが、処理の順序もわかりやすい方が良いので
        }, member -> { // もし、複数件数をまとめて処理したいなら、Listに詰めて実行するような制御を書くこと
            recorder.asProcessed(); // 処理したってマークだよん
            try {
                processMember(member); // ここで、BusinessSkip例外とか予期せぬ例外があがるかも
                recorder.asSuccess(); // 成功したってマークだよん (こいつが例外なげることは基本ない)
            } catch (JobBusinessSkipException continued) { // 単なる制御用の例外なので特に情報を引き継いだりしない
                recorder.asBusinessSkip(continued.getMessage()); // ログ出力も含む
            } catch (RuntimeException continued) { // NullPointerもSQL例外もあり得る
                recorder.asError("memberAccount=" + member.getMemberAccount(), continued); // ログ出力や同じ例外チェックも含む
            }
        });

        // もし、業務固有のデータで表示しておきたいものがあれば、最後にここで登録
        //runtime.showEndTitleRoll(data -> {
        //    data.register("sea", land);
        //});
    }

    private void arrangeJobTargetCB(MemberCB cb) { // plan件数検索と実データ検索で同じ条件にするために
        cb.query().setMemberStatusCode_Equal_Formalized(); // Exampleなのではちゃめちゃシンプル
    }

    // ===================================================================================
    //                                                                         Process One
    //                                                                         ===========
    private void processMember(Member member) {
        transactionStage.requiresNew(tx -> { // 前後のデータに関連はないのでここでトランザクション
            updateMember(member);
            updateService(member);
        });
    }

    // ===================================================================================
    //                                                                              Update
    //                                                                              ======
    protected void updateMember(Member member) { // UnitTestでTransactionのテストするためにprotectedメソッド
        // 排他制御: Exampleなのですれ違いしようがしまいが関係ないが、Exampleとして排他制御している
        member.setMemberName("超でたらめ"); // Exampleなので更新値はでたらめ
        int updatedCount = memberBhv.queryUpdate(member, cb -> { // ☆後述のtipsコメントを参照
            cb.query().setMemberId_Equal(member.getMemberId()); // ほらっ、PK忘れずに！
            arrangeJobTargetCB(cb); // 加えて、業務状態をチェックする条件
        });
        if (updatedCount == 0) { // ないじゃん！すれ違いで処理対象じゃなくなっちゃったー
            throw new JobBusinessSkipException("memberAccount=" + member.getMemberAccount()); // asBusinessSkip()想定
        }
    }

    private void updateService(Member member) {
        MemberService service = new MemberService();
        service.uniqueBy(member.getMemberId());
        service.setServicePointCount(99999); // Exampleなので更新値はでたらめ
        // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
        // 他のプロセスの更新テーブルの構成や順序が同じである前提で、基点テーブルの更新ロックにまかせてこっちは普通に更新。
        // そもそも順序が違えばデッドロックするし、構成が違うと条件判定が非常にややこしいのでディベロッパー同士で合わせる方が現実的。
        // また、バージョン番号があるテーブルならupdateNonstrict()。(どのちみバッチでバージョン番号による排他制御に意味はないため)
        // _/_/_/_/_/_/_/_/_/_/
        memberServiceBhv.updateNonstrict(service);
    }

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // [tips] ※わりと一般的な話
    //
    // まずはそもそもの前提から、すれ違い更新の可能性に関する選択肢について:
    //
    //  A. 画面の更新は気にせず  // バッチ優先: バッチに影響が出る業務状態の変更がない or レアケース過ぎるので気にしない or あっても問題にならないと割り切り
    //  　=> 夜間バッチだからすれ違うことは業務的にないと言える、それで割り切ってもOKという判断のとき
    //  　=> 発生する確率が非常に低いと想定し、いざ発生しても、人間的な運用で賄えばOKという判断のとき
    //  　=> 一方で、バッチを優先しつつすれ違い更新を防ぎたいのであれば、画面を参照モードにするという選択肢もある
    //
    //  B. 画面からの更新を考慮  // 画面優先: バッチは一件処理のときの更新直前に業務状態をチェック (例えば、業務状態をチェックする絞り込み条件の検索をしてから更新)
    //  　=> そして、"直前の業務状態チェック" と "実際の更新" の間にすれ違いを防ぐための手段として幾つか選択肢がある。
    //  　=> B-1. for update: 直前の業務状態チェック を select for update で行う。一瞬だけ画面の更新を待たせて一貫性を保つ (その一瞬だけバッチ優先)
    //  　=> B-2. QueryUpdate: update文の絞り込み条件にPKに加えて業務状態チェックも入れて、一つのSQLで一緒にやってしまう (SQLの瞬間だけバッチ優先)
    //  　=> (B-3. バージョン番号楽観排他: バージョン番号で更新を検知してもバッチに影響のある更新とは限らないので業務状態チェックからリトライが必要なので微妙)
    //
    //  C. 画面でデータロック   // 画面最強: ロック状態をDBに持たせて、バッチは一件処理時に更新ロック付きでロック状態をチェック
    //
    //  ※さらに、Jobごとに必要化どうか関係なく全体で統一的な対応をするか？Jobごとに A, B, C を決めて対応するか？の選択肢がある
    //  ※さらに、複数テーブルの更新だと、二個目のテーブルの制御を...基点テーブルの更新ロックに任せてシンプルにする？つどチェックするか？の選択肢がある
    //  ※ちなみに、B-3にもあるように、バッチではバージョン番号の楽観排他は意味をなさないので、更新は制御なしでOK (updateNonstrict()相当の更新)
    //  　=> 無論、画面側で楽観排他させるために、バージョン番号のインクリメントは必要 (そもそも排他制御有無に関わらずバージョン番号はインクリメントするもの)
    //
    // ...
    //
    // もし、selectCursor()してからupdateに到達するまでの間に別のプロセスが更新処理が発生して、
    // それによって本来ここで処理すべきではないデータに変更されてしまっているのを防ぎたいのであれば、
    // ここで、もう一度業務的な処理対象条件にマッチしているかをチェックして厳密な実行可否を担保すべし。
    // (先程の選択肢で言うと "B" に相当する)
    //
    // そのとき、一瞬のすれ違いもしないようにするために、そのチェックのための検索を select for update にする。
    // 存在しなかったときは素通りロールバックしたいので、alwaysPresent()で例外throwしてトランザクションの外側でcatch。
    // e.g.
    //  private boolean updateMember(Member member) {
    //      memberBhv.selectEntity(cb -> {
    //          cb.specify().columnMemberId();
    //          cb.query().setMemberId_Equal(member.getMemberId());
    //          arrangeJobTargetCB(cb);
    //          cb.lockForUpdate();
    //      }).ifPresent(updated -> { // なければ素通り
    //          updated.setMemberName("超でたらめ");
    //          memberBhv.update(updated);
    //
    //          MemberService service = new MemberService();
    //          service.uniqueBy(member.getMemberId());
    //          service.setServicePointCount(99999);
    //          memberServiceBhv.updateNonstrict(service); // 基点テーブルの更新ロックにまかせてこっちは普通
    //      }).;
    //  }
    //
    // この場合は、厳密には "バッチ処理を優先する" というニュアンスではなくなり、
    // "バッチの中で一件処理が始まる前であれば画面などの更新を優先できる" というニュアンスになる。
    // (スーパー厳密には、for updateの一件検索と更新処理の一瞬の隙間に関してはバッチ処理が優先されると言える)
    //
    // ただ、別のプロセスの更新テーブルの構成も同じであることが前提である。
    // もし、別のプロセスがこちらのバッチの更新テーブルの二個目以降のものだけを更新する場合は、
    // 二個目以降も select for update をするなどして業務状態をつどつどチェックする必要がある。
    // そのとき、二個目以降に存在しなかったときは必ずロールバックする必要があるので、
    // トランザクションの外で EntityAlreadyDeletedException を catch して業務スキップさせてあげる必要もある。
    // とはいえ、通常そこまですれ違いを気にするのであれば、更新テーブルの構成などはシステム全体で合わせるべきとも言える。
    //
    // ...
    //
    // このすれ違いを防ぐために、悲観ロックではなく楽観ロックでも実現できなくはないが、あまり得策ではない。
    // VERSION_NOの食い違いはバッチ処理をスキップさせるべき更新なのかどうか正確に判断できないので、
    // 更新処理で排他制御例外が発生したときに、処理対象条件の一件検索を論理的に無限リトライする必要があり、
    // その無限リトライも気持ち悪いからリトライ回数を決めて諦める処理も必要になり、実装コストが高い。
    // 一件検索と更新処理の一瞬の隙間も完全に画面での更新が優先されるという動きに変えることができるが、
    // それが業務的なメリットになるとは言えない。悲観ロックで画面側ほんの一瞬だけなら待たせても何も問題はない。
    //
    // どうしても悲観ロックを避けたいのであれば、QueryUpdateを使って更新と業務状態のチェックを同時にやる方が良い。
    // e.g.
    //  member.setMemberName("超でたらめ");
    //  int updatedCount = memberBhv.queryUpdate(member, cb -> {
    //      cb.query().setMemberId_Equal(member.getMemberId());
    //      arrangeJobTargetCB(cb);
    //  });
    //  if (updatedCount > 0) { // 成功してたら1のはず
    //      MemberService service = new MemberService();
    //      service.uniqueBy(member.getMemberId());
    //      service.setServicePointCount(99999);
    //      memberServiceBhv.updateNonstrict(service); // 基点テーブルの更新ロックにまかせてこっちは普通
    //  }
    //
    // ...
    //
    // そして、リモートAPI呼び出しなどロールバックできない処理がバッチ処理の中に含まれている場合、
    // 論理的な厳密性を考慮するならば、先に更新処理を行ってから同じトランザクション内でリモートAPI呼び出しをすることになる。
    // ただ、そのようにすると、万が一通信が戻ってこなくて更新ロック抱えたままになってしまうと、サービスが止まってしまう可能性もある。
    // 論理的な厳密性とサービスの安定性のトレードオフが存在する。
    // ここはサービスの安定性を考慮して、リモート側のリカバリは運用で行うというのもアリである。
    //
    // ...
    //
    // ちなみに、もしバッチ処理を優先で排他制御なしであれば、単純に更新処理を並べるだけ。
    // ここでも同様に、VERSION_NOのあるテーブルであれば updateNonstrict() で
    // (排他制御をするにしてもバージョン番号は使わないので、どのみち updateNonstrict())
    // e.g.
    //  member.setMemberName("超でたらめ");
    //  memberBhv.updateNonstrict(member);
    //
    //  MemberService service = new MemberService();
    //  service.uniqueBy(member.getMemberId());
    //  service.setServicePoint(99999);
    //  memberServiceBhv.updateNonstrict(service);
    // _/_/_/_/_/_/_/_/_/_/

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // [tips] ※Example実装の話
    //
    // このExampleでは、画面の更新を優先しつつ悲観ロック避けたいということで、queryUpdate()を基軸とすることにした。
    //
    // でもって、なぜ制御が必要なのか？つどつどすれ違いの想定をJobのコード上にコメントすること！
    // なんで排他制御してるの？と聞かれて「そうやるって決まったから...」というのは無しで。
    // 例えば、このように...
    // 　「排他制御: xxxAPIの処理でバッチ中にxxxステータスを更新される可能性があるため」
    // 　「排他制御: 今はないかもしれないが、将来更新する処理が作られるかもしれないことを想定して」
    // 　「排他制御: すれ違いがあるかわからないので、念のためやっておく」
    // 最低でも、最後のように "念のため" でもいいから意図は明示しておくこと。
    // Jobは実装に選択肢が多いため、意図のないコードは正しくても後の人を惑わせてしまう。
    //
    // また、業務的なスキップをされたかどうかも記録するために、
    // JobBusinessSkipException を throw して、asBusinessSkip()として記録していく。
    // e.g.
    //  } catch (JobBusinessSkipException continued) { // 単なる制御用の例外なので特に情報を引き継いだりしない
    //      recorder.asBusinessSkip(continued.getMessage()); // ログ出力も含む
    //  }
    //  ...
    //  if (updatedCount == 0) { // ないじゃん！すれ違いで処理対象じゃなくなっちゃったー
    //      throw new JobBusinessSkipException("memberAccount=" + member.getMemberAccount()); // asBusinessSkip()想定
    //  }
    //
    // 悲観ロックを避ける理由としては:
    // ・ロックすることによる影響範囲がわからない(怖いから)
    // ・ロックしなくても今の仕組みであれば、対応できると見込めたから: queryUpdate + arrangeJobTargetQuery()
    //
    // もし、リモートAPI呼び出しなどロールバックできない処理がバッチ処理の中に含まれている場合でも、
    // 更新ロックを取ってからその処理をするのではなく、リモートAPIを呼び出してから更新処理をする。
    // 厳密には、リモートAPI呼び出し側の結果が取り残される可能性があるが、それはログなどから運用でリカバリする。
    // 更新ロックを取ってからリモートAPIを呼び出したりすると、通信が戻ってこなくて更新ロック抱えたままになって、
    // サービスが止まってしまう可能性もある。ここでは論理的な厳密性よりもサービスの安定性を優先する。
    // _/_/_/_/_/_/_/_/_/_/
}
