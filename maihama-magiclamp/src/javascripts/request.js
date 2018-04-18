import Validator from './validator';
import sa from 'superagent';

export default class Request {

  post(url, queryParams, onSuccess, onError, withoutAuthorization) {
    let _sa = sa.post(apiPrefix + url).send(queryParams);
    this.request(_sa, onSuccess, onError, withoutAuthorization);
  }

  get(url, onSuccess, onError, withoutAuthorization) {
    let _sa = sa.get(apiPrefix + url);
    this.request(_sa, onSuccess, onError, withoutAuthorization);
  }

  request(_sa, onSuccess, onError, withoutAuthorization) {
    if (!withoutAuthorization) {
      const authkey = sessionStorage.getItem(SESSION.auth.key);
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
          observable.trigger(EVENT.route.change, '/');
          observable.trigger(EVENT.reload.header);
          observable.trigger(EVENT.reload.root);
        } else {
          onError(toValidationErros(response.body.errors));
        }
      } else if (response.clientError && response.body.cause === 'VALIDATION_ERROR') {
        onError(toValidationErros(response.body.errors));
      } else {
        onError({ _global: ['Please retry!'] });
      }
    });
  }
}

const apiPrefix = '/api';
const validator = new Validator();

function toValidationErros(errors) {
  const validationErrors = {};
  errors.forEach((element) => {
    validationErrors[element.field] = validator.generateMessage(element);
  });
  return validationErrors;
}