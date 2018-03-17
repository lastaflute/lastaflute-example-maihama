<product-list>
  <h2 class="content-title">List of Product</h2>
  <section class="content-box">
    <h3 class="content-title-second">Search Condition</h3>
    <span class="errors" if={validationErrors._global}> {validationErrors._global}</span>
    <form class="form" oninput={onSearchProductListIncremental}>
      <dl>
        <dt>Product Name</dt>
        <dd>
          <input type="text" ref="productName" />
          <span if={validationErrors.productName} class="errors"> {validationErrors.productName}</span>
        </dd>
        <dt>Product Status</dt>
        <dd>
          <select ref="productStatus">
            <option value=""></option>
            <option each={productStatusList} value={key}>{value}</option>
          </select>
        </dd>
        <dt>Purchase Member</dt>
        <dd>
          <input type="text" ref="purchaseMemberName" />
          <span if={validationErrors.purchaseMemberName} class="errors"> {validationErrors.purchaseMemberName}</span>
        </dd>
        <dd>
          <label>
            <input type="checkbox" ref="incrementalSearch"> incremental search
          </label>
        </dd>
      </dl>

      <button class="btn btn-success" onclick={onSearchProductList}>Search</button>
    </form>
  </section>
  <section class="content-box">
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
          <td>
            <a href="/product/detail/{productId}">{productName}</a>
          </td>
          <td>{productStatus}</td>
          <td>{productCategory}</td>
          <td>¥{helper.formatMoneyComma(regularPrice)}</td>
          <td>{latestPurchaseDate}</td>
        </tr>
      </tbody>
    </table>
    <pagination></pagination>
  </section>

  <script>
    var self = this;
    this.mixin('product')

    this.productList = [];
    this.validationErrors = {};
    this.page = "";

    // ===================================================================================
    //                                                                               Event
    //                                                                               =====
    this.on('mount', () => {
      if (opts.back) {
        var queryParams = self.getSessionSearchCondition();
        var href = self.getSearchProductListUrl(queryParams);
        observable.trigger(EVENT.route.change, href);
        return
      }
      self.selectProductStatus(opts.productStatus);
      self.setSearchCondition(opts);
      self.searchProductList(opts);
    });

    this.on('before-unmount', () => {
      self.setSessionSearchCondition()
    });

    this.onSearchProductList = (e) => {
      if (e) {
        e.preventDefault();
      }
      var queryParams = self.getSearchCondition();
      queryParams.page = "";
      var href = self.getSearchProductListUrl(queryParams);
      history.pushState(null, null, href);
      self.searchProductList(queryParams);
    };

    this.onSearchProductListIncremental = (e) => {
      if (self.refs.incrementalSearch.checked) {
        self.onSearchProductList(e);
      }
    };

    // ===================================================================================
    //                                                                               Logic
    //                                                                               =====
    this.getSearchProductListUrl = (queryParams) => {
      return '/product/list' + helper.joinQueryParams(queryParams);
    };

    this.searchProductList = (queryParams) => {
      var page = queryParams.page || 1;
      delete queryParams.page;

      self.validationErrors = {};

      request.post(this.api.product.list + page, queryParams,
        (response) => {
          var data = JSON.parse(response.text);
          self.productList = data.rows;
          self.update();
          observable.trigger(EVENT.pagenation.set, data);
        },
        (errors) => {
          self.validationErrors = errors;
          self.update();
        },
        false);
    };

    this.selectProductStatus = (productStatus) => {
      request.get(this.api.product.status,
        (response) => {
          self.productStatusList = JSON.parse(response.text);
          self.update();
          self.refs.productStatus.value = productStatus;
        },
        (errors) => { },
        false);
    };

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    this.setSearchCondition = (queryParams) => {
      self.refs.productName.value = queryParams.productName || "";
      self.refs.productStatus = queryParams.productStatus || "";
      self.refs.purchaseMemberName.value = queryParams.purchaseMemberName || "";
      if (queryParams.incrementalSearch === "true") {
        self.refs.incrementalSearch.checked = true;
      }
      self.page = queryParams.page || "";
    };

    this.getSearchCondition = () => {
      return {
        productName: self.refs.productName.value,
        productStatus: self.refs.productStatus.value,
        purchaseMemberName: self.refs.purchaseMemberName.value,
        incrementalSearch: self.refs.incrementalSearch.checked,
        page: self.page
      }
    };

    // ===================================================================================
    //                                                                     Session Storage
    //                                                                     ===============
    this.getSessionSearchCondition = () => {
      const paramsString = sessionStorage.getItem('product-list-search-condition')
      if (!paramsString) {
        return self.getSearchCondition()
      }
      return JSON.parse(paramsString)
    };

    this.setSessionSearchCondition = () => {
      const searchCondition = JSON.stringify(self.getSearchCondition())
      sessionStorage.setItem('product-list-search-condition', searchCondition)
    };
  </script>
</product-list>