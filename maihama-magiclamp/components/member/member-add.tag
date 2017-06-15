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
    var RC = window.RC || {};
    var helper = window.helper || {};
    var obs = window.observable || {};
    var self = this;

    this.validationErrors = {};
    this.memberDetail = {};
    // ===================================================================================
    //                                                                               Event
    //                                                                               =====
    this.on('mount', () => {
      selectMemberStatus();
    });

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    onRegister = function() {
      self.validationErrors = helper.validate(this.refs)
      for (const name in self.validationErrors) {
          if (self.validationErrors[name] !== undefined) {
              return
          }
      }

      helper.post(RC.API.member.add, getQueryParams(),
        () => {
          console.log('success add');
          obs.trigger(RC.EVENT.route.change, '/member/list');
        },
        (errors) => {
          self.validationErrors = errors;
          self.update();
        });
    }

    // ===================================================================================
    //                                                                               Logic
    //                                                                               =====
    selectMemberStatus = function () {
      helper.get(RC.API.member.status,
        (response) => {
          self.memberStatusList = JSON.parse(response.text);
          self.update();
        });
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    getQueryParams = () => {
      var params = {
        memberName: self.refs.memberName.value,
        memberStatus: self.refs.memberStatus.value,
        memberAccount: self.refs.memberAccount.value
      }
      if (self.refs.birthdate.value) {
        params.birthdate = self.refs.birthdate.value;
      }
      return params;
    }
  </script>
</member-add>