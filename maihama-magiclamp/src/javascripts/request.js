export default class Request {

  post(url, queryParams, onSuccess, onError, withoutAuthorization) {
    let _sa = sa.post(url).send(queryParams);
    if (!withoutAuthorization) {
      const authkey = sessionStorage.getItem('authkey');
      _sa = _sa.set('x-authorization', authkey);
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

  get(url, onSuccess, onError, withoutAuthorization) {
    let _sa = sa.get(url);
    if (!withoutAuthorization) {
      const authkey = sessionStorage.getItem('authkey');
      _sa = _sa.set('x-authorization', authkey);
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