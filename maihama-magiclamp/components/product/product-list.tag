<product-list>
  <div class="wrap">
    <form class="form" onsubmit={searchProductList} oninput={searchProductListIncremental}>
      <dl>
        <dt><label>product name search</label></dt>
        <dd><input type="text" ref="word" placeholder="keyword..."></dd>
      </dl>
      <div class="btnBox">
        <input type="checkbox" onchange={switchIncremental}> incremental search
        <button class="btn btn-success">Search</button>
      </div>
    </form>
    <table class="table table-stripe">
      <thead>
        <tr>
          <th>Product Name</th>
          <th>Price</th>
        </tr>
      </thead>
      <tbody>
        <tr each={productList}>
          <td>
            <a href="/product/detail/{productId}">{productName}</a>
          </td>
          <td>¥{window.helper.formatMoneyComma(regularPrice)}</td>
        </tr>
      </tbody>
    </table>
  </div>
  <pagination></pagination>

  <script>
    var RC = window.RC || {};
    var sa = window.superagent || {};
    var obs = window.observable || {};
    var self = this;

    this.incrementalChecked = false;
    this.productList = [];

    this.on('mount', () => {
      obs.trigger(RC.EVENT.route.product.list, opts);
    });

    moveDetail = function(e) {
      e.preventDefault();
      var href= e.target.pathname + e.target.search;
      obs.trigger(RC.EVENT.route.change, href);
    };

    searchProductList = function(e) {
      e.preventDefault();
      queryParams = {
        "productName": self.refs.word.value,
        "page": 1
      };
      var href = location.pathname + window.helper.joinQueryParams(queryParams);
      history.pushState(null, null, href);
      obs.trigger(RC.EVENT.route.product.list, queryParams);
    };

    switchIncremental = function(e) {
      self.incrementalChecked = e.target.checked;
    }

    searchProductListIncremental = function(e) {
      if (self.incrementalChecked) {
        searchProductList(e);
      }
    }

    obs.on(RC.EVENT.route.product.list, function(queryParams) {
      var page = queryParams.page || 1;
      var word = queryParams.productName || "";
      var request = sa.post(RC.API.product.list + page);
      if (word) {
        request = request.send({productName: word});
        self.refs.word.value = word;
      }
      request
        .end(function(error, response) {
          if (response.ok) {
            obs.trigger(RC.EVENT.route.product.listLoaded, JSON.parse(response.text));
          }
        });
    });

    obs.on(RC.EVENT.route.product.listLoaded, function(data) {
      self.productList = data.rows;
      self.update();
      obs.trigger(RC.EVENT.pagenation.set, data);
    });

    this.on('unmount', () => {
      obs.off(RC.EVENT.route.product.list);
      obs.off(RC.EVENT.route.product.listLoaded);
    });
  </script>
</product-list>
