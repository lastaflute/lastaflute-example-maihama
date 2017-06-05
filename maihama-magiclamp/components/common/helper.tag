<helper>
  <script>
    var sa = window.superagent || {};
    window.helper = {};

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

    window.helper.mappingQueryParams = function() {
      var raw = location.search;
      if (raw === "") {
        return [];
      }
      var splitParams = raw.replace("?", "").split("&")
      var queryParams = {};
      splitParams.forEach(function(p) {
        var split = p.split("=");
        queryParams[split[0]] = split[1]; // simple for example
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

    window.helper.post = function(url, queryParams, onSuccess, onError, withoutCredentials) {
      var _sa = sa.post(url).send(queryParams);
      if (!withoutCredentials) {
        _sa.withCredentials();
      }
      _sa.end(function(error, response) {
          if (response.ok) {
            onSuccess(response);
          }
          else if (response.clientError && response.body.cause === "VALIDATION_ERROR") {
            onError(toValidationErros(response.body.errors));
          }
        });
    }

    window.helper.get = function(url, onSuccess, onError, withoutCredentials) {
      var _sa = sa.get(url);
      if (!withoutCredentials) {
        _sa.withCredentials();
      }
      _sa.end(function(error, response) {
          if (response.ok) {
            onSuccess(response);
          }
          else if (response.clientError && response.body.cause === "VALIDATION_ERROR") {
            onError(toValidationErros(response.body.errors));
          }
        });
    }

    toValidationErros = function(errors) {
      var validationErrors = {};
      errors.forEach(function(element) {
        validationErrors[element.field] = element.messages;
      });
      return validationErrors;
    }
  </script>
</helper>
