<member-detail>
  <div class="contents">
    <h2 class="content-title">Detail of Member</h2>
    <section class="member-detail-box">
      <dl class="member-detail-list">
        <dt>Member Name</dt>
        <dd>{memberDetail.memberName}</dd>
        <dt>Category Name</dt>
        <dd>{memberDetail.categoryName}</dd>
        <dt>Regular Price</dt>
        <dd>¥{window.helper.formatMoneyComma(memberDetail.regularPrice)}</dd>
      </dl>
      <a href="/member/list/back">back to list</a>
    </section>
  </div>

  <script>
    var RC = window.RC || {};
    var sa = window.superagent || {};
    var self = this;

    this.memberDetail = {};
    this.memberDetail.regularPrice = 0;

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
      console.log(RC.API.member.detail + (member || 1));
      sa.get(RC.API.member.detail + (member || 1))
        .withCredentials()
        .end(function(error, response) {
          console.log(error)
          if (response.ok) {
            detailLoaded(JSON.parse(response.text));
          }
        });
    }

    // ===================================================================================
    //                                                                               Logic
    //                                                                               =====
    detailLoaded = function(data) {
      self.memberDetail = data;
      self.update();
    }
  </script>
</member-detail>