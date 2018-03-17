<member-edit>
  <h2 class="content-title">Edit Member</h2>
  <section class="content-box">
    <span class="errors" if={validationErrors._global}> {validationErrors._global}</span>
    <section class="form">
      <dl>
        <dt>Member Name</dt>
        <dd>
          <input type="text" ref="memberName" />
          <span if={validationErrors.memberName} class="errors"> {validationErrors.memberName}</span>
        </dd>
        <dt>Member Account</dt>
        <dd>
          <input type="text" ref="memberAccount" />
          <span if={validationErrors.memberAccount} class="errors"> {validationErrors.memberAccount}</span>
        </dd>
        <dt>Brithdate</dt>
        <dd>
          <input type="text" ref="birthdate" />
          <span if={validationErrors.birthdate} class="errors"> {validationErrors.birthdate}</span>
        </dd>
        <dt>Member Status</dt>
        <dd>
          <select ref="memberStatus">
            <option value=""></option>
            <option each={memberStatusList} value={key}>{value}</option>
          </select>
        </dd>
      </dl>
      <button class="btn btn-success" onclick={onUpdate}>update</button>
    </section>
  </section>
  <section class="content-box">
    <div class="listback">
      <a href="/member/list/back">back to list</a>
    </div>
  </section>

  <script>
    var self = this;
    this.mixin('member');

    this.validationErrors = {};
    this.memberDetail = {};
    // ===================================================================================
    //                                                                               Event
    //                                                                               =====
    this.on('mount', () => {
      self.detailLoad(opts.memberId);
    });

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    this.detailLoad = (member) => {
      request.get(this.api.member.detail + (member || 1),
        (response) => {
          self.detailLoaded(JSON.parse(response.text));
        },
        (errors) => {
          self.validationErrors = errors;
          self.update();
        });
    };

    this.selectMemberStatus = (memberStatus) => {
      request.get(this.api.member.status,
        (response) => {
          self.memberStatusList = JSON.parse(response.text);
          self.update();
          self.refs.memberStatus.value = memberStatus;
        });
    };

    this.onUpdate = () => {
      request.post(this.api.member.update, this.getQueryParams(),
        () => {
          console.log('success update')
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
      self.memberDetail = data;
      self.setRefValue(data);
      self.selectMemberStatus(data.memberStatus);
      self.update();
    };

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    this.setRefValue = (data) => {
      self.refs.memberName.value = data.memberName || "";
      self.refs.memberAccount.value = data.memberAccount || "";
      if (data.birthdate) {
        self.refs.birthdate.value = data.birthdate;
      }
    };

    this.getQueryParams = () => {
      var params = {
        memberId: self.memberDetail.memberId,
        versionNo: self.memberDetail.versionNo,
        memberName: self.refs.memberName.value,
        memberStatus: self.refs.memberStatus.value,
        memberAccount: self.refs.memberAccount.value,
      }
      if (self.refs.birthdate.value) {
        params.birthdate = self.refs.birthdate.value;
      }
      return params;
    };
  </script>
</member-edit>