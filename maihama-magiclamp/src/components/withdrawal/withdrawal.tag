<withdrawal>
  <div class="contents">
    <h2 class="content-title">Withdraw Account</h2>
    <section class="product-detail-box">
      <h3 class="content-title-second">Your withdrawal reason</h3>
      <ul class="withdrawal-reason-list">
        <li class="withdrawal-reason-element">
          <select ref="withdrawalReason" required>
            <option value=""></option>
            <option each={withdrawalReasonList} value={key}>{value}</option>
          </select>
          <span if={validationErrors.withdrawalReason} class="errors"> {validationErrors.withdrawalReason}</span>
        </li>
        <li class="withdrawal-reason-element">
          <div>Withdrawal Reason</div>
          <textarea rows="3" cols="30" class="withdrawal-write-reason" ref="reasonInput" />
          <span if={validationErrors.reasonInput} class="errors"> {validationErrors.reasonInput}</span>
        </li>
      </ul>
      <button class="btn btn-success" onclick={onWithdraw}>withdraw</button>
    </section>
  </div>

  <script>
    var self = this;
    this.mixin('withdrawal')

    this.validationErrors = {};
    // ===================================================================================
    //                                                                               Event
    //                                                                               =====
    this.on('mount', () => {
      self.selectWithdrawalReason();
    });

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    this.onWithdraw = () => {
      self.validationErrors = validator.validate(this.refs)
      for (const name in self.validationErrors) {
        if (self.validationErrors[name] !== undefined) {
          return
        }
      }

      request.post(this.api.withdrawal.done, this.getQueryParams(),
        () => {
          console.log('withdrawal ok');
          observable.trigger(RC.EVENT.route.change, '/');
        },
        (errors) => {
          self.validationErrors = errors;
          self.update();
        });
    }

    // ===================================================================================
    //                                                                               Logic
    //                                                                               =====
    this.selectWithdrawalReason = () => {
      request.get(this.api.withdrawal.reason,
        (response) => {
          self.withdrawalReasonList = JSON.parse(response.text);
          self.update();
        });
    };

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    this.getQueryParams = () => {
      return {
        selectedReason: self.refs.withdrawalReason.value,
        reasonInput: self.refs.reasonInput.value
      };
    };
  </script>
</withdrawal>