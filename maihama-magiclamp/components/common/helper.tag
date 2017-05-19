<helper>
  <script>
    window.helper = {};
    window.helper.fetchQueryParams = function(queryParams, key) {
      var value;
      queryParams.forEach(function(queryParam) {
        if (queryParam.key == key) {
          value = queryParam.value;
        }
      });
      return value;
    }

    window.helper.joinQueryParams = function(queryParams) {
      var queries = [];
      
      for (var key in queryParams) {
        if (queryParams[key] !== "") {
          queries.push(key + "=" + queryParams[key]);
        }
      }      
      if (queries.length > 0) {
        return "?" + queries.join("&");
      } else {
        return "";
      }
    }

    window.helper.updateOrInsertQueryParams = function(queryParams, key, value) {
      var newQueryParams = [];
      var found = false;
      queryParams.forEach(function(queryParam) {
        if (queryParam.key == key) {
          newQueryParams.push({key: key, value: value});
          found = true;
        } else {
          newQueryParams.push(queryParam);
        }
      });
      if (!found) {
        newQueryParams.push({key: key, value: value});
      }
      return newQueryParams;
    }

    window.helper.deleteKeyFromQueryParams = function(queryParams, key) {
      var newQueryParams = [];
      queryParams.forEach(function(q, i) {
        if (q.key != key) {
          newQueryParams.push(q);
        }
      });
      return newQueryParams;
    }

    window.helper.mappingQueryParams = function() {
      var raw = location.search;
      if (raw === "") {
        return [];
      }
      var splitParams = raw.replace("?", "").split("&")
      var queryParams = [];
      splitParams.forEach(function(p) {
        var split = p.split("=");
        queryParams.push({key: split[0], value: split[1]}); // simple for example
      });
      return queryParams;
    }

    window.helper.formatMoneyComma = function(money) { // simple for example
      var inner = function(input, output) {
        if (input.length > 0) {
          if (input.length % 3 == 0 && output.length > 0) {
            return inner(input.substr(1), output + "," + input[0]);
          } else {
            return inner(input.substr(1), output + input[0]);
          }
        } else {
          return output;
        }
      };
      return inner(money.toString(), "");
    }
  </script>
</helper>
