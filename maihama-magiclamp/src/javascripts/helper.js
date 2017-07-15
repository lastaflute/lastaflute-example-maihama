export default class Helper {

  joinQueryParams(queryParams) {
    const queries = [];

    for (const key in queryParams) {
      if (queryParams[key] !== '') {
        queries.push(key + '=' + queryParams[key]);
      }
    }
    if (queries.length > 0) {
      return '?' + queries.join('&');
    } else {
      return '';
    }
  }

  mappingQueryParams() {
    const raw = location.search;
    if (raw === '') {
      return [];
    }
    const splitParams = raw.replace('?', '').split('&');
    const queryParams = {};
    splitParams.forEach((p) => {
      const split = p.split('=');
      queryParams[split[0]] = split[1]; // simple for example
    });
    return queryParams;
  }

  formatMoneyComma(money) { // simple for example
    const inner = (input, output) => {
      if (input.length > 0) {
        if (input.length % 3 == 0 && output.length > 0) {
          return inner(input.substr(1), output + ',' + input[0]);
        } else {
          return inner(input.substr(1), output + input[0]);
        }
      } else {
        return output;
      }
    };
    return inner(money.toString(), '');
  }

  formatDatetime(datetimeString) {
    const datetime = new Date(datetimeString);
    return datetime.getFullYear() +
      '/' + this.pad(datetime.getMonth() + 1) +
      '/' + this.pad(datetime.getDate()) +
      ' ' + this.pad(datetime.getHours()) +
      ':' + this.pad(datetime.getMinutes()) +
      ':' + this.pad(datetime.getSeconds());
  }

  pad(number) {
    if (number < 10) {
      return '0' + number;
    }
    return number;
  }
}