<signin>
  <h2 class="sg-content-title">Sign In</h2>
  <section class="sign-in-box">
    <div class="signin-form">
      <form name="signin_signinAction_index_Form" onsubmit={doSignin}>
        <dl>
          <dt><label>Account</label></dt>
          <dd><input type="text" ref="account" value="" placeholder="input Pixy"></dd>
        </dl>
        <dl>
          <dt><label>Password</label></dt>
          <dd><input type="password" ref="password" value="" placeholder="input sea"></dd>
        </dl>
        <dl>
          <dd><label><input type="checkbox" ref="autoLogin" value="on"> Remember Account</label></dd>
        </dl>
        <div>
          <button type="submit" class="btn btn-success">Sign in</button>
        </div>
      </form>
    </div>
    
    <style scoped>
      .sign-in-box {
        width: 320px;
        margin: 32px auto;
        border: solid 4px #E5E5E5;
        padding: 24px 64px;
      }
      .signin-form li {
        padding: 6px 0;
      }
      .signin-form li p {
        padding-bottom: 6px;
        color: #444;
      }
      .signin-form input[type="text"],.signin-form input[type="password"] {
        padding: 6px 8px;
        width: 80%;
      }
      .signin-form input[type="submit"] {
        margin-top: 8px;
        padding: 10px 32px;
        height: 36px;
        background-color: #660032;
        border: 1px solid #660032;
        color: #FFF;
        line-height: 1;
        font-size: 14px;
        font-weight: bold;
        vertical-align: top;
        cursor: pointer;
      }
    </style>

    <script>
      var RC = window.RC || {};
      var sa = window.superagent || {};
      var obs = window.observable || {};

      this.doSignin = function(e) {
        e.preventDefault();
        var account = this.refs.account;
        var password = this.refs.password;
        sa
          .post(RC.API.auth.signin)
          .send({account: account.value, password: password.value})
          .withCredentials()
          .end(function(error, response) {
            if (response.ok) {
              account.value = "";
              password.value = "";
              obs.trigger(window.RC.EVENT.auth.check);
            }
          });
      };
    </script>
  </section>
</signin>
