<common-header>
  <header>
    <section class="nav-main cf">
      <div class="wrap">
        <h1 class="main-title"><a href="/">Maihama, MagicLamp to Hangar<span> (LastaFlute Example)</span></a></h1>
        <ul class="nav-home">
          <li><a href="/product/list/"><span class="link-block">Products</span></a></li>
          <li if={isLogin}><a href="/member/list/"><span class="link-block">Members</span></a></li>
          <li if={isLogin}><a href="/withdrawal/"><span class="link-block">Withdrawal</span></a></li>
        </ul>
        <ul if={isLogin} class="nav-user">
          <li>
            <p class="nameHeader">Welcome, { memberName }</p>
            <ul class="child">
              <li><a href="/profile/">Profile</a></li>
              <li><a href="/signout/" onclick={onSignout}>Sign Out</a></li>
            </ul>
          </li>
        </ul>
      </div>
    </section>
  </header>

  <style>
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
    var self = this;

    this.isLogin = false;

    // ===================================================================================
    //                                                                               Event
    //                                                                               =====
    this.on('mount', () => {
      if (sessionStorage[RC.SESSION.member.info]) {
        self.displayLoginInfo();
        observable.trigger(RC.EVENT.auth.sign, true);
      } else {
        observable.trigger(RC.EVENT.auth.check);
      }
    });

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    this.onSignout = (e) => {
      e.preventDefault();
      request.get(RC.API.auth.signout,
        (response) => {
          self.isLogin = false;
          self.update();
          observable.trigger(RC.EVENT.route.change, "/");
          observable.trigger(RC.EVENT.auth.sign, false);
          sessionStorage.removeItem(RC.SESSION.auth.key);
          sessionStorage.removeItem(RC.SESSION.member.info);
        });
    };

    observable.on(RC.EVENT.auth.check, (state) => {
      request.get(RC.API.member.info,
        (response) => {
          sessionStorage[RC.SESSION.member.info] = response.text;
          observable.trigger(RC.EVENT.auth.sign, true);
        },
        (errors) => {
          observable.trigger(RC.EVENT.auth.sign, false);
        });
    });

    observable.on(RC.EVENT.auth.sign, (state) => {
      self.isLogin = state;
      if (state) {
        self.displayLoginInfo();
      }
      self.update();
    });

    // ===================================================================================
    //                                                                               Logic
    //                                                                               =====
    this.displayLoginInfo = () => {
      var json = JSON.parse(sessionStorage[RC.SESSION.member.info]);
      self.isLogin = true;
      self.memberName = json.memberName;
      self.update();
    };
  </script>
</common-header>