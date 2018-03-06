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
      reload();
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
          sessionStorage.removeItem(SESSION.auth.key);
          sessionStorage.removeItem(SESSION.member.info);
          observable.trigger(EVENT.route.change, "/");
          observable.trigger(EVENT.auth.sign);
        });
    };

    observable.on(EVENT.auth.check, () => {
      request.get(this.api.member.info,
        (response) => {
          sessionStorage[SESSION.member.info] = response.text;
          observable.trigger(EVENT.auth.sign);
        },
        (errors) => {
          observable.trigger(EVENT.auth.sign);
        });
    });

    observable.on(EVENT.auth.sign, () => {
      reload();
    });

    // ===================================================================================
    //                                                                               Logic
    //                                                                               =====
    const reload = () => {
      this.isLogin = sessionStorage[SESSION.member.info];
      if (this.isLogin) {
        var json = JSON.parse(sessionStorage[SESSION.member.info]);
        this.memberName = json.memberName;
      }
      this.update();
    }
  </script>
</common-header>