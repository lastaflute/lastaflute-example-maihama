export default class Resource {

  constructor() {
    this.API = {
      'member': {
        'info': '/member/info',
        'status': '/member/status/',
        'list': '/member/list/',
        'detail': '/member/edit/',
        'update': '/member/edit/update/',
        'add': '/member/add/register/',
        'purchase': '/member/purchase/list/'
      },
      'mypage': '/mypage',
      'product': {
        'status': '/product/list/status/',
        'list': '/product/list/search/',
        'detail': '/product/detail/'
      },
      'profile': '/profile',
      'withdrawal': {
        'reason': '/withdrawal/reason',
        'done': '/withdrawal/done',
      },
      'auth': {
        'signin': '/signin',
        'signup': '/signup',
        'signout': '/signout'
      }
    };

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