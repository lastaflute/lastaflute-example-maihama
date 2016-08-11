<common-header>
  <header>
    <section class="nav-main cf">
      <div class="wrap">
        <h1 class="sg-main-title"><a href="/" onclick={goRoot}>Maihama, MagicLamp to Hangar<span> (LastaFlute Example)</span></a></h1>
        <ul class="nav-home">
          <li><a href="/product/list/" onclick={goProductList}><span class="link-block">Products</span></a></li>
          <li><a href="/member/list/" onclick={goMemberList}><span class="link-block">Members</span></a></li>
          <li><a href="/withdrawal/" onclick={goWithdrawalList}><span class="link-block">Withdrawal</span></a></li>
        </ul>
        <nav-user></nav-user>
      </div>
    </section>
  </header>

  <script>
    var RC = window.RC || {};
    var observable = window.observable || {};

    this.goRoot = function(e) {
      e.preventDefault();
      observable.trigger(RC.EVENT.route.change, "/");
    };

    this.goProductList = function(e) {
      e.preventDefault();
      observable.trigger(RC.EVENT.route.change, "/product/list/");
    };

    this.goMemberList = function(e) {
      e.preventDefault();
      observable.trigger(RC.EVENT.route.change, "/member/list/");
    };

    this.goWithdrawalList = function(e) {
      e.preventDefault();
      observable.trigger(RC.EVENT.route.change, "/withdrawal/");
    };
  </script>
</common-header>
