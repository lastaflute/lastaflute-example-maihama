<common-header>
  <header>
    <section class="nav-main cf">
      <div class="wrap">
        <h1 class="sg-main-title"><a href="/" onclick={goRoot}>Maihama<span> (LastaFlute Example)</span></a></h1>
        <ul class="nav-home">
          <li><a href="/product/list/" onclick={goProductList}><span class="link-block">Product</span></a></li>
          <li><a href="/member/list/" onclick={goMemberList}><span class="link-block">Member</span></a></li>
          <li><a href="/purchase/list/" onclick={goPurchaseList}><span class="link-block">Purchase</span></a></li>
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

    this.goPurchaseList = function(e) {
      e.preventDefault();
      observable.trigger(RC.EVENT.route.change, "/purchase/list/");
    };

    this.goVariousUpload = function(e) {
      e.preventDefault();
      observable.trigger(RC.EVENT.route.change, "/various/upload/");
    };
  </script>
</common-header>
