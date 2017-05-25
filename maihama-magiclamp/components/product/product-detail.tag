<product-detail>

  <h1>{productDetail.productName}</h1>
  <p>{productDetail.categoryName}</p>
  <p>¥{window.helper.formatMoneyComma(productDetail.regularPrice)}</p>
  <a href="/product/list/back">一覧に戻る</a>

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
