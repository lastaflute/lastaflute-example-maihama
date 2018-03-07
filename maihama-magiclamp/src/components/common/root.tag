<root>
  <section if={authChecked && !isLogin}>
    <signin></signin>
  </section>
  <section if={authChecked && isLogin}>
    <div class="wrap">
      <article class="main-col">
        <h2 class="content-title">My Page</h2>

        <section class="mypage-product-box">
          <ul>
            <li>
              <h3>Recent Products</h3>
              <table class="list-tbl">
                <thead>
                  <tr>
                    <th>ProductName</th>
                    <th>RegularPrice</th>
                  </tr>
                </thead>
                <tbody>
                  <tr each="{recentProducts}">
                    <td>{productName}</td>
                    <td>¥{helper.formatMoneyComma(regularPrice)}</td>
                  </tr>
                </tbody>
              </table>
            </li>
            <li>
              <h3>High-Price Products</h3>
              <table class="list-tbl">
                <thead>
                  <tr>
                    <th>ProductName</th>
                    <th>RegularPrice</th>
                  </tr>
                </thead>
                <tbody>
                  <tr each="{highPriceProducts}">
                    <td>{productName}</td>
                    <td>¥{helper.formatMoneyComma(regularPrice)}</td>
                  </tr>
                </tbody>
              </table>
            </li>
          </ul>
        </section>
        <section class="mypage-following-box">
          <h3>Your Following</h3>
          making now
        </section>
      </article>
    </div>
  </section>

  <style>
    .mypage-product-box {
      width: 960px;
      margin: 32px auto 0;
    }

    .mypage-product-box table {
      margin-bottom: 12px;
    }

    .mypage-product-box table th {
      text-align: center;
    }

    .mypage-following-box {
      width: 960px;
      margin: 32px auto 0;
    }
  </style>

  <script>
    var self = this;
    this.mixin('common')

    this.isLogin = false;
    this.authChecked = false;

    this.on('mount', () => {
      reload()
      observable.on(EVENT.reload.root, () => {
        reload()
      });
    });

    this.on('unmount', () => {
      observable.off(EVENT.reload.root);
    });

    const reload = () => {
      this.authChecked = true
      this.isLogin = sessionStorage[SESSION.member.info];
      if (this.isLogin) {
        request.get(this.api.mypage,
          (response) => {
            var obj = JSON.parse(response.text);
            self.recentProducts = obj.recentProducts;
            self.highPriceProducts = obj.highPriceProducts;
            self.update();
          });
      } else {
        this.update();
      }
    }
  </script>
</root>