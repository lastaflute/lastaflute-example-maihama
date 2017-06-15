<product-detail>
  <div class="contents">
    <h2 class="content-title">Detail of Product</h2>
    <section class="product-detail-box">
      <dl class="product-detail-list">
        <dt>Product Name</dt>
        <dd>{productDetail.productName}</dd>
        <dt>Category Name</dt>
        <dd>{productDetail.categoryName}</dd>
        <dt>Regular Price</dt>
        <dd>¥{window.helper.formatMoneyComma(productDetail.regularPrice)}</dd>
      </dl>
      <a href="/product/list/back">back to list</a>
    </section>
  </div>

  <script>
    var RC = window.RC || {};
    var helper = window.helper || {};
    var self = this;

    this.productDetail = {};
    this.productDetail.regularPrice = 0;

    // ===================================================================================
    //                                                                               Event
    //                                                                               =====
    this.on('mount', () => {
      self.detailLoad(opts.productId);
    });

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    this.detailLoad = function(product) {
      helper.get(RC.API.product.detail + (product || 1),
        (response) => {
          self.detailLoaded(JSON.parse(response.text));
        },
        (errors) => {
          self.validationErrors = errors;
          self.update();
        },
        false);
    }

    // ===================================================================================
    //                                                                               Logic
    //                                                                               =====
    this.detailLoaded = function(data) {
      self.productDetail = data;
      self.update();
    }
  </script>
</product-detail>