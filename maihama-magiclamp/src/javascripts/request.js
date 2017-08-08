export default class Request {

  constructor() {
    this.apiPrefix = '/api';
  }

  post(url, queryParams, onSuccess, onError, withoutAuthorization) {
    let _sa = sa.post(this.apiPrefix + url).send(queryParams);
    this.request(_sa, onSuccess, onError, withoutAuthorization);
  }

  get(url, onSuccess, onError, withoutAuthorization) {
    let _sa = sa.get(this.apiPrefix + url);
    this.request(_sa, onSuccess, onError, withoutAuthorization);
  }

  request(_sa, onSuccess, onError, withoutAuthorization) {
    if (!withoutAuthorization) {
      const authkey = sessionStorage.getItem(RC.SESSION.auth.key);
      _sa = _sa.set('x-authorization', authkey);
    }
    _sa.end((error, response) => {
      if (response.ok) {
        onSuccess(response);
      } else if (response.clientError && response.body.cause === 'BUSINESS_ERROR') {
        const hasLoginRequired = response.body.errors.some(error => {
          return error.code === 'LOGIN_REQUIRED';
        });
        if (hasLoginRequired) {
          observable.trigger(RC.EVENT.route.change, '/');
          observable.trigger(RC.EVENT.auth.sign, false);
        } else {
          onError(this.toValidationErros(response.body.errors));
        }
      } else if (response.clientError && response.body.cause === 'VALIDATION_ERROR') {
        onError(this.toValidationErros(response.body.errors));
      } else {
        onError({ _global: ['Please retry!'] });
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