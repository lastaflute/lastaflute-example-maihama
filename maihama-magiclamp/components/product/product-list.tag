<product-list>
  <div class="wrap">
    <h2 class="content-title">List of Product</h2>
    <section class="product-search-box">
      <h3 class="content-title-second">Search Condition</h3>
      <form class="product-search-form" oninput={onSearchProductListIncremental}>
        <ul class="product-search-condition-list">
          <li>
            <span>Product Name</span>
            <input type="text" ref="productName" />
            <!--<span errors="productName"></span>-->
          </li>
          <li>
            <span>Product Status</span>
            <select ref="productStatus">
              <option value=""></option>
              <option each={productStatusList} value={key}>{value}</option>
            </select>
            <!--<span errors="productStatus"></span>-->
          </li>
          <li>
            <span>Purchase Member</span>
            <input type="text" ref="purchaseMemberName" />
            <!--<span errors="purchaseMemberName"></span>-->
          </li>
        </ul>

        <input type="checkbox" onchange={switchIncremental}> incremental search
        <button class="btn btn-success" onclick={onSearchProductList}>Search</button>
      </form>
  </div>
  <section class="product-result-box">
    <h3 class="content-title-second">Search Result</h3>
    <table class="list-tbl">
      <thead>
        <tr>
          <th>ID</th>
          <th>Product Name</th>
          <th>Status</th>
          <th>Category</th>
          <th>Price</th>
          <th>Latest Purchase</th>
        </tr>
      </thead>
      <tbody>
        <tr each={productList}>
          <td>{productId}</td>
          <td><a href="/product/detail/{productId}">{productName}</a></td>
          <td>{productStatus}</td>
          <td>{productCategory}</td>
          <td>¥{window.helper.formatMoneyComma(regularPrice)}</td>
          <td>{latestPurchaseDate}</td>
        </tr>
      </tbody>
    </table>
    <section class="product-list-paging-box">
      <pagination></pagination>
    </section>
  </section>
  </div>

  <script>
    var RC = window.RC || {};
    var sa = window.superagent || {};
    var obs = window.observable || {};
    var self = this;

    this.incrementalChecked = false;
    this.productList = [];

    // ===================================================================================
    //                                                                               Event
    //                                                                               =====
    this.on('mount', () => {
      if (opts.back) {
        var queryParams = getSessionSearchCondition();
        var href = getSearchProductListUrl(queryParams);
        obs.trigger(RC.EVENT.route.change, href);
        return
      }
      selectProductStatus(opts.productStatus);
      setSearchCondition(opts);
      searchProductList(opts);
    });

    this.on('before-unmount', () => {
      setSessionSearchCondition()
    })

    onSearchProductList = function (e) {
      if (e) {
        e.preventDefault();
      }
      var queryParams = getSearchCondition();
      var href = getSearchProductListUrl(queryParams);
      history.pushState(null, null, href);
      searchProductList(queryParams);
    };

    switchIncremental = function (e) {
      self.incrementalChecked = e.target.checked;
    }

    onSearchProductListIncremental = function (e) {
      if (self.incrementalChecked) {
        onSearchProductList(e);
      }
    }

    moveDetail = function(e) {
      e.preventDefault();
      var href = e.target.pathname + e.target.search;
      obs.trigger(RC.EVENT.route.change, href);
    };

    // ===================================================================================
    //                                                                               Logic
    //                                                                               =====
    getSearchProductListUrl = function(queryParams) {
      return '/product/list' + window.helper.joinQueryParams(queryParams);
    }

    searchProductList = function (queryParams) {
      var page = queryParams.page || 1;
      delete queryParams.page;

      sa.post(RC.API.product.list + page)
        .send(queryParams)
        .end(function (error, response) {
          if (response.ok) {
            var data = JSON.parse(response.text);
            self.productList = data.rows;
            self.update();
            obs.trigger(RC.EVENT.pagenation.set, data);
          }
        });
    }

    selectProductStatus = function (productStatus) {
      sa.get(RC.API.product.status)
        .end(function (error, response) {
          if (response.ok) {
            self.productStatusList = JSON.parse(response.text);
            self.update();
            self.refs.productStatus.value = productStatus;
          }
        })
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    setSearchCondition = (queryParams) => {
      self.refs.productName.value = queryParams.productName || "";
      self.refs.productStatus = queryParams.productStatus || "";
      self.refs.purchaseMemberName.value = queryParams.purchaseMemberName || "";
    }

    getSearchCondition = () => {
      return {
        productName: self.refs.productName.value,
        productStatus: self.refs.productStatus.value,
        purchaseMemberName: self.refs.purchaseMemberName.value
      }
    }

    // ===================================================================================
    //                                                                     Session Storage
    //                                                                     ===============
    getSessionSearchCondition = () => {
      const paramsString = sessionStorage.getItem('product-list-search-condition')
      if (!paramsString) {
        return self.getSearchCondition()
      }
      return JSON.parse(paramsString)
    }

    setSessionSearchCondition = () => {
      const searchCondition = JSON.stringify(getSearchCondition())
      sessionStorage.setItem('product-list-search-condition', searchCondition)
    }
  </script>
</product-list>