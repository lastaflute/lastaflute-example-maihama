<profile>
  <div class="contents">
    <h2 class="content-title">Profile</h2>
    <article class="profile-contents-box">
      <section class="profile-info-box">
        <h3 class="content-title-second">{profile.memberName}</h3>
        <ul class="profile-info-detail">
          <li>MemberStatus : {profile.memberStatusName}</li>
          <li>PointCount : {profile.servicePointCount}</li>
          <li>ServiceRank : {profile.serviceRankName}</li>
        </ul>
      </section>
      <section class="profile-product-box">
        <h3 class="content-title-second">PurchaseHistory</h3>
        <table class="list-tbl">
          <thead>
            <tr>
              <th>ProductName</th>
              <th>Price</th>
              <th>PurchaseDateTime</th>
            </tr>
          </thead>
          <tbody>
            <tr each={profile.purchaseList}>
              <td>{productName}</td>
              <td>¥{helper.formatMoneyComma(regularPrice)}</td>
              <td>{purchaseDateTime}</td>
            </tr>
          </tbody>
        </table>
      </section>
    </article>
  </div>

  <script>
    var self = this;

    this.profile = {};

    // ===================================================================================
    //                                                                               Event
    //                                                                               =====
    this.on('mount', () => {
      self.detailLoad(opts.productId);
    });

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    this.detailLoad = function (product) {
      request.get(RC.API.profile,
        (response) => {
          self.detailLoaded(JSON.parse(response.text));
        },
        (errors) => {
          self.validationErrors = errors;
          self.update();
        });
    }

    // ===================================================================================
    //                                                                               Logic
    //                                                                               =====
    this.detailLoaded = function (data) {
      self.profile = data;
      self.update();
    }
  </script>
</profile>