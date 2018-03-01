<common-header>
  <header>
    <section class="nav-main cf">
      <div class="wrap">
        <h1 class="main-title">
          <a href="/">Maihama, MagicLamp to Hangar
            <span> (LastaFlute Example)</span>
          </a>
        </h1>
        <ul class="nav-home">
          <li>
            <a href="/product/list/">
              <span class="link-block">Products</span>
            </a>
          </li>
          <li if={isLogin}>
            <a href="/member/list/">
              <span class="link-block">Members</span>
            </a>
          </li>
          <li if={isLogin}>
            <a href="/withdrawal/">
              <span class="link-block">Withdrawal</span>
            </a>
          </li>
        </ul>
        <ul if={isLogin} class="nav-user">
          <li>
            <p class="nameHeader">Welcome, { memberName }</p>
            <ul class="child">
              <li>
                <a href="/profile/">Profile</a>
              </li>
              <li>
                <a href="/signout/" onclick={onSignout}>Sign Out</a>
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </section>
  </header>

  <style>
    .nav-user>li {
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

    .nav-user>li>a:hover {
      opacity: 0.8;
    }
  </style>

  <script>
    var self = this;
    this.mixin('common');

    this.isLogin = false;

    // ===================================================================================
    //                                                                               Event
    //                                                                               =====
    this.on('mount', () => {
      if (sessionStorage[SESSION.member.info]) {
        self.displayLoginInfo();
        observable.trigger(EVENT.auth.sign, true);
      } else {
        observable.trigger(EVENT.auth.check);
      }
    });

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    this.onSignout = (e) => {
      e.preventDefault();
      request.get(this.api.auth.signout,
        (response) => {
          self.isLogin = false;
          self.update();
          observable.trigger(EVENT.route.change, "/");
          observable.trigger(EVENT.auth.sign, false);
          sessionStorage.removeItem(SESSION.auth.key);
          sessionStorage.removeItem(SESSION.member.info);
        });
    };

    observable.on(EVENT.auth.check, (state) => {
      request.get(this.api.member.info,
        (response) => {
          sessionStorage[SESSION.member.info] = response.text;
          observable.trigger(EVENT.auth.sign, true);
        },
        (errors) => {
          observable.trigger(EVENT.auth.sign, false);
        });
    });

    observable.on(EVENT.auth.sign, (state) => {
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
      var json = JSON.parse(sessionStorage[SESSION.member.info]);
      self.isLogin = true;
      self.memberName = json.memberName;
      self.update();
    };
  </script>
</common-header>