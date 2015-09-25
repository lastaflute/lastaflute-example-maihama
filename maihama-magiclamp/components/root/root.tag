<root>
  <section if={!isLogin}>
    <signin if={state}></signin>
    <signup if={!state}></signup>
  </section>
  <section if={isLogin}>
    <div class="wrap">
      <article class="main-col">
        <h2 class="page-title">My Page</h2>
        <table class="table table-stripe">
          <colgroup>
            <col width="200">
            <col>
          </colgroup>
          <tbody>
            <tr>
              <th>お名前</th>
              <td>{memberName}</td>
            </tr>
            <tr>
              <th>会員ID</th>
              <td>{memberId}</td>
            </tr>
            <tr>
              <th>会員ランク</th>
              <td>{memberServiceName}</td>
            </tr>  
            <tr>
              <th>暗号化された<br>あなたのパスワード</th>
              <td>{memberPassword}</td><!-- Simple for Example-->
            </tr>
            <tr>
              <th>お住まい</th>
              <td>{memberAddress}</td>
            </tr>
          </tbody>
        </table>
      </article>
    </div>
  </section>

  <script>
    var RC = window.RC || {};
    var sa = window.superagent || {};
    var obs = window.observable || {};
    var self = this;

    this.isLogin = false;
    this.state = true;

    obs.on(window.RC.EVENT.auth.sign, function(state) {
      self.isLogin = state;
      self.update();
      if(self.isLogin) {
        sa
          .get(RC.API.mypage)
          .withCredentials()
          .end(function(error, response) {
            if(response.ok) {
              var obj = JSON.parse(response.text);
              self.memberId = obj.memberId;
              self.memberName = obj.memberName;
              self.memberStatusCode = obj.memberStatusCode;
              self.memberServiceName = obj.memberServiceName;
              self.memberPassword = obj.memberPassword;
              self.memberAddress = obj.memberAddress;
              self.update();
            }
          });
      }
    });
  </script>
</root>
