<nav-user>
  <ul if={isLogin} class="nav-user">
    <li>
      <p class="nameHeader">Welcome, { memberName }</p>
      <ul class="child">
        <li><a href="#">Profile</a></li>
        <li><a href="/signout/" onclick={onSignout}>Sign Out</a></li>
      </ul>
    </li>
  </ul>

  <style scoped>
    .nav-user > li {
      float: right;
      position: relative;
      width: 120px;
      margin: 0;
      padding: 5px;
      background-color: #660032;
      font-size: 14px;
    }
    .nav-user:hover .child {
      display: block;
    }
    .nav-user > li > a:hover{
      opacity: 0.8;
    }
  </style>

  <script>
    var RC = window.RC || {};
    var sa = window.superagent || {};
    var obs = window.observable || {};
    var self = this;

    this.isLogin = false;

    this.displayLoginInfo = function() {
      var json = JSON.parse(sessionStorage[RC.SESSION.member.info]);
      self.isLogin = true;
      self.memberName = json.memberName;
      self.update();
    };

    this.onSignout = function(e) {
      e.preventDefault();
      sa
        .get(RC.API.auth.signout)
        .withCredentials()
        .end(function (error, response) {
          if (response.ok) {
            self.isLogin = false;
            self.update();
            obs.trigger(RC.EVENT.auth.sign, false);
            sessionStorage.removeItem(RC.SESSION.member.info);
          }
        });
    };

    obs.on(RC.EVENT.auth.check, function(state) {
      sa
        .get(RC.API.member.info)
        .withCredentials()
        .end(function (error, response) {
          if (response.ok) {
            sessionStorage[RC.SESSION.member.info] = response.text;
            console.log("success");
            obs.trigger(RC.EVENT.auth.sign, true);
          }
        });
    });

    obs.on(RC.EVENT.auth.sign, function(state) {
      self.isLogin = state;
      if (state) {
        self.displayLoginInfo();
      }
      self.update();
    });

    setTimeout(function() {
      if (sessionStorage[RC.SESSION.member.info]) {
        self.displayLoginInfo();
        obs.trigger(RC.EVENT.auth.sign, true);
      } else {
        obs.trigger(RC.EVENT.auth.check);
      }
    }, 10);
  </script>
</nav-user>
