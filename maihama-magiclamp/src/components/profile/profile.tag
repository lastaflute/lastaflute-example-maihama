<profile>
  <h2 class="content-title">Profile</h2>
  <section class="content-box">
    <h3 class="content-title-second">{profile.memberName}</h3>
    <section class="form">
      <dl>
        <dt>MemberStatus</dt>
        <dd>{profile.memberStatusName}</dd>
        <dt>PointCount</dt>
        <dd>{profile.servicePointCount}</dd>
        <dt>ServiceRank</dt>
        <dd>{profile.serviceRankName}</dd>
      </dl>
    </section>
  </section>
  <section class="content-box">
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
          <td>{helper.formatDatetime(purchaseDateTime)}</td>
        </tr>
      </tbody>
    </table>
  </section>

  <script>
    var self = this;
    this.mixin('profile')

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
    this.detailLoad = (product) => {
      request.get(this.api.profile,
        (response) => {
          self.detailLoaded(JSON.parse(response.text));
        },
        (errors) => {
          self.validationErrors = errors;
          self.update();
        });
    };

    // ===================================================================================
    //                                                                               Logic
    //                                                                               =====
    this.detailLoaded = (data) => {
      self.profile = data;
      self.update();
    };
  </script>
</profile>