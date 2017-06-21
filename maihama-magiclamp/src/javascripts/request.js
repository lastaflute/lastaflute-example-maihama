export default class Request {

  post(url, queryParams, onSuccess, onError, withoutCredentials) {
    const _sa = sa.post(url).send(queryParams);
    if (!withoutCredentials) {
      _sa.withCredentials();
    }
    _sa.end((error, response) => {
      if (response.ok) {
        onSuccess(response);
      }
      else if (response.clientError && (response.body.cause === 'VALIDATION_ERROR' || response.body.cause === 'BUSINESS_ERROR')) {
        onError(this.toValidationErros(response.body.errors));
      }
      else if (response.clientError && response.body.cause === 'LOGIN_REQUIRED') {
        observable.trigger(RC.EVENT.route.change, '/');
        observable.trigger(RC.EVENT.auth.sign, false);
      }
    });
  }

  get(url, onSuccess, onError, withoutCredentials) {
    const _sa = sa.get(url);
    if (!withoutCredentials) {
      _sa.withCredentials();
    }
    _sa.end((error, response) => {
      if (response.ok) {
        onSuccess(response);
      }
      else if (response.clientError && (response.body.cause === 'VALIDATION_ERROR' || response.body.cause === 'BUSINESS_ERROR')) {
        onError(this.toValidationErros(response.body.errors));
      }
      else if (response.clientError && response.body.cause === 'LOGIN_REQUIRED') {
        observable.trigger(RC.EVENT.route.change, '/');
        observable.trigger(RC.EVENT.auth.sign, false);
      }
    });
  }

  toValidationErros(errors) {
    const validationErrors = {};
    errors.forEach((element) => {
      validationErrors[element.field] = element.messages;
    });
    return validationErrors;
  }

}