<member-purchase-list>
  <div class="wrap">
    <h2 class="content-title">List of Purchase</h2>
  </div>
  <section class="member-result-box">
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
          <td>{purchaseDatetime}</td>
          <td>{productName}</td>
          <td>{purchasePrice}</td>
          <td>{purchaseCount}</td>
          <td>
            <span if="{paymentComplete}">Complete</span>
          </td>
        </tr>
      </tbody>
    </table>
    <section class="member-purchase-list-paging-box">
      <pagination></pagination>
    </section>
    <div class="listback">
      <a href="/member/list/back">back to list</a>
    </div>
  </section>

  <script>
    var self = this;

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

      request.get(`${RC.API.member.purchase + memberId}/${page}`,
        (response) => {
          var data = JSON.parse(response.text);
          self.purchaseList = data.rows;
          self.update();
          observable.trigger(RC.EVENT.pagenation.set, data);
        },
        (errors) => {
          self.validationErrors = errors;
          self.update();
        });
    };
  </script>
</member-purchase-list>