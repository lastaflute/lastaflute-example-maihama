<pagination>
  <p>{currentPage} / {totalPages}</p>
  <ul>
    <li if={isFirst}><a href={prevPageParam} onclick={movePage}>prev</a></li>
    <li each={prevPages}><a href={pageParam} onclick={movePage}>{pageNum}</a></li>
    <li>{currentPage}</li>
    <li each={nextPages}><a href={pageParam} onclick={movePage}>{pageNum}</a></li>
    <li if={isEnd}><a href={nextPageParam} onclick={movePage}>next</a></li>
  </ul>

  <script>
    var RC = window.RC || {};
    var obs = window.observable || {};
    var self = this;
    var range = 3;

    movePage = function(e) {
      e.preventDefault();
      var href= e.target.pathname + e.target.search
      obs.trigger(RC.EVENT.route.change, href);
    };

    this.currentPage = 1;
    this.totalPages = 1;
    this.prevPages = [];
    this.nextPages = [];
    this.isFirst = false;
    this.isEnd = false;

    var mappingPagenation = function(data) {
      self.prevPages = [];
      self.nextPages = [];
      self.currentPage = data.currentPage;
      self.totalPages = data.totalPages;
      var queryParams = window.helper.mappingQueryParams();

      // set prevPages
      var prevStart = self.currentPage - range;
      prevStart = (prevStart <= 1) ? 1 : prevStart;
      for (var i = prevStart; i < self.currentPage; i++) {
        var q = window.helper.updateOrInsertQueryParams(queryParams, "page", i);
        self.prevPages.push({pageNum: i, pageParam: window.helper.joinQueryParams(q)});
      }

      // set nextPages
      var nextEnd = self.currentPage + range;
      nextEnd = (nextEnd >= self.totalPages) ? self.totalPages : nextEnd;
      for (var i = self.currentPage + 1; i <= nextEnd; i++) {
        var q = window.helper.updateOrInsertQueryParams(queryParams, "page", i);
        self.nextPages.push({pageNum: i, pageParam: window.helper.joinQueryParams(q)});
      }

      // is First or End
      self.isFirst = self.currentPage != 1;
      var prevQuery = window.helper.updateOrInsertQueryParams(queryParams, "page", self.currentPage - 1);
      self.prevPageParam = window.helper.joinQueryParams(prevQuery);
      var nextQuery = window.helper.updateOrInsertQueryParams(queryParams, "page", self.currentPage + 1);
      self.nextPageParam = window.helper.joinQueryParams(nextQuery);
      self.isEnd = self.currentPage != self.totalPages;

      self.update();
    }

    obs.on(RC.EVENT.pagenation.set, function(data) {
      mappingPagenation(data);
    });
  </script>
</pagination>
