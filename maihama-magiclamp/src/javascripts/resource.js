global.RC = {};

RC.API = {
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
  'auth': {
    'signin': '/signin',
    'signup': '/signup',
    'signout': '/signout'
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
  },
  'auth': {
    'key': 'authKey'
  }
};
