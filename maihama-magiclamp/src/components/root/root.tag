<root>
  <section if={!isLogin}>
    <signin></signin>
  </section>
  <section if={isLogin}>
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
                    <td>{regularPrice}</td>
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
                    <td>{regularPrice}</td>
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

  <script>
    var self = this;

    this.isLogin = false;

    this.on('mount', () => {
      observable.trigger(RC.EVENT.auth.check);
    });

    observable.on(RC.EVENT.auth.sign, (state) => {
      self.isLogin = state;
      if (self.isLogin) {
        request.get(RC.API.mypage,
          (response) => {
            var obj = JSON.parse(response.text);
            self.recentProducts = obj.recentProducts;
            self.highPriceProducts = obj.highPriceProducts;
            self.update();
          });
      } else {
        self.update();
      }
    });
  </script>
</root>