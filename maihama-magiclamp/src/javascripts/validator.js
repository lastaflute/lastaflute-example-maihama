import messages from './messages.json';

export default class Validator {

  constructor() {
    this.validators = {
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
  }

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
      if (ref.attributes.required && this.validators.required(ref.value)) {
        validationError.push('REQUIRED');
      } else {
        if (ref.attributes.max && this.validators.max(ref.value, ref.attributes.max.nodeValue)) {
          validationError.push(`MAX | max:${ref.attributes.max.nodeValue}`);
        }
        if (ref.attributes.min && this.validators.min(ref.value, ref.attributes.min.nodeValue)) {
          validationError.push(`MIN | min:${ref.attributes.min.nodeValue}`);
        }
        if (ref.attributes.maxlength && this.validators.maxlength(ref.value, ref.attributes.maxlength.nodeValue)) {
          validationError.push(`MAXLENGTH | max:${ref.attributes.maxlength.nodeValue}`);
        }
        if (ref.attributes.minlength && this.validators.minlength(ref.value, ref.attributes.minlength.nodeValue)) {
          validationError.push(`MINLENGTH | min:${ref.attributes.minlength.nodeValue}`);
        }
        if (ref.attributes.pattern && this.validators.pattern(ref.value, ref.attributes.pattern.nodeValue)) {
          validationError.push(`PATTERN | regexp:${ref.attributes.pattern.nodeValue}`);
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
    const split = target.split('|');
    const key = split[0].trim();
    const values = split.length > 1 ? toValues(split[1].trim()) : {};

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