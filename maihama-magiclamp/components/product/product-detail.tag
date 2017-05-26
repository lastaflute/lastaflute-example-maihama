<product-detail>
  <div class="contents">
    <h2 class="content-title">Detail of Product</h2>
    <section class="product-detail-box">
      <dl class="product-detail-list">
        <dt>Product Name</dt>
        <dd>{productDetail.productName}</dd>
        <dt>{productDetail.categoryName}</dt>
        <dd>¥{window.helper.formatMoneyComma(productDetail.regularPrice)}</dd>
      </dl>
      <a href="/product/list/back">一覧に戻る</a>
    </section>
  </div>

  <script>
    var RC = window.RC || {};
    var sa = window.superagent || {};
    var self = this;

    this.productDetail = {};
    this.productDetail.regularPrice = 0;

    // ===================================================================================
    //                                                                               Event
    //                                                                               =====
    this.on('mount', () => {
      detailLoad(opts.productId);
    });

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    detailLoad = function(product) {
      sa.get(RC.API.product.detail + (product || 1))
        .end(function(error, response) {
          if (response.ok) {
            detailLoaded(JSON.parse(response.text));
          }
        });
    }

    // ===================================================================================
    //                                                                               Logic
    //                                                                               =====
    detailLoaded = function(data) {
      self.productDetail = data;
      self.update();
    }
  </script>
</product-detail>