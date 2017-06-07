<member-edit>
  <div class="contents">
    <h2 class="content-title">Edit Member</h2>
    <section class="product-detail-box">
      <dl class="product-detail-list">
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
      <div class="listback">
        <a href="/member/list/back">back to list</a>
      </div>
    </section>
  </div>

  <script>
    var RC = window.RC || {};
    var helper = window.helper || {};
    var self = this;

    this.validationErrors = {};
    this.memberDetail = {};
    // ===================================================================================
    //                                                                               Event
    //                                                                               =====
    this.on('mount', () => {
      detailLoad(opts.memberId);
    });

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    detailLoad = function(member) {
      helper.get(RC.API.member.detail + (member || 1),
        (response) => {
          detailLoaded(JSON.parse(response.text));
        });
    }

    selectMemberStatus = function (memberStatus) {
      helper.get(RC.API.member.status,
        (response) => {
          self.memberStatusList = JSON.parse(response.text);
          self.update();
          self.refs.memberStatus.value = memberStatus;
        });
    }

    onUpdate = function() {
      helper.post(RC.API.member.update, getQueryParams(),
        () => {
          console.log('success update')
        },
        (errors) => {
          self.validationErrors = errors;
          self.update();
        });
    }

    // ===================================================================================
    //                                                                               Logic
    //                                                                               =====
    detailLoaded = function(data) {
      self.memberDetail = data;
      setRefValue(data);
      selectMemberStatus(data.memberStatusCode);
      self.update();
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    setRefValue = function(data) {
      self.refs.memberName.value = data.memberName || "";
      self.refs.memberAccount.value = data.memberAccount || "";
      if (data.birthdate) {
        self.refs.birthdate.value = data.birthdate;
      }
    }

    getQueryParams = () => {
      return {
        memberId: self.memberDetail.memberId,
        versionNo: self.memberDetail.versionNo,        
        memberName: self.refs.memberName.value,
        memberStatusCode: self.refs.memberStatus.value,
        memberAccount: self.refs.memberAccount.value,
        birthdate: self.refs.birthdate.value
      }
    }
  </script>
</member-edit>