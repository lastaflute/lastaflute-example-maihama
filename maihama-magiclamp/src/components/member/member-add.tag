<member-add>
  <div class="contents">
    <h2 class="content-title">Add Member</h2>
    <section class="product-detail-box">
      <span class="errors" if={validationErrors._global}> {validationErrors._global}</span>
      <dl class="product-detail-list">
        <dt>Member Name</dt>
        <dd>
          <input type="text" ref="memberName" required />
          <span if={validationErrors.memberName} class="errors"> {validationErrors.memberName}</span>
        </dd>
        <dt>Member Account</dt>
        <dd>
          <input type="text" ref="memberAccount" required />
          <span if={validationErrors.memberAccount} class="errors"> {validationErrors.memberAccount}</span>
        </dd>
        <dt>Brithdate</dt>
        <dd>
          <input type="text" ref="birthdate" />
          <span if={validationErrors.birthdate} class="errors"> {validationErrors.birthdate}</span>
        </dd>
        <dt>Member Status</dt>
        <dd>
          <select ref="memberStatus" required>
            <option value=""></option>
            <option each={memberStatusList} value={key}>{value}</option>
          </select>
          <span if={validationErrors.memberStatus} class="errors"> {validationErrors.memberStatus}</span>
        </dd>
      </dl>
      <button class="btn btn-success" onclick={onRegister}>register</button>
      <div class="listback">
        <a href="/member/list/back">back to list</a>
      </div>
    </section>
  </div>

  <script>
    var self = this;
    this.mixin('member');

    this.validationErrors = {};
    this.memberDetail = {};
    // ===================================================================================
    //                                                                               Event
    //                                                                               =====
    this.on('mount', () => {
      self.selectMemberStatus();
    });

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    this.onRegister = () => {
      self.validationErrors = validator.validate(this.refs)
      for (const name in self.validationErrors) {
        if (self.validationErrors[name] !== undefined) {
          return
        }
      }

      request.post(this.api.member.add, this.getQueryParams(),
        () => {
          console.log('success add');
          observable.trigger(EVENT.route.change, '/member/list');
        },
        (errors) => {
          self.validationErrors = errors;
          self.update();
        });
    }

    // ===================================================================================
    //                                                                               Logic
    //                                                                               =====
    this.selectMemberStatus = () => {
      request.get(this.api.member.status,
        (response) => {
          self.memberStatusList = JSON.parse(response.text);
          self.update();
        });
    };

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    this.getQueryParams = () => {
      var params = {
        memberName: self.refs.memberName.value,
        memberStatus: self.refs.memberStatus.value,
        memberAccount: self.refs.memberAccount.value
      }
      if (self.refs.birthdate.value) {
        params.birthdate = self.refs.birthdate.value;
      }
      return params;
    };
  </script>
</member-add>