<helper>
  <script>
    var RC = window.RC || {};
    var obs = window.observable || {};
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
          else if (response.clientError && (response.body.cause === "VALIDATION_ERROR" || response.body.cause === "BUSINESS_ERROR")) {
            onError(toValidationErros(response.body.errors));
          }
          else if (response.clientError && response.body.cause === "LOGIN_REQUIRED") {
            obs.trigger(RC.EVENT.route.change, "/");
            obs.trigger(RC.EVENT.auth.sign, false);
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
          else if (response.clientError && (response.body.cause === "VALIDATION_ERROR" || response.body.cause === "BUSINESS_ERROR")) {
            onError(toValidationErros(response.body.errors));
          }
          else if (response.clientError && response.body.cause === "LOGIN_REQUIRED") {
            obs.trigger(RC.EVENT.route.change, "/");
            obs.trigger(RC.EVENT.auth.sign, false);
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

    window.helper.validate = function(refs, ...targetRefs){
        var validationErrors = {}
        for (var name in refs) {
            if (!refs.hasOwnProperty(name)) {
                continue
            }
            const ref = refs[name]
            if (targetRefs.length > 0 && targetRefs.indexOf(ref) < 0) {
                continue
            }

            const validationError = []
            if (ref.attributes.required && validators.required(ref.value)) {
                validationError.push('REQUIRED')
            } else {
                if (ref.attributes.max && validators.max(ref.value, ref.attributes.max.nodeValue)) {
                    validationError.push(`MAX | max:${ref.attributes.max.nodeValue}`)
                }
                if (ref.attributes.min && validators.min(ref.value, ref.attributes.min.nodeValue)) {
                    validationError.push(`MIN | min:${ref.attributes.min.nodeValue}`)
                }
                if (ref.attributes.maxlength && validators.maxlength(ref.value, ref.attributes.maxlength.nodeValue)) {
                    validationError.push(`MAXLENGTH | max:${ref.attributes.maxlength.nodeValue}`)
                }
                if (ref.attributes.minlength && validators.minlength(ref.value, ref.attributes.minlength.nodeValue)) {
                    validationError.push(`MINLENGTH | min:${ref.attributes.minlength.nodeValue}`)
                }
                if (ref.attributes.pattern && validators.pattern(ref.value, ref.attributes.pattern.nodeValue)) {
                    validationError.push(`PATTERN | regexp:${ref.attributes.pattern.nodeValue}`)
                }
            }

            if (validationError.length > 0) {
                validationErrors[name] = validationError.map((message) => generateMessage(message))
            } else {
                validationErrors[name] = undefined
            }
        }
        return validationErrors
    };

    generateMessage = function(target){
        const split = target.split('|')
        const key = split[0].trim()
        const values = split.length > 1 ? toValues(split[1].trim()) : {}

        let message = messages[key]
        for (const k in values) {
            if (!values.hasOwnProperty(k)) {
                continue
            }
            message = message.replace(`{${k}}`, values[k])
        }
        return message
    }

    validators = {
        required: (val) => {
            return val.trim() === ''
        },
        max: (val, expected) => {
            return parseInt(val.trim(), 10) > expected
        },
        min: (val, expected) => {
            return parseInt(val.trim(), 10) < expected
        },
        maxlength: (val, expected) => {
            return val.length > expected
        },
        minlength: (val, expected) => {
            return val.length < expected
        },
        pattern: (val, expected) => {
            return !(new RegExp(expected)).test(val.trim())
        },
    }

    messages = {
        REQUIRED: 'is required',
        MAX: 'must be less than or equal to {max}',
        MIN: 'must be greater than or equal to {min}',
        LENGTH: 'length must be between {min} and {max}',
        MAXLENGTH: 'length must be less than or equal to {max}',
        MINLENGTH: 'length must be greater than or equal to {min}',
        PATTERN: 'must match "{regexp}"',
    }
  </script>
</helper>
