global.RC = {};

const url = '/api';
RC.API = {
  'member': {
    'info': url + '/member/info',
    'status': url + '/member/status/',
    'list': url + '/member/list/',
    'detail': url + '/member/edit/',
    'update': url + '/member/edit/update/',
    'add': url + '/member/add/register/'
  },
  'mypage': url + '/mypage',
  'product': {
    'status': url + '/product/list/status/',
    'list': url + '/product/list/search/',
    'detail': url + '/product/detail/'
  },
  'profile': url + '/profile',
  'auth': {
    'signin': url + '/signin',
    'signup': url + '/signup',
    'signout': url + '/signout'
  }
};

RC.EVENT = {
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

RC.SESSION = {
  'member': {
    'info': 'memberInfo'
  }
};
