import messages from './messages.json';

export default class Validator {

  validate(refs, ...targetRefs) {
    const validationErrors = {};
    for (let name in refs) {
      if (!refs.hasOwnProperty(name)) {
        continue;
      }
      const ref = refs[name];
      if (targetRefs.length > 0 && targetRefs.indexOf(ref) < 0) {
        continue;
      }

      const validationError = [];
      if (ref.attributes.required && validators.required(ref.value)) {
        validationError.push({ code: 'REQUIRED' });
      } else {
        if (ref.attributes.max && validators.max(ref.value, ref.attributes.max.nodeValue)) {
          validationError.push({ code: 'MAX', data: { max: '${ref.attributes.max.nodeValue}' } });
        }
        if (ref.attributes.min && validators.min(ref.value, ref.attributes.min.nodeValue)) {
          validationError.push({ code: 'MIN', data: { min: '${ref.attributes.min.nodeValue}' } });
        }
        if (ref.attributes.maxlength && validators.maxlength(ref.value, ref.attributes.maxlength.nodeValue)) {
          validationError.push({ code: 'LENGTH', data: { min: '0', max: '${ref.attributes.maxlength.nodeValue}' } });
        }
        if (ref.attributes.minlength && validators.minlength(ref.value, ref.attributes.minlength.nodeValue)) {
          validationError.push({ code: 'LENGTH', data: { min: '${ref.attributes.minlength.nodeValue}', max: 'no-limit' } });
        }
        if (ref.attributes.pattern && validators.pattern(ref.value, ref.attributes.pattern.nodeValue)) {
          validationError.push({ code: 'PATTERN' });
        }
      }

      if (validationError.length > 0) {
        validationErrors[name] = validationError.map((message) => this.generateMessage(message));
      } else {
        validationErrors[name] = undefined;
      }
    }
    return validationErrors;
  }

  generateMessage(target) {
    const key = target.code;
    const values = target.data || {};

    let message = messages[key];
    for (const k in values) {
      if (!values.hasOwnProperty(k)) {
        continue;
      }
      message = message.replace(`{${k}}`, values[k]);
    }
    return message;
  }
}

const validators = {
  required: (val) => {
    return val.trim() === '';
  },
  max: (val, expected) => {
    return parseInt(val.trim(), 10) > expected;
  },
  min: (val, expected) => {
    return parseInt(val.trim(), 10) < expected;
  },
  maxlength: (val, expected) => {
    return val.length > expected;
  },
  minlength: (val, expected) => {
    return val.length < expected;
  },
  pattern: (val, expected) => {
    return !(new RegExp(expected)).test(val.trim());
  },
};