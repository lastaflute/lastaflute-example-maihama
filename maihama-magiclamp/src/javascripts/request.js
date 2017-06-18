var RC = window.RC || {};
var obs = window.observable || {};
window.request = {};

window.request.post = function(url, queryParams, onSuccess, onError, withoutCredentials) {
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

window.request.get = function(url, onSuccess, onError, withoutCredentials) {
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

var toValidationErros = function(errors) {
  var validationErrors = {};
  errors.forEach(function(element) {
    validationErrors[element.field] = element.messages;
  });
  return validationErrors;
}
