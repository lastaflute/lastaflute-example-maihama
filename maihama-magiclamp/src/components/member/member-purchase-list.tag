<member-purchase-list>
  <h2 class="content-title">List of Purchase</h2>
  <section class="content-box">
    <table class="list-tbl">
      <thead>
        <tr>
          <th>Purchase Id</th>
          <th>Purchase Datetime</th>
          <th>Product Name</th>
          <th>Price</th>
          <th>Count</th>
          <th>Payment</th>
        </tr>
      </thead>
      <tbody>
        <tr each={purchaseList}>
          <td>{purchaseId}</td>
          <td>{helper.formatDatetime(purchaseDatetime)}</td>
          <td>{productName}</td>
          <td>¥{helper.formatMoneyComma(purchasePrice)}</td>
          <td>{purchaseCount}</td>
          <td>
            <span if="{paymentComplete}">Complete</span>
          </td>
        </tr>
      </tbody>
    </table>
    <pagination></pagination>
    <a href="/member/list/back">back to list</a>
  </section>

  <script>
    var self = this;
    this.mixin('member');

    this.purchaseList = [];
    this.validationErrors = {};
    this.page = "";

    // ===================================================================================
    //                                                                               Event
    //                                                                               =====
    this.on('mount', () => {
      self.searchPurchaseList(opts);
    });

    // ===================================================================================
    //                                                                               Logic
    //                                                                               =====

    this.searchPurchaseList = (queryParams) => {
      console.log(queryParams);
      const page = queryParams.page || 1;
      const memberId = queryParams.memberId;

      self.validationErrors = {};

      request.get(`${this.api.member.purchase + memberId}/${page}`,
        (response) => {
          var data = JSON.parse(response.text);
          self.purchaseList = data.rows;
          self.update();
          observable.trigger(EVENT.pagenation.set, data);
        },
        (errors) => {
          self.validationErrors = errors;
          self.update();
        });
    };
  </script>
</member-purchase-list>