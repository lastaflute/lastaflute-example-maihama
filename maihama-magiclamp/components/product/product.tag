<product>

  <product-list if={route == window.RC.EVENT.route.product.list}></product-list>
  <product-detail if={route == window.RC.EVENT.route.product.detail}></product-detail>

  <script>
    var RC = window.RC || {};
    var obs = window.observable || {};
    var helper = window.helper || {};
    var self = this;

    this.route = RC.EVENT.route.product.list;
    obs.on(RC.EVENT.route.product.root, function(paths, queryParams) {
      if (paths[1]) {
        switch(paths[1]) {
          case "list":
            self.route = RC.EVENT.route.product.list;
            obs.trigger(RC.EVENT.route.product.list, queryParams);
            self.update();
            break;
          case "detail":
            var product = helper.fetchQueryParams(queryParams, "product");
            if (!product) {
              obs.trigger(RC.EVENT.route.change, "../../product/list/");
              return;
            }
            self.route = RC.EVENT.route.product.detail;
            obs.trigger(RC.EVENT.route.product.detail, product);
            self.update();
            break;
        }
      }
    });
  </script>
</product>
