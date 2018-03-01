export default class Resource {

  constructor() {
    this.EVENT = {
      'route': {
        'change': 'onRounteChange'
      },
      'auth': {
        'check': 'onCheckSignin',
        'sign': 'onSign'
      },
      'pagenation': {
        'set': 'onPagenationSet'
      }
    };

    this.SESSION = {
      'member': {
        'info': 'memberInfo'
      },
      'auth': {
        'key': 'authKey'
      }
    };
  }
}