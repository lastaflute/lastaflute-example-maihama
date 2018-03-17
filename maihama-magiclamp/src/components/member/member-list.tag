<member-list>
  <h2 class="content-title">List of Member</h2>
  <section class="content-box">
    <h3 class="content-title-second">Search Condition</h3>
    <span class="errors" if={validationErrors._global}> {validationErrors._global}</span>
    <form class="form">
      <dl>
        <dt>Member Name</dt>
        <dd>
          <input type="text" ref="memberName" />
          <span if={validationErrors.memberName} class="errors"> {validationErrors.memberName}</span>
        </dd>
        <dt>Purchase Product</dt>
        <dd>
          <input type="text" ref="purchaseProductName" />
          <span if={validationErrors.purchaseProductName} class="errors"> {validationErrors.purchaseProductName}</span>
        </dd>
        <dt>Member Status</dt>
        <dd>
          <select ref="memberStatus">
            <option value=""></option>
            <option each={memberStatusList} value={key}>{value}</option>
          </select>
        </dd>
        <dt>Formalized Date</dt>
        <dd>
          <input type="text" ref="formalizedFrom" size="10" /> -
          <input type="text" ref="formalizedTo" size="10" />
          <span if={validationErrors.formalizedFrom} class="errors"> {validationErrors.formalizedFrom}</span>
        </dd>
        <dd>
          <label>
            <input type="checkbox" ref="unpaid" /> has unpaid?
          </label>
        </dd>
      </dl>

      <button class="btn btn-success" onclick={onSearchMemberList}>Search</button>
    </form>
  </section>

  <section class="content-box">
    <h3 class="content-title-second">Search Result</h3>
    <section class="add-box">
      <a href="/member/add">add Member</a>
    </section>
    <table class="list-tbl">
      <thead>
        <tr>
          <th>Member Id</th>
          <th>Member Name</th>
          <th>Member Status</th>
          <th>Formalized Date</th>
          <th>Update Datetime</th>
          <th>Editable</th>
          <th>Purchase</th>
        </tr>
      </thead>
      <tbody>
        <tr each={memberList}>
          <td>{memberId}</td>
          <td>{memberName}</td>
          <td>{memberStatusName}</td>
          <td>{formalizedDate}</td>
          <td>{helper.formatDatetime(updateDatetime)}</td>
          <td>
            <span if="{!withdrawalMember}">
              <a href="/member/edit/{memberId}">Edit</a>
            </span>
            <span if="{withdrawalMember}">Cannot</span>
          </td>
          <td>
            <a if="{purchaseCount > 0}" href="/member/purchase/{memberId}">has Purchases</a>
          </td>
        </tr>
      </tbody>
    </table>
    <pagination></pagination>
  </section>

  <style>
    .add-box {
      text-align: right;
    }
  </style>

  <script>
    var self = this;
    this.mixin('member');

    this.memberList = [];
    this.validationErrors = {};
    this.page = "";

    // ===================================================================================
    //                                                                               Event
    //                                                                               =====
    this.on('mount', () => {
      if (opts.back) {
        var queryParams = self.getSessionSearchCondition();
        var href = self.getSearchMemberListUrl(queryParams);
        observable.trigger(EVENT.route.change, href);
        return
      }
      self.selectMemberStatus(opts.memberStatus);
      self.setSearchCondition(opts);
      self.searchMemberList(opts);
    });

    this.on('before-unmount', () => {
      self.setSessionSearchCondition()
    });

    this.onSearchMemberList = (e) => {
      if (e) {
        e.preventDefault();
      }
      var queryParams = self.getSearchCondition();
      queryParams.page = "";
      var href = self.getSearchMemberListUrl(queryParams);
      history.pushState(null, null, href);
      self.searchMemberList(queryParams);
    };

    this.onSearchMemberListIncremental = (e) => {
      if (self.refs.incrementalSearch.checked) {
        self.onSearchMemberList(e);
      }
    };

    // ===================================================================================
    //                                                                               Logic
    //                                                                               =====
    this.getSearchMemberListUrl = (queryParams) => {
      return '/member/list' + helper.joinQueryParams(queryParams);
    };

    this.searchMemberList = (queryParams) => {
      var page = queryParams.page || 1;
      delete queryParams.page;

      self.validationErrors = {};

      request.post(this.api.member.list + page, queryParams,
        (response) => {
          var data = JSON.parse(response.text);
          self.memberList = data.rows;
          self.update();
          observable.trigger(EVENT.pagenation.set, data);
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

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    this.setSearchCondition = (queryParams) => {
      self.refs.memberName.value = queryParams.memberName || "";
      self.refs.purchaseProductName = queryParams.purchaseProductName || "";
      self.refs.memberStatus = queryParams.memberStatus || "";
      if (queryParams.unpaid === "true") {
        self.refs.unpaid.checked = true;
      }
      if (queryParams.formalizedFrom) {
        self.refs.formalizedFrom.value = queryParams.formalizedFrom;
      }
      if (queryParams.formalizedTo) {
        self.refs.formalizedTo.value = queryParams.formalizedTo;
      }
      self.page = queryParams.page || "";
    };

    this.getSearchCondition = () => {
      var condition = {
        memberName: self.refs.memberName.value,
        memberStatus: self.refs.memberStatus.value,
        unpaid: self.refs.unpaid.checked,
        page: self.page
      }
      if (self.refs.formalizedFrom.value) {
        condition.formalizedFrom = self.refs.formalizedFrom.value;
      }
      if (self.refs.formalizedTo.value) {
        condition.formalizedTo = self.refs.formalizedTo.value;
      }
      return condition;
    };

    // ===================================================================================
    //                                                                     Session Storage
    //                                                                     ===============
    this.getSessionSearchCondition = () => {
      const paramsString = sessionStorage.getItem('member-list-search-condition')
      if (!paramsString) {
        return self.getSearchCondition()
      }
      return JSON.parse(paramsString)
    }

    this.setSessionSearchCondition = () => {
      const searchCondition = JSON.stringify(self.getSearchCondition())
      sessionStorage.setItem('member-list-search-condition', searchCondition)
    };
  </script>
</member-list>