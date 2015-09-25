<controller>
  <main class="sg-content">
    <root if={this.route == window.RC.ROUTE.root}></root>
    <product if={this.route == window.RC.ROUTE.product.root}></product>
    <h2 if={this.route == window.RC.ROUTE.member}>Member List</h2>
    <h2 if={this.route == window.RC.ROUTE.purchase}>Purchase List</h2>
    <h2 if={this.route == window.RC.ROUTE.various}>Various</h2>
  </main>

  <script>
    var RC = window.RC || {};
    var obs = window.observable || {};
    var self = this;

    this.route = RC.ROUTE.root;

    this.routing = function(pathname) {
      var queryParams = window.helper.mappingQueryParams();
      var split = pathname.split("/");
      switch(split[1]) {
        case RC.ROUTE.root:
          self.route = RC.ROUTE.root;
          break;
        case RC.ROUTE.product.root:
          self.route = RC.ROUTE.product.root;
          obs.trigger(RC.EVENT.route.product.root, trimPathArray(split), queryParams);
          break;
        case RC.ROUTE.member:
          self.route = RC.ROUTE.member;
          obs.trigger(RC.EVENT.route.member, trimPathArray(split), queryParams);
          break;
        case RC.ROUTE.purchase:
          self.route = RC.ROUTE.purchase;
          obs.trigger(RC.EVENT.route.purchase, trimPathArray(split), queryParams);
          break;
        case RC.ROUTE.various:
          self.route = RC.ROUTE.various;
          obs.trigger(RC.EVENT.route.various, trimPathArray(split), queryParams);
          break;
      }
      self.update();
    };

    this.on("mount", function(e) {
      self.routing(location.pathname);
    });

    obs.on(RC.EVENT.route.change, function(pathname) {
      history.pushState(null, null, pathname);
      self.routing(pathname);
    });

    window.addEventListener("popstate", function(e) {
      self.routing(e.target.location.pathname);
    });

    var trimPathArray = function(arr) {
      var destArr = [];
      arr.forEach(function(item) {
        if (item !== "") {
          destArr.push(item);
        }
      });
      return destArr;
    };

  </script>
</controller>
