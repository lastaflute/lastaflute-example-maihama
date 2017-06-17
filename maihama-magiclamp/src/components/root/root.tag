<root>
  <section if={!isLogin}>
    <signin></signin>
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
              <th>Member Name</th>
              <td>{memberName}</td>
            </tr>
            <tr>
              <th>Member Status</th>
              <td>{memberStatus}</td>
            </tr>  
            <tr>
              <th>Service Rank</th>
              <td>{serviceRank}</td>
            </tr>  
            <tr>
              <th>Encrypted<br>Password</th>
              <td>{cipheredPassword}</td> <!-- #simple_for_example-->
            </tr>
            <tr>
              <th>Address</th>
              <td>{memberAddress}</td>
            </tr>
          </tbody>
        </table>
      </article>
    </div>
  </section>

  <script>
    var RC = window.RC || {};
    var helper = window.helper || {};
    var obs = window.observable || {};
    var self = this;

    this.isLogin = false;

    this.on('mount', function() {
      obs.trigger(RC.EVENT.auth.check);
    });
    
    obs.on(RC.EVENT.auth.sign, function(state) {
      self.isLogin = state;
      if(self.isLogin) {
        helper.get(RC.API.mypage,
          (response) => {
            var obj = JSON.parse(response.text);
            self.memberId = obj.memberId;
            self.memberName = obj.memberName;
            self.memberStatus = obj.memberStatus;
            self.serviceRank = obj.serviceRank;
            self.cipheredPassword = obj.cipheredPassword;
            self.memberAddress = obj.memberAddress;
            self.update();
          });
      } else {
        self.update();
      }
    });
  </script>
</root>
